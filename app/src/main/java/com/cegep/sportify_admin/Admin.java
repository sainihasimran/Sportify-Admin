package com.cegep.sportify_admin;

public class Admin {

    public String adminId;
    public String brandname;
    public String email;
    public String image;

    public Admin() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Admin(String email,String image) {
        this.brandname = brandname;
        this.email = email;
        this.image = image;
    }
}
