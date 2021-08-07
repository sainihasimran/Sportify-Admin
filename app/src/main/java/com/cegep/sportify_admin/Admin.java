package com.cegep.sportify_admin;

public class Admin {

    public String adminId;
    public String brandname;
    public String email;
    public String image;
    public String returnPolicyUrl;

    public Admin(){
        //default constructor
    }

    public Admin(String email,String image) {
        this.brandname = brandname;
        this.email = email;
        this.image = image;
    }

    public Admin(Admin admin) {
        this.brandname = admin.brandname;
        this.email = admin.email;
        this.image = admin.image;
    }
}
