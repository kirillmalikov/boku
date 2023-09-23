package com.boku.boku.api.v1;

import com.boku.boku.command.Command;
import com.boku.boku.command.GetContentCommand.Parameter;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/sms")
@RequiredArgsConstructor
public class SmsController {

    private final Command<Parameter> command;

    @GetMapping
    public ResponseEntity<String> getContent(
            @RequestParam("message_id") String messageId,
            @RequestParam("sender") String sender,
            @RequestParam("text")
            @Pattern(regexp = "^(TXT|FOR).*", message = "text must start with 'TXT' or 'FOR'")
            String text,
            @RequestParam("receiver")
            @Digits(integer = 10, fraction = 0)
            String receiver,
            @RequestParam("operator") String operator,
            @RequestParam(value = "timestamp", required = false) String timestamp
    ) {
        try {
            command.execute(new Parameter(messageId, sender, text, receiver, operator));
        } catch (Exception e) {
            log.error("Error occurred during command execution", e);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
