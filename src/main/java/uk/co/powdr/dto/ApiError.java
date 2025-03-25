package uk.co.powdr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {

    @Getter
    @JsonProperty("status")
    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("message")
    private String message;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ApiError message(String message) {
        this.message = message;
        return this;
    }
}
