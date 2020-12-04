package com.example.eCommerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class DetailFragment extends Fragment {

    TextView productCatagory, productTitle, datePublished , userName , userPhone ;
    ImageView imgUrl;

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

        productCatagory   =   view.findViewById(R.id.textView_Catagory);
        productTitle   =   view.findViewById(R.id.textView_Title);
        datePublished =   view.findViewById(R.id.textView_Date);
        userName =   view.findViewById(R.id.textView_By);
        userPhone =   view.findViewById(R.id.textView_Phone);
        imgUrl = view.findViewById(R.id.imageView_Pic);
        
        loadData(productModel);
    }

    private void loadData(ProductModel productModel) {
        productTitle.setText(productModel.getNote());
        productCatagory.setText(productModel.getType());
        datePublished.setText(productModel.getDate());

        //image url
        Glide.with(getContext())
                .load("https://www.1datagroup.com/wp-content/uploads/2020/10/ecommerce-solutions.jpg")
                .into(imgUrl);

    }


}