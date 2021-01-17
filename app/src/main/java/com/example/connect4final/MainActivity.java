package com.example.connect4final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private boolean gameType;

    public MainActivity(boolean aGame){
        this.gameType = aGame;
    }

    
    //Create a 2d array of the board and set the pieces
    String[][] board = new String[6][7];
    ImageView[][] pieces = new ImageView[6][7];


    //Create players
    Players player1 = new Players("R", 1);
    Players player2 = new Players("Y", -1);

    //Column button availability
    public void colBuStatus(boolean x){
        for (int i =1;i<=7;i++){
            int id= getResources().getIdentifier("col" +i +"Bu", "id", getPackageName());
            findViewById(id).setEnabled(x);
        }
    }

    //Create game
    public void createGame(){
        //Reset and start timer
       resetChronometer();
       startChronometer();

        //Set pieces and set board
        for(int row = 0; row<6; row++){
            for(int column = 0; column<7; column++){
                int id= getResources().getIdentifier("piece"+(row+1) +(column+1), "id", getPackageName());
                pieces[row][column]= (ImageView)findViewById(id);
                pieces[row][column].setAlpha(0f);
                board[row][column] = "X";
            }
        }
        //Enables column buttons
        colBuStatus(true);

        findViewById(R.id.textView5).setEnabled(true);
        //Sets both players to no wins
        player1.setWin(false);
        player2.setWin(false);
        borderVisibility();
        findViewById(R.id.dim).setAlpha(0f);
        findViewById(R.id.mainMenuBu).setVisibility(View.GONE);
        findViewById(R.id.rematchBu).setVisibility(View.GONE);
        findViewById(R.id.gameResult).setVisibility(View.GONE);
        findViewById(R.id.resumeBu).setVisibility(View.GONE);
    }

    public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    //Avatar border visibility
    public void borderVisibility(){
        if (player1.getTurn()){
            findViewById(R.id.avatarBorder1).animate().alpha(1f).setDuration(500);
            findViewById(R.id.avatarBorder2).animate().alpha(0f).setDuration(500);
        }
        else{
            findViewById(R.id.avatarBorder1).animate().alpha(0f).setDuration(500);
            findViewById(R.id.avatarBorder2).animate().alpha(1f).setDuration(500);
        }
    }


    //Add piece
    public void addPiece(int column){
        //Adds piece to place in column where a piece does not exist
        for( int row = 5; row>=0; row--){
            if(board[row][column].equals("X")){
                pieces[row][column].setTranslationY(-1000);
                if(player1.getTurn()){
                    pieces[row][column].setImageResource(R.drawable.red_piece);
                    board[row][column]=player1.getPiece();
                }
                else{
                    pieces[row][column].setImageResource(R.drawable.yellow_piece);
                    board[row][column]=player2.getPiece();
                }
                //Changes turns
                player1.changeTurn();
                player2.changeTurn();

                pieces[row][column].setAlpha(1f);
                pieces[row][column].animate().translationYBy(1000).setDuration(1000);
                break;
            }
        }
        checkWin();
        borderVisibility();
    }



    //Open end screen
    public void openEndScreen(){
        //After the piece
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                findViewById(R.id.dim).setAlpha(0.5f);
                findViewById(R.id.mainMenuBu).setVisibility(View.VISIBLE);
                findViewById(R.id.rematchBu).setVisibility(View.VISIBLE);
                TextView text = findViewById(R.id.gameResult);
                text.setVisibility(View.VISIBLE);

                if(player1.getWin()){

                    text.setText("Player 1 Wins!!!");
                }
                else{
                    text.setText("Player 2 Wins!!!");
                }

            }
        }, 5500);
        pauseChronometer();
        findViewById(R.id.textView5).setEnabled(false);
    }


    //Rematch button
    public void rematchBu(View view){
        createGame();
    }


    //Main menu button
    public void mainMenuBu(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    //Main menu button on top left
    public void menuBu(View view){
        findViewById(R.id.rematchBu).setVisibility(View.VISIBLE);
        findViewById(R.id.mainMenuBu).setVisibility(View.VISIBLE);
        findViewById(R.id.resumeBu).setVisibility(View.VISIBLE);
        findViewById(R.id.dim).setAlpha(0.5f);
        colBuStatus(false);
        pauseChronometer();

    }

    public void resumeBu(View view){
        findViewById(R.id.rematchBu).setVisibility(View.GONE);
        findViewById(R.id.mainMenuBu).setVisibility(View.GONE);
        findViewById(R.id.resumeBu).setVisibility(View.GONE);
        findViewById(R.id.dim).setAlpha(0f);
        colBuStatus(true);
        startChronometer();
    }


    //Win animation
    public void win(final ImageView view){
        colBuStatus(false);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i =0;i<4;i++) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            view.animate().alpha(0f).setDuration(500);
                        }
                    }, 500 + 2*i*500);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            view.animate().alpha(1f).setDuration(500);
                        }
                    }, 1000 + 2*i*500);

                }
                openEndScreen();
            }
        }, 1000 );
    }


    //Checks win in vertical, horizontal and diagonal directions
    public void checkWin(){
        //Check Horizontal Win
        for(int row=0;row<6;row++){
            for(int column=0;column<7;column++){
                //Check Horizontal Win
                if(column<4){
                    if(board[row][column].equals("R")){
                        if(board[row][column].equals("R")&&board[row][column+1].equals("R")&&board[row][column+2].equals("R")&&board[row][column+3].equals("R")){
                            player1.setWin(true);
                            for(int i=0;i<4;i++) {
                                win(pieces[row][column+i]);
                            }

                        }
                    }
                    if(board[row][column].equals("Y")){
                        if(board[row][column].equals("Y")&&board[row][column+1].equals("Y")&&board[row][column+2].equals("Y")&&board[row][column+3].equals("Y")){
                            player2.setWin(true);
                            for(int i=0;i<4;i++) {
                                win(pieces[row][column+i]);
                            }
                        } } }

                //Check Vertical Win
                if(row<3){
                    if(board[row][column].equals("R")){
                        if(board[row][column].equals("R")&&board[row+1][column].equals("R")&&board[row+2][column].equals("R")&&board[row+3][column].equals("R")){
                            player1.setWin(true);
                            for(int i=0;i<4;i++) {
                                win(pieces[row+i][column]);
                            }


                        }
                    }
                    if(board[row][column].equals("Y")){
                        if(board[row][column].equals("Y")&&board[row+1][column].equals("Y")&&board[row+2][column].equals("Y")&&board[row+3][column].equals("Y")){
                            player2.setWin(true);
                            for(int i=0;i<4;i++) {
                                win(pieces[row+i][column]);
                            }
                        } } }


                //Check Diagonal Win
                if(row>=3&&column<4){
                    if(board[row][column].equals("R")&&board[row-1][column+1].equals("R")&&board[row-2][column+2].equals("R")&&board[row-3][column+3].equals("R")){
                        player1.setWin(true);
                        for(int i=0;i<4;i++) {
                            win(pieces[row-i][column+i]);
                        }
                    }
                    if(board[row][column].equals("Y")&&board[row-1][column+1].equals("Y")&&board[row-2][column+2].equals("Y")&&board[row-3][column+3].equals("Y")){
                        player2.setWin(true);
                        for(int i=0;i<4;i++) {
                            win(pieces[row-i][column+i]);
                        }
                    } }

                //Check Diagonal Win
                if(row>=3&&column>2){
                    if(board[row][column].equals("R")&&board[row-1][column-1].equals("R")&&board[row-2][column-2].equals("R")&&board[row-3][column-3].equals("R")){
                        player1.setWin(true);
                        for(int i=0;i<4;i++) {
                            win(pieces[row-i][column-i]);
                        }
                    }
                    if(board[row][column].equals("Y")&&board[row-1][column-1].equals("Y")&&board[row-2][column-2].equals("Y")&&board[row-3][column-3].equals("Y")){
                        player2.setWin(true);
                        for(int i=0;i<4;i++) {
                            win(pieces[row-i][column-i]);
                        }
                    }

                } } }
    }


    //Click each column
    public void col1Click(View view){
        addPiece(0);
    }
    public void col2Click(View view){
        addPiece(1);
    }
    public void col3Click(View view){
        addPiece(2);
    }
    public void col4Click(View view){
        addPiece(3);
    }
    public void col5Click(View view){
        addPiece(4);
    }
    public void col6Click(View view){
        addPiece(5);
    }
    public void col7Click(View view){
        addPiece(6);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        //Resets the game
        createGame();
    }
}
