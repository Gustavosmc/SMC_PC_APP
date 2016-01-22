package com.epmind.smc_pc.connection;

/**
 * Created by gustavosmc on 14/12/15.
 */
public final class Comands {
    private Comands(){}

    public static final String SERVER_COMAND = " SC";
    public static final String DOWN_KEY = " DK";
    public static final String UP_KEY = " UK";
    public static final String CLICK_KEY = " CK";

    public static final String DOWN_MOUSE = " DM";
    public static final String UP_MOUSE = " UM";
    public static final String CLICK_MOUSE = " CM";
    public static final String MOVE_MOUSE = " MM";
    public static final String DEFINE_MOUSE = " RM";
    public static final String LONG_MOUSE = " LM";

    public static final String LEFT_SEPARATOR = "(";
    public static final String CENTER_SEPARATOR = ",";
    public static final String RIGHT_SEPARATOR =")";

    public static String getDownMouse(String key) {
        return (DOWN_MOUSE + LEFT_SEPARATOR + key + RIGHT_SEPARATOR);
    }

    public static String getLongClickMouse(String key){
        return (LONG_MOUSE+LEFT_SEPARATOR+key+RIGHT_SEPARATOR);
    }

    public static String getUpMouse(String key){
        return (UP_MOUSE+LEFT_SEPARATOR+key+RIGHT_SEPARATOR);
    }

    public static String getDownKey(String key){
        return (DOWN_KEY+ LEFT_SEPARATOR +key+ RIGHT_SEPARATOR);
    }

    public static String getUpKey(String key){
        return (UP_KEY+ LEFT_SEPARATOR +key+ RIGHT_SEPARATOR);
    }

    public static String getClickKey(String key){
        return (CLICK_KEY+ LEFT_SEPARATOR +key+ RIGHT_SEPARATOR);
    }

    public static String getMoveMouse(int x, int y){
        return(MOVE_MOUSE+ LEFT_SEPARATOR +x+CENTER_SEPARATOR+y+ RIGHT_SEPARATOR);
    }

    public static String getClickMouse(String key){
        return(CLICK_MOUSE+LEFT_SEPARATOR+key+RIGHT_SEPARATOR);
    }

    public static String getDefineMouse(int x, int y){
        return(DEFINE_MOUSE+ LEFT_SEPARATOR +x+CENTER_SEPARATOR+y+ RIGHT_SEPARATOR);

    }

}
