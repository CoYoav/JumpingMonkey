package com.example.jumpingmonkey.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jumpingmonkey.R;
import com.example.jumpingmonkey.util.StateManager;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private StateManager stateManager = StateManager.getInstance();
    private static final int SPEECH_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> contentLauncher;

    EditText nameInput;
    Button startButton;
    ImageButton micButton;
    ImageButton imgContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.nameInput);
        startButton = findViewById(R.id.startButton);
        micButton = findViewById(R.id.micButton);
        imgContact = findViewById(R.id.imgContact);
        initcontentP();
        imgContact.setOnClickListener(v -> {

            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK);
            contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            contentLauncher.launch(contactPickerIntent);
        });
        startButton.setOnClickListener(v -> {
            stateManager.setPlayerName(nameInput.getText().toString());
            if (!stateManager.getPlayerName().equals("")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // optional
            }
        });

        // Mic button logic
        micButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your name");

            try {
                startActivityForResult(intent, SPEECH_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Speech not supported", Toast.LENGTH_SHORT).show();
                Log.e("SpeechError", e.toString());
            }
        });
    }

    // Handle speech result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                EditText nameInput = findViewById(R.id.nameInput);
                nameInput.setText(result.get(0));
            }
        }
    }

    private void initcontentP() {

        contentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Cursor cursor = null;

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            try {
                                Uri uri = intent.getData();

                                cursor = getContentResolver().query(uri, null, null, null, null);
                                cursor.moveToFirst();


                                int phoneIndexName = cursor.getColumnIndex
                                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);


                                String phoneName = cursor.getString(phoneIndexName);
                                //  tvHead.setText(phoneName + " ");

                                nameInput.setText(phoneName);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }
}
