package com.pandasdroid.otpreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pandasdroid.otpreader.pojomessages.DataMessages;
import com.pandasdroid.otpreader.pojomessages.ResMessages;
import com.pandasdroid.otpreader.pojomessages.ResultItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterUserList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> list_users = new ArrayList<>();
    Context context;

    public AdapterUserList(Context context,ArrayList<String> list_users){
        this.context = context;
        this.list_users = list_users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_users,parent,false);
        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderUsers h = (ViewHolderUsers) holder;
        h.tv_users.setText(list_users.get(position));
        h.ll_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityMessages.class);
                i.putExtra("user",list_users.get(position));
                context.startActivity(i);
            }
        });
        h.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete User?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
                        Api api = retrofit.create(Api.class);
                        Call<ResponseBody> call = api.DeleteUser(list_users.get(position));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null){
                                    try {
                                        String str = response.body().string();
                                        if (str.toLowerCase().contains("success")){
                                            list_users.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        dialog.show();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_users.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder{

        public LinearLayout ll_users;
        public TextView tv_users;
        public LinearLayout img_delete;

        public ViewHolderUsers(@NonNull View itemView) {
            super(itemView);
            tv_users = itemView.findViewById(R.id.tv_users);
            ll_users = itemView.findViewById(R.id.ll_users);
            img_delete = itemView.findViewById(R.id.img_delete);
            if (!context.getSharedPreferences("App", Context.MODE_PRIVATE).getString("key", "").contains("admin")){
                img_delete.setVisibility(View.GONE);
            }
        }
    }
}
