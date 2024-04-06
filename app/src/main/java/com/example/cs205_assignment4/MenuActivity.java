package com.example.cs205_assignment4;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView imgStart = findViewById(R.id.imgStart);
        ImageView imgHelp = findViewById(R.id.imgHelp);
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MenuHelpActivity.class);
                startActivity(intent);
            }
        });


    }

    public void buttonClicked(View view) {
        Intent indent = new Intent(this, MenuActivity.class);
        startActivity(indent);
    }
}