package com.example.whatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.R;

import java.util.LinkedList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_RECEIVED = 1;
    private static final int VIEW_TYPE_SENT = 2;
    public LinkedList<Message> mList;
    public Contact contact;
    private final Context context;


    public MessageAdapter(Context context, LinkedList<Message> mList, Contact contact) {
        this.context = context;
        this.mList = mList;
        this.contact = contact;
    }

    public void setMessageList(LinkedList<Message> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        /*this.mList = list;
        notifyDataSetChanged();*/
    }

    public LinkedList<Message> getMessageList() {
        return this.mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* connect to viewholder
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new MessageAdapter.MessageViewHolder(v);*/


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_RECEIVED) {
            View receivedView = inflater.inflate(R.layout.item_message_rec, parent, false);
            return new ReceivedViewHolder(receivedView);
        } else {
            View sentView = inflater.inflate(R.layout.item_message_send, parent, false);
            return new SentViewHolder(sentView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mList.get(position);
        if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).textViewMessageReceived.setText(message.getContent());
        } else if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).textViewMessageSent.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Message message = mList.get(position);
        String send = message.getSender();
        String user = contact.getUsername();
        if ( send.equals(user) ) {
            return VIEW_TYPE_RECEIVED;
        } else {
            return VIEW_TYPE_SENT;
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessageReceived;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageReceived = itemView.findViewById(R.id.textViewMessageReceived);
        }
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessageSent;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageSent = itemView.findViewById(R.id.textViewMessageSent);
        }
    }
}

