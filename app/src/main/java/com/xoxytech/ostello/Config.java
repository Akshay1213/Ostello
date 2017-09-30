package com.xoxytech.ostello;

/**
 * Created by akshay on 26/6/17.
 */

public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://ostallo.com/ostello/register.php";
    public static final String CONFIRM_URL = "http://ostallo.com/ostello/confirm.php";
    public static final String RESETPASSWORD_URL = "http://ostallo.com/ostello/resetpassword.php";
    public static final String FORGOTPASSWORD_URL = "http://ostallo.com/ostello/forgotpassword.php";
    public static final String LOGIN_URL = "http://ostallo.com/ostello/login_verification.php";
    public static final String AutoComplete_URL = "http://ostallo.com/ostello/fetchcities.php";
    public static final String SEARCHHOSTELS_URL = "http://ostallo.com/ostello/fetchhostels.php";
    public static final String History_URL = "http://ostallo.com/ostello/getUserHistory.php";
    public static final String VERIFYOTP_URL = "http://ostallo.com/ostello/checkotp.php";
    public static final String LIKESDISLIKES_URL = "http://ostallo.com/ostello/returnLikesOfHostel.php";
    public static final String DELETEACCOUNT_URL = "http://ostallo.com/ostello/DeleteUserAccount.php";
    public static final String SEARCHSPECIFICHOSTEL_URL = "http://ostallo.com/ostello/fetchrequestedhostel.php";
    public static final String ENQUIRY_URL = "http://ostallo.com/ostello/fetchownerdeatils.php";
    public static final String UPDATELIKEDISLIKE_URL = "http://ostallo.com/ostello/updatelikedislike.php";
    public static final String USERNAME_URL = "http://ostallo.com/ostello/getusername.php";
    public static final String FETCHONCLICKHOSTELS_URL = "http://ostallo.com/ostello/fetchhostelsonclick.php";
    public static final String CONTACTUS_URL = "http://ostallo.com/ostello/sendmail.php";
    public static final String FAVOURITES_URL = "http://ostallo.com/ostello/getFavouriteHostels.php";
    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";
}