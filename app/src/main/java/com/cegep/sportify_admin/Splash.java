package com.cegep.sportify_admin;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.cegep.sportify_admin.settings.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.imageView);

        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                  /*  if ( !(fuser == null))
                    {
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        finish();
                        startActivity(i);
                    }

                   */

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    Intent i;
                    if (currentUser == null) {
                        i = new Intent(Splash.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Admin");
                        Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DataSnapshot adminSnapshot = null;
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    adminSnapshot = child;
                                }
                                Admin admin = adminSnapshot.getValue(Admin.class);
                                SportifyAdminApp.admin = admin;
                                Intent intent;
                                if (admin == null) {
                                    intent = new Intent(Splash.this, LoginActivity.class);
                                } else {
                                    intent = new Intent(Splash.this, HomeActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }



                }
            }
        };
        timer.start();

    }
}