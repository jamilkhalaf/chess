package ui;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HandleClientRequest {

    public static String sendPostRequest(String url, String json) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()

                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.body();
    }
    public static String sendGameRequest(String url, String json) throws Exception {
        String authToken = PreLoginUI.getAuthToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()

                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.body();
    }

    public static String sendDeleteRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        String authToken = PreLoginUI.getAuthToken();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization",authToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return new String(connection.getInputStream().readAllBytes());
        } else {
            throw new Exception("Failed to logout: HTTP error code : " + responseCode);
        }
    }

    public static String sendGetRequest(String urlString) throws Exception {
        String authToken = PreLoginUI.getAuthToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("Authorization", authToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Failed to fetch games: HTTP error code : " + response.statusCode());
        }
    }

    public static String sendPutRequest(String url, String json) throws Exception {
        String authToken = PreLoginUI.getAuthToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.body();
    }
}
