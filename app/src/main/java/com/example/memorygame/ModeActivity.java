package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

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
    private RelativeLayout simpleLayout, tryLimitedLayout, timeLimitedLayout, oneTryLayout;
    private boolean simple_flag = false, try_limited_flag = false, time_limited_flag = false, one_try_flag = false;
    private RelativeLayout rl_simple, rl_try_limited, rl_time_limited, rl_one_try;
    private List<Button> buttons = new ArrayList<Button>();
    private Gson gson = new Gson();
    private AppDatabase db;
    private RoundDao roundDao;
    private ModeDao modeDao;
    private ModeExecutorAdapter modeExecutorAdapter;
    private RoundExecutorAdapter roundExecutorAdapter;
    private final String TAG = "ModeActivity";
    private AnimatorSet animation = new AnimatorSet();


    Mode mode = new Mode();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        init();
    }

    private void init() {

        initializeVariable();
        setClickEvent();
    }

    private void initializeVariable() {
        simpleLayout = findViewById(R.id.simple_mode_container);
        tryLimitedLayout = findViewById(R.id.try_limited_mode_container);
        timeLimitedLayout = findViewById(R.id.time_limited_mode_container);
        oneTryLayout = findViewById(R.id.one_try_mode_container);
    }

    private void initDatabase() {
//        db = AppDatabase.getDatabase(getApplicationContext());
//        roundDao = db.roundDao();
//        modeDao = db.modeDao();
//        modeExecutorAdapter = new ModeExecutorAdapter(getApplicationContext(), modeDao);
//        roundExecutorAdapter = new RoundExecutorAdapter(getApplicationContext(), roundDao);
//
//        //Initialize MODE TABLE
//        modeExecutorAdapter.reset();
//        modeExecutorAdapter.insert(new Mode("simple", btn_simple.getId()));
//        modeExecutorAdapter.insert(new Mode("try_limited", btn_try_limited.getId()));
//        modeExecutorAdapter.insert(new Mode("time_limited", btn_time_limited.getId()));
//        modeExecutorAdapter.insert(new Mode("one_try", btn_one_try.getId()));
//
//        //Initialize ROUND TABLE
//        roundExecutorAdapter.reset();
//
//        //Insert round to Database;
//        for (int modeIdx = 0; modeIdx < buttons.size(); modeIdx++){
//
//            Button button = buttons.get(modeIdx);
//            Mode mode = modeExecutorAdapter.getById(button.getId());
//
//            if (mode == null){
//                Log.d(TAG, "mode is null");
//            } else{
//                Gson gson2 = new Gson();
//                String modeJson = gson.toJson(mode);
//                Log.d(TAG, modeJson);
//            }
//
//            for (int roundIdx = 0; roundIdx < mode.getRoundCount(); roundIdx++){
//                Round round = new Round();
//                round.setModeName(mode.getName());
//                round.setModeId(mode.getId());
//                String roundName = "round_" + Integer.toString(roundIdx+1);
//                round.setRoundName(roundName);
//                round.setRoundValue(roundIdx+1);
//                roundExecutorAdapter.insert(round);
//            }
//        }
    }

    private void setClickEvent() {

        simpleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout selectedMode = (RelativeLayout)findViewById(R.id.simple_mode_front_layout);
                LinearLayout parentLayout = findViewById(R.id.simple_diff_option);
                if (simple_flag == false){

                    LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                    layoutInflater.inflate(R.layout.simple_difficult_option_layout, parentLayout, true);

                    LinearLayout diffOptionLayout = findViewById(R.id.simple_diff_option_layout);
                    float distance = selectedMode.getHeight();
                    diffOptionLayout.animate().translationY(distance).setDuration(500);
                    animation = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.view_appear_slidedown);
                    animation.setTarget(diffOptionLayout);
                    animation.start();

                    simple_flag = true;
                }
                else {
                    LinearLayout diffOptionLayout = findViewById(R.id.simple_diff_option_layout);
                    float distance = diffOptionLayout.getHeight() * (-1);
                    diffOptionLayout.animate().translationY(distance).setDuration(1000);
//                    animation = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.view_disappear_slideup);
//                    animation.setTarget(diffOptionLayout);
//                    animation.start();
                    Runnable removeView = new Runnable() {
                        @Override
                        public void run() {
                            parentLayout.removeView(diffOptionLayout);
                        }
                    };
                    Handler myHandler = new Handler();
                    myHandler.postDelayed(removeView, 1000);
                    simple_flag = false;
                }

            }
        });
        tryLimitedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout selectedMode = (RelativeLayout)v;
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                layoutInflater.inflate(R.layout.try_limited_difficult_option_layout, selectedMode, true);

                LinearLayout diffOptionLayout = findViewById(R.id.try_limited_diff_option_layout);
                float distance = selectedMode.getHeight();
                diffOptionLayout.animate().translationY(distance).setDuration(500);
            }
        });
        timeLimitedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout selectedMode = (RelativeLayout)v;
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                layoutInflater.inflate(R.layout.time_limited_difficult_option_layout, selectedMode, true);

                LinearLayout diffOptionLayout = findViewById(R.id.time_limited_diff_option_layout);
                float distance = selectedMode.getHeight();
                diffOptionLayout.animate().translationY(distance).setDuration(500);
            }
        });
        oneTryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout selectedMode = (RelativeLayout)v;
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                layoutInflater.inflate(R.layout.one_try_difficult_option_layout, selectedMode, true);

                LinearLayout diffOptionLayout = findViewById(R.id.one_try_diff_option_layout);
                float distance = selectedMode.getHeight();
                diffOptionLayout.animate().translationY(distance).setDuration(500);
            }
        });

//        View.OnClickListener modeClickListener = new View.OnClickListener(){
//            @SuppressLint("NewApi")
//            public void onClick(View view){
//                Button button = (Button) view;
//
//                //Save animation flag;
//                AnimationFlag animationFlag = new AnimationFlag(1);
//                String jsonString = gson.toJson(animationFlag);
//                FileAdapter file = new FileAdapter(getApplicationContext(), "animation_flag.json");
//                file.write(jsonString);
//
//                Mode mode = modeExecutorAdapter.getById(button.getId());
//                //save selected mode:
//                Mode selectedMode = new Mode(mode.getName(), mode.getId());
//                String jsonString2 = gson.toJson(selectedMode);
//                FileAdapter file2 = new FileAdapter(getApplicationContext(), "selected_mode.json");
//                file2.write(jsonString2);
//
//                PopupMenu diffMenu = new PopupMenu(ModeActivity.this, view);
//                diffMenu.getMenu().add("EASY");
//                diffMenu.getMenu().add("NORMAL");
//                diffMenu.getMenu().add("HARD");
//                diffMenu.getMenu().add("VERY HARD");
//                diffMenu.getMenu().add("NIGHTMARE");
//                diffMenu.getMenu().add("ULTIMATE");
//                diffMenu.setGravity(Gravity.CENTER);
//                PopupMenu.OnMenuItemClickListener diffClickListener = new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Intent intent = new Intent();
//                        intent.setClass(ModeActivity.this, RoundActivity.class);
//                        startActivity(intent);
//                        return false;
//                    }
//                };
//                diffMenu.setOnMenuItemClickListener(diffClickListener);
//                diffMenu.show();
//            }
//        };
//        for (int idx = 0; idx < buttons.size(); idx++) {
//            buttons.get(idx).setOnClickListener(modeClickListener);
//        }
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