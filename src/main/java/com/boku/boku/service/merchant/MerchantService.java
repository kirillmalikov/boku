package com.boku.boku.service.merchant;

import com.boku.boku.service.HttpClientService;
import com.boku.boku.service.merchant.model.MerchantRequest;
import com.boku.boku.service.merchant.model.MerchantResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantService {

    @Value(value = "${boku.merchant.url}")
    private String url;
    @Value(value = "${boku.merchant.response}")
    private String unsuccessfulResponse;

    private final HttpClientService httpService;
    private final ObjectMapper objectMapper;

    public MerchantResponse request(MerchantRequest request) throws IOException, InterruptedException {
        var jsonBody = objectMapper.writeValueAsString(request);
        var response = httpService.sendPostRequest(url + request.keyword().toLowerCase(), jsonBody);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), MerchantResponse.class);
        }
        log.error("Unsuccessful response from merchant service");

        return new MerchantResponse(unsuccessfulResponse);
    }
}
