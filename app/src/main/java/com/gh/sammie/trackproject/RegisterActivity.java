package com.gh.sammie.trackproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gh.sammie.trackproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER";
    private Button signup;
    private EditText fullname, email, password, cnfpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog pdialog;
    private DatabaseReference mDatabase;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Init
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cnfpassword = findViewById(R.id.cnfpassword);
        signup = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();


        /*End of oncreate */

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = fullname.getText().toString();
                final String mail = email.getText().toString();
                final String pwd = password.getText().toString();
                final String cnfpwd = cnfpassword.getText().toString();
                String newPass = Common.getEncryptedString(pwd);

                Log.d(TAG, "onClick: " + newPass);

                if (checkFields(name, mail, pwd, cnfpwd)) {
                    pdialog = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
                    pdialog.setMessage("Signing up...");
                    pdialog.setIndeterminate(true);
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.setCancelable(false);
                    pdialog.show();
                    createuser(mail, cnfpwd, name, newPass);
                }
            }
        });
    }

    private void createuser(final String email, final String password, final String name, String newPass) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(email, password, name, newPass);
                        } else {
                            if (task.getException().toString().contains("already in use")) {
                                pdialog.dismiss();
                                responseWarningSweetDialog("Sorry !", "User already exists, please sign in your correct credentials");
//                                Toast.makeText(RegisterActivity.this, "User already exists, please sign in !", Toast.LENGTH_LONG).show();
                            } else {
                                pdialog.dismiss();
//                                Toast.makeText(RegistryActivity.this, "" + password, Toast.LENGTH_SHORT).show();
                                responseErrorSweetDialog("Sorry!", "Something wrong happened please try again later or use Easy signUp with google");
//                                Toast.makeText(RegisterActivity.this, "Something wrong happened please try again later or use Easy signUp with google", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

    }

    private void signIn(final String email, final String password, final String name, String newPass) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserDetails(name, email, newPass);
                } else {
                    pdialog.dismiss();
                    responseErrorSweetDialog("🤷‍♂️Oops🤷‍♂", "Something went wrong please try again");
//                    Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void closeView(View view) {
        finish();
    }

    private boolean checkFields(String name, String mail, String pwd, String cnfpwd) {

        if (TextUtils.isEmpty(name)) {
            fullname.setError("Name can't be empty");
        }
        if (TextUtils.isEmpty(mail)) {
            email.setError("Email can't be empty");
        }
        if (TextUtils.isEmpty(pwd)) {
            password.setError("Please enter a password");
        }
        if (TextUtils.isEmpty(cnfpwd)) {
            cnfpassword.setError("Please confirm your password");
        } else if (name.length() < 3) {
            fullname.setError("Name must be of at least 3 characters");
        } else if (!mail.contains("@") || !mail.contains(".")) {
            email.setError("Please enter a valid email");
        } else if (pwd.length() < 6) {
            password.setError("Password must be of at least 6 characters");
        } else if (!cnfpwd.matches(pwd)) {
            password.setError("Passwords don't match !");
            cnfpassword.setError("Passwords don't match !");
        } else {
            return true;
        }

        return false;
    }


    private void saveUserDetails(String name, String email, String password) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = null;
        if (current_user != null) {
            uid = current_user.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Log.d(TAG, "saveUserDetails: " + password);

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("pwd", password);
        user.put("phone", "02412121200");
        user.put("uid", current_user.getUid());

        mDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    moveToDashBoard();
                    Toast.makeText(RegisterActivity.this, "Moving you to User DashBoard", Toast.LENGTH_SHORT).show();
//                    mDatabase.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            User user = snapshot.child(current_user.getUid()).getValue(User.class);
//                            Common.currentUser = user;
//                            Intent mainIntent = new Intent(RegisterActivity.this, UserDashboardActivity.class);
//                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(mainIntent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

//                    moveToDashBoard();

                } else
                    responseErrorSweetDialog("🤷‍♂️Oops🤷‍♂", "Something went wrong please try again");

            }
        });

    }

    private void moveToDashBoard() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                Log.d("LOGIN", "onDataChange: " + user.getName() + user.getUid());
                Intent mainIntent = new Intent(RegisterActivity.this, UserDashboardActivity.class);
                Common.currentUser = user;
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Intent mainIntent = new Intent(RegisterActivity.this, UserDashboardActivity.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
    }

    private void responseErrorSweetDialog(String txtTitle, String message) {
        Log.d("SAMMIE", "ResponseDialogPaymentSweetDialog: " + message);
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(txtTitle)
                .setContentText(message)
                .show();
    }

    private void responseWarningSweetDialog(String txtTitle, String message) {
        Log.d("SAMMIE", "ResponseDialogPaymentSweetDialog: " + message);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(txtTitle)
                .setContentText(message)
                .show();
    }
}