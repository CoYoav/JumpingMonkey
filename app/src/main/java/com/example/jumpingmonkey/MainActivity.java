package com.example.jumpingmonkey;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MyCanvas myCanvas;  // Reference to the MyCanvas class
    private StateManager stateManager = StateManager.getInstance();
    private ScoreDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        db = ScoreDatabase.getInstance(this);

        setContentView(R.layout.activity_main);

        // Get the MyCanvas view and assign it to the variable
        myCanvas = findViewById(R.id.myCanvas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Continuously check if the game is over
        checkGameOver();
    }

    private void checkGameOver() {
        // Create a Handler to check for game over state periodically
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the game is over using the isGameOver() method from MyCanvas
                if (myCanvas.isGameOver()) {
                    db.updateScore(stateManager.getPlayerName(), myCanvas.getScore());
                    // If game over, delay for 1.5 seconds and then transition to MenuActivity
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish(); // Close MainActivity so it doesn't stay in the back stack
                    }, 500); // 1.5 seconds delay
                } else {
                    // If the game is not over, check again after a short delay
                    handler.postDelayed(this, 500); // Check every 500 ms
                }
            }
        }, 500); // Start the first check after 500 ms
    }
}