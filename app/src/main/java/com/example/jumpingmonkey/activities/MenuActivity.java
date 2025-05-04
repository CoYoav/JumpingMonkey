package com.example.jumpingmonkey.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText; // <-- Added import

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jumpingmonkey.R;
import com.example.jumpingmonkey.util.ScoreDatabase;
import com.example.jumpingmonkey.util.StateManager;

public class MenuActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set text to developer-only textbox
        EditText message = findViewById(R.id.message);
        if (message != null) {
            StateManager stateManager = StateManager.getInstance();
            int lastScore = stateManager.getLastScore();
            message.setText(stateManager.getPlayerName() + ", you scored: " + lastScore);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exit) {
            finish(); // Exit the app
            return true;
        }

        Intent intent = null;

        if (id == R.id.leaderBoard) {
            intent = new Intent(this, LeaderBoardActivity.class);
        } else if (id == R.id.retry) {
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.home) {
            intent = new Intent(this, LoginActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
