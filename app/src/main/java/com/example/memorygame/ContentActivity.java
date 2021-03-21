package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

public class ContentActivity extends AppCompatActivity {

    private static final String TAG = "ContentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Button btn_fruits = findViewById(R.id.btn_fruits);
        btn_fruits.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ContentActivity.this, PlayActivity.class);
                startActivity(intent);
            };
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        Gson gson = new Gson();
        AnimationFlag animationFlag = new AnimationFlag(2);
        String jsonString = gson.toJson(animationFlag);
        FileAdapter file = new FileAdapter(getApplicationContext(), "animation_flag.json");
        file.write(jsonString);
        super.onBackPressed();
    }
}