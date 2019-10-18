package com.epicture;
import java.util.Date;

/* CALLBACK URL:
        https://www.epicture.com/oauth2/callback#
        access_token=SOMETHING
        &expires_in=SOMETHING
        &token_type=bearer
        &refresh_token=SOMETHING
        &account_username=SOMETHING
        &account_id=SOMETHING
*/
public class OAuth2Values {
    private String access_token;
    private Long expires_in; //Milliseconds in EPOCH time
    private String token_type;
    private String refresh_token;
    private String account_username;
    private String account_id;

    public String getAccess_token() {
        return access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getAccount_username() {
        return account_username;
    }

    public String getAccount_id() {
        return account_id;
    }

    public OAuth2Values(String access_token, Long lifeTime, String token_type, String refresh_token, String account_username, String account_id){
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.account_username = account_username; //stam0325
        this. account_id = account_id;
        this.expires_in = lifeTime * 1000 + System.currentTimeMillis();
    }
}
