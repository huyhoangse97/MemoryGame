package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.example.memorygame.database.AppDatabase;
import com.example.memorygame.database.Mode;
import com.example.memorygame.database.Round;
import com.example.memorygame.database.RoundDao;
import com.example.memorygame.database.RoundExecutorAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RoundActivity extends AppCompatActivity {
    private List<ImageButton> roundButtons = new ArrayList<ImageButton>();
    private AppDatabase db;
    private RoundDao roundDao;
    private RoundExecutorAdapter roundExecutorAdapter;
    private final String TAG = "RoundActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        //Initialize roundName and roundId;
        FileAdapter file = new FileAdapter(getApplicationContext(), "selected_mode.json");
        String jsonString = file.read();
        Log.d(TAG, jsonString);
        Gson gson = new Gson();
        Mode mode = gson.fromJson(jsonString, Mode.class);

        initDatabaseVariable();

        initRoundButton(mode);

//        initLayout(); //If the roundCount of each Mode is not the same => Inflate respectively layout;

        updateRoundDatabase(mode);

        setRoundOnClickListener(mode);
    }

    private void initDatabaseVariable() {
        db = AppDatabase.getDatabase(getApplicationContext());
        roundDao = db.roundDao();
        roundExecutorAdapter = new RoundExecutorAdapter(getApplicationContext(), roundDao);
    }

    private void setRoundOnClickListener(Mode mode) {
        for (int idx = 0; idx < roundButtons.size(); idx++){
            ImageButton selectedRound = roundButtons.get(idx);
            View.OnClickListener roundButtonClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent();
                    intent.setClass(RoundActivity.this, ContentActivity.class);
                    startActivity(intent);

                    //Cache selected round;
                    Round option = roundExecutorAdapter.getByModeAndRoundId(mode.getId(), selectedRound.getId());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(option);
                    FileAdapter file = new FileAdapter(getApplicationContext(), "selected_round.json");
                    file.write(jsonString);
                    Log.d(TAG, jsonString);
                }
            };
            selectedRound.setOnClickListener(roundButtonClickListener);
        }
    }

    private void updateRoundDatabase(Mode mode) {
        //Update roundId;
        List<Round> rounds = new ArrayList<Round>();
        rounds = roundExecutorAdapter.getByModeId(mode.getId());
        for (int idx = 0; idx < rounds.size(); idx++){
            Round round = rounds.get(idx);
            ImageButton roundButton = roundButtons.get(idx);
            round.setRoundId(roundButton.getId());
            roundExecutorAdapter.update(round);
        }
    }

    private void initRoundButton(Mode mode) {
        for (int idx = 0; idx < mode.getRoundCount(); idx++){
            String idName = "ib_round" + Integer.toString(idx+1);
            int roundButtonId = getResources().getIdentifier(idName, "id", getPackageName());
            ImageButton roundButton = (ImageButton) findViewById(roundButtonId);
            roundButtons.add(roundButton);
        }
    }

    @Override
    protected void onStart() {
        //animation set
        Gson gson = new Gson();
        FileAdapter file = new FileAdapter(getApplicationContext(), "animation_flag.json");
        String jsonString = file.read();
        AnimationFlag animationFlag = gson.fromJson(jsonString, AnimationFlag.class);
        int flag = animationFlag.getFlag();
        if (flag == 1){
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }
        else {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}