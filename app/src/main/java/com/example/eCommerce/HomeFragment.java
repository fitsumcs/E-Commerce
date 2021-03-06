package com.example.eCommerce;

import android.graphics.Path;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnItemClickedListener{



    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;
    TextView emptyView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeLayout;



    ArrayList<ProductModel> product_List = new ArrayList<>();;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mDatabase= FirebaseDatabase.getInstance().getReference().child("E_Commerece");

        mDatabase.keepSynced(true);


        recyclerView= view.findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());

        emptyView = (TextView)view.findViewById(R.id.emptyView);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        // Getting SwipeContainerLayout
        swipeLayout = view.findViewById(R.id.swipe_container);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        loadActivity();

        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emptyView.getText().toString().equalsIgnoreCase("No Internet Connection Click this text when u are connected!!"))
                {
                    loadActivity();
                }
            }
        });

        //swip listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadActivity();
                swipeLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //load data
    public void loadActivity()
    {


        if(new UtilitiesClass().isNetworkAvailable(getContext()))
        {

            //read all Shopping List
            readAllData();

        }

        else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Internet Connection Click this text when u are connected!!");
            recyclerView.setVisibility(View.GONE);
        }
    }

    //read all data from databse
    private void readAllData() {

        progressBar.setVisibility(View.VISIBLE);

        product_List.clear();;



        mDatabase.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);


                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    ProductModel shopingListModel = snapshot.getValue( ProductModel.class);

                    product_List.add(shopingListModel);

                }

                recycleAdapter =  new RecycleAdapter(getContext(),product_List,HomeFragment.this::onItemClicked);

                recyclerView.setAdapter(recycleAdapter);


                if(recycleAdapter.getItemCount() == 0)
                {
                    emptyView.setText(" No Product Posted!!");
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "Error",Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onItemClicked(int position) {

        ProductModel productModelModle = product_List.get(position);

        new UtilitiesClass().openFragment(getContext(),DetailFragment.getNewInstance(productModelModle));

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

               recycleAdapter.getFilter().filter(s);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}