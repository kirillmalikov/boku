package com.boku.boku.service.payment.provider;

import com.boku.boku.service.exception.ProviderException;
import com.boku.boku.service.HttpClientService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderService {

    @Value(value = "${boku.provider.url}")
    private String url;
    @Value(value = "${boku.provider.user}")
    private String user;
    @Value(value = "${boku.provider.password}")
    private String password;

    private final HttpClientService httpService;

    public void request(Map<String, String> queryParams) throws IOException, InterruptedException {
        var response = httpService.sendGetRequest(url, queryParams, HttpHeaders.AUTHORIZATION, getAuth());

        if (response.statusCode() != 200) {
            log.error("Unsuccessful response from provider service {}", response.statusCode());

            throw new ProviderException("Provider error");
        }
    }

    private String getAuth() {
        return httpService.getBasicAuthHeaderValue(user, password);
    }
}
