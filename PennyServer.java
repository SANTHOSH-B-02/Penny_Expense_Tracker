import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;

/**
 * Penny expense tracker backend.
 * Java serves the HTML page, provides the /api/state endpoint,
 * and stores application data in penny-data.json.
 */
public class PennyServer {
    private static final int PORT = 8080;
    private static final Path DATA_FILE = Paths.get("penny-data.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object DATA_LOCK = new Object();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/state", PennyServer::handleState);
        server.createContext("/", PennyServer::handleStatic);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Penny is running at http://localhost:" + PORT);
        System.out.println("Data file: " + DATA_FILE.toAbsolutePath());
        System.out.println("Press Ctrl+C to stop the server.");
    }

    private static void handleState(HttpExchange exchange) throws IOException {
        addCommonHeaders(exchange.getResponseHeaders());

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            synchronized (DATA_LOCK) {
                if (Files.exists(DATA_FILE)) {
                    byte[] data = Files.readAllBytes(DATA_FILE);
                    send(exchange, 200, "application/json; charset=utf-8", data);
                } else {
                    send(exchange, 200, "application/json; charset=utf-8",
                        "{\"expenses\":[],\"budget\":{\"total\":0,\"cats\":{}},\"cur\":\"$\"}"
                            .getBytes(StandardCharsets.UTF_8));
                }
            }
            return;
        }

        if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            try (InputStream input = exchange.getRequestBody()) {
                String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject state = JsonParser.parseString(body).getAsJsonObject();

                // Keep a predictable data shape and reject malformed requests.
                if (!state.has("expenses") || !state.get("expenses").isJsonArray()) {
                    sendText(exchange, 400, "Invalid state: expenses must be an array.");
                    return;
                }
                if (!state.has("budget") || !state.get("budget").isJsonObject()) {
                    state.add("budget", new JsonObject());
                }
                if (!state.has("cur")) {
                    state.addProperty("cur", "$");
                }

                synchronized (DATA_LOCK) {
                    Path temp = Paths.get(DATA_FILE.toString() + ".tmp");
                    Files.writeString(temp, GSON.toJson(state), StandardCharsets.UTF_8);
                    Files.move(temp, DATA_FILE, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
                }
                sendText(exchange, 200, "Saved");
            } catch (Exception ex) {
                sendText(exchange, 400, "Could not save data: " + ex.getMessage());
            }
            return;
        }

        exchange.getResponseHeaders().set("Allow", "GET, PUT");
        sendText(exchange, 405, "Method not allowed");
    }

    private static void handleStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path == null || "/".equals(path) || "/index.html".equals(path)) {
            try (InputStream in = PennyServer.class.getResourceAsStream("/static/index.html")) {
                if (in == null) {
                    sendText(exchange, 500, "Could not find static/index.html in the application.");
                    return;
                }
                send(exchange, 200, "text/html; charset=utf-8", in.readAllBytes());
            }
            return;
        }
        sendText(exchange, 404, "Not found");
    }

    private static void addCommonHeaders(Headers headers) {
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("Cache-Control", "no-store");
    }

    private static void sendText(HttpExchange exchange, int status, String message) throws IOException {
        send(exchange, status, "text/plain; charset=utf-8", message.getBytes(StandardCharsets.UTF_8));
    }

    private static void send(HttpExchange exchange, int status, String contentType, byte[] body)
            throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}
