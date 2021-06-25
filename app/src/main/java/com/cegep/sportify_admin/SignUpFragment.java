package com.cegep.sportify_admin;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class SignUpFragment extends Fragment {

    DatabaseReference mDatabase;

    public SignUpFragment() {
        // Required empty public constructor
    }
    //private ImagePickerLauncher imagepickerLauncher = null;
 //   private List<Image> images = new ArrayList<>();

    Button btnsign;
    TextInputLayout txtmail, txtpswd, brandname,txtcpswd;
    TextView tvlogin;
    ProgressBar bar;
    String fuser;

    FirebaseAuth fauth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        btnsign = view.findViewById(R.id.btn1);
        tvlogin = view.findViewById(R.id.btn2);
        brandname = (TextInputLayout) view.findViewById(R.id.bname);
        txtmail = (TextInputLayout) view.findViewById(R.id.email);
        txtpswd = (TextInputLayout) view.findViewById(R.id.pwd);
        txtcpswd = (TextInputLayout) view.findViewById(R.id.cnfpwd);
        bar = (ProgressBar) view.findViewById(R.id.progressBar3);

        fauth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("Admin");

//        if(fauth.getCurrentUser() != null)
//        {
//            LoginFragment fragment2 = new LoginFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment2);
//            fragmentTransaction.commit();
//
//        }

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);

                String email = txtmail.getEditText().getText().toString();
                String password = txtpswd.getEditText().getText().toString();
                String cpassword = txtcpswd.getEditText().getText().toString();
                String bname = brandname.getEditText().getText().toString();
//
                if (bname.isEmpty() == true)
                {
                    bar.setVisibility(View.INVISIBLE);
                    brandname.setError("Brand name field is empty!");
                }
                if(TextUtils.isEmpty(email))
                {
                    bar.setVisibility(View.INVISIBLE);
                    txtmail.setError("Email is Required.");
                }

                if(TextUtils.isEmpty(password))
                {
                    bar.setVisibility(View.INVISIBLE);
                    txtpswd.setError("Password is Required.");
                }

                if(password.length()<9) {
                    bar.setVisibility(View.INVISIBLE);
                    txtpswd.setError("Length of password is not less than 9.");
                }
                if(!password.equals(cpassword)){
                    bar.setVisibility(View.INVISIBLE);
                    txtcpswd.setError("Password not matched!");
                }

                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);

                            fuser = fauth.getCurrentUser().getUid();

                            LoginFragment fragment2 = new LoginFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment2);
                            fragmentTransaction.commit();
                            Toast.makeText(getActivity(), "User Registered", Toast.LENGTH_SHORT).show();

                            String email = txtmail.getEditText().getText().toString();
                            Admin admin = new Admin(email);
                            admin.brandname = bname;
                            mDatabase.push().setValue(admin);

                        } else {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Registeration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginFragment fragment2 = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                fragmentTransaction.commit();

            }
        });
        return  view;

    }
}