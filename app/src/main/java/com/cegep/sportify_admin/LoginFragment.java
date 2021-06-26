package com.cegep.sportify_admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cegep.sportify_admin.settings.ProfileActivity;
import com.cegep.sportify_admin.settings.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText admin_email, admin_pass;
    Button signin_btn;
    TextView joinus;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        admin_email = view.findViewById(R.id.email_et);
        admin_pass = view.findViewById(R.id.password_et);
        signin_btn = view.findViewById(R.id.Signin);
        joinus = view.findViewById(R.id.signup);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Login clicked");
                if (!checkEmptyFields()) {
                    String a_email = admin_email.getText().toString();
                    String a_pass = admin_pass.getText().toString();
                    doSignin(a_email, a_pass);
                }
            }
        });

        joinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here you can redirect to SignUp fragment

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // For sending in drashboard and not login each time
            Toast.makeText(getActivity().getApplicationContext(), "User Already Signin.", Toast.LENGTH_SHORT).show();
        }
    }

    private void doSignin(String email, String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), ProfileActivity.class);
                            startActivity(intent);
                        }

                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Authenticate Failed!", Toast.LENGTH_SHORT).show();
                     }
                });
    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(admin_email.getText().toString()))
        {
            admin_email.setError("Email cannot be empty!");
            admin_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(admin_pass.getText().toString()))
        {
            admin_pass.setError("Password cannot be empty!");
            admin_pass.requestFocus();
            return true;
        }
        return false;
    }
}