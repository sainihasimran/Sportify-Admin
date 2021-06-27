package com.cegep.sportify_admin;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePickerLauncher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SignUpFragment extends Fragment {

    DatabaseReference mDatabase;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    int REQUEST_STORAGE=1;
    int REQUEST_FILE=2;

    public SignUpFragment() {
        // Required empty public constructor
    }
    private ImagePickerLauncher imagepickerLauncher = null;

    Button btnsign;
    TextInputLayout txtmail, txtpswd, brandname,txtcpswd;
    TextView tvlogin;
    ProgressBar bar;
    String stringpath;
    ImageView img;
    FirebaseAuth fauth;
    Uri imguri;
    Intent idata;


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
        img = (ImageView) view.findViewById(R.id.imgsignup);

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

                            String adminId = mDatabase.push().getKey();
                            String email = txtmail.getEditText().getText().toString();
                            Admin admin = new Admin(email,stringpath);
                            admin.adminId = adminId;
                            admin.brandname = bname;
                            mDatabase.child(adminId).setValue(admin);

                            Intent intent = new Intent(requireContext(), LoginActivity.class);


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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                } else {
                    pickImage();
                }
            }
        });
        return  view;

    }

    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_FILE && resultCode == RESULT_OK){
            if(data!=null)
            {
                imguri = data.getData();
                idata = data;

                try{
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imguri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    img.setImageBitmap(bitmap);

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                final StorageReference storageReference = storageRef.child("images/"+ UUID.randomUUID().toString());

                storageReference.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl = uri;
                                        stringpath = uri.toString();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getContext(), "Image cannot be uploaded.", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        }
    }


}
