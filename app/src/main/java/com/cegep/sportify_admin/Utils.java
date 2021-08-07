package com.cegep.sportify_admin;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    public static final String ORDER_PENDING = "pending";

    public static final String ORDER_ACCEPTED = "accepted";

    public static final String ORDER_DECLINED = "declined";

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

    public static FirebaseDatabase getClientDatabase() {
        return FirebaseDatabase.getInstance(FirebaseApp.getInstance(SportifyAdminApp.CLIENT_FIREBASE));
    }
}
