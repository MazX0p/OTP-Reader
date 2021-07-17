package com.pandasdroid.otpreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasdroid.otpreader.pojomessages.DataMessages;

import java.util.ArrayList;

public class AdapterMessageList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<DataMessages> list_messages = new ArrayList<>();
    Context context;

    public AdapterMessageList(Context context, ArrayList<DataMessages> list_messages){
        this.context = context;
        this.list_messages = list_messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_messages,parent,false);
        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderUsers h = (ViewHolderUsers) holder;
        DataMessages data = list_messages.get(position);

        h.tv_message.setText(data.getMessage());
        h.tv_sender.setText(data.getSender());
        h.tv_date.setText(data.getMsgTime());

        h.ll_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context,ActivityMessages.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_messages.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder{

        public LinearLayout ll_users;
        public TextView tv_message,tv_date,tv_sender;

        public ViewHolderUsers(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_sender = itemView.findViewById(R.id.tv_sender);
            tv_date = itemView.findViewById(R.id.tv_date);
            ll_users = itemView.findViewById(R.id.ll_users);
        }
    }
}
