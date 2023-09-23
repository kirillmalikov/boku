package com.boku.boku.service;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpClientService {

    private final HttpClient httpClient;

    public HttpResponse<String> sendPostRequest(String uri, String jsonBody, String... headers)
            throws IOException, InterruptedException {

        var request = HttpRequest
                .newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .uri(URI.create(uri))
                .POST(BodyPublishers.ofString(jsonBody));

        if (headers.length > 1) {
            request.headers(headers);
        }

        return httpClient.send(request.build(), BodyHandlers.ofString());
    }

    public HttpResponse<String> sendGetRequest(String uri, Map<String, String> queryParams, String... headers)
            throws IOException, InterruptedException {
        var request = HttpRequest
                .newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .uri(URI.create(uriWithParams(uri, queryParams)))
                .GET();

        if (headers.length > 1) {
            request.headers(headers);
        }

        return httpClient.send(request.build(), BodyHandlers.ofString());
    }

    public String getBasicAuthHeaderValue(String user, String password) {
        return "Basic " + Base64Util.encode(user + ":" + password);
    }

    private String uriWithParams(String url, Map<String, String> queryParams) {
        var uri = new StringBuilder(url).append("?");
        queryParams.forEach((key, value) -> uri.append(key).append("=").append(value).append("&"));

        return uri.toString();
    }
}
