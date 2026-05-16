package com.di.architecture;

import com.di.annotations.Configuration;
import com.di.annotations.RestController;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class Server {
    private static int PORT_NUMBER = 8080;

    private final Map<String, Route> routes = new HashMap<>();

    public void registerRoute(Object bean, Method... methods) {
        for (var method : methods) {
            if (method.isAnnotationPresent(GET.class)) {
                String path = method.getAnnotation(GET.class).value();
                routes.put("GET " + path, new Route(bean, method));
                System.out.println("Registered route: GET " + path);
            } else if (method.isAnnotationPresent(POST.class)) {
                String path = method.getAnnotation(POST.class).value();
                routes.put("POST " + path, new Route(bean, method));
                System.out.println("Registered route: POST " + path);
            }
        }
    }

    public void run() {
        //Map<String, Route> routes = new HashMap<>();

//        for (Object bean : beans.values()) {
//            if (bean.getClass().isAnnotationPresent(RestController.class)) {
//                for (Method method : bean.getClass().getDeclaredMethods()) {
//                    if (method.isAnnotationPresent(GET.class)) {
//                        String path = method.getAnnotation(GET.class).value();
//                        routes.put("GET " + path, new Route(bean, method));
//                        System.out.println("Registered route: GET " + path);
//                    } else if (method.isAnnotationPresent(POST.class)) {
//                        String path = method.getAnnotation(POST.class).value();
//                        routes.put("POST " + path, new Route(bean, method));
//                        System.out.println("Registered route: POST " + path);
//                    }
//                }
//            }
//        }

        try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Listening on http://localhost:8080");

            while (true) {
                try (Socket socket = server.accept()) {
                    final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final var out = socket.getOutputStream();

                    // Read request line
                    var line = in.readLine();
                    if (line == null || line.isEmpty()) continue;

                    System.out.println("Request: " + line);
                    String[] requestParts = line.split(" ");
                    String httpMethod = requestParts[0];
                    String path = requestParts[1];

                    // Consume remaining headers
                    while (!(line = in.readLine()).isEmpty()) {
                        // Just consume
                    }

                    Route route = routes.get(httpMethod + " " + path);
                    String body;
                    int statusCode = 200;
                    String statusText = "OK";

                    if (route != null) {
                        try {
                            Object result = route.method().invoke(route.bean());
                            body = result != null ? result.toString() : "";
                        } catch (Exception e) {
                            e.printStackTrace();
                            statusCode = 500;
                            statusText = "Internal Server Error";
                            body = "Error: " + e.getMessage();
                        }
                    } else {
                        statusCode = 404;
                        statusText = "Not Found";
                        body = "404 Not Found";
                    }

                    final var response =
                            "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "\r\n" +
                            body;

                    out.write(response.getBytes());
                } catch (Exception e) {
                    System.err.println("Error handling request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER);
            System.exit(-1);
        }
    }

    private record Route(Object bean, Method method) {}
}
