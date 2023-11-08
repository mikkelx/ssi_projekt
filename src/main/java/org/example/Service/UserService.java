package org.example.Service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.util.RegisterRequest;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserService {

    private static UserService instance;

    private static UserDao userDao;

    private UserService() {
        userDao = UserDao.getInstance();
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



    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
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

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return false;
        }

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


    public String register(RegisterRequest registerRequest) {
        if(!isRegisterRequestValid(registerRequest)) {
            return "Register request is invalid";
        }

        if(checkIfUserExists(registerRequest.getEmail())) { //409 resource conflict
            return "User with email: " + registerRequest.getEmail() + " is already registered!";
        }

        if(!verifyPassword(registerRequest.getPassword(), registerRequest.getPasswordRepeated())) {
            return "Password is not matching conditions";
        }

        if(!isEmailValid(registerRequest.getEmail())) {
            return "Email is not matching requirements";
        }

    }


}
