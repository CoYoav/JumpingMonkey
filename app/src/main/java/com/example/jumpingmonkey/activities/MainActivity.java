package com.example.jumpingmonkey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jumpingmonkey.R;
import com.example.jumpingmonkey.animation.MyCanvas;
import com.example.jumpingmonkey.util.ScoreDatabase;
import com.example.jumpingmonkey.util.StateManager;

/**
 * MainActivity is the core gameplay screen. It initializes the canvas view,
 * listens for game over state, and handles transition to the Menu screen.
 */
public class MainActivity extends AppCompatActivity {

    private MyCanvas myCanvas;
    private final StateManager stateManager = StateManager.getInstance();
    private ScoreDatabase db;

    /**
     * Initializes the game view and starts the game-over check loop.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = ScoreDatabase.getInstance(this);
        myCanvas = findViewById(R.id.myCanvas);

        // Apply padding to avoid system bars overlapping
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Start checking for game-over condition
        checkGameOver();
    }

    /**
     * Periodically checks if the game has ended.
     * If so, updates the score, stores it in the database, and transitions to the MenuActivity.
     */
    private void checkGameOver() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myCanvas.isGameOver()) {
                    int finalScore = myCanvas.getScore();
                    db.updateScore(stateManager.getPlayerName(), finalScore);
                    stateManager.setLastScore(finalScore);

                    // Transition to MenuActivity after a short delay
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }, 500); // 0.5 second delay before transitioning
                } else {
                    // Continue checking every 500ms
                    handler.postDelayed(this, 500);
                }
            }
        }, 500); // Initial delay before first check
    }
}
