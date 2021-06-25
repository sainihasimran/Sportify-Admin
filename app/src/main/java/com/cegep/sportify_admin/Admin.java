package com.cegep.sportify_admin;

public class Admin {

    public String brandname;
    public String email;
    public String Uid;

    public Admin() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Admin(String email) {
        this.brandname = brandname;
        this.email = email;
        this.Uid = Uid;
    }
}
