package com.example.whatsapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.whatsapp.Adapters.ContactAdapter;
import com.example.whatsapp.Api.UserApi;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.User;
import com.example.whatsapp.R;
import com.example.whatsapp.Sql;
import com.example.whatsapp.ViewModels.ContactsViewModel;
import com.example.whatsapp.databinding.ActivityContactsBinding;

import java.util.LinkedList;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ContactsViewModel viewModel;
    private BroadcastReceiver messageReceiver;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        MutableLiveData<User> user = new MutableLiveData<>();
        Intent i = getIntent();
        if (i.hasExtra("username")) {
            username = (String) i.getSerializableExtra("username");
            User u = new User(username);
            user.setValue(u);
            UserApi userApi = new UserApi(user, this);
            userApi.getUser();

        }


        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        viewModel.reload();
        RecyclerView recyclerViewContacts;
        ContactAdapter contactAdapter;
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this,new LinkedList<>());
        recyclerViewContacts.setAdapter(contactAdapter);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

        viewModel.getContactList().observe(this, contacts -> {
            LinkedList<Contact> linkedListContacts = new LinkedList<>(contacts);
            contactAdapter.setContactList(linkedListContacts);
            refreshLayout.setRefreshing(false);
        });

        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
            refreshLayout.setRefreshing(false);
        });

        contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                contact.setProfilePic("");
                intent.putExtra("contactObject", contact);
                startActivity(intent);
            }
        });


// Create and register the BroadcastReceiver
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ContactsActivity")) {
                    String chatId = intent.getStringExtra("chatId");
                    String newMsg = intent.getStringExtra("message");

                    // Update the chat UI with the new message
                    //updateChatUI(chatId, newMsg);
                    viewModel.reload();
                }
            }
        };

        binding.btnAddPeople.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
            builder.setTitle("Add User");

            // Create an EditText for user input
            EditText editText = new EditText(ContactsActivity.this);
            editText.setHint("Enter username");
            builder.setView(editText);

            // Set positive button
            builder.setPositiveButton("Add", (dialog, which) -> {
                String username = editText.getText().toString().trim();

                // Check if the username is not empty
                if (!username.isEmpty()) {
                    viewModel.add(username);
                }
            });

            // Set negative button
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Cancel button clicked, do nothing
                dialog.dismiss();
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        binding.btnLogOut.setOnClickListener(view -> {
            Sql sql = new Sql(getApplicationContext());
            sql.deleteAll();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        binding.btnSetting.setOnClickListener(view ->{
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        } );

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.reload();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("ContactsActivity"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
    @Override
    public void onBackPressed() {
        // Block the back button functionality
        // Add your desired logic here, or leave it empty to do nothing
        // For example, you can display a message or prompt the user before allowing them to go back

        // To completely block the back button, remove the super call:
        // super.onBackPressed();
    }

}