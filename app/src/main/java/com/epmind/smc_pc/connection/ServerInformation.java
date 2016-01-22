package com.epmind.smc_pc.connection;

import android.text.util.Linkify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gustavosmc on 16/12/15.
 */
public class ServerInformation {
    private static String VALIDATE_PATTERN = "\\((\\w|.){1,},\\d{3,},.{4,}\\)";


    private String information;
    private String pass;
    private String ip;
    private int port;

    public ServerInformation(String information) throws NoInformationException{
        if(!validateInformation(information)){
            throw new NoInformationException(NoInformationException.msg);
        }
        if(!assignsFields(information)){
            throw new NoInformationException();
        }
        this.information = information;
    }


    private boolean assignsFields(String information){
        information = information.replace("(", "").replace(")", "");
        String fields[] = information.split(",");
        try{
            this.ip = fields[0];
            this.port = Integer.parseInt(fields[1]);
            this.pass = fields[2];
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public boolean validateInformation(String information){
        Pattern pattern = Pattern.compile(VALIDATE_PATTERN);
        Matcher matcher = pattern.matcher(information);
        return (matcher.matches()) ? true : false;
    }


    public String getInformation() {
        return information;
    }

    public String getPass() {
        return pass;
    }


    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }

    public static String getValidatePattern() {
        return VALIDATE_PATTERN;
    }

    public static void setValidatePattern(String validatePattern) {
        VALIDATE_PATTERN = validatePattern;
    }




}
