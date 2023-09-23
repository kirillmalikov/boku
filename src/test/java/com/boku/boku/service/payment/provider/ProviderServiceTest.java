package com.boku.boku.service.payment.provider;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.boku.boku.service.HttpClientService;
import com.boku.boku.service.exception.ProviderException;
import java.net.http.HttpResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private HttpClientService httpClient;
    @InjectMocks
    private ProviderService providerService;

    @Test
    void withUnsuccessfulResponseShouldThrowException() throws Exception {
        HttpResponse response = mock(HttpResponse.class);

        when(httpClient.sendGetRequest(any(), anyMap(), anyString(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(400);

        assertThatExceptionOfType(ProviderException.class).isThrownBy(() -> providerService.request(Map.of()));
    }

    @Test
    void withSuccessfulResponseShouldNotThrowException() throws Exception {
        HttpResponse response = mock(HttpResponse.class);

        when(httpClient.sendGetRequest(any(), anyMap(), anyString(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);

        assertThatNoException().isThrownBy(() -> providerService.request(Map.of()));
    }
}