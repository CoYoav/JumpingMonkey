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

/**
 * LoginActivity allows users to input their name, use speech recognition,
 * or select a contact to autofill their name before starting the game.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 1;

    private final StateManager stateManager = StateManager.getInstance();
    private ActivityResultLauncher<Intent> contactPickerLauncher;

    private EditText nameInput;
    private Button startButton;
    private ImageButton micButton;
    private ImageButton imgContact;

    /**
     * Called when the activity is first created.
     * Sets up UI components, button listeners, and contact/speech integration.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Apply system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        nameInput = findViewById(R.id.nameInput);
        startButton = findViewById(R.id.startButton);
        micButton = findViewById(R.id.micButton);
        imgContact = findViewById(R.id.imgContact);

        initContactPicker();

        // Launch contact picker on button click
        imgContact.setOnClickListener(v -> {
            Intent contactIntent = new Intent(Intent.ACTION_PICK);
            contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            contactPickerLauncher.launch(contactIntent);
        });

        // Start game if name is entered
        startButton.setOnClickListener(v -> {
            String playerName = nameInput.getText().toString().trim();
            stateManager.setPlayerName(playerName);
            if (!playerName.isEmpty()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        // Launch speech recognizer
        micButton.setOnClickListener(v -> {
            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your name");

            try {
                startActivityForResult(speechIntent, SPEECH_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(this, "Speech not supported", Toast.LENGTH_SHORT).show();
                Log.e("SpeechError", "Error launching speech recognizer", e);
            }
        });
    }

    /**
     * Handles results from activities like speech input.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                nameInput.setText(result.get(0));
            }
        }
    }

    /**
     * Initializes the contact picker using Activity Result API.
     * When a contact is selected, sets their name into the input field.
     */
    private void initContactPicker() {
        contactPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("Range")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri contactUri = result.getData().getData();
                            try (Cursor cursor = getContentResolver().query(contactUri, null, null, null, null)) {
                                if (cursor != null && cursor.moveToFirst()) {
                                    String contactName = cursor.getString(
                                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                                    );
                                    nameInput.setText(contactName);
                                }
                            } catch (Exception e) {
                                Log.e("ContactPicker", "Failed to read contact", e);
                            }
                        }
                    }
                }
        );
    }
}
