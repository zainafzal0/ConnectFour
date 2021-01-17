package com.example.connect4final;
public class Players {
    private String piece;
    private int turn;
    private boolean wins = false;

    //1 for red and 2 for yellow
    public Players(String piece, int turn){
        this.piece = piece;
        this.turn = turn;
    }

    public void changeTurn(){
        this.turn = -1* this.turn;
    }
    public boolean getTurn(){
        if (this.turn == -1){
            return false;
        }
        else{
            return true;
        }

    }
    public void setWin(boolean result){
        this.wins = result;
    }
    public boolean getWin(){
        return this.wins;
    }
    public String getPiece(){
        return this.piece;
    }
}