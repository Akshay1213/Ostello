package com.xoxytech.ostello;

/**
 * Created by akshay on 26/6/17.
 */

public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://ostallo.com/ostello/register.php";
    public static final String CONFIRM_URL = "http://ostallo.com/ostello/confirm.php";
    public static final String LOGIN_URL = "http://ostallo.com/ostello/login_verification.php";
    public static final String AutoComplete_URL = "http://ostallo.com/ostello/fetchcities.php";
    public static final String SEARCHHOSTELS_URL = "http://ostallo.com/ostello/fetchhostels.php";
    public static final String SEARCHSPECIFICHOSTEL_URL = "http://ostallo.com/ostello/fetchrequestedhostel.php";
    public static final String FETCHONCLICKHOSTELS_URL = "http://ostallo.com/ostello/fetchhostelsonclick.php";
    public static final String CONTACTUS_URL = "http://ostallo.com/ostello/sendmail.php";
    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";
}