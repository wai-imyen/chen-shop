package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.androbe.adapter.GridProductAdapter;
import com.yen.androbe.object.JsonBean;
import com.yen.androbe.object.Product;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends BaseActivity {

    private ImageView thumb;
    private Gallery gallery;
    private List<String> images;
    private String product_id;
    private TextView name;
    private TextView description;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        gallery = findViewById(R.id.simpleGallery);
        thumb = findViewById(R.id.thumb);


        // 接收參數
        final Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");

        setToolbar();
        setupNav();

        // 初始化 Request 序列
        RequestQueue queue = Volley.newRequestQueue(this);

        // 抓取商品 Api
        String url_api = config.getHost() + "/api/products/" + product_id + "&token=" + config.getToken();

        // Get
        StringRequest stringGetRequest = new StringRequest(Request.Method.GET, url_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type objectType = new TypeToken<JsonBean<Product>>() {}.getType();
                JsonBean<Product> json_data = gson.fromJson(response, objectType);
                Product product = json_data.getData();
                name.setText(product.getName());
                price.setText(product.getPrice());
//                Log.d("tag", "結果：" + response);
//                Log.d("tag", "名稱：" + product.getName());
//                Log.d("tag", "圖片：" + product.getThumb());

                imageLoader.displayImage(getApplicationContext(), product.getThumb(), thumb);

                images = new ArrayList<>();
                images.add(product.getThumb());
                images.add(config.getHost() + "/image/catalog/product/imageQC2000008350_2000_4.jpg");
                images.add(config.getHost() + "/image/catalog/product/imageQC2000008350_2000_5.jpg");
                images.add(config.getHost() + "/image/catalog/product/imageQC2000008350_2000_6.jpg");
                images.add(config.getHost() + "/image/catalog/product/imageQC2000008350_2000_7.jpg");


                GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), images);
                gallery.setAdapter(galleryAdapter);
                gallery.setSpacing(50);
                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageLoader.displayImage(view.getContext(), images.get(position), thumb);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", error.getMessage());
            }
        });
        queue.add(stringGetRequest);
    }

    public class GalleryAdapter extends BaseAdapter{

        public Context context;
        public List<String> images;

        public GalleryAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // create a ImageView programmatically
            ImageView imageView = new ImageView(context);
            imageLoader.displayImage(context, images.get(position), imageView);
            //保持圖片長寬比例
            imageView.setAdjustViewBounds(true);
            //縮放為置中
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //設置圖片長寬
            imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setLayoutParams(new Gallery.LayoutParams(500, 500)); // set ImageView param
            return imageView;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
