package org.waw.project.ate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_EPROJECT_APP = "eProjectApp";

    public static final String SP_ID = "spID";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_API_TOKEN = "spAPITokem";
    public static final String SP_DEVICE_ID = "spDeviceID";



    public static final String SP_LOGGED_IN = "spAlreadyLoggedIn";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_EPROJECT_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPAPIToken(){
        return sp.getString(SP_API_TOKEN, "");
    }

    public String getSPName(){
        return sp.getString(SP_ID, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_LOGGED_IN, false);
    }

    public String getSPDeviceID(){
        return sp.getString(SP_DEVICE_ID, "");
    }

}
