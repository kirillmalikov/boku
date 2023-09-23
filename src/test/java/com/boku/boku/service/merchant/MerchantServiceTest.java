package com.boku.boku.service.merchant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.boku.boku.service.HttpClientService;
import com.boku.boku.service.merchant.model.MerchantRequest;
import com.boku.boku.service.merchant.model.MerchantResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    private static final String UNSUCCESSFUL_RESPONSE_MESSAGE = "There is nothing. Only warm, primordial blackness.";
    private static final String SUCCESSFUL_RESPONSE_MESSAGE = "You have taught me the meaning of wisdom, Cuno.";

    @Mock
    private HttpClientService httpClient;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MerchantService merchantService;

    @BeforeEach
    void beforeAll() {
        ReflectionTestUtils.setField(
                merchantService,
                "unsuccessfulResponse",
                UNSUCCESSFUL_RESPONSE_MESSAGE
        );
    }

    @Test
    void withUnsuccessfulResponseShouldReturnUnsuccsessfulMessage() throws Exception {
        HttpResponse response = mock(HttpResponse.class);

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(httpClient.sendPostRequest(any(), anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(400);

        var result = merchantService.request(mock(MerchantRequest.class));

        assertThat(result.reply_message()).isEqualTo(UNSUCCESSFUL_RESPONSE_MESSAGE);
    }

    @Test
    void withSuccessfulResponseShouldNotThrowException() throws Exception {
        HttpResponse response = mock(HttpResponse.class);

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(httpClient.sendPostRequest(any(), anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(objectMapper.readValue((String) response.body(), MerchantResponse.class)).thenReturn(new MerchantResponse(
                SUCCESSFUL_RESPONSE_MESSAGE));

        var result = merchantService.request(mock(MerchantRequest.class));

        assertThat(result.reply_message()).isEqualTo(SUCCESSFUL_RESPONSE_MESSAGE);
    }
}