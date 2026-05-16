package com.di.architecture;

import com.di.annotations.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Configuration
public class Server {
    private static int PORT_NUMBER = 8080;

    public void run() {
        try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Listening on http://localhost:8080");

            while (true) {
                Socket socket = server.accept();

                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = socket.getOutputStream();

                // Read request line
                var line = in.readLine();

                // Consume remaining headers
                do {
                    System.out.println(line);
                } while (!(line = in.readLine()).isEmpty());

                final var body = "Hello from Java";

                final var response =
                        "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "\r\n" +
                        body;

                out.write(response.getBytes());
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER);
            System.exit(-1);
        }
    }
}
