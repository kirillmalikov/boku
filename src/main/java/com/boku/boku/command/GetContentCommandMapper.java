package com.boku.boku.command;

import com.boku.boku.command.GetContentCommand.Parameter;
import com.boku.boku.service.exception.MerchantException;
import com.boku.boku.service.merchant.model.MerchantRequest;
import java.util.Map;

public class GetContentCommandMapper {

    public static MerchantRequest toMerchantRequest(Parameter parameter) {
        var text = parameter.text();
        if (!(text.startsWith("TXT") || text.startsWith("FOR"))) {

            throw new MerchantException("Invalid keyword");
        }

        return new MerchantRequest(parameter.receiver(),
                                   text.substring(0, 3),
                                   text,
                                   parameter.operator(),
                                   parameter.sender().replace("+", ""),
                                   parameter.messageId()
        );
    }

    public static Map<String, String> toProviderParams(Parameter parameter, String message) {
        return Map.of(
                "message", message.replace(" ", "+"),
                "mo_message_id", parameter.messageId(),
                "receiver", parameter.sender().replace("+", "").replace(" ", ""),
                "operator", parameter.operator()
        );
    }
}
