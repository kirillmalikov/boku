package com.boku.boku.api.v1;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.boku.boku.service.merchant.MerchantService;
import com.boku.boku.service.payment.provider.ProviderService;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * {@link SmsController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmsControllerIntegrationTest {

    private static final String URI = "/v1/sms";
    private ClientAndServer merchantServer;
    private ClientAndServer providerServer;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProviderService providerService;

    @BeforeAll
    void beforeAll() {
        merchantServer = ClientAndServer.startClientAndServer();
        providerServer = ClientAndServer.startClientAndServer();
        ReflectionTestUtils.setField(
                merchantService,
                "url",
                String.format("http://localhost:%s/", merchantServer.getPort())
        );

        ReflectionTestUtils.setField(
                providerService,
                "url",
                String.format("http://localhost:%s/", providerServer.getPort())
        );
    }

    @BeforeEach
    void beforeEach() {
        merchantServer.reset();
        providerServer.reset();
    }

    @AfterAll
    void afterAll() {
        merchantServer.stop();
        providerServer.stop();
    }

    @ParameterizedTest
    @ValueSource(ints = { 200, 400, 500 })
    void withValidRequestRegardlessOfMerchantResponseStatusShouldReturnOk(int merchantStatus) throws Exception {
        var merchantResponse = new JSONObject().put("reply_message", "Where are we, Lieutenant Kitsuragi?");

        merchantServer
                .when(HttpRequest.request().withMethod("POST"))
                .respond(HttpResponse.response().withStatusCode(merchantStatus).withBody(merchantResponse.toString()));
        providerServer
                .when(HttpRequest.request().withMethod("GET"))
                .respond(HttpResponse.response().withStatusCode(200));

        mockMvc.perform(validRequest()).andExpect(status().isOk())
                .andExpect(jsonPath("$", is("OK")));
    }

    @Test
    void withProviderErrorShouldReturnNoContent() throws Exception {
        mockMvc.perform(validRequest()).andExpect(status().isNoContent());
    }

    @Test
    void withMissingParameterShouldReturnBadRequest() throws Exception {
        mockMvc.perform(invalidRequest())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath(
                        "$.message",
                        is("Required request parameter 'text' for method parameter type String is not present")
                ));
    }

    @Test
    void withInvalidTextShouldReturnBadRequest() throws Exception {
        mockMvc.perform(validRequest("INVALID TEXT", "13011"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath(
                        "$.message",
                        is("getContent.text: text must start with 'TXT' or 'FOR'")
                ));
    }

    @Test
    void withInvalidShortNumberShouldReturnBadRequest() throws Exception {
        mockMvc.perform(validRequest("TXT COINS", "13011a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath(
                        "$.message",
                        is("getContent.receiver: numeric value out of bounds (<10 digits>.<0 digits> expected)")
                ));
    }

    private MockHttpServletRequestBuilder validRequest(String text, String receiver) {
        return composeRequest(composeUrl(text, receiver).toString());
    }

    private MockHttpServletRequestBuilder validRequest() {
        return composeRequest(composeUrl("TXT COINS", "13011").toString());
    }

    private MockHttpServletRequestBuilder invalidRequest() {
        return composeRequest(composeUrl().toString());
    }

    private MockHttpServletRequestBuilder composeRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private StringBuilder composeUrl() {
        return new StringBuilder((URI))
                .append("?message_id=e39ce00e-f8b5-4b0b-96ce-d68f94525704")
                .append("&operator=Etisalat")
                .append("&sender=+37255555555")
                .append("&timestamp=2019-09-03 12:32:13");
    }

    private StringBuilder composeUrl(String text, String receiver) {
        return composeUrl()
                .append("&receiver=").append(receiver)
                .append("&text=").append(text);
    }
}