package com.akin.speller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class KlavyeApi {
    private static Trie trie = new Trie();

    public static void main(String[] args) throws Exception {
        System.out.println("Veri seti yükleniyor...");
        veriSetiniYukle("words.txt");

        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        
        server.createContext("/api/oneriler", new OneriHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("🚀 Backend API Sunucusu Başladı!");
        System.out.println("Frontend için hazır... Port: 8080");
    }

    
    static class OneriHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            
            String query = exchange.getRequestURI().getQuery();
            String prefix = "";
            if (query != null && query.startsWith("q=")) {
                prefix = query.substring(2);
            }

            
            List<String> oneriler = trie.getSuggestions(prefix, 3);

            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < oneriler.size(); i++) {
                json.append("\"").append(oneriler.get(i)).append("\"");
                if (i < oneriler.size() - 1) json.append(",");
            }
            json.append("]");

            
            byte[] response = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);

            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    
    private static void veriSetiniYukle(String dosyaYolu) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosyaYolu), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    trie.insert(line.trim());
                }
            }
            System.out.println("Kelimeler belleğe yüklendi!");
        } catch (Exception e) {
            System.out.println("Hata: words.txt bulunamadı!");
        }
    }
}
