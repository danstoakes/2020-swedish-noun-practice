package com.example.swedishnounpractice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.QuestionAdapter;
import com.example.swedishnounpractice.object.CloseDialog;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.ErrorDialog;
import com.example.swedishnounpractice.object.Noun;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.object.SnackbarFactory;
import com.example.swedishnounpractice.utility.DatabaseHelper;
import com.example.swedishnounpractice.utility.FlagHelper;
import com.example.swedishnounpractice.utility.PermissionHelper;
import com.example.swedishnounpractice.utility.QuestionManager;
import com.example.swedishnounpractice.utility.ScrollingLayoutManager;
import com.example.swedishnounpractice.utility.ScrollingRecyclerView;
import com.example.swedishnounpractice.utility.SoundPlayer;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity
{
    private QuestionManager manager;

    private SoundPlayer player;

    private PermissionHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoBar);
        setContentView(R.layout.activity_question);

        if (savedInstanceState != null)
            manager = savedInstanceState.getParcelable("questionManager");

        FlagHelper.setFlags(this);

        initialise();
    }

    public void initialise ()
    {
        if (getIntent().getIntExtra("moduleID", 0) == 0)
        {
            ErrorDialog dialog = new ErrorDialog(QuestionActivity.this);
            dialog.show ();
        } else
        {
            player = new SoundPlayer(this);

            helper = new PermissionHelper(this);

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new ScrollingLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false, false));

            QuestionAdapter adapter;
            if (manager == null)
            {
                int moduleID = getIntent().getIntExtra("moduleID", 0);

                adapter = new QuestionAdapter(this, setQuestions (moduleID));
            } else
            {
                adapter = new QuestionAdapter(this, manager.getQuestions());
            }
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed()
    {
        CloseDialog dialog = new CloseDialog(QuestionActivity.this);
        dialog.show ();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable("questionManager", manager);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public List<Question> setQuestions (int moduleID)
    {
        List<Question> questions = new ArrayList<>();

        DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());

        Noun placeholder = new Noun (
                0, moduleID, null, null, null, null);

        List<DatabaseObject> nounObjects = helper.getList(placeholder);

        if (nounObjects.size() == 0)
        {
            ErrorDialog dialog = new ErrorDialog(QuestionActivity.this);
            dialog.show ();
        } else
        {
            for (int i = 0; i < nounObjects.size(); i++)
            {
                Noun noun = ((Noun) nounObjects.get(i));

                questions.add(new Question(noun, noun.getEnglish(), noun.getSwedish(), true));
                questions.add(new Question(noun, noun.getSwedish(), noun.getEnglish(), false));
            }
            Collections.shuffle(questions);

            manager = new QuestionManager (questions);
        }
        return questions;
    }

    public void updateManager (String answer)
    {
        Question question = manager.getCurrentQuestion();

        String correctAnswer = question.getAnswer().toLowerCase();
        answer = answer.toLowerCase();

        int characterDifference = Math.abs(answer.length() - correctAnswer.length());

        SnackbarFactory factory = new SnackbarFactory(this);

        String output = getString(R.string.app_answer_incorrect);
        if (answer.contains(correctAnswer) && characterDifference <= 1)
        {
            output = getString(R.string.app_answer_correct);

            if (characterDifference == 1)
                output = getString(R.string.app_answer_correct_spelling, question.getAnswer());

            factory.make(output, Snackbar.LENGTH_SHORT, R.color.correctAnswer, false);

            if (helper.getCorrectSoundsOn())
                requestSound(false, "correct");

            requestVibrate(150);
        } else
        {
            factory.make(output, Snackbar.LENGTH_SHORT, R.color.incorrectAnswer, true);
            factory.setHeader(question.getAnswer ());

            if (helper.getErrorSoundsOn())
                requestSound(false, "incorrect");

            requestVibrate(250);
        }
        factory.show();
    }

    public void loadNextQuestion ()
    {
        manager.loadNextQuestion ();

        ScrollingRecyclerView recyclerView = findViewById(R.id.recyclerView);

        ScrollingLayoutManager layoutManager = (ScrollingLayoutManager) recyclerView.getLayoutManager();

        if (layoutManager != null)
            layoutManager.setHorizontalScrollEnabled(true);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        recyclerView.smoothScrollBy(metrics.widthPixels, 0);
    }

    public int getQuestionNumber ()
    {
        return manager.getPointerLocation() + 1;
    }

    public void requestSound (boolean wordSound, String soundName)
    {
        Question question = manager.getCurrentQuestion();

        if (!player.isPlaying())
        {
            try
            {
                int soundID = R.raw.class.getField(
                        question.getNoun().getReferenceID()).getInt(null);

                if (!wordSound)
                    soundID = R.raw.class.getField(soundName).getInt(null);

                player.playSound(soundID);
            } catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void requestVibrate (int length)
    {
        if (helper.getVibrationsOn())
        {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                vibrator.vibrate(
                        VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));
            } else
            {
                vibrator.vibrate(length);
            }
        }
    }
}