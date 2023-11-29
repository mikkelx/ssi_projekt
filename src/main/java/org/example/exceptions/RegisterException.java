package org.example.exceptions;

import lombok.Getter;

@Getter
public class RegisterException extends Exception{

    private Integer entityId;

    private Object entityParam;

    private RegisterException(){}

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, int entityId) {
        super(message);
        this.entityId = entityId;
    }

    public RegisterException(String message, Object entityParam) {
        super(message);
        this.entityParam = entityParam;
    }
}
