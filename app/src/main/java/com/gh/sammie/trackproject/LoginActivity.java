package com.gh.sammie.trackproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private ProgressDialog mProgressDialog;
    private Button login;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);


        mAuth = FirebaseAuth.getInstance();

        //init variables
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinWithmail();
            }
        });



        /*End of on create */
    }

    private void signinWithmail() {
        String useremail = email.getText().toString();
        String userpassword = password.getText().toString();

        if (TextUtils.isEmpty(useremail)) {
            email.setError("Please enter your email");
        } else if (!useremail.contains("@") || !useremail.contains(".")) {
            email.setError("Please enter a valid Email");
        } else if (TextUtils.isEmpty(userpassword)) {
            password.setError("Please enter your password");
        } else {
            pdialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
            pdialog.setMessage("Please wait...");
            pdialog.setIndeterminate(true);
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
            pdialog.show();

            mAuth.signInWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                moveToDashBoard();
                                pdialog.dismiss();
                            } else {
                                String message = task.getException().toString();
                                if (message.contains("password is invalid")) {
                                    pdialog.dismiss();
                                    responseWarningSweetDialog("Sorry!!!", "Email or Password is Incorrect please check and try again");
//                                    Toast.makeText(LoginActivity.this, "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                                } else if (message.contains("There is no user")) {
                                    pdialog.dismiss();
//                                    Toast.makeText(LoginActivity.this, "Account doesn't exists", Toast.LENGTH_LONG).show();
                                    responseErrorSweetDialog("Sorry!!!", "Account doesn't exists please check well and try again");
                                } else {
                                    pdialog.dismiss();
                                    responseWarningSweetDialog("\uD83E\uDD37\u200D♂️Oops\uD83E\uDD37\u200D♂", "Unable to login kindly try again");
//                                    Toast.makeText(LoginActivity.this, "Unable to Login !", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }

    private void moveToDashBoard() {
        Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

    public void closeView(View view) {
        finish();
    }
}