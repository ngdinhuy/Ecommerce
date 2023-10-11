package com.example.ecommerce.utils;

import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static ResponseEntity<BaseResponse> getResponse(Integer statusCodeResponse, String[] errors, Object data) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(statusCodeResponse, errors, data)
        );
    }

    public static void updateUser(User user, User updateUser) {
        user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setPhoneNumber(updateUser.getPhoneNumber());
        user.setDob(updateUser.getDob());
    }

    public static String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM YYYY");
        return localDate.format(formatter);
    }

    public static String validatePassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return "Password must be less than 20 and more than 8 characters";
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            return "Password must have atleast one uppercase character";
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            return "Password must have atleast one lowercase character";
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            return "Password must have atleast one number";
        }
        String specialChars = "(.*[@,#,$,%].*$)";
        if (!password.matches(specialChars)) {
            return "Password must have atleast one special character";
        }
        return "";
    }
}
