package com.cegep.sportify_admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    EditText admin_email, admin_pass;
    Button signin_btn;
    TextView joinus;
    NavController navController;

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

        navController = Navigation.findNavController(getActivity(), R.id.signin_host);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Networkconfig networkCheck = new Networkconfig();

                if (!checkEmptyFields()) {
                    String a_email = admin_email.getText().toString();
                    String a_pass = admin_pass.getText().toString();
                    doSignin(a_email, a_pass);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // For sending in drashboard and not login each time
            updateUI(firebaseUser);
            Toast.makeText(getActivity().getApplicationContext(), "User Already Signin.", Toast.LENGTH_SHORT).show();
        }
    }

    private void doSignin(String email, String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), task -> {
                     if (task.isSuccessful())
                     {
                        Toast.makeText(getActivity().getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                        firebaseUser = firebaseAuth.getCurrentUser();
                         updateUI(firebaseUser);
                     } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Authenticate Failed!", Toast.LENGTH_SHORT).show();
                     }
                });
    }

    public void updateUI(FirebaseUser user)
    {
        Bundle b = new Bundle();
        b.putParcelable("user",user);
        navController.navigate(R.id.dashboard,b);
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