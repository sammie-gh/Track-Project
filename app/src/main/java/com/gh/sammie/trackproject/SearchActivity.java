package com.gh.sammie.trackproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gh.sammie.trackproject.ViewHolder.GoodsViewHolder;
import com.gh.sammie.trackproject.model.Goods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter<Goods, GoodsViewHolder> adapter;
    private FirebaseDatabase database;
    private DatabaseReference bookNoteslist;
    private String categoryId = "";
    //    private SwipeRefreshLayout mResfreshLayout;
    //Search Functionality
    private FirebaseRecyclerAdapter<Goods, GoodsViewHolder> searchAdapter;
    private List<String> suggestion = new ArrayList<>();
    private MaterialSearchBar materialSearchBar;
    private String name_of_search_item;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // get intent from listing
        Intent intent = getIntent();
        name_of_search_item = intent.getStringExtra("SearchId");
        bookNoteslist = database.getReference("Goods").child(mAuth.getCurrentUser().getUid());
        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Search
        materialSearchBar = findViewById(R.id.search_bar);
        materialSearchBar.setHint("Search...");
        materialSearchBar.setPlaceHolder("Click Here to Search");
        loadSearchSuggest();

        materialSearchBar.setLastSuggestions(suggestion);
        materialSearchBar.setCardViewElevation(10);

        /*Disabled to prevent loading all goods */
        //Load Goods if internet or network
//        if (Common.isConnectedToInternet(getBaseContext()))
//            loadAllSearchNotes();
//        else {
//            Toast.makeText(SearchActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//        }

//        mResfreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (Common.isConnectedToInternet(getBaseContext()))
//
//                    loadAllSearchNotes();
//
//                else {
//                    Toast.makeText(SearchActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //when user type their text, we will change sugesst list
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestion) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search is closed
                // restore original adapter

                if (!enabled)
                    recyclerView.setAdapter(adapter);

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish
                //show of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    //load suggestion
    private void loadSearchSuggest() {
        bookNoteslist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Goods item = postSnapshot.getValue(Goods.class);
                    if (item != null) {
                        suggestion.add(item.getName());
                    }
                }
                materialSearchBar.setLastSuggestions(suggestion);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void startSearch(CharSequence text) {
        //create query by name
        Query searchByName = bookNoteslist.orderByChild("name").equalTo(text.toString());

        //create options with query
        FirebaseRecyclerOptions<Goods> foodOptions = new FirebaseRecyclerOptions.Builder<Goods>()
                .setQuery(searchByName, Goods.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<Goods, GoodsViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull GoodsViewHolder viewHolder, int position, @NonNull Goods model) {
                viewHolder.txtBooktName.setText(model.getName());
                viewHolder.item_price.setText(MessageFormat.format("Clearance Charge:GH ₵{0}", model.getPrice()));
                viewHolder.txt_status.setText("status: " + model.getStatus());
                viewHolder.txt_location.setText(model.getLocation());
                viewHolder.txtDescription.setText("Description : " + model.getDescription());
                viewHolder.txt_date.setText("Container Arrived on: " + model.getDate());
                viewHolder.txt_terminal.setText("Terminal: " + model.getTerminal());

//                Picasso.get().load(model.getImage())
//                        .into(viewHolder.bookImageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int pos, boolean isLongClick) {
//                        //start new Activity
                        if (name_of_search_item != null) {
                            switch (name_of_search_item) {
                                case "Freebies":
                                    Intent intentTextBooks = new Intent(SearchActivity.this, GoodsDetailPage.class);
                                    intentTextBooks.putExtra("BookIdFree", searchAdapter.getRef(pos).getKey());
                                    startActivity(intentTextBooks);
                                    break;

                                default:
                                    Toast.makeText(SearchActivity.this, "No value", Toast.LENGTH_SHORT).show();
                            }

                            recyclerView = findViewById(R.id.recycler_search);
                        }

                    }
                });
            }

            @NonNull
            @Override
            public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_items_list, parent, false);
                return new GoodsViewHolder(itemView);
            }
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter); //set adpter for Recyc... is search result

    }

    @Override
    protected void onStop() {
        if (adapter != null)
            adapter.stopListening();

        if (searchAdapter != null)
            searchAdapter.stopListening();
        super.onStop();

    }

    @Override
    protected void onPostResume() {
        if (adapter != null)
            adapter.startListening();
        if (searchAdapter != null)
            searchAdapter.startListening();
        super.onPostResume();
    }

    private void loadAllSearchNotes() {
        //create query by category id
        Query searchByName = bookNoteslist;
        //create options with query
        FirebaseRecyclerOptions<Goods> foodOptions = new FirebaseRecyclerOptions.Builder<Goods>()
                .setQuery(searchByName, Goods.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Goods, GoodsViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final GoodsViewHolder viewHolder, final int position,
                                            @NonNull final Goods model) {


                viewHolder.txtBooktName.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(viewHolder.bookImageView);

                final Goods local = model;
                viewHolder.setItemClickListener((view, position1, isLongClick) -> {
                    //start new Activity
                    if (name_of_search_item != null) {
                        switch (name_of_search_item) {
                            case "Freebies":
                                Intent intentTextBooks = new Intent(SearchActivity.this, GoodsDetailPage.class);
                                intentTextBooks.putExtra("BookIdFree", adapter.getRef(position1).getKey());
                                startActivity(intentTextBooks);
                                break;
                            default:
                                Toast.makeText(SearchActivity.this, "No value", Toast.LENGTH_SHORT).show();
                        }

                        recyclerView = findViewById(R.id.recycler_search);
                    }

                });
            }

            @NonNull
            @Override
            public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_items_list, parent, false);
                return new GoodsViewHolder(itemView);
            }
        };

        adapter.startListening();
        Log.d(TAG, "" + adapter.getItemCount());

//        //Animation
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();

        adapter.startListening();
        recyclerView.setAdapter(adapter);
//        mResfreshLayout.setRefreshing(false); //end ring
    }

}