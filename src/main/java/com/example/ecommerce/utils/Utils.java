package com.example.ecommerce.utils;

import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
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

    public static File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
            File convFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convFile;
    }

    public static String getFileProductName(Integer id, Integer position){
        return "product/product_"+id+"/"+position;
    }

    public static String getFileName(String id){
        return "Product" + id + new Date().getTime();
    }

    public static String getCurrentMonth(){
        return (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + (Calendar.getInstance().get(Calendar.YEAR));
    }

    public static Double formatDouble(Double x){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedNumber = decimalFormat.format(x);
        return Double.parseDouble(formattedNumber);
    }
}
