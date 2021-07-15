package com.gh.sammie.trackproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gh.sammie.trackproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserDashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private LinearLayout btn_track, btn_profile;
    private ImageView img_track_btn;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //Inits
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        btn_track = findViewById(R.id.btn_track);
        img_track_btn = findViewById(R.id.img_track_btn);
        btn_profile = findViewById(R.id.btn_profile);


        /* On click buttons*/
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                        Intent mainIntent = new Intent(UserDashboardActivity.this, TrackDashboardActivity.class);
                        Common.currentUser = user;
                        startActivity(mainIntent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        img_track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                        Log.d("DASHBOARD", "onDataChange: " + user.getName() + user.getUid());
                        Intent mainIntent = new Intent(UserDashboardActivity.this, SearchActivity.class);
                        Common.currentUser = user;
                        startActivity(mainIntent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }


    public void LogoutUser(View view) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to logout ?")
                .setContentText("You will have to login again to continue")
                .setConfirmText("Yes,log me out !")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        mAuth.signOut();
                        Intent mainIntent = new Intent(UserDashboardActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();


    }

    public void LikeBtnFunction(View view) {
        Toast.makeText(this, "Action not set by admin", Toast.LENGTH_SHORT).show();
    }

    public void feedbackClick(View view) {
        Toast.makeText(this, "Action not set by admin", Toast.LENGTH_SHORT).show();
    }


    public void expressServiceClick(View view) {
        Toast.makeText(this, "Action not set by admin", Toast.LENGTH_SHORT).show();
    }

    public void ImporterClick(View view) {
        Toast.makeText(this, "Action not set by admin", Toast.LENGTH_SHORT).show();
    }

    public void exporterClick(View view) {
        Toast.makeText(this, "Action not set by admin", Toast.LENGTH_SHORT).show();
    }
}