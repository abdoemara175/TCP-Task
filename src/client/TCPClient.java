package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCPClient
 *
 * A simple TCP client that:
 * - Connects to the TCPServer on a given host and port.
 * - Reads user input from the console.
 * - Sends commands to the server and prints the responses.
 *
 * Supported commands (type them in the client console):
 *   ADD x y
 *   SUB x y
 *   EXIT   -> closes client connection
 */
public class TCPClient {

    // Change HOST if the server is running on a different machine.
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        System.out.println("TCP Client starting...");
        System.out.println("Connecting to server " + SERVER_HOST + ":" + SERVER_PORT + "...");

        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server.");

            // Read initial server messages (welcome text)
            String serverMessage;
            // Read until no more immediate lines (non-blocking pattern kept simple)
            // Here, just read two lines as sent from the server.
            for (int i = 0; i < 2; i++) {
                serverMessage = serverIn.readLine();
                if (serverMessage != null) {
                    System.out.println("SERVER: " + serverMessage);
                }
            }

            System.out.println();
            System.out.println("Type commands like: ADD 5 3 or SUB 10 4");
            System.out.println("Type EXIT to close the client.");

            String userLine;
            while (true) {
                System.out.print("CLIENT> ");
                userLine = userInput.readLine();

                if (userLine == null) {
                    // EOF on stdin
                    break;
                }

                userLine = userLine.trim();
                if (userLine.isEmpty()) {
                    continue;
                }

                // Send the line to the server
                serverOut.println(userLine);

                // Read and print server response
                String response = serverIn.readLine();
                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                System.out.println("SERVER> " + response);

                // If user requested EXIT, stop the client loop
                if (userLine.equalsIgnoreCase("EXIT")) {
                    break;
                }
            }

            System.out.println("Client terminated.");
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
