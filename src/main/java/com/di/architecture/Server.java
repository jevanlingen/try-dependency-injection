package com.di.architecture;

import com.di.annotations.Configuration;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Server {
    private static int PORT_NUMBER = 8080;

    private final List<Route> routes = new ArrayList<>();

    public void registerRoute(Object bean, Method... methods) {
        for (var method : methods) {
            String httpMethod = null;
            String path = null;
            if (method.isAnnotationPresent(GET.class)) {
                httpMethod = "GET";
                path = method.getAnnotation(GET.class).value();
            } else if (method.isAnnotationPresent(POST.class)) {
                httpMethod = "POST";
                path = method.getAnnotation(POST.class).value();
            }

            if (httpMethod != null) {
                routes.add(new Route(httpMethod, path, bean, method));
                System.out.println("Registered route: " + httpMethod + " " + path);
            }
        }
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Listening on http://localhost:8080");

            while (true) {
                try (Socket socket = server.accept()) {
                    final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final var out = socket.getOutputStream();

                    var line = in.readLine();
                    if (line == null || line.isEmpty()) continue;

                    System.out.println("Request: " + line);
                    String[] requestParts = line.split(" ");
                    String httpMethod = requestParts[0];
                    String path = requestParts[1];

                    while (!(line = in.readLine()).isEmpty()) { }

                    String body;
                    int statusCode = 200;
                    String statusText = "OK";

                    MatchedRoute matched = findRoute(httpMethod, path);

                    if (matched != null) {
                        try {
                            Object[] args = convertArgs(matched.route().method(), matched.params());
                            Object result = matched.route().method().invoke(matched.route().bean(), args);
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

    private MatchedRoute findRoute(String httpMethod, String path) {
        String[] pathSegments = path.split("/");
        for (Route route : routes) {
            if (!route.httpMethod().equals(httpMethod)) continue;
            
            String[] routeSegments = route.path().split("/");
            if (pathSegments.length != routeSegments.length) continue;

            List<String> params = new ArrayList<>();
            boolean match = true;
            for (int i = 0; i < routeSegments.length; i++) {
                if (routeSegments[i].startsWith("{") && routeSegments[i].endsWith("}")) {
                    params.add(pathSegments[i]);
                } else if (!routeSegments[i].equals(pathSegments[i])) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return new MatchedRoute(route, params);
            }
        }
        return null;
    }

    private Object[] convertArgs(Method method, List<String> params) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            if (i < params.size()) {
                String val = params.get(i);
                if (paramTypes[i] == int.class || paramTypes[i] == Integer.class) {
                    args[i] = Integer.parseInt(val);
                } else {
                    args[i] = val;
                }
            }
        }
        return args;
    }

    private record Route(String httpMethod, String path, Object bean, Method method) {}
    private record MatchedRoute(Route route, List<String> params) {}
}
