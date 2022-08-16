package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    //ab0d908e22be4954b47f9411bc01adf5
    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private  ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1493612276216-ee3925520721?ixlib=rb-1.2.1&raw_url=true&q=80&fm=jpg&crop=entropy&cs=tinysrgb&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=464"));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1451187580459-43490279c0fa?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dGVjaG5vbG9naWVzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1532094349884-543bc11b234d?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8c2NpZW5jZSUyMGxhYnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500"));
       categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1517649763962-0c623066013b?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8c3BvcnRzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500"));
//       categoryRVModalArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1493612276216-ee3925520721?ixlib=rb-1.2.1&raw_url=true&q=80&fm=jpg&crop=entropy&cs=tinysrgb&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=464"));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1591696205602-2f950c417cb9?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fGJ1c2luZXNzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500"));
       categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1586899028174-e7098604235b?ixlib=rb-1.2.1&raw_url=true&q=80&fm=jpg&crop=entropy&cs=tinysrgb&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=871"));
     categoryRVModalArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1511688878353-3a2f5be94cd7?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8aGVhbHRofGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500"));

        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apikey=ab0d908e22be4954b47f9411bc01adf5";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishdAt&language=en&apikey=ab0d908e22be4954b47f9411bc01adf5";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }
        else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),
                            articles.get(i).getDescription(),
                            articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(),
                            articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCategoryClick(int position) {

        String category = categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}