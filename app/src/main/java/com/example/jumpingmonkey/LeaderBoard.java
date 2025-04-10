package com.example.jumpingmonkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LeaderBoard extends AppCompatActivity {

    private ScoreDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = ScoreDatabase.getInstance(this);
        populateLeaderboard();

        findViewById(R.id.start_button).setOnClickListener(v -> {
            startActivity(new Intent(LeaderBoard.this, MainActivity.class));
            finish();
        });
    }

    private void populateLeaderboard() {
        TableLayout table = findViewById(R.id.table);

        // Remove all rows except the header
        while (table.getChildCount() > 1) {
            table.removeViewAt(1);
        }

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

    private TextView makeCell(String text, int gravity) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(getResources().getColor(android.R.color.white, null));
        tv.setPadding(12, 8, 12, 8);
        tv.setGravity(gravity);
        return tv;
    }
}