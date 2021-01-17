package com.example.connect4final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    public void duelBu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity game = new MainActivity(false);
        startActivity(intent);
    }

    public void singleBU(View view){
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity game = new MainActivity(true);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
}
