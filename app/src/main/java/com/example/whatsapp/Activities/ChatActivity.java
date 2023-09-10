package com.example.whatsapp.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Adapters.MessageAdapter;
import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.ContactDao;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.R;
import com.example.whatsapp.ViewModels.MessagesViewModel;
import com.example.whatsapp.databinding.ActivityChatBinding;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private Contact contact;
    private ActivityChatBinding binding;
    private MessagesViewModel viewModel;
    private BroadcastReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        // Check if the intent has extra data
        if (intent.hasExtra("contactObject")) {
            // Retrieve the serialized Contact object from the intent
            contact = (Contact) intent.getSerializableExtra("contactObject");

            ImageView profilePicImageView = binding.profilePicImageView;
            AppDB database = AppDB.getInstance(MyApp.context);
            ContactDao contactDao = database.contactDao();
            FoundPic foundPic = new FoundPic(contactDao, contact, profilePicImageView);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(foundPic);

            TextView username = binding.displayNameTextView;
            username.setText(contact.getDisplayName());

            RecyclerView recyclerViewMessages;
            MessageAdapter messageAdapter;
            recyclerViewMessages = binding.recyclerViewChat;
            recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
            messageAdapter = new MessageAdapter(this, new LinkedList<>(), contact);
            recyclerViewMessages.setAdapter(messageAdapter);

            viewModel.setChatId(contact.getId());
            viewModel.reload();

            viewModel.getMessageList().observe(this, message -> {
                LinkedList<Message> linkedListMessage = new LinkedList<>(message);
                messageAdapter.setMessageList(linkedListMessage);
                recyclerViewMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
            });

            messageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("ChatActivity")) {
                        String chatId = intent.getStringExtra("chatId");
                        String newMsg = intent.getStringExtra("message");

                        // Update the chat UI with the new message
                        //updateChatUI(chatId, newMsg);
                        // Check if the received message is from the current chat
                        if (chatId.equals(contact.getId())) {
                            // Update the chat UI with the new message
                            //updateChatUI(chatId, newMsg);
                            viewModel.reload();
                        } else {
                            // Create a notification for the new message
                            createNotification(chatId, newMsg);
                        }
                    }
                }
            };

            binding.buttonSend.setOnClickListener(view -> {
                String name = binding.editTextMessage.getText().toString();
                if (!name.equals("")) {
                    viewModel.add(name);
                }
                binding.editTextMessage.setText("");
            });

            binding.btnSetting.setOnClickListener(view -> {
                Intent in = new Intent(this, SettingsActivity.class);
                startActivity(in);
            });
        }
    }

    private void createNotification(String chatId, String newMsg) {
        // Create an intent to open the ChatActivity when the notification is clicked
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chatId);
        intent.putExtra("message", newMsg);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set notification content
        String channelId = "com.example.chatapp.notification_channel";
        String channelName = "Chat Notifications";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Message")
                .setContentText(newMsg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Create the notification channel for devices running Android 8.0 (API level 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        int notificationId = 1; // A unique ID for the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.reload();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("ChatActivity"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    private static class FoundPic implements Runnable {
        private ContactDao dao;
        private Contact contact;
        private ImageView profilePicImageView;

        public FoundPic(ContactDao dao, Contact contact, ImageView profilePicImageView) {
            this.dao = dao;
            this.contact = contact;
            this.profilePicImageView = profilePicImageView;
        }

        @Override
        public void run() {
            String id = contact.getId();
            Contact retrievedContact = dao.get(id);

            // Ensure updating the UI is done on the main thread
            profilePicImageView.post(new Runnable() {
                @Override
                public void run() {
                    if (retrievedContact != null) {
                        String base64Image = retrievedContact.getProfilePic().substring(retrievedContact.getProfilePic().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        profilePicImageView.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }
}
