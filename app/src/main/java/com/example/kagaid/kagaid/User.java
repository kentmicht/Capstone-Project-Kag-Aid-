package com.example.kagaid.kagaid;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class User {
    private String firstname;
    private String lastname;
    private String password;
    private String uId;
    private String username;

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String username) {
        this.uId = uId;
    }


    public User(String firstname, String lastname, String password, String uId, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.uId = uId;
        this.username = username;
    }

    public User() {
    }


}
