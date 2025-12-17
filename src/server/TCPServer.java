package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCPServer
 *
 * A simple TCP server that:
 * - Listens on a fixed port.
 * - Accepts one client at a time.
 * - Processes multiple commands sequentially from that client.
 * - Supports:
 *     ADD x y  -> returns x + y
 *     SUB x y  -> returns x - y
 * - Returns an error message for invalid commands.
 *
 * The server keeps running until it is manually stopped (Ctrl + C).
 */
public class TCPServer {

    // You can change this port if needed, but keep it the same in the client.
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Starting TCP Server on port " + SERVER_PORT + "...");

        // Use try-with-resources for automatic resource cleanup of the ServerSocket.
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running and waiting for client connection...");

            // The server runs forever until the process is terminated.
            while (true) {
                // accept() blocks until a client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                // Handle this client in the same thread, processing multiple requests sequentially.
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles communication with a single connected client.
     * The method reads lines until the client closes the connection
     * or sends the EXIT command.
     */
    private static void handleClient(Socket clientSocket) {
        try (
            // Input stream to receive data from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Output stream to send data back to the client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Connected to TCP Calculator Server.");
            out.println("Available commands: ADD x y, SUB x y, EXIT");

            String line;
            // Read commands line by line until client disconnects or sends EXIT
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("EXIT")) {
                    out.println("Goodbye!");
                    break;
                }

                String response = processCommand(line);
                out.println(response);
            }

            System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore close exception
            }
        }
    }

    /**
     * Parses and executes a single command string.
     *
     * Supported commands:
     *   ADD x y  -> returns x + y
     *   SUB x y  -> returns x - y
     *
     * On invalid commands or parameters, returns an error message.
     */
    private static String processCommand(String commandLine) {
        if (commandLine.isEmpty()) {
            return "ERROR: Empty command.";
        }

        // Split by whitespace
        String[] parts = commandLine.split("\\s+");
        if (parts.length != 3) {
            return "ERROR: Invalid format. Use: ADD x y or SUB x y";
        }

        String command = parts[0].toUpperCase();
        String xStr = parts[1];
        String yStr = parts[2];

        int x;
        int y;
        try {
            x = Integer.parseInt(xStr);
            y = Integer.parseInt(yStr);
        } catch (NumberFormatException e) {
            return "ERROR: x and y must be integers.";
        }

        switch (command) {
            case "ADD":
                return "RESULT: " + (x + y);
            case "SUB":
                return "RESULT: " + (x - y);
            default:
                return "ERROR: Unknown command. Use ADD or SUB.";
        }
    }
}
