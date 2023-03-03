package kvServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
public class KVServer {
	public static final int PORT = 8078;
	private final String apiToken;
	private final HttpServer server;
	private final Map<String, String> data = new HashMap<>();

	public KVServer() throws IOException {
		apiToken = generateApiToken();
		server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
		server.createContext("/register", this::register);
		server.createContext("/save", this::save);
		server.createContext("/load", this::load);
	}

	private void load(HttpExchange h) throws IOException {
		try {
			if (!hasAuth(h)) {
				System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				h.sendResponseHeaders(403, 0);
				return;
			}

			if ("GET".equals(h.getRequestMethod())) {
				server.createContext("/load/tasks", this::returnTasks);
				server.createContext("/load/subtasks", this::returnSubTasks);
				server.createContext("/load/epics", this::returnEpics);
				server.createContext("/load/history", this::returnHistory);
			} else {
				System.out.println("/load ждёт GET-запрос, а получил: " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
		} finally {
			h.close();
		}
	}

	private void returnHistory(HttpExchange exchange) throws IOException {
		try{
			System.out.println("\n/load/history");
			if(data.containsKey("history")){
				sendText(exchange, data.get("history"));
			} else {
				System.out.println("Ключа /history не существует.");
				exchange.sendResponseHeaders(400, 0);
			}
		}finally {
			exchange.close();
		}
	}

	private void returnEpics(HttpExchange exchange) throws IOException{
		try{
			System.out.println("\n/load/epics");
			if(data.containsKey("epics")){
				sendText(exchange, data.get("epics"));
			} else {
				System.out.println("Ключа /epics не существует.");
				exchange.sendResponseHeaders(400, 0);
			}
		}finally {
			exchange.close();
		}
	}

	private void returnSubTasks(HttpExchange exchange) throws IOException{
		try{
			System.out.println("\n/load/subtasks");
			if(data.containsKey("subtasks")){
				sendText(exchange, data.get("subtasks"));
			} else {
				System.out.println("Ключа /subtasks не существует.");
				exchange.sendResponseHeaders(400, 0);
			}
		}finally {
			exchange.close();
		}
	}

	private void returnTasks(HttpExchange exchange) throws IOException{
		try{
			System.out.println("\n/load/tasks");

			if(data.containsKey("tasks")){
				sendText(exchange, data.get("tasks"));
			} else {
				System.out.println("Ключа /tasks не существует.");
				exchange.sendResponseHeaders(400, 0);
			}
		} finally {
			exchange.close();
		}
	}

	
	private void save(HttpExchange h) throws IOException {
		try {
			System.out.println("\n/save");
			if (!hasAuth(h)) {
				System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				h.sendResponseHeaders(403, 0);
				return;
			}
			if ("POST".equals(h.getRequestMethod())) {
				String key = h.getRequestURI().getPath().substring("/save/".length());
				if (key.isEmpty()) {
					System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
					h.sendResponseHeaders(400, 0);
					return;
				}
				String value = readText(h);
				if (value.isEmpty()) {
					System.out.println("Value для сохранения пустой. value указывается в теле запроса");
					h.sendResponseHeaders(400, 0);
					return;
				}
				data.put(key, value);
				System.out.println("Значение для ключа " + key + " успешно обновлено!");
				h.sendResponseHeaders(200, 0);
			} else {
				System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
		} finally {
			h.close();
		}
	}

	private void register(HttpExchange h) throws IOException {
		try {
			System.out.println("\n/register");
			if ("GET".equals(h.getRequestMethod())) {
				sendText(h, apiToken);
			} else {
				System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
		} finally {
			h.close();
		}
	}

	public void start() {
		System.out.println("Запускаем сервер на порту " + PORT);
		System.out.println("Открой в браузере http://localhost:" + PORT + "/");
		System.out.println("API_TOKEN: " + apiToken);
		server.start();
	}

	private String generateApiToken() {
		return "" + System.currentTimeMillis();
	}

	protected boolean hasAuth(HttpExchange h) {
		String rawQuery = h.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
	}

	protected String readText(HttpExchange h) throws IOException {
		return new String(h.getRequestBody().readAllBytes(), UTF_8);
	}

	protected void sendText(HttpExchange h, String text) throws IOException {
		byte[] resp = text.getBytes(UTF_8);
		h.getResponseHeaders().add("Content-Type", "application/json");
		h.sendResponseHeaders(200, resp.length);
		h.getResponseBody().write(resp);
	}

	public void stop(int delay){
		server.stop(delay);
	}
}
