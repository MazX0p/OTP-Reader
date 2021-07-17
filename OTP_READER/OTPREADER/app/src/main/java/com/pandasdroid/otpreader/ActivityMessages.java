package com.pandasdroid.otpreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pandasdroid.otpreader.pojomessages.DataMessages;
import com.pandasdroid.otpreader.pojomessages.ResMessages;
import com.pandasdroid.otpreader.pojomessages.ResultItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityMessages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterMessageList adapter;
    private ArrayList<DataMessages> list_users = new ArrayList<>();

    private ProgressDialog progressDialog;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        progressDialog = new ProgressDialog(ActivityMessages.this);
        progressDialog.setTitle("Loading Please Wait...");
        progressDialog.setCancelable(false);

        initview();
        initAction();
        initList();
    }


    private void initList() {

        progressDialog.show();

        list_users.clear();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        Api api = retrofit.create(Api.class);
        Call<ResMessages> call = api.GetMessages(getIntent().getStringExtra("user"));
        call.enqueue(new Callback<ResMessages>() {
            @Override

            public void onResponse(Call<ResMessages> call, Response<ResMessages> response) {
                progressDialog.dismiss();
                if (response.body() != null){
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_result.setVisibility(View.GONE);
                    Log.wtf("ResponseBody","Success");
                    List<ResultItem> res = response.body().getResult();
                    for (int i = 0;i < res.size();i++){
                        ResultItem td = res.get(i);
                        list_users.add(new DataMessages(td.getSender(),td.getId(),td.getMsgTime(),td.getMessage(),td.getUser()));
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    recyclerView.setVisibility(View.GONE);
                    tv_result.setVisibility(View.VISIBLE);
                    Log.wtf("ResponseBody","Body Null");
                }
            }

            @Override
            public void onFailure(Call<ResMessages> call, Throwable t) {
                progressDialog.dismiss();
                recyclerView.setVisibility(View.GONE);
                tv_result.setVisibility(View.VISIBLE);
                Log.wtf("Failed",t.getMessage());
                Toast.makeText(ActivityMessages.this, "Failed to get Messages", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initAction() {

        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityMessages.this,RecyclerView.VERTICAL,false));

        adapter = new AdapterMessageList(ActivityMessages.this,list_users);

        recyclerView.setAdapter(adapter);

    }

    private void initview() {
        recyclerView = findViewById(R.id.rv_users);
        tv_result = findViewById(R.id.tv_result);
    }
}