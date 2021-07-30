package com.cegep.sportify_admin.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cegep.sportify_admin.Admin;
import com.cegep.sportify_admin.LoginActivity;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    ImageView userImage;
    EditText emailId, brandName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userImage = view.findViewById(R.id.userImage);
        brandName = view.findViewById(R.id.brand_name_text);
        emailId = view.findViewById(R.id.email_input_text);

        view.findViewById(R.id.update_btn).setOnClickListener(v -> validateUpdateAdmin());
        view.findViewById(R.id.sign_out_btn).setOnClickListener(v -> onClick());

        userImage.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)     {
                Log.v("flag", "1");
                v.setSelected(true);
            }
        });
        brandName.setText(SportifyAdminApp.admin.brandname);
        emailId.setText(SportifyAdminApp.admin.email);

        setSelection(brandName);
        setSelection(emailId);
    }

    private void onClick() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }


    private void validateUpdateAdmin() {
        Admin newadmin = new Admin(SportifyAdminApp.admin);

        String brandname = brandName.getText().toString();
        String email = emailId.getText().toString();

        if (SportifyAdminApp.admin.brandname.equals(brandname) && SportifyAdminApp.admin.email.equals(email)) {
            endUpdateAdmin();
            return;
        } else {
            if (newadmin.email.equals(email)) {
                newadmin.brandname = brandname;

                updateAdmin(newadmin);
            } else {
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Sensitive operation performed. Please re-login", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        newadmin.brandname = brandname;
                        newadmin.email = email;

                        updateAdmin(newadmin);
                    }
                });
            }
        }
    }

    private void updateAdmin(Admin admin) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Admin").child(SportifyAdminApp.admin.adminId);
        userReference.setValue(admin).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SportifyAdminApp.admin = admin;
                endUpdateAdmin();
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endUpdateAdmin() {
        requireActivity().finish();
    }

    private void setSelection(EditText editText) {
        Selection.setSelection(editText.getText(), editText.length());
    }
}


