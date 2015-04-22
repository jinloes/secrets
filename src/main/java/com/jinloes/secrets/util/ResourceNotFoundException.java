package com.jinloes.secrets.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be thrown when a resource does not exist.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The requested resource could not be found.")
public class ResourceNotFoundException extends RuntimeException {
}
