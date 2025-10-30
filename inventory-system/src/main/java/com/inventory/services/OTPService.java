package com.inventory.services;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    public static final Map<String,String> otpStore=new HashMap<>();

    public static String generateOTP(String email){
        String otp=String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    public static boolean validateOTP(String email,String enteredOTP){
        String storedOTP=otpStore.get(email);
        if(storedOTP!=null && storedOTP.equals(enteredOTP)){
            otpStore.remove(email);
            return true;
        }
        return false;
    }
}
