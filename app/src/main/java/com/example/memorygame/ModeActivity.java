package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.memorygame.database.AppDatabase;
import com.example.memorygame.database.Mode;
import com.example.memorygame.database.ModeDao;
import com.example.memorygame.database.ModeExecutorAdapter;
import com.example.memorygame.database.Round;
import com.example.memorygame.database.RoundDao;
import com.example.memorygame.database.RoundExecutorAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeActivity extends AppCompatActivity {
    private ImageButton ib_simple, ib_try_limited, ib_time_limited, ib_one_try;
    private List<ImageButton> imageButtons = new ArrayList<ImageButton>();
    private Gson gson = new Gson();
    private AppDatabase db;
    private RoundDao roundDao;
    private ModeDao modeDao;
    private ModeExecutorAdapter modeExecutorAdapter;
    private RoundExecutorAdapter roundExecutorAdapter;
    private final String TAG = "ModeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        init();
    }

    private void init() {
        initButtons();

        initDatabase();

        setClickEvent();
    }

    private void initDatabase() {
        db = AppDatabase.getDatabase(getApplicationContext());
        roundDao = db.roundDao();
        modeDao = db.modeDao();
        modeExecutorAdapter = new ModeExecutorAdapter(getApplicationContext(), modeDao);
        roundExecutorAdapter = new RoundExecutorAdapter(getApplicationContext(), roundDao);

        //Initialize MODE TABLE
        modeExecutorAdapter.reset();
        modeExecutorAdapter.insert(new Mode("simple", ib_simple.getId()));
        modeExecutorAdapter.insert(new Mode("try_limited", ib_try_limited.getId()));
        modeExecutorAdapter.insert(new Mode("time_limited", ib_time_limited.getId()));
        modeExecutorAdapter.insert(new Mode("one_try", ib_one_try.getId()));

        //Initialize ROUND TABLE
        roundExecutorAdapter.reset();

        //Insert round to Database;
        for (int modeIdx = 0; modeIdx < imageButtons.size(); modeIdx++){

            ImageButton imageButton = imageButtons.get(modeIdx);
            Mode mode = modeExecutorAdapter.getById(imageButton.getId());

            for (int roundIdx = 0; roundIdx < mode.getRoundCount(); roundIdx++){
                Round round = new Round();
                round.setModeName(mode.getName());
                round.setModeId(mode.getId());
                String roundName = "round_" + Integer.toString(roundIdx+1);
                round.setRoundName(roundName);

                roundExecutorAdapter.insert(round);
            }
        }
    }

    private void setClickEvent() {
        for (int idx = 0; idx < imageButtons.size(); idx++){
            ImageButton imageButton = imageButtons.get(idx);

            View.OnClickListener modeClickListener = new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent();
                    intent.setClass(ModeActivity.this, RoundActivity.class);
                    startActivity(intent);

                    //Save animation flag;
                    AnimationFlag animationFlag = new AnimationFlag(1);
                    String jsonString = gson.toJson(animationFlag);
                    FileAdapter file = new FileAdapter(getApplicationContext(), "animation_flag.json");
                    file.write(jsonString);

                    Mode mode = modeExecutorAdapter.getById(imageButton.getId());
                    //save selected mode:
                    Mode selectedMode = new Mode(mode.getName(), mode.getId());
                    String jsonString2 = gson.toJson(selectedMode);
                    FileAdapter file2 = new FileAdapter(getApplicationContext(), "selected_mode.json");
                    file2.write(jsonString2);
                }
            };
            imageButton.setOnClickListener(modeClickListener);
        }
    }

    private void initButtons() {
        ib_simple = findViewById(R.id.ib_simple);
        ib_try_limited = findViewById(R.id.ib_try_limited);
        ib_time_limited = findViewById(R.id.ib_time_limited);
        ib_one_try = findViewById(R.id.ib_one_try);
        imageButtons.add(ib_simple);
        imageButtons.add(ib_try_limited);
        imageButtons.add(ib_time_limited);
        imageButtons.add(ib_one_try);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}