package com.seawindsolution.podphotographer.Activity;

import java.util.regex.Pattern;

/**
 * Created by Ronak Gopani on 24/6/19 at 5:57 PM.
 */
public class Aadhaar {

    public static boolean validateAadharNumber(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
}
