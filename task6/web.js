const net = require('net');
const crypto = require('crypto');
const fs = require('fs');

const connectedClients = new Set();


// Create the HTTP server that will serve the index.html file if the request starts with GET or GET/index.html
const httpServer = net.createServer((socket) => {
  socket.on('data', (buffer) => {
    const request = buffer.toString();
    if(request.startsWith('GET') || request.startsWith('GET/index.html')) {
      fs.readFile('index.html', (err, data) => {
        if(err) {
          socket.end('HTTP/1.1 500 Internal Server Error\r\nError loading index.html');
          socket.end();
        } else {
          socket.write('HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n');
          socket.write(data);
          socket.end();
        }
        });
     } else {
    socket.end('HTTP/1.1 404 Not Found\r\n');
    socket.end();
    }
  });
});

httpServer.listen(3000, () => {
  console.log('Server listening on port 3000');
});

// Create the WebSocket server by looking for the Upgrade: websocket header in the request. 
// Performs the handshake and adds the client to the connectedClients set.
const webSocketServer = net.createServer((socket) => {
    socket.once('data', (buffer) => {
      const headers = buffer.toString().split('\r\n');
      if(headers.find(h => h.includes('Upgrade: websocket'))) {
        const keyHeader = headers.find(h => h.startsWith('Sec-WebSocket-Key'));
        const key = keyHeader.split(': ')[1].trim();
        const acceptKey = generateAcceptValue(key);

        const responseHeaders = [
          'HTTP/1.1 101 Switching Protocols',
          'Upgrade: websocket',
          'Connection: Upgrade',
          `Sec-WebSocket-Accept: ${acceptKey}`,
          '\r\n'
        ];
        socket.write(responseHeaders.join('\r\n'));
        console.log('WebSocket connection established');
        connectedClients.add(socket);
      }
    });
    socket.on('end', () => {
        console.log('Client disconnected');
    });
    // Parses the message and sends it to all connected clients
    socket.on('data', (data) => {
      const messageText = parseMessage(data);
      if (messageText) {
          try {
              const recieve = JSON.parse(messageText).message;
              console.log('Received message:', recieve);
              const reply = prepareMessage(recieve);

              //const reply = constructReply(messageData);
              
              connectedClients.forEach(client => {
                  client.write(reply);
              });
          } catch (error) {
              console.error('Error parsing message as JSON:', error);
          }
      }
  });
});

webSocketServer.listen(3001, () => {
  console.log('Server listening on port 3001');
});

// Generate the accept value for the Sec-WebSocket-Accept header
function generateAcceptValue(clientAcceptKey) {
  const uuid = '258EAFA5-E914-47DA-95CA-C5AB0DC85B11'; // unique identifier
  return crypto
    .createHash('sha1')
    .update(clientAcceptKey + uuid, 'binary')
    .digest('base64');
}

function parseMessage(buffer) {
  let secondByte = buffer.readUInt8(1);
  let isMasked = (secondByte & 0x80) === 0x80;
  let payloadLength = secondByte & 0x7F;
  let maskStart = 2;
  let dataStart = maskStart + 4;
  let decoded = Buffer.alloc(payloadLength);  // Save the decoded data here.

  if (isMasked) {
      const maskingKeys = buffer.slice(maskStart, dataStart);

      for (let i = 0; i < payloadLength; i++) {
          let byte = buffer[dataStart + i] ^ maskingKeys[i % 4];
          decoded[i] = byte;
      }
      return decoded.toString(); 
  } else {
      console.error("The message from the client is not masked.");
      return null;
  }
}


function prepareMessage(message) {
	const msg = Buffer.from(message);
	const msgSize = msg.length;
  
	const header = Buffer.alloc(2);
	header.writeUInt8(0x81, 0);
	header.writeUInt8(msgSize, 1);
  
	const frame = Buffer.concat([header, msg]);
	return frame;
  }