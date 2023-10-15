package com.example.ecommerce.utils;

public class Define {
    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_SELLER = 1;
    public static final int ROLE_CUSTOMER = 2;

    public static final String BUCKET_NAME = "huy.ecommerce";

    public static final String FOLDER_PRODUCT = "product/product_%d/";

    public static final String PATH_PRODUCT_URL = "product/product_%d/%d";

    public static final String PATH_AVATAR_URL = "avatar/avatar_%d/%d";


    public static final String FOLDER_AVATER = "avatar/";

    public static final String END_POINT_URL = "https://s3.ap-southeast-1.amazonaws.com/%s/%s";


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
}
