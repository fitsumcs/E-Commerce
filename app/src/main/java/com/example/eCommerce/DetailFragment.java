package com.example.eCommerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailFragment extends Fragment {

    TextView productCatagory, productTitle, datePublished ,productPrice, userName , userEmail, userPhone ;
    ImageView imgUrl;

    private DatabaseReference mDatabase;

    private  ProductModel productModel;



    public DetailFragment(ProductModel productModel) {

        this.productModel = productModel;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("E_Users");

        mDatabase.keepSynced(true);

        productCatagory   =   view.findViewById(R.id.textView_Catagory);
        productTitle   =   view.findViewById(R.id.textView_Title);
        productPrice   =   view.findViewById(R.id.textView_Price);
        datePublished =   view.findViewById(R.id.textView_Date);
        userName =   view.findViewById(R.id.textView_By);
        userPhone =   view.findViewById(R.id.textView_Phone);
        userEmail=   view.findViewById(R.id.textView_Email);
        imgUrl = view.findViewById(R.id.imageView_Pic);
        
        loadData(productModel);
    }

    private void loadData(ProductModel productModel) {
        productTitle.setText(productModel.getTitle());
        productCatagory.setText(productModel.getType());
        datePublished.setText(new UtilitiesClass().getTimeDate(productModel.getTimestamp(true)));

        String birr = "</font> <font color=#cdc0b0>"  + productModel.getAmount()  + " Birr" + "</font>";
        productPrice.setText( Html.fromHtml(birr));

        //image url
        Glide.with(getContext())
                .load("https://www.1datagroup.com/wp-content/uploads/2020/10/ecommerce-solutions.jpg")
                .into(imgUrl);

        // Read from the database
        mDatabase.child(productModel.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                String phone = "</font> <font color=#cc0029>"  + user.getUserPhoneNumber() + "</font>";
                String email = "</font> <font color=#cc0029>" + user.getUserEmail() + "</font>";
                userPhone.setText(Html.fromHtml(phone));
                userEmail.setText(Html.fromHtml(email));

                userName.setText("By  " + user.getUserFirstName() + " " + user.getUserLastName() );



            }

            @Override
            public void onCancelled(DatabaseError error) {
            // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }


}