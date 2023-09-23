package com.boku.boku.command;

import static com.boku.boku.command.GetContentCommandMapper.toMerchantRequest;
import static com.boku.boku.command.GetContentCommandMapper.toProviderParams;

import com.boku.boku.command.GetContentCommand.Parameter;
import com.boku.boku.service.merchant.MerchantService;
import com.boku.boku.service.payment.provider.ProviderService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetContentCommand implements Command<Parameter> {

    private final MerchantService merchantService;
    private final ProviderService providerService;

    @Override
    public void execute(Parameter parameter) throws IOException, InterruptedException {
        var merchantResponse = merchantService.request(toMerchantRequest(parameter));
        providerService.request(toProviderParams(parameter, merchantResponse.reply_message()));
    }

    public record Parameter(
            String messageId,
            String sender,
            String text,
            String receiver,
            String operator
    ) {}
}
