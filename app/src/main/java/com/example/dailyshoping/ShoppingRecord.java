package com.example.dailyshoping;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ShoppingRecord extends AppCompatActivity implements  OnItemClickedListener{


    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;

    private TextView totalsumResult;

    ArrayList<ProductModel> shope_List = new ArrayList<>();;

    //Global variable..

    private String type;
    private int amount;
    private String note;
    private String post_key;

    TextView emptyView;
    Spinner mySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_record);


        totalsumResult=findViewById(R.id.total_ammount);


        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        mDatabase.keepSynced(true);


        recyclerView=findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        emptyView = (TextView)findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mySpinner = (Spinner)findViewById(R.id.tdates);

        new UtilitiesClass().getDates(this,mySpinner);




        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readAllData(adapterView.getItemAtPosition(i).toString());
                loadTotalAmount(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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



    }

    public void onItemClicked(int position) {

    }

    public void loadActivity()
    {


        if(new UtilitiesClass().isNetworkAvailable(ShoppingRecord.this))
        {
            //Total sum number
            loadTotalAmount(new UtilitiesClass().getFormatedDate());
            //read all Shopping List
            readAllData(new UtilitiesClass().getFormatedDate());


        }

        else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Internet Connection Click this text when u are connected!!");
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void readAllData(String date) {



        shope_List.clear();;



        mDatabase.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    ProductModel shopingListModel = snapshot.getValue( ProductModel.class);

                    shope_List.add(shopingListModel);
                    Collections.reverse(shope_List);

                }

                recycleAdapter =  new RecycleAdapter(ShoppingRecord.this,shope_List,ShoppingRecord.this::onItemClicked);

                recyclerView.setAdapter(recycleAdapter);


                if(recycleAdapter.getItemCount() == 0)
                {
                    emptyView.setText(" No Shopping List Added!!");
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

                Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();

            }
        });


    }


    public  void loadTotalAmount(String date)
    {
        mDatabase.orderByChild("date").equalTo(date) .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float totalammount = (float) 0.0;

                for (DataSnapshot snap:dataSnapshot.getChildren()){

                    ProductModel data=snap.getValue(ProductModel.class);

                    totalammount+=data.getAmount();

                    String sttotal=String.valueOf(totalammount);

                    totalsumResult.setText(sttotal + " Birr");

                }
                if(totalammount ==0.0)
                {
                    totalsumResult.setText("0.00 Birr");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}