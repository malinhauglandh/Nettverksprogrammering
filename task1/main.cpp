#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <algorithm>
#include <chrono>

using namespace std;
using namespace chrono;

mutex result_mutex;
vector<pair<int, int>> primeNumbersWithThread; // Pair of (thread number, prime number)

bool isPrime(int num) {
    if (num < 2) {
        return false;
    }
    for (int i = 2; i * i <= num; i++) {
        if (num % i == 0) {
            return false;
        }
    }
    return true;
}

void findPrimesInRange(int threadNumber, int start, int end) {
    vector<pair<int, int>> primesWithThread;
    for (int i = start; i <= end; i++) {
        if (isPrime(i)) {
            primesWithThread.push_back({threadNumber, i});
        }
    }
    // lock the mutex before modifying the shared resource
    lock_guard<mutex> lock(result_mutex);
    // add the primes we found in one interval to the shared vector
    primeNumbersWithThread.insert(
            primeNumbersWithThread.end(),
            primesWithThread.begin(),
            primesWithThread.end()
    );
}

int main() {
    // Capture the start time
    auto startTime = high_resolution_clock::now();

    vector<thread> threads; // stores multiple threads

    const int startRange = 1;
    const int endRange = 10000;
    const int threadCount = 10;

    int interval = (endRange - startRange + 1) / threadCount;

    // creates 10 threads. each thread finds primes in its interval
    for (int i = 0; i < threadCount; i++) {
        int start = startRange + i * interval;
        int end = (i == threadCount - 1) ? endRange : startRange + (i + 1) * interval - 1;

        threads.emplace_back(findPrimesInRange, i, start, end);
    }

    // joins all threads so that the main thread will wait for all 10.
    for (auto &thread : threads) { // &thread creates a reference to each element.
        thread.join();
    }

    // sort the global list of primes with thread numbers
    sort(primeNumbersWithThread.begin(), primeNumbersWithThread.end());

    // print the sorted list of primes with thread numbers
    for (const auto &entry : primeNumbersWithThread) {
        cout << "Thread " << entry.first << ": " << entry.second << endl;
    }

    // Capture the end time
    auto endTime = high_resolution_clock::now();

    // Calculate the elapsed time
    auto duration = duration_cast<milliseconds>(endTime - startTime);

    cout << "Total runtime: " << duration.count() << " milliseconds" << endl;

    return 0;
}
