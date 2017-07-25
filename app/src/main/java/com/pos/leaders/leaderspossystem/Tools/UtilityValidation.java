package com.pos.leaders.leaderspossystem.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Win8.1 on 7/4/2017.
 */

public class UtilityValidation {

    // FUNCTION TO VALIDATE THE EMAIL ...
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    //FUNCTION TO VALIDATE NAME ...
    public static boolean isValidName(String name){

        if (name.matches("")) {
            return false;
        }

        return true;
    }

    public static boolean isValidJob(String job){

        if (job.matches("")) {
            return false;
        }

        return true;
    }



    public static boolean isValidID(String id){

        if (id.matches("")) {
            return false;
        }

        return true;
    }


    public static boolean isValidGender(String gender){

        if (gender.matches("")) {
            return false;
        }

        return true;
    }
    //FUNCTION TO VALIDATE THE PHONE NUMBER
    public static boolean isValidMobile(String phoneNumber)
    {

        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+",phoneNumber ))
        {
            if(phoneNumber.length() < 6 || phoneNumber.length() > 13)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }


    // FUNCTION TO VALIDATE THE ADDRESS OF THE CONTACT
    public static boolean isValidAddress(String address){

        if (address.matches("")) {
            return false;
        }
        return true;
    }

    public static boolean isValidBirthdate(String birth){
        if(birth.matches(""))
        {
            return true;
        }
        return false;
    }



}
