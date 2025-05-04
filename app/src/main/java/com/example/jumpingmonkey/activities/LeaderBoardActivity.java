package com.example.jumpingmonkey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jumpingmonkey.R;
import com.example.jumpingmonkey.util.ScoreDatabase;

/**
 * LeaderBoardActivity displays the top scores from the ScoreDatabase in a table format.
 */
public class LeaderBoardActivity extends AppCompatActivity {

    private ScoreDatabase db;

    /**
     * Called when the activity is first created.
     * Sets up the UI, applies system window insets, initializes the database, and populates the leaderboard.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);

        // Apply padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = ScoreDatabase.getInstance(this);
        populateLeaderboard();

        // Set listener to return to the main activity
        findViewById(R.id.start_button).setOnClickListener(v -> {
            startActivity(new Intent(LeaderBoardActivity.this, MainActivity.class));
            finish();
        });
    }

    /**
     * Populates the leaderboard table with the top 5 scores from the ScoreDatabase.
     * Each row includes a ranking number, player name, and score.
     */
    private void populateLeaderboard() {
        TableLayout table = findViewById(R.id.table);

        // Remove all rows except the header
        while (table.getChildCount() > 1) {
            table.removeViewAt(1);
        }

        // Add top 5 scores
        for (int i = 1; i <= 5; i++) {
            String name = db.getName(i);
            int score = db.getScore(i);

            if (name == null) name = "---";
            if (score < 0) score = 0;

            TableRow row = new TableRow(this);
            row.setPadding(4, 4, 4, 4);

            TextView numView = makeCell(i + ".", Gravity.START);
            TextView nameView = makeCell(name, Gravity.CENTER);
            TextView scoreView = makeCell(String.valueOf(score), Gravity.END);

            row.addView(numView);
            row.addView(nameView);
            row.addView(scoreView);

            table.addView(row);
        }
    }

    /**
     * Creates and returns a styled TextView to be used as a cell in the leaderboard table.
     *
     * @param text    The text to display in the cell.
     * @param gravity The gravity (alignment) of the text.
     * @return A configured TextView instance.
     */
    private TextView makeCell(String text, int gravity) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        tv.setPadding(12, 8, 12, 8);
        tv.setGravity(gravity);
        return tv;
    }
}
