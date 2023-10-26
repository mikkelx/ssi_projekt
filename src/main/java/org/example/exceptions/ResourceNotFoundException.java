package org.example.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ResourceNotFoundException extends Exception{

    private Integer entityId;

    private Object entityParam;

    private ResourceNotFoundException(){}

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, int entityId) {
        super(message);
        this.entityId = entityId;
    }

    public ResourceNotFoundException(String message, Object entityParam) {
        super(message);
        this.entityParam = entityParam;
    }
}
