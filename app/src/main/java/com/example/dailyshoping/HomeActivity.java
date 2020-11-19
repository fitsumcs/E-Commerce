package com.example.dailyshoping;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity  implements  OnItemClickedListener{



    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;

    private TextView totalsumResult;

    FloatingActionButton floatingActionButton;

    ArrayList<ShopingModle> shope_List = new ArrayList<>();;

    //Global variable..

    private String type;
    private int amount;
    private String note;
    private String post_key;

    TextView emptyView, dateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        totalsumResult=findViewById(R.id.total_ammount);


        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

       mDatabase.keepSynced(true);


        recyclerView=findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        emptyView = (TextView)findViewById(R.id.emptyView);
        dateTextView = (TextView)findViewById(R.id.currentDate);
        dateTextView.setText(new UtilitiesClass().getFormatedDate());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);








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

    public void loadActivity()
    {


        if(new UtilitiesClass().isNetworkAvailable(HomeActivity.this))
        {
            //Total sum number
            loadTotalAmount(new UtilitiesClass().getFormatedDate());
            //read all Shopping List
            readAllData(new UtilitiesClass().getFormatedDate());
            floatingActionButton.setEnabled(true);


        }

        else {
            floatingActionButton.setEnabled(false);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Internet Connection Click this text when u are connected!!");
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void addShoppingItem(View view) {
        addShoppingDialog();
    }



    private void readAllData(String date) {



        shope_List.clear();;



        mDatabase.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    ShopingModle shopingListModel = snapshot.getValue( ShopingModle.class);

                    shope_List.add(shopingListModel);
                    Collections.reverse(shope_List);

                }

                recycleAdapter =  new RecycleAdapter(HomeActivity.this,shope_List,HomeActivity.this::onItemClicked);

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

    private void addShoppingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_shoping_item);
        dialog.show();

        final Spinner type= dialog .findViewById(R.id.edt_type);
        final EditText amount=dialog .findViewById(R.id.edt_ammount);
        final EditText note=dialog .findViewById(R.id.edt_note);
        Button btnSave=dialog .findViewById(R.id.btn_save);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shoppingType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType=type.getSelectedItem().toString();
                String mAmount=amount.getText().toString().trim();
                String mNote=note.getText().toString().trim();



                if (TextUtils.isEmpty(mType) || TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mNote)){
                    Toast.makeText(getApplicationContext(),"All Fields are  Required.. ",Toast.LENGTH_SHORT).show();
                    return;
                }

                float ammint = Float.parseFloat(mAmount);

                String id= mDatabase.push().getKey();

                String date= new UtilitiesClass().getFormatedDate();

                ShopingModle data = new ShopingModle(mType,ammint,mNote,date,id);

                writeToDb(id,data,"Data Added Successfully !! ");


                dialog.dismiss();
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

                    ShopingModle data=snap.getValue(ShopingModle.class);

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

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                logout();
                break;
            case R.id.viewRecord:
                startActivity(new Intent(getApplicationContext(),ShoppingRecord.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //logout

    public  void logout()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onItemClicked(int position) {

        ShopingModle shopingModle = shope_List.get(position);

        String type=shopingModle.getType();
        float amount=shopingModle.getAmount();
        String note=shopingModle.getNote();
        String myId = shopingModle.getId();
        uppdateDialog(myId ,type,amount,note);



    }

    private void uppdateDialog( String id,String type, float amount, String note) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_layout);
        dialog.show();



        final Spinner Vtype= dialog .findViewById(R.id.edt_typeUpdate);
        final EditText Vamount =dialog .findViewById(R.id.edt_ammount_upd);
        final EditText Vnote =dialog .findViewById(R.id.edt_note_upd);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shoppingType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Vtype.setAdapter(adapter);

        List<String> Lines = Arrays.asList(getResources().getStringArray(R.array.shoppingType_array));

         int spinnerPosition = Lines.indexOf(type);
        Vtype.setSelection(spinnerPosition);
        Vamount.setText(String.valueOf(amount));
        Vnote.setText(note);




        Button btnUpdate = dialog .findViewById(R.id.btn_SAVE_upd);
        Button btnDelete = dialog .findViewById(R.id.btn_delete_upd);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType=Vtype.getSelectedItem().toString();
                String mAmount=Vamount.getText().toString().trim();
                String mNote=Vnote.getText().toString().trim();
                float ammint = Float.parseFloat(mAmount);

                if (TextUtils.isEmpty(mType) || TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mNote)){
                    Toast.makeText(getApplicationContext(),"All Fields are  Required.. ",Toast.LENGTH_SHORT).show();
                    return;
                }

                String date= new UtilitiesClass().getFormatedDate();

                ShopingModle data = new ShopingModle(mType,ammint,mNote,date,id);
                writeToDb(id,data,"Data Updated Successfully !! ");

                dialog.dismiss();

            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(id).removeValue();
                readAllData(new UtilitiesClass().getFormatedDate());
                //Total sum number
                loadTotalAmount(new UtilitiesClass().getFormatedDate());
                Toast.makeText(getApplicationContext(),"Data Deleted Successfully !! ",Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });



    }

    public void writeToDb(String id,ShopingModle data, String type)
    {
        mDatabase.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
                    readAllData(new UtilitiesClass().getFormatedDate());
                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}