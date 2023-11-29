package com.example.ecommerce.utils;

import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import org.apache.commons.logging.Log;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String DATE_FORMAT_1 = "dd/MM/yyyy";
    public static String DATE_FORMAT_2 = "HH:mm dd/MM";
    public static String DATE_FORMAT_3 = "dd/MM/year";
    public static String DATE_FORMAT_4 = "HH:mm";

    public static String DATE_FORMAT_5 = "MM/yyyy";



    public static ResponseEntity<BaseResponse> getResponse(Integer statusCodeResponse, String[] errors, Object data) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(statusCodeResponse, errors, data)
        );
    }

    public static void updateUser(User user, User updateUser) {
        if (!updateUser.getEmail().isBlank())
            user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setPhoneNumber(updateUser.getPhoneNumber());
        user.setDob(updateUser.getDob());
        user.setMailPaypal(updateUser.getMailPaypal());
    }

    public static String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return localDate.format(formatter);
    }

    public static Date getCurrentDateTime(){
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        String string = formatter.format(localDate);
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getCurrentDateAndMinute() {
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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

    public static String convertDateToString(String typeFormat, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(typeFormat);
        return sdf.format(date);
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String setUpDateChatList(Date date){
        LocalDateTime dateTime = Utils.convertToLocalDateTimeViaInstant(date);
        LocalDateTime current = LocalDateTime.now();

        Duration between = Duration.between(dateTime, current);
        //Nếu nhỏ hơn 60s hiện now
        if (between.getSeconds() < 60) {
            return "Now";
        }
        // Nhỏ hơn 1h thì hiện x phút trước
        if (between.toHours() < 1)
            return between.toMinutes() + " mins ago";

        if (between.toDays() < 1){
            return between.toHours() + " hours ago";
        }

        long year = ChronoUnit.YEARS.between(LocalDateTime.now(), LocalDateTime.now().plus(between) );
        if (year < 2){
            return Utils.convertDateToString(Utils.DATE_FORMAT_2, date);
        }
        return Utils.convertDateToString(DATE_FORMAT_1, date);
    }

    public static String setUpDateDetail(Date date){
        LocalDateTime dateTime = Utils.convertToLocalDateTimeViaInstant(date);
        LocalDateTime current = LocalDateTime.now();

        Duration between = Duration.between(dateTime, current);

        if (between.toDays() < 1){
            return Utils.convertDateToString(Utils.DATE_FORMAT_4, date);
        }

        long year = ChronoUnit.YEARS.between(LocalDateTime.now(), LocalDateTime.now().plus(between) );
        if (year < 2){
            return Utils.convertDateToString(Utils.DATE_FORMAT_2, date);
        }
        return Utils.convertDateToString(DATE_FORMAT_1, date);
    }

    public static<T> void reverseList(List<T> list)
    {
        // base case: the list is empty, or only one element is left
        if (list == null || list.size() <= 1) {
            return;
        }

        // remove the first element
        T value = list.remove(0);

        // recur for remaining items
        reverseList(list);

        // insert the top element back after recurse for remaining items
        list.add(value);
    }
}
