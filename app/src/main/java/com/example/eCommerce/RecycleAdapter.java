package com.example.eCommerce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecycleAdapter  extends RecyclerView.Adapter<RecycleAdapter.MyIdeaHandler>  implements Filterable {

    Context context;
    ArrayList<ProductModel> productList;
    ArrayList<ProductModel> copy_productList;
    private  OnItemClickedListener m_onItemClickedListener;

    public RecycleAdapter(Context con, ArrayList<ProductModel> productList, OnItemClickedListener onItemClickedListener)
    {

        this.context = con;
        this.productList = productList;
        this.m_onItemClickedListener = onItemClickedListener;
        this.copy_productList = new ArrayList<>(productList);


    }

    @NonNull
    @Override
    public MyIdeaHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        MyIdeaHandler myIdeaHandler = new MyIdeaHandler(view,m_onItemClickedListener);
        return myIdeaHandler;

    }

    @Override
    public void onBindViewHolder(@NonNull MyIdeaHandler holder, int position) {

        ProductModel productModel = productList.get(position);
        String catagory = productModel .getType();
        String title = productModel .getNote();
        String date = productModel .getDate();

        //set the view
        holder.productTitle.setText(title);
        holder.productCatagory.setText(catagory);
        holder.datePublished.setText(date);

        Log.e("Copy Size ", String.valueOf(copy_productList.size()));

        //image url
        Glide.with(context)
                .load("https://www.1datagroup.com/wp-content/uploads/2020/10/ecommerce-solutions.jpg")
                .into(holder.imgUrl);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return myFilteredItem ;
    }

    //filter
    private  Filter myFilteredItem = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ProductModel> filterList = new ArrayList<>();

            if(charSequence == null || charSequence.length()==0)
            {
                filterList.addAll(copy_productList);
            }
            else{
                String fillterPattern = charSequence.toString().toLowerCase();
                for (ProductModel item: copy_productList)
                {
                    if(item.getNote().toLowerCase().contains(fillterPattern))
                    {
                        filterList.add(item);
                    }
                }


            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            productList.clear();
            productList.addAll((Collection<? extends ProductModel>) filterResults.values);
            notifyDataSetChanged();


        }
    };

    public class MyIdeaHandler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productCatagory, productTitle, datePublished ;
        ImageView imgUrl;
        OnItemClickedListener onItemClickedListener;

        public MyIdeaHandler(@NonNull View myview , OnItemClickedListener onItemClickedListener) {
            super(myview);
           productCatagory   =   myview.findViewById(R.id.textView_Catagory);
           productTitle   =   myview.findViewById(R.id.textView_Title);
           datePublished =   myview.findViewById(R.id.textView_Date);
           imgUrl = myview.findViewById(R.id.imageView_Pic);

           this.onItemClickedListener = onItemClickedListener;

           myview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onItemClickedListener.onItemClicked(getAdapterPosition());

        }
    }


}

