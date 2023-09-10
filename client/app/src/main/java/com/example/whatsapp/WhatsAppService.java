package com.example.whatsapp;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class WhatsAppService extends FirebaseMessagingService {

    public WhatsAppService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Handle the received message data
            String chatId = remoteMessage.getData().get("chatId");
            String newMsg = remoteMessage.getData().get("message");

            // Update the chat activity with the new message
            updateChatActivity(chatId, newMsg);
            updateContactActivity(chatId, newMsg);
        }
    }

    private void updateContactActivity(String chatId, String newMsg) {
        // Implement your logic to update the chat activity
        // You can use any mechanism to pass the message data to the chat activity,
        // such as broadcasting an intent or using a callback/event system.
        // Here, we'll assume you have a ChatActivity class and use a broadcast intent.

        Intent intent = new Intent("ContactsActivity");
        intent.putExtra("chatId", chatId);
        intent.putExtra("message", newMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void updateChatActivity(String chatId, String newMsg) {
        // Implement your logic to update the chat activity
        // You can use any mechanism to pass the message data to the chat activity,
        // such as broadcasting an intent or using a callback/event system.
        // Here, we'll assume you have a ChatActivity class and use a broadcast intent.

        Intent intent = new Intent("ChatActivity");
        intent.putExtra("chatId", chatId);
        intent.putExtra("message", newMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
