package com.gh.sammie.trackproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gh.sammie.trackproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private TextView txt_user_name, txt_user_email;
    private String current_user_id = "";
    private String userName, userEmail;
    private User user_info;
    private Button uid_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Init view
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        txt_user_email = findViewById(R.id.txt_user_email);
        uid_number = findViewById(R.id.uid_number);
        txt_user_name = findViewById(R.id.txt_user_name);


        if (mAuth.getCurrentUser() != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
            userEmail = mAuth.getCurrentUser().getEmail();
            userName = mAuth.getCurrentUser().getDisplayName();
        }

        if (!current_user_id.isEmpty()) {
            //user_info
            UserInfo();
        }
    }

    private void UserInfo() {

        DatabaseReference mUserDatabase = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_info = dataSnapshot.getValue(User.class);

                String userImage = null;

                //replace with do while loop
                try {
                    userImage = user_info.getImage();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.d("userInfo NUll", "onDataChange: ");
                }

                txt_user_email.setText(userEmail);
                uid_number.setText("User ID :  " +current_user_id);

                if (!TextUtils.isEmpty(userName)) {
                    txt_user_name.setText(userName);
                } else txt_user_name.setText(user_info.getName());


                /*Get user image */
//                if (!TextUtils.isEmpty(userImage)) {
//                    Picasso.get()
//                            .load(user_info.getImage())
////                            .error(R.drawable.antwi_image)
////                            .placeholder(R.drawable.antwi_image)
//                            .into(navUserPhot);
//
////                    user_image.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////
////
////                            AlertDialog.Builder builder;
////                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
////                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
////                            else
////                                builder = new AlertDialog.Builder(context);
////
////                            builder = new AlertDialog.Builder(context)
////                                    .setTitle("SignOut ???")
////                                    .setMessage("Are you sure on Action ?")
////                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////
////                                            mAuth.signOut();
////
////                                            // Google sign out
////                                            mGoogleSignInClient.signOut().addOnCompleteListener(HomeActivity.this,
////                                                    new OnCompleteListener<Void>() {
////                                                        @Override
////                                                        public void onComplete(@NonNull Task<Void> task) {
////                                                            Toast.makeText(context, "log out success", Toast.LENGTH_SHORT).show();
////                                                        }
////                                                    });
////
////                                            Intent a = new Intent(context, MainActivity.class);
////                                            startActivity(a);
////                                            finish();
////                                        }
////                                    })
////                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////
////                                            dialog.dismiss();
////                                        }
////                                    });
////
////                            builder.show();
////
////
////                        }
////                    });
//                }

//                    user_image.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void CloseView(View view) {
        finish();
    }
}