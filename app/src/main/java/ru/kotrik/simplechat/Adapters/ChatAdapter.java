package ru.kotrik.simplechat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kotrik.simplechat.Models.ChatItem;
import ru.kotrik.simplechat.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_MESSAGE_SERVICE = 0;
    private final int TYPE_MESSAGE_MY = 1;
    private final int TYPE_MESSAGE_OTHER = 2;

    ArrayList<ChatItem> items;

    public ChatAdapter(ArrayList<ChatItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_MESSAGE_SERVICE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_middle, parent, false);
            return new ServiceViewHolder(v);
        } else if(viewType == TYPE_MESSAGE_MY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my, parent, false);
            return new MyViewHolder(v);
        } else if(viewType == TYPE_MESSAGE_OTHER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent, false);
            return new OtherViewHolder(v);
        }
        throw new RuntimeException("No found type for "+viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ServiceViewHolder) {
            ((ServiceViewHolder) holder).message.setText(items.get(position).message);
        } else if(holder instanceof MyViewHolder) {
            ChatItem item = items.get(position);
            ((MyViewHolder) holder).nickname.setText(item.nickname);
            ((MyViewHolder) holder).message.setText(item.message);
            ((MyViewHolder) holder).time.setText(item.time);
        } else if(holder instanceof OtherViewHolder) {
            ChatItem item = items.get(position);
            ((OtherViewHolder) holder).nickname.setText(item.nickname);
            ((OtherViewHolder) holder).message.setText(item.message);
            ((OtherViewHolder) holder).time.setText(item.time);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (items.get(position).type) {
            case TYPE_MESSAGE_SERVICE:
                return 0;
            case TYPE_MESSAGE_MY:
                return 1;
            case TYPE_MESSAGE_OTHER:
                return 2;
            default:
                return -1;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, message, time;
        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.text_message_name);
            message = itemView.findViewById(R.id.text_message_body);
            time = itemView.findViewById(R.id.text_message_time);
        }
    }

    class OtherViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, message, time;
        public OtherViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.text_message_name);
            message = itemView.findViewById(R.id.text_message_body);
            time = itemView.findViewById(R.id.text_message_time);
        }
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder{
        public TextView message;
        public ServiceViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.text_middle);
        }
    }
}
