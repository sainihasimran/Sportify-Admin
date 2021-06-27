package com.cegep.sportify_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.cegep.sportify_admin.settings.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
                    } else {
                        i = new Intent(Splash.this, HomeActivity.class);
                    }
                    startActivity(i);

                    finish();

                }
            }
        };
        timer.start();

    }
}