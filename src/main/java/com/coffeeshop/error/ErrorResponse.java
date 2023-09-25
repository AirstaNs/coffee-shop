package com.coffeeshop.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;


@Schema(name = "ErrorResponse", description = "Error response")
public record ErrorResponse(HttpStatus status, String path, String message) {}