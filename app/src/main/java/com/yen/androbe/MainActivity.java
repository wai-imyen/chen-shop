package com.yen.androbe;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.androbe.adapter.GridProductAdapter;
import com.yen.androbe.library.GlideImageLoader;
import com.yen.androbe.library.ProductTest;
import com.yen.androbe.object.JsonBean;
import com.yen.androbe.object.Product;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnBannerListener {

    private List<Product> featured_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent();
//        intent.setClass(this, RegisterActivity.class);
//        this.startActivity(intent);

        setToolbar();
        setupNav();

        // 載入大圖輪播
        setupIndexbanner();

        // 設定特色商品
        setupFeaturedProducts();



        // Recycler
//        RecyclerView recyclerView = findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//        // Adapter
//        FeaturedAdapter featuredAdapter = new FeaturedAdapter(this, featured_products);
//        recyclerView.setAdapter(featuredAdapter);
    }

    private void setupFeaturedProducts() {
        featured_products = new ArrayList<>();

//        featured_products.add(new ProductTest("商品0", "http://charles.codepulse.com.tw/image/no_image.png", "NT$2,000"));
//        featured_products.add(new ProductTest("商品1", "https://www.androbe.com/image/catalog/Product/%E8%8A%A5%E6%9C%AB%E9%BB%83%E9%AD%9A%E5%B0%BE%E8%A3%99/JET_0435.jpg", "NT$2,000"));
//        featured_products.add(new ProductTest("商品2", "https://www.androbe.com/image/catalog/Product/%E5%92%8C%E8%BA%AB%E5%85%BC%E5%B8%B6%E8%8A%B1%E6%B4%8B%E8%A3%9D-%E7%B2%89%E8%89%B2/JET_0180v.jpg","NT$2,000"));
//        featured_products.add(new ProductTest("商品3", "https://www.androbe.com/image/catalog/Product/%E5%90%88%E8%BA%AB%E8%82%A9%E5%B8%B6%E8%8A%B1%E6%B4%8B%E8%A3%9D-%E9%BB%83%E8%89%B2/JET_0376.jpg", "NT$2,000"));

        // 初始化 Request 序列
        RequestQueue queue = Volley.newRequestQueue(this);

        // 抓取首頁商品
        String url_get_featured = "http://charles.codepulse.com.tw/api/products/&token=6Cy0==QfiAjLw4SMiojI2JCLiEkI6ICZiwiIlNHb1BXZk92YiojIzJyeXtRP";

        // Get
        StringRequest stringGetRequest = new StringRequest(Request.Method.GET, url_get_featured, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Type objectType = new TypeToken<JsonBean<List<Product>>>() {}.getType();
                JsonBean<List<Product>> json_data = gson.fromJson(response, objectType);
                List<Product> products = json_data.getData();

                for(Product product : products){
                    featured_products.add(new Product(product.getProduct_id(), product.getName(), product.getPrice(), product.getThumb()));
                    Log.d("tag", "結果：" + product.getName());
                }

                GridView gridView = findViewById(R.id.featured_grid);
                GridProductAdapter gridProductAdapter = new GridProductAdapter(MainActivity.this, featured_products);
                gridView.setAdapter(gridProductAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "你選取了" + featured_products.get(position).getName(), Toast.LENGTH_SHORT).show();

                    // 跳轉頁面
                    Intent product_intent = new Intent(MainActivity.this, ProductActivity.class);
                    product_intent.putExtra("product_id", featured_products.get(position).getProduct_id());

                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(MainActivity.this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                    MainActivity.this.startActivity(product_intent, bundle);

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", error.getMessage());
            }
        });
        queue.add(stringGetRequest);
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(this, "你點了第 " + (position + 1) + "張輪播圖", Toast.LENGTH_SHORT).show();
    }

    private void setupIndexbanner() {
        List<String> images = new ArrayList<>();
        images.add("https://www.androbe.com/image/catalog/Banner/JET_0963_banner.jpg");
        images.add("https://www.androbe.com/image/catalog/Banner/JET_0324_bannerv.jpg");
        com.youth.banner.Banner banner = findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                //設定每張圖片要呈現的時間
                .setDelayTime(3000)
                .start();
    }
}
