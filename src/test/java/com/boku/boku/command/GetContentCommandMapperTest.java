package com.boku.boku.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;

import com.boku.boku.command.GetContentCommand.Parameter;
import com.boku.boku.service.exception.MerchantException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class GetContentCommandMapperTest {

    @Test
    public void testToMerchantRequestValidText() {
        var parameter = new Parameter("messageId", "+37255554444", "TXTValidText", "13011", "operator");

        var merchantRequest = GetContentCommandMapper.toMerchantRequest(parameter);

        assertThat(merchantRequest.shortcode()).isEqualTo(parameter.receiver());
        assertThat(merchantRequest.keyword()).isEqualTo("TXT");
        assertThat(merchantRequest.message()).isEqualTo(parameter.text());
        assertThat(merchantRequest.operator()).isEqualTo(parameter.operator());
        assertThat(merchantRequest.sender()).isEqualTo("37255554444");
        assertThat(merchantRequest.transaction_id()).isEqualTo(parameter.messageId());
    }

    @Test
    public void testToMerchantRequestInvalidText() {
        var parameter = new Parameter("messageId", "+372 5555 4444", "InvalidText", "13011", "operator");

        assertThatExceptionOfType(MerchantException.class).isThrownBy(() -> GetContentCommandMapper.toMerchantRequest(
                parameter));
    }

    @Test
    public void testToProviderParams() {
        var parameter = new Parameter("messageId", "+372 5555 4444", "TXTValidText", "13011", "operator");
        String message = "This is a test message";

        Map<String, String> providerParams = GetContentCommandMapper.toProviderParams(parameter, message);

        assertThat(providerParams).containsOnly(
                entry("message", "This+is+a+test+message"),
                entry("mo_message_id", parameter.messageId()),
                entry("operator", parameter.operator()),
                entry("receiver", "37255554444")
        );
    }
}
