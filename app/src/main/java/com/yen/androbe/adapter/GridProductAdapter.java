package com.yen.androbe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yen.androbe.R;
import com.yen.androbe.library.GlideImageLoader;
import com.yen.androbe.object.Product;

import java.util.List;

public class GridProductAdapter extends BaseAdapter {

    public LayoutInflater mLayoutInflater;
    public Context context;
    public List<Product> products;

    public GridProductAdapter(Context context, List<Product> products) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView featuredImage;
        TextView featuredText;
        TextView featuredPrice;

        View view = LayoutInflater.from(context).inflate(R.layout.index_featured_item, parent, false);

        featuredImage = view.findViewById(R.id.featuredImage);
        featuredText = view.findViewById(R.id.featuredText);
        featuredPrice = view.findViewById(R.id.featuredPrice);

        Product product = products.get(position);
        featuredText.setText(product.getName());
        featuredPrice.setText(product.getPrice());
        new GlideImageLoader().displayImage(featuredImage.getContext(), product.getThumb(), featuredImage);

        return view;
    }
}
