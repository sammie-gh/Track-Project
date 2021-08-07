package com.gh.sammie.trackproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gh.sammie.trackproject.ViewHolder.PaymentViewHolder;
import com.gh.sammie.trackproject.model.Goods;
import com.gh.sammie.trackproject.model.Payment;
import com.gh.sammie.trackproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportPage extends AppCompatActivity {
    private Button refreshButton;
    private String current_user_id = "", CurrentGoodName;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth mAuth;
    private User user_info;
    private Goods currentBook;
    private SweetAlertDialog pDialog;
    private RelativeLayout layout;
    private String currentPrice;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
    public static final int SLYDEPAY_REQUEST_CODE = 9999;
    private LottieAnimationView animationView;


    private FirebaseRecyclerOptions<Payment> options = new FirebaseRecyclerOptions.Builder<Payment>()


            .setQuery(FirebaseDatabase.getInstance().getReference().child("Payment").child(Common.currentUser.getUid()),
                    Payment.class)
            .build();


    private FirebaseRecyclerAdapter<Payment, PaymentViewHolder> adapter = new FirebaseRecyclerAdapter<Payment, PaymentViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull PaymentViewHolder viewHolder, int position, @NonNull final Payment model) {

            //get values from model
            String bookTitle = model.getId().trim();
            if (bookTitle.length() > 20) {
                bookTitle = bookTitle.substring(0, 12);
                bookTitle = bookTitle + "...";
            } else bookTitle = model.getId().trim();

            viewHolder.txtBooktName.setText(bookTitle);
            viewHolder.txtDescription.setText("Desc: " + model.getDesc());
//            viewHolder.txtBooktName.setTextSize(20);
            viewHolder.item_price.setText("Charge: GH ₵ " + model.getPrice());
            viewHolder.txt_status.setText("status: " + model.getStatus());
            viewHolder.txt_email.setText("Contact: " + model.getEmail());


//            viewHolder.setItemClickListener((view, position1, isLongClick) -> {
////                    Get category id and send to new activity
//                Intent list = new Intent(TrackDashboardActivity.this, GoodsDetailPage.class);
//                list.putExtra("BookIdFree", adapter.getRef(position1).getKey());
////                finish();
//                startActivity(list);
//            });

        }

        @Override
        public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payment_items_list, parent, false);
            return new PaymentViewHolder(itemView);

        }

        @Override
        public void onDataChanged() {
            super.onDataChanged();

            if (getItemCount() == 0) {
                recyclerView.setVisibility(View.GONE);
                animationView.setVisibility(View.VISIBLE);
                layout.setBackgroundColor(Color.parseColor("#406AA8"));
                Toast.makeText(ReportPage.this, "No Payment found ", Toast.LENGTH_LONG).show();
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);
                layout.setBackgroundColor(Color.parseColor("#ffffff"));

            }
            if (pDialog != null) {
                pDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);
        //Init
        mAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipeHomeLayout);
        recyclerView = findViewById(R.id.recycler_books);
        animationView = findViewById(R.id.animationView);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = (RelativeLayout) findViewById(R.id.main_content);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        refreshButton = findViewById(R.id.btn_refresh);

        /*Load Goods*/
        loadBooks();


        swipeRefreshLayout.setColorSchemeResources(R.color.card2, R.color.card1,
                R.color.colorButtonPress);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            //add layout viewing no Data
            if (Common.isConnectedToInternet(this)) {
                loadBooks();
            } else {
                errorSweetDialog();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(ReportPage.this));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layot_fall_down);
        recyclerView.setLayoutAnimation(controller);

    }


    private void loadBooks() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

        //animation
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onResume() {
        super.onResume();
        assert adapter != null;
        adapter.startListening();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
        pDialog = null;
    }

    private void showDialog() {
        pDialog = new SweetAlertDialog(ReportPage.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void errorSweetDialog() {
        swipeRefreshLayout.setRefreshing(false);
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong! \n Please check Your connection")
                .show();
    }


}