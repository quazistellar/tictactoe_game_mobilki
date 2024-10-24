package com.example.tictactoe_game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FriendGame extends AppCompatActivity {

    private Button[][] btns = new Button[3][3];
    private int[][] board = new int[3][3];
    private int
            player1Wins = 0,
            player1Pass = 0,
            player1Draw = 0;
    private int
            player2Wins = 0,
            player2Pass = 0,
            player2Draw = 0;
    private boolean player1Turn = true;

    Button btn_exit;
    TextView text_turn;
    TextView text_stats;
    private SharedPreferences data;
    private SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_game);

        text_turn = findViewById(R.id.playerTurnTextView);
        btn_exit = findViewById(R.id.back_to);
        text_stats = findViewById(R.id.statisticsTextView);
        data = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        editor = data.edit();

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentt = new Intent(FriendGame.this, MainActivity.class);
                startActivity(intentt);
            }
        });

        loadStats();
        updateStats();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int buttonId = getResources().getIdentifier("button" + i + j, "id", getPackageName());
                btns[i][j] = findViewById(buttonId);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int columnLine = j;
                btns[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (board[row][columnLine] == 0) {
                            if (player1Turn) {
                                btns[row][columnLine].setText("X");
                                btns[row][columnLine].setTextColor(Color.parseColor("#000000"));
                                board[row][columnLine] = 1;
                            } else {
                                btns[row][columnLine].setText("0");
                                btns[row][columnLine].setTextColor(Color.parseColor("#000080"));
                                board[row][columnLine] = 2;
                            }
                            player1Turn = !player1Turn;
                            who_play_text();
                            if (checkWin()) {
                                gameEnd();
                            } else if (checkDraw()) {
                                Toast.makeText(FriendGame.this, "It's draw!", Toast.LENGTH_SHORT).show();
                                gameEnd();
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            }
        }
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != 0 && board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                return true;
            }
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true;
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true;
        }
        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void who_play_text() {
        if (player1Turn) {
            text_turn.setText("Player 1 (X) turn");
        } else {
            text_turn.setText("Player 2 (O) turn");
        }
    }

    private void gameEnd() {
        if (player1Turn) {
            player2Pass++;
            player1Wins++;
        } else {
            player1Pass++;
            player2Wins++;
        }
        if (checkDraw()) {
            player1Draw++;
            player2Draw++;
        }
        saveStatistics();
        updateStats();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btns[i][j].setEnabled(false);
            }
        }
    }

    private void saveStatistics() {
        editor.putInt("player1Wins", player1Wins);
        editor.putInt("player1Losses", player1Pass);
        editor.putInt("player1Draws", player1Draw);
        editor.putInt("player2Wins", player2Wins);
        editor.putInt("player2Losses", player2Pass);
        editor.putInt("player2Draws", player2Draw);
        editor.apply();
    }

    private void loadStats() {
        player1Wins = data.getInt("player1Wins", 0);
        player1Pass = data.getInt("player1Losses", 0);
        player1Draw = data.getInt("player1Draws", 0);
        player2Wins = data.getInt("player2Wins", 0);
        player2Pass = data.getInt("player2Losses", 0);
        player2Draw = data.getInt("player2Draws", 0);
    }

    private void updateStats() {
        text_stats.setText(
                "                   Player 1\n\n" +
                        "    Wins     Deafeats     Draws\n" +
                        "      " +    player1Wins + "               " + player1Pass + "                 " + player1Draw + "\n" +
                        "\n\n" +
                        "                   Player 2\n\n" +
                        "    Wins     Deafeats     Draws\n" +
                        "      " +    player2Wins + "                " + player2Pass + "                " + player2Draw + "\n"
        );
    }
    public void resetGame(View view) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btns[i][j].setText("");
                btns[i][j].setEnabled(true);
                board[i][j] = 0;
            }
        }
        player1Turn = true;
        who_play_text();
    }
}
