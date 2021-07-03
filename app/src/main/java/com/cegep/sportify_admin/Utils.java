package com.cegep.sportify_admin;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    public static DatabaseReference getAdminReference() {
        return FirebaseDatabase.getInstance().getReference("Admin");
    }

    public static DatabaseReference getProductsReference() {
        return FirebaseDatabase.getInstance().getReference("Products");
    }

    public static DatabaseReference getEquipmentsReference() {
        return FirebaseDatabase.getInstance().getReference("Equipments");
    }

    public static DatabaseReference getSportWithTeamsReference() {
        return FirebaseDatabase.getInstance().getReference("SportWithTeams");
    }
}
