package com.example.whatsapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.DateConverter;
import com.example.whatsapp.R;

import java.util.LinkedList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    public LinkedList<Contact> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public ContactAdapter(Context context, LinkedList<Contact> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setContactList(LinkedList<Contact> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public LinkedList<Contact> getContactList() {
        return this.mList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // connect to viewholder
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ContactViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String base64Image = mList.get(position).getProfilePic().substring(mList.get(position).getProfilePic().indexOf(",") + 1);
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.profilePicImageView.setImageBitmap(bitmap);

        holder.displayNameTextView.setText(String.valueOf(mList.get(position).getDisplayName()));
        holder.lastMessageTextView.setText(String.valueOf(mList.get(position).getLastContent()));
        String date = DateConverter.getCreatedDate(mList.get(position).getLastCreated());
        holder.lastMessageDateTextView.setText(date);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mList.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicImageView;
        TextView displayNameTextView;
        TextView lastMessageTextView;
        TextView lastMessageDateTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            // search in view the item
            profilePicImageView = itemView.findViewById(R.id.profilePicImageView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            lastMessageDateTextView = itemView.findViewById(R.id.lastMessageDateTextView);
        }
    }

}