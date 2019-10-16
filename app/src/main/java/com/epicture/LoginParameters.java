package com.epicture;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginParameters {

    static public void storeValues(OAuth2Values values, Context context){
        SharedPreferences preferences = context.getSharedPreferences("OAuth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", values.getAccess_token());
        editor.putLong("expires_in", values.getExpires_in());
        editor.putString("token_type", values.getToken_type());
        editor.putString("refresh_token", values.getRefresh_token());
        editor.putString("account_username", values.getAccount_username());
        editor.putString("account_id", values.getAccount_id());
        editor.apply();
    }

    static public OAuth2Values retrieveValues(Context context){
        SharedPreferences preferences = context.getSharedPreferences("OAuth", Context.MODE_PRIVATE);

        String access_token = preferences.getString("access_token", "");
        if (access_token.equals(""))
            return null;
        Long expires_in = preferences.getLong("expires_in", -1);
        String token_type = preferences.getString("token_type", "");
        String refresh_token = preferences.getString("refresh_token", "");
        String account_username = preferences.getString("account_username", "");
        String account_id = preferences.getString("account_id", "");
        OAuth2Values values = new OAuth2Values(access_token, Long.valueOf(0), token_type, refresh_token, account_username, account_id);
        values.setExpires_in(expires_in);
        return values;
    }

    static boolean isLogged(Context context){
        SharedPreferences preferences = context.getSharedPreferences("OAuth", Context.MODE_PRIVATE);

        String access_token = preferences.getString("access_token", "");
        if (access_token.equals(""))
            return false;
        return true;
    }
}
