package org.example.exceptions;

import org.example.exceptions.ResourceNotFoundException;

import java.sql.SQLException;

import static spark.Spark.*;
import static spark.Spark.exception;

public class ExceptionHandler {
    private static spark.ExceptionHandler<ResourceNotFoundException> handleResourceNotFoundException = (e, request, response) -> {
        response.type("application/json");
        response.status(404);
        String message = "";
        if (e.getEntityId() != null) {
            message = String.format("Obiekt typu %s o id %s nie istnieje.", e.getMessage(), e.getEntityId());
        } else if (e.getEntityParam() != null) {
            message = String.format("Obiekt typu %s zawierający '%s' nie istnieje.", e.getMessage(), e.getEntityParam());
        }
        response.body(message);
    };

    private static spark.ExceptionHandler<SQLException> handleSQLException = (e, request, response) -> {
        response.type("application/json");
        response.status(500);
        response.body(e.getMessage());
    };

    public static void registerExceptions() {
        exception(ResourceNotFoundException.class, handleResourceNotFoundException);
        exception(SQLException.class, handleSQLException);
    }
}
