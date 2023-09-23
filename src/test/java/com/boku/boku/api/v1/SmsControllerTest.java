package com.boku.boku.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.boku.boku.command.Command;
import com.boku.boku.command.GetContentCommand.Parameter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SmsControllerTest {

    @Mock
    private Command<Parameter> command;
    @InjectMocks
    private SmsController controller;

    @Test
    void withExceptionInCommandShouldReturnNoContent() throws Exception {
        doThrow(Exception.class).when(command).execute(any());

        assertThat(callEndpoint()).isEqualTo(new ResponseEntity<String>(NO_CONTENT));
    }

    @Test
    void withNoExceptionsInCommandShouldReturnOk() throws Exception {
        doNothing().when(command).execute(any());

        assertThat(callEndpoint()).isEqualTo(new ResponseEntity<String>("OK", OK));
    }

    private ResponseEntity<String> callEndpoint() {
        return controller.getContent(UUID.randomUUID().toString(),
                                     "+372 5555 4444",
                                     "TXT COINS",
                                     "13011",
                                     "Elisa",
                                     "2023-09-23 17:31:13"
        );
    }
}