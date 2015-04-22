package com.jinloes.secrets.util;

/**
 * Rest utilities.
 */
public class RestPreconditions {
    /**
     * Checks if the object is null. If the object is null,
     * then {@link ResourceNotFoundException} will be thrown.
     *
     * @param object object to check
     */
    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new ResourceNotFoundException();
        }
    }
}
