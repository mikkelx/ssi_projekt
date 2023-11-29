package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;
import org.example.general.Message;
import org.example.request.LoginRequest;
import org.example.request.RegisterRequest;

import java.sql.SQLException;

import static spark.Spark.halt;

public class UserService {

    private static UserService instance;

    private static UserDao userDao;

    private static SecurityService securityService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private UserService() {
        userDao = UserDao.getInstance();
        securityService = SecurityService.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public String register(RegisterRequest registerRequest) {
        if(!isRegisterRequestValid(registerRequest)) {
            return "Register request is invalid";
        }

        if(checkIfUserExists(registerRequest.getUsername())) { //409 resource conflict
            return "User with username: " + registerRequest.getUsername() + " is already registered!";
        }

        if(!verifyPassword(registerRequest.getPassword(), registerRequest.getPasswordRepeated())) {
            return "Password is not matching conditions";
        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                "CLIENT"
        );

        String id = null;
        try {
            id = String.valueOf(userDao.getUserDao().create(user));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public String login(LoginRequest loginRequest) {
        try {
            User user = userDao.getUserByUsername(loginRequest.getUsername());
            if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
                halt(401, "Invalid username or password");
            }
            if (user.getBlocked()) {
                halt(401, "User is blocked");
            }
            Message jwt = new Message(securityService.createJWT(user.getId().toString(), user.getUsername(), user.getRole()));

            return objectMapper.writeValueAsString(jwt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verifyPassword(String password, String repeatedPassword) {
        if(!password.equals(repeatedPassword))
            return false;

        if (password.length() < 8 || password.length() > 25) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasUppercase && hasLowercase && hasDigit) {
                break;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    private boolean isRegisterRequestValid(RegisterRequest request) {
        if (request == null)
            return false;


        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return false;
        }

        if (request.getPasswordRepeated() == null || request.getPasswordRepeated().isEmpty()) {
            return false;
        }

        if (!request.getPassword().equals(request.getPasswordRepeated())) {
            return false;
        }

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean checkIfUserExists(String username) {
        User user = null;
        try {
            user = userDao.getUserByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException("Error during user verification with username: " + username);
        } catch (ResourceNotFoundException e) {
            return false;
        }

        return user != null;
    }




}
