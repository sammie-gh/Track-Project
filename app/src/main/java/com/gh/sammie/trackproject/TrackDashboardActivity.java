package com.gh.sammie.trackproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gh.sammie.trackproject.ViewHolder.GoodsViewHolder;
import com.gh.sammie.trackproject.model.Goods;
import com.gh.sammie.trackproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TrackDashboardActivity extends AppCompatActivity {
    private Button refreshButton;
    private String current_user_id = "";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth mAuth;
    private User user_info;
    private DatabaseReference mUserDatabase;
    private FirebaseDatabase database;
    private SweetAlertDialog pDialog;


    private FirebaseRecyclerOptions<Goods> options = new FirebaseRecyclerOptions.Builder<Goods>()

//            .setQuery(FirebaseDatabase.getInstance().getReference().child("Freebies"),
//                    Books.class)
//            .build();

            .setQuery(FirebaseDatabase.getInstance().getReference().child("Goods").child(Common.currentUser.getUid()),
                    Goods.class)
            .build();

    private FirebaseRecyclerAdapter<Goods, GoodsViewHolder> adapter = new FirebaseRecyclerAdapter<Goods, GoodsViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull GoodsViewHolder viewHolder, int position, @NonNull final Goods model) {

            String bookTitle = model.getName().trim();
            if (bookTitle.length() > 12) {
                bookTitle = bookTitle.substring(0, 12);
                bookTitle = bookTitle + "...";
            } else bookTitle = model.getName().trim();

            viewHolder.txtBooktName.setText(bookTitle);
//            viewHolder.txtBooktName.setTextSize(20);

            viewHolder.itemPrice.setText("Clearance Charge:" + model.getPrice());

            Picasso.get().load(model.getImage())
                    .into(viewHolder.bookImageView);

            viewHolder.setItemClickListener((view, position1, isLongClick) -> {
//                    Get category id and send to new activity
                Intent list = new Intent(TrackDashboardActivity.this, FreebiesBookDetail.class);
//                    Common.currentRestaurant = clickItem;
//                    Common.restaurant_selected = adapter.getRef(position).getKey();
                list.putExtra("BookIdFree", adapter.getRef(position1).getKey());
                startActivity(list);
            });

        }

        @Override
        public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_items_list, parent, false);
            return new GoodsViewHolder(itemView);

        }

        @Override
        public void onDataChanged() {
            super.onDataChanged();
            if (pDialog != null) {
                pDialog.dismiss();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_dashboard);

        //Init
        mAuth = FirebaseAuth.getInstance();
        swipeRefreshLayout = findViewById(R.id.swipeHomeLayout);
        recyclerView = findViewById(R.id.recycler_books);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        refreshButton = findViewById(R.id.btn_refresh);

        /*Load Goods*/
        loadGoods();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.card1,
                R.color.colorButtonPress);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            //add layout viewing no Data
            if (Common.isConnectedToInternet(this)) {
                loadGoods();
            } else {
                errorSweetDialog();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(TrackDashboardActivity.this));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layot_fall_down);
        recyclerView.setLayoutAnimation(controller);

    }


    private void loadGoods() {
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
        pDialog = new SweetAlertDialog(TrackDashboardActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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