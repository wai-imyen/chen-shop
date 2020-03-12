package com.yen.androbe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yen.androbe.R;
import com.yen.androbe.library.GlideImageLoader;
import com.yen.androbe.library.ProductTest;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.featuredHolder>{

    public Context context;
    public List<ProductTest> featured_products;

    public FeaturedAdapter(Context context, List<ProductTest> featured_products) {
        this.context = context;
        this.featured_products = featured_products;
    }

    @NonNull
    @Override
    public featuredHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.index_featured_item, parent, false);
        return new featuredHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull featuredHolder holder, int position) {
        ProductTest featured_product = featured_products.get(position);
        holder.featuredText.setText(featured_product.getName());
        holder.featuredPrice.setText(featured_product.getPrice());
        new GlideImageLoader().displayImage(holder.featuredImage.getContext(), featured_product.getImage(), holder.featuredImage);
    }

    @Override
    public int getItemCount() {
        return featured_products.size();
    }

    public class featuredHolder extends RecyclerView.ViewHolder{
        ImageView featuredImage;
        TextView featuredText;
        TextView featuredPrice;
        public featuredHolder(@NonNull View itemView) {
            super(itemView);
            featuredImage = itemView.findViewById(R.id.featuredImage);
            featuredText = itemView.findViewById(R.id.featuredText);
            featuredPrice = itemView.findViewById(R.id.featuredPrice);
        }
    }
}