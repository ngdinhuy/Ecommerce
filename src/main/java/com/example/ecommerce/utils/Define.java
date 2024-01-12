package com.example.ecommerce.utils;

import org.springframework.data.relational.core.sql.In;

import java.util.ArrayList;
import java.util.List;

public class Define {
    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_SELLER = 1;
    public static final int ROLE_CUSTOMER = 2;

    public static final int NUMBER_PER_PAGE = 10;

    //paypal
    public static final String CLIENT_ID = "AWC3WiwnQ4HQAmvLqlXehCbmG4S0PTnZ1_7BpuDMQ5r595i0-HGEesLHw1j0gSK7_VH-sepTyyEbQA19";
    public static final String PRIVATE_KEY = "ENN7_MWVSjBI54pgVJ1uEd0yjXzSBkBxhFfYpWsh4e82qzCFVaZdVDgNMzd4BcglXFJCtKks2SGm_e6i";

    //AWS S3

    public static final String BUCKET_NAME = "huy.ecommerce";
    public static final String FOLDER_PRODUCT = "product/product_%d/";
    public static final String PATH_PRODUCT_URL = "product/product_%d/%d";
    public static final String PATH_AVATAR_URL = "avatar/avatar_%d/%d";

    public static final String FILE_PATH = "/Users/mac/Documents/Django/ChatBot/context_data/data";
    public static final String FILE_NAME = "product_%d.txt";
    public static final String PRODUCT_PATH = "train/product_%d";

    public static final String SCHEME_PRODUCT_URL = "https://s3.ap-southeast-1.amazonaws.com/huy.ecommerce/";

    public static final String FOLDER_AVATER = "avatar/";
    public static final String END_POINT_URL = "https://s3.ap-southeast-1.amazonaws.com/%s/%s";

    //url chatbot
    public static final String URL_CHAT = "http://127.0.0.1:8000/api/";


    public static final int ERROR_CODE = -1;

    public static String SUCCESS = "success";

    public static String ERROR = "error";
    public static String NOT_FOUND = "Username is incorrect!";
    public static String PASSWORD_IS_INCORRECT="Password is incorrect!";

    public static String USERNAME_WAS_EXIST = "Username was exist!";

    public static String USER_CART_IS_NOT_EXIST = "User cart is not exist!";

    public static class FilferType{
        public static Integer CUSTOMER_REVIEW = 1;
        public static Integer PRICE_LOWEST_TO_HIGH = 2;
        public static Integer PRICE_HIGHEST_TO_LOW = 3;

    }

    public static String[] listMonth = {
            "1/2023", "2/2023", "3/2023", "4/2023", "5/2023", "6/2023", "7/2023", "8/2023", "9/2023", "10/2023", "11/2023", "12/2023"
    };

    public static class StatisticType{
        public static Integer ORDER_CURRENT_MONTH = 1;
        public static Integer RATE_CURRENT_MONTH = 2;
    }

    public static class StatePayment{
        public static Integer POST_PAYMENT = 1;
        public static Integer PAYMENT_WITH_PAYPAL = 2;
    }

    public static class UserChatFrom{
        public static Integer FROM_BOT = 0;
    }

    public static class StatusReadMessage{
        public static Integer UNREAD = 0;
        public static Integer READ = 1;
    }
}
