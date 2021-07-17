package com.pandasdroid.otpreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.pandasdroid.otpreader.pojomessages.DataMessages;
import com.pandasdroid.otpreader.pojomessages.ResMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterUserList adapterUserList;
    private ArrayList<String> list_users = new ArrayList<>();

    private Button btn_clear;
    private Button add_user;
    private EditText et_username;
    private LinearLayout ll_1,ll_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        /*if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(ActivityUsers.this)) {
            AutoStartPermissionHelper.getInstance().getAutoStartPermission(ActivityUsers.this);
        }*/

        initview();
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    private void initList() {
        if (!getSharedPreferences("App", Context.MODE_PRIVATE).getString("key", "").contains("admin")){
            Intent i = new Intent(ActivityUsers.this, ActivityMessages.class);
            i.putExtra("user",getSharedPreferences("App", Context.MODE_PRIVATE).getString("key", ""));
            finish();
            startActivity(i);
        }
        list_users.clear();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        Api api = retrofit.create(Api.class);
        Call<ResUsers> call = api.GetUserList("true");
        call.enqueue(new Callback<ResUsers>() {
            @Override
            public void onResponse(Call<ResUsers> call, Response<ResUsers> response) {
                if (response.body() != null) {
                    List<ResultItem> res = response.body().getResult();
                    for (int i = 0; i < res.size(); i++) {
                        ResultItem td = res.get(i);
                        list_users.add(td.getUsername());
                        adapterUserList.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResUsers> call, Throwable t) {
                Toast.makeText(ActivityUsers.this, "Failed to get UserList" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initAction() {
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityUsers.this, RecyclerView.VERTICAL, false));

        adapterUserList = new AdapterUserList(ActivityUsers.this, list_users);

        recyclerView.setAdapter(adapterUserList);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearOTP();
            }
        });
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_username.getText().toString().length() != 0) {

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
                    Api api = retrofit.create(Api.class);
                    Call<ResponseBody> call = api.AddUser(et_username.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.body() != null) {
                                try {

                                    String str = response.body().string();

                                    if (str.toLowerCase().contains("success")) {
                                        et_username.getText().clear();
                                        initList();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ActivityUsers.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void ClearOTP() {

        ProgressDialog progressDialog = new ProgressDialog(ActivityUsers.this);
        progressDialog.setTitle("Cleaning OTP");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.ClearOTP(getSharedPreferences("App", Context.MODE_PRIVATE).getString("key", ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        String str = response.body().string();
                        Toast.makeText(ActivityUsers.this, str.toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ActivityUsers.this, "Failed to clean OTP", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ActivityUsers.this, "Failed to clean OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ActivityUsers.this, "Failed to clear OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview() {
        recyclerView = findViewById(R.id.rv_users);
        btn_clear = findViewById(R.id.btn_clear);
        add_user = findViewById(R.id.add_user);
        et_username = findViewById(R.id.et_username);
        ll_1 = findViewById(R.id.ll1);
        ll_2 = findViewById(R.id.ll_2);
        if (!getSharedPreferences("App", Context.MODE_PRIVATE).getString("key", "").contains("admin")){
            ll_1.setVisibility(View.GONE);
            ll_2.setVisibility(View.GONE);
        }
    }
}
