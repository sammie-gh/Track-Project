package com.gh.sammie.trackproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.apps.norris.paywithslydepay.core.PayWithSlydepay;
import com.gh.sammie.trackproject.model.Goods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gh.sammie.trackproject.Common.priceToString;
import static com.gh.sammie.trackproject.TrackDashboardActivity.SLYDEPAY_REQUEST_CODE;

public class GoodsDetailPage extends AppCompatActivity {
    public static final String downloadDirectory = "Lesson plans";
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
    private static final String TAG = "Download Directory";
    private static CoordinatorLayout FoodLayout;
    private DatabaseReference downlaoders;
    private TextView book_name, book_price, book_description, txt_payment;
    private ImageView book_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String selectedPrice, selectedName, selectedDesc;
    private String goodsId = "";
    private Context mContext;
    private LinearLayout konteenLayout, overLayout;
    private Animation fromSmall, fromNothing, forImage, togoAnime;
    private FirebaseDatabase database;
    private DatabaseReference books;
    private SweetAlertDialog sweetAlertDialog;
    private Goods currentGoods;
    private String current_user_id = "";
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private long downloadID;
    private Button btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freebies_book_detail);
        mContext = GoodsDetailPage.this;

        Toolbar mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        mAuth = FirebaseAuth.getInstance();
        FoodLayout = findViewById(R.id.Layout);

        //Firebase
        database = FirebaseDatabase.getInstance();
        books = database.getReference("Goods").child(mAuth.getCurrentUser().getUid());
        books.keepSynced(true);
        downlaoders = database.getReference("Downloader");

        if (mAuth.getCurrentUser() != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
//            mUserRef = FirebaseDatabase.getInstance().getReference()
//            .child("Users").
//            child(mAuth.getCurrentUser().getUid());


        }


        fromSmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall);
        fromNothing = AnimationUtils.loadAnimation(this, R.anim.fromnoth);
        forImage = AnimationUtils.loadAnimation(this, R.anim.froomimage);
        togoAnime = AnimationUtils.loadAnimation(this, R.anim.togo);

        konteenLayout = findViewById(R.id.myKonteen);
        konteenLayout.setAlpha(0);


        book_description = findViewById(R.id.book_description);
        book_price = findViewById(R.id.book_Price);
        book_name = findViewById(R.id.book_name);
        txt_payment = findViewById(R.id.txt_payment);
        book_image = findViewById(R.id.img_book);
        btn_download = findViewById(R.id.btn_download);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //check user login
//        userLogVer();


        if (getIntent() != null)
            goodsId = getIntent().getStringExtra("BookIdFree");//check later for cart
        if (goodsId.isEmpty()) {
            finish();
        }

        if (!TextUtils.isEmpty(goodsId)) {
            if (Common.isConnectedToInternet(getBaseContext())) {

                getGoodsDetail(goodsId);
            } else {
                Toast.makeText(mContext, "Action denied Please check\n your internet connections", Toast.LENGTH_LONG).show();
                finish();
            }

        }

        books.child(goodsId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentGoods = dataSnapshot.getValue(Goods.class);
                assert currentGoods != null;
                selectedPrice = currentGoods.getPrice();
                selectedDesc = currentGoods.getDescription();
                selectedName = currentGoods.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slydePayment(selectedPrice, selectedName, selectedDesc);
            }
        });

        //Endof oncreate


    }

    private void getGoodsDetail(String bookId) {

        books.child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentGoods = dataSnapshot.getValue(Goods.class);


                collapsingToolbarLayout.setTitle(currentGoods.getName());
                book_price.setText("₵" + currentGoods.getPrice());
                book_name.setText(currentGoods.getName());
                txt_payment.setText("payment status: " + currentGoods.getPaymentStatus());
                book_description.setText(currentGoods.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProgressbar() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Downloading");
        sweetAlertDialog.setContentText("Please wait...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }

    private void responseWarningSweetDialog(String txtTitle, String message) {
        Log.d("SAMMIE", "ResponseDialogPaymentSweetDialog: " + message);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(txtTitle)
                .setContentText(message)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SLYDEPAY_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                //change access
                savePurchaseValueToDatabase();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(GoodsDetailPage.this, "Payment failed", Toast.LENGTH_SHORT).show();


            } else if (resultCode == Activity.RESULT_FIRST_USER)
                Toast.makeText(GoodsDetailPage.this, "Payment was cancelled by user", Toast.LENGTH_SHORT).show();


        }


    }

    private void savePurchaseValueToDatabase() {
//        DBpay = database.getReference().child("Goods").child(mAuth.getCurrentUser().getUid());
//        mUserDatabase = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//        database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        books.child("TRACK ID").child("Payment").child(mAuth.getCurrentUser().getUid())
                .setValue("PAID")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        successPaymentSweetDialog("Payment verified successfully !");

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(GoodsDetailPage.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void userLogVer() {
        //conditions for verify auth enabling buttons
        if (!current_user_id.isEmpty()) {
            slydePayment(currentGoods.getPrice(), currentGoods.getName(), currentGoods.getDescription());

            //floatingBuuton
//            fDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //set clickListener
//                    btn_download.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                if (ContextCompat.checkSelfPermission(FreebiesBookDetail.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        != PackageManager.PERMISSION_GRANTED) {
//                                    ActivityCompat.requestPermissions(FreebiesBookDetail.this,
//                                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
//                                } else {
//                                    getBookDownload(bookId);
//                                }
//                            } else {
//                                getBookDownload(bookId);
//                            }
//                        }
//                    });
//
//
//                }
//            });
        }

    }


    private void getBookDownload(String bookId) {

        books.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void slydePayment(String price, String name, String destination) {
        Intent intent = new Intent(getApplicationContext(), TrackDashboardActivity.class);
        startActivityForResult(intent, SLYDEPAY_REQUEST_CODE);
        PayWithSlydepay.Pay(GoodsDetailPage.this, name,

                Double.parseDouble(priceToString(Double.valueOf(price))),

                "Payment made purchased by " + mAuth.getCurrentUser().getEmail() +
                        " " + "userId: " +
                        mAuth.getCurrentUser().getUid() +
                        " " + name
                        + " Version - " + BuildConfig.VERSION_NAME,
                mAuth.getCurrentUser().getDisplayName(),
                mAuth.getCurrentUser().getEmail(), "121", "", SLYDEPAY_REQUEST_CODE);
    }
}

