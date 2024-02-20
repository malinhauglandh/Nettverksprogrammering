#include <iostream>
#include <vector>
#include <functional>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <list>

using namespace std;

class Workers {
private:
    list<function<void()>> tasks;
    vector<thread> worker_threads;
    mutex access_tasks_mutex;
    condition_variable condition;
    bool stop_worker_threads; // Flag to stop worker threads
    int num_threads;

public:
    explicit Workers (int threads) {
        num_threads = threads;
        stop_worker_threads = false;
    }

    void stop() {
        if(num_threads > 1) cout << "Stopping worker threads" << endl;
        else cout << "Stopping event loop" << endl;

        {
            unique_lock<mutex> lock(access_tasks_mutex);
            stop_worker_threads = true;
        }

        condition.notify_all();
        for (auto &t: worker_threads) {
            t.join();
        }
    }

    void start() {
        if(num_threads > 1) cout << "Starting worker threads" << endl;
        else cout << "Starting event loop" << endl;
        for (int i = 0; i < num_threads; i++) {
            worker_threads.emplace_back([this] {
                while (true) {
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(access_tasks_mutex);
                        condition.wait(lock, [this] { return stop_worker_threads || !tasks.empty(); });
                        if (stop_worker_threads && tasks.empty()) {
                            if(num_threads > 1) cout << "Stopping worker thread " << this_thread::get_id() << endl;
                            else cout << "Stopping event loop thread" << endl;
                            return;
                        }
                        else {
                            task = *tasks.begin(); // Copy task for later use
                            tasks.pop_front(); // Remove task from list
                        }

                    }
                    if (task)
                        task(); // Run task outside mutex lock
                }
            });
        }
    }

    void post_tasks() {
        for (int i = 0; i < 10; i++) {
            unique_lock<mutex> lock(access_tasks_mutex);
            tasks.emplace_back([i] {
                cout << "Task " << i << " is running in thread " << this_thread::get_id() << endl;
            });
            condition.notify_one();
        }
    }

    void post(const function<void()>& task_){
        unique_lock<mutex> lock(access_tasks_mutex);
        tasks.emplace_back(task_);
        condition.notify_one();
    }

    void join()  {
        this -> stop();
    }
    void post_timeout(int timeout_ms, const function<void()>& task_){
        auto delayed_task = [timeout_ms, task_] {
            this_thread::sleep_for(chrono::milliseconds(timeout_ms));
            task_();
        };
        {
            unique_lock<mutex> lock(access_tasks_mutex);
            tasks.emplace_back(delayed_task);
            condition.notify_one();
        }
    }
};

int main() {
    Workers worker_threads(4);
    Workers event_loop(1);

    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post([] {
        // Task A (kjøttdeig)
        cout << "Worker Task A is running in thread " << this_thread::get_id() << endl;
    });
    worker_threads.post([] {
        // Task B (agurk)
        // Might run in parallel with task A
        cout << "Worker Task B is running in thread " << this_thread::get_id() << endl;
    });

    event_loop.post([] {
        // Task C (guacc)
        // Might run in parallel with task A and B
        cout << "Event loop task C is running in thread " << this_thread::get_id() << endl;
    });
    event_loop.post([] {
        // Task D (tomat)
        // Will run after task C
        // Might run in parallel with task A and B
        cout << "Event loop task D is running in thread " << this_thread::get_id() << endl;
    });

    this_thread::sleep_for(chrono::seconds(1));
    worker_threads.post_timeout(1000, [] {
        // Task E (løk)
        // Will run after 1 second
        cout << "Post timeout task was run" << this_thread::get_id() << endl;
    });

    this_thread::sleep_for(chrono::seconds(1));
    worker_threads.join(); // Calls join() on the worker threads
    event_loop.join();

    return 0;
}