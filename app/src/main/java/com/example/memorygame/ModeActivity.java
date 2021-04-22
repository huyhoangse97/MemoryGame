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
import android.view.ViewGroup;
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
    private ArrayList<RelativeLayout> modeLayouts;
    private ArrayList<RelativeLayout> modeFrontLayouts;
    private ArrayList<Boolean> diffInflateFlags;
    private ArrayList<String> diffStringIdNames;
    private final int MODE_COUNT = 4;
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
        RelativeLayout simpleLayout = findViewById(R.id.simple_mode_container);
        RelativeLayout tryLimitedLayout = findViewById(R.id.try_limited_mode_container);
        RelativeLayout timeLimitedLayout = findViewById(R.id.time_limited_mode_container);
        RelativeLayout oneTryLayout = findViewById(R.id.one_try_mode_container);
        modeLayouts = new ArrayList<RelativeLayout>(Arrays.asList(simpleLayout, tryLimitedLayout,
                timeLimitedLayout, oneTryLayout));

        RelativeLayout simpleFrontLayout = findViewById(R.id.simple_mode_front_layout);
        RelativeLayout tryLimitedFrontLayout = findViewById(R.id.try_limited_mode_front_layout);
        RelativeLayout timeLimitedFrontLayout = findViewById(R.id.time_limited_mode_front_layout);
        RelativeLayout oneTryFrontLayout = findViewById(R.id.one_try_mode_front_layout);
        modeFrontLayouts = new ArrayList<RelativeLayout>(Arrays.asList(simpleFrontLayout, tryLimitedFrontLayout,
                timeLimitedFrontLayout, oneTryFrontLayout));

        diffInflateFlags = new ArrayList<Boolean> (Arrays.asList(false, false, false, false));
        diffStringIdNames = new ArrayList<String> (Arrays.asList("simple_difficult_option_layout",
                "try_limited_difficult_option_layout", "time_limited_difficult_option_layout", "one_try_difficult_option_layout"));
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

        for (int idx = 0; idx < MODE_COUNT; idx++) {
            int finalIdx = idx;
            View.OnClickListener modeClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout selectedMode = (RelativeLayout)v;
                    if (!diffInflateFlags.get(finalIdx)){
                        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                        int diffLayoutId = getResources().getIdentifier(diffStringIdNames.get(finalIdx), "layout", getPackageName());
                        layoutInflater.inflate(diffLayoutId, selectedMode, true);

                        int diffViewId = getResources().getIdentifier(diffStringIdNames.get(finalIdx), "id", getPackageName());
                        LinearLayout diffView = findViewById(diffViewId);
                        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        int aboveViewId = modeFrontLayouts.get(finalIdx).getId();
                        params.addRule(RelativeLayout.BELOW, aboveViewId);
                        diffView.setLayoutParams(params);

                        diffInflateFlags.set(finalIdx, true);
                    }
                    else {
                        int diffLayoutId = getResources().getIdentifier(diffStringIdNames.get(finalIdx),
                                "id", getPackageName());
                        LinearLayout diffLayout = findViewById(diffLayoutId);
                        selectedMode.removeView(diffLayout);
                        diffInflateFlags.set(finalIdx, false);
                    }

                }
            };
            RelativeLayout modeLayout = modeLayouts.get(idx);
            modeLayout.setOnClickListener(modeClickListener);
        }
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