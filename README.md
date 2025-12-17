# TCP-Task – Simple Java TCP Client–Server Calculator

This project is a minimal TCP-based client–server application written in pure Java using `java.net` and `java.io`.
The server listens on a fixed port and processes simple calculator commands sent by a TCP client.
Supported operations:

- `ADD x y` → returns `x + y`
- `SUB x y` → returns `x - y`

If the client sends an invalid command or wrong format, the server returns an error message.

> Note: This project does **not** use UDP, Maven, or any external frameworks.

---

## Project Structure

```
TCP-Task/
│── README.md
└── src/
    ├── server/
    │   └── TCPServer.java
    └── client/
        └── TCPClient.java
```

---

## Requirements

- Java Development Kit (JDK) 8 or later installed
- Basic understanding of compiling and running Java from the command line

---

## How to Compile

From the `TCP-Task` root directory:

```
cd src
javac server/TCPServer.java client/TCPClient.java
```

This will generate the `.class` files in the same directories.

---

## How to Run the Server

From inside the `src` directory:

```
java server.TCPServer
```

The server will:

- Start listening on port `5000`
- Stay running until you stop it manually (Ctrl + C)
- Handle multiple requests sequentially from each connected client

You should see console output similar to:

```
Starting TCP Server on port 5000...
Server is running and waiting for client connection...
```

---

## How to Run the Client

In a **separate** terminal window, from the same `src` directory:

```
java client.TCPClient
```

The client will:

- Connect to `localhost:5000`
- Print the welcome messages from the server
- Allow you to type commands in the console

You should see something like:

```
TCP Client starting...
Connecting to server localhost:5000...
Connected to server.
SERVER: Connected to TCP Calculator Server.
SERVER: Available commands: ADD x y, SUB x y, EXIT
Type commands like: ADD 5 3 or SUB 10 4
Type EXIT to close the client.
CLIENT>
```

---

## Example Commands and Output

### Example 1 – Addition

Client input:

```
CLIENT> ADD 5 3
```

Server response:

```
SERVER> RESULT: 8
```

### Example 2 – Subtraction

Client input:

```
CLIENT> SUB 10 4
```

Server response:

```
SERVER> RESULT: 6
```

### Example 3 – Invalid Command

Client input:

```
CLIENT> MUL 2 3
```

Server response:

```
SERVER> ERROR: Unknown command. Use ADD or SUB.
```

### Example 4 – Invalid Parameters

Client input:

```
CLIENT> ADD five 3
```

Server response:

```
SERVER> ERROR: x and y must be integers.
```

### Example 5 – Exit

Client input:

```
CLIENT> EXIT
```

Server response:

```
SERVER> Goodbye!
```

The client then closes the connection and terminates.

---

## Notes

- Make sure the server is running **before** starting the client.
- If you change the port in `TCPServer.java`, update the same port in `TCPClient.java`.
- You can push this folder (`TCP-Task/`) to GitHub directly or compress it as a ZIP for submission.
