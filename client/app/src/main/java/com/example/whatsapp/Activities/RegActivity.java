package com.example.whatsapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.Api.UserApi;
import com.example.whatsapp.Entities.User;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivityRegBinding;

import java.io.IOException;
import java.io.InputStream;

public class RegActivity extends AppCompatActivity {
    private ActivityRegBinding binding;
    private MutableLiveData<User> userListData;
    private UserApi userApi;
    private ActivityResultLauncher<String> imageChooserLauncher;

    private String userName;
    private String password;
    private String nick;


    private static final int REQUEST_CODE = 1;

    private ImageView photoImageView;

    String base64ImageForUploadedPhoto = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAB4AAAAQ4AQMAAADSHVMAAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAGUExURf8AAP8AAP87Ju4AAAABdFJOU/4a4wd9AAAED0lEQVR42u3PQQ0AAAgEIDf7V1ZfpjhoQG2WKWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYeHccIj+8AGdU9s1O0HsQgAAAABJRU5ErkJggg==";



    // Convert the image to a base64 string
    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            // Convert the image bytes to a base64 string
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // handles the 'upload a photo' button and its preview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();

            // Convert the image to a base64 string
            base64ImageForUploadedPhoto = "data:image/png;base64," + convertImageToBase64(imageUri);

            // Set the selected image URI to the ImageView
            photoImageView.setImageURI(imageUri);
            photoImageView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userListData = new MutableLiveData<>();

        binding.login.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // Button to handle the 'upload a photo' request
        Button btnUploadPicture = findViewById(R.id.btnUploadPicture);

        btnUploadPicture.setOnClickListener(view -> {
            // opens the file picker
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            // filter the file picker to show only image files
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            // we use startActivityForResult() to launch the file picker and handle the result
            startActivityForResult(intent, REQUEST_CODE);
        });

        // contains the preview of the photo that is selected by the user to be his profile picture
        photoImageView = findViewById(R.id.photoImageView);




        binding.btnSetting.setOnClickListener(view ->{
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } );

        binding.signUpBtn.setOnClickListener(view -> {
            Log.i("reg", "pressed");
             userName = binding.editTextUsername.getText().toString();
             password = binding.editTextPassword.getText().toString();
             EditText passwordValid = binding.editTextConfirmPassword;
             nick = binding.editTextDisplayName.getText().toString();

            if(userName.isEmpty() || nick.isEmpty()
                    || password.isEmpty() || passwordValid.getText().toString().isEmpty()){
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }
            else if( password.length() < 4 ){
                Toast.makeText(this,"Password length should be at least 4 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            else if (!password.equals(passwordValid.getText().toString())){
                Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show();
                return;
            } else if (base64ImageForUploadedPhoto.length() > 10 * 1024 * 1024) {
                Toast.makeText(this,"Photo is too big",Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(userName, nick, password,base64ImageForUploadedPhoto);
            userListData.setValue(newUser);
            userApi = new UserApi(userListData,getApplicationContext());
            userApi.addUser();
        });
    }

}