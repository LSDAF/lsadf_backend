package com.lsadf.lsadf_backend.utils;


import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for response creation
 */
@UtilityClass
public class ResponseUtils {
    public static final String SUCCESS = "Success";

    /**
     * Builds a response
     * @param status HTTP status of the response
     * @param message Human readable message status
     * @param responseObj Object to return
     * @return ResponseEntity of GenericResponse containing all inputs
     * @param <T> Type of object to return
     */
    public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = generateGenericResponse(status, message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    /**
     * private method to build a GenericResponse
     * @param status HTTP status of the response
     * @param message Human readable message status
     * @param responseObj Object to return
     * @return GenericResponse containing all inputs
     * @param <T> Type of object to return
     */
    private static <T> GenericResponse<T> generateGenericResponse(HttpStatus status, String message, T responseObj) {
        return GenericResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(responseObj)
                .build();
    }
}
