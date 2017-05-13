package com.com2027.group03;

/**
 * Created by victor on 13.05.2017.
 */

public class User {
    private String nickname;
    private String email;
    private String unique_id;
    private String password;
    private int anonymity;
    //For future, to allow user to change password
    private String old_password;
    private String new_password;

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public int getAnonymity(){
        return anonymity;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setAnonymity(int anonymity){
        this.anonymity = anonymity;
    }

    public void setOld_password(String old_password){
        this.old_password = old_password;
    }

    public void setNew_password(String new_password){
        this.new_password = new_password;
    }
}
