package com.example.swedishnounpractice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.QuestionAdapter;
import com.example.swedishnounpractice.dialog.CloseDialog;
import com.example.swedishnounpractice.dialog.DialogOptionSelectedListener;
import com.example.swedishnounpractice.dialog.FeedbackDialog;
import com.example.swedishnounpractice.dialog.SnackbarOptionSelectedListener;
import com.example.swedishnounpractice.helper.DrawableHelper;
import com.example.swedishnounpractice.layout.ScrollCompletedListener;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.dialog.ErrorDialog;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.object.Noun;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.dialog.SnackbarFactory;
import com.example.swedishnounpractice.utility.DatabaseHelper;
import com.example.swedishnounpractice.helper.FlagHelper;
import com.example.swedishnounpractice.helper.PreferenceHelper;
import com.example.swedishnounpractice.utility.QuestionManager;
import com.example.swedishnounpractice.layout.ScrollingLayoutManager;
import com.example.swedishnounpractice.layout.ScrollingRecyclerView;
import com.example.swedishnounpractice.utility.SoundPlayer;
import com.example.swedishnounpractice.helper.VibrationHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity
{
    private QuestionManager manager;

    private SoundPlayer player;

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
            ErrorDialog dialog = new ErrorDialog (QuestionActivity.this);
            dialog.show ();
        } else
        {
            player = new SoundPlayer(this);

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

    private List<Question> setQuestions (int moduleID)
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
        final Question question = manager.getCurrentQuestion();

        String correctAnswer = question.getAnswer().toLowerCase();
        answer = answer.toLowerCase();

        int characterDifference = Math.abs(answer.length() - correctAnswer.length());

        final SnackbarFactory factory = new SnackbarFactory(this);

        String output = getString(R.string.app_answer_incorrect);
        if (answer.contains(correctAnswer) && characterDifference <= 1)
        {
            output = getString(R.string.app_answer_correct);

            if (characterDifference == 1)
                output = getString(R.string.app_answer_correct_spelling, question.getAnswer());

            factory.make(output, Snackbar.LENGTH_SHORT, R.color.correctAnswer, false);

            if (PreferenceHelper.getSoundPreference(this, R.string.correct_sounds_key, true))
                requestSound(false, "correct");

            VibrationHelper.vibrate(this, true, "correct");

            manager.setCorrectWeight();
        } else
        {
            factory.make(output, Snackbar.LENGTH_SHORT, R.color.incorrectAnswer, true);
            factory.setHeader(question.getAnswer ());

            if (PreferenceHelper.getSoundPreference(this, R.string.error_sounds_key, true))
                requestSound(false, "incorrect");

            VibrationHelper.vibrate(this, true, "incorrect");

            manager.setIncorrect(question);
            manager.setIncorrectWeight(0.05);
        }
        Snackbar snackbar = factory.getSnackbar();
        snackbar.addCallback(new Snackbar.Callback()
        {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event)
            {
                super.onDismissed(transientBottomBar, event);

                handleQuestion(factory.getHalted());
            }
        });

        final QuestionActivity currentActivity = this;

        factory.setSnackbarOptionSelectedListener(new SnackbarOptionSelectedListener()
        {
            @Override
            public void onSnackbarOptionSelected(String header)
            {
                FeedbackDialog dialog = new FeedbackDialog(currentActivity, factory);
                dialog.setHeader(header);
                dialog.setOnDialogOptionSelectedListener(new DialogOptionSelectedListener()
                {
                    @Override
                    public void onDialogOptionSelected()
                    {
                        factory.setHalted(false);

                        handleQuestion(factory.getHalted());
                    }
                });
                dialog.show();
            }
        });
        factory.show();
    }

    private void handleQuestion (boolean dialogShowing)
    {
        if (!dialogShowing)
        {
            if (manager.getPointerLocation() == manager.getQuestionsSize() - 1)
            {
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

                for (Question answered : manager.getQuestions())
                    helper.update(answered.getNoun());

                Module module = new Module(manager.getCurrentQuestion().getNoun().getModuleID());

                double averageWeight = helper.getModuleWeight(module);

                String difficulty = "Easy";
                if (averageWeight > 0 && averageWeight < 0.2)
                {
                    difficulty = "Normal";
                } else if (averageWeight > 0.2)
                {
                    difficulty = "Hard";
                }

                module.setDifficulty(difficulty);

                helper.update(module);

                Intent resultIntent = new Intent(QuestionActivity.this, ResultActivity.class)
                        .putExtra("score", (manager.getQuestionsSize() - manager.getIncorrectSize()))
                        .putExtra("total", manager.getQuestionsSize());

                startActivity(resultIntent);
            } else
            {
                loadNextQuestion();
            }
        }
    }

    public void loadNextQuestion ()
    {
        manager.loadNextQuestion ();

        ScrollingRecyclerView recyclerView = findViewById(R.id.recyclerView);

        ScrollingLayoutManager layoutManager = (ScrollingLayoutManager) recyclerView.getLayoutManager();

        if (layoutManager != null)
            layoutManager.setHorizontalScrollEnabled(true);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        recyclerView.setScrollCompletedListener(new ScrollCompletedListener()
        {
            @Override
            public void onScrollCompleted ()
            {
                if (PreferenceHelper.getSoundPreference(getApplicationContext(), R.string.word_sounds_key, true))
                    requestSound(true, null);
            }
        });
        recyclerView.smoothScrollBy(metrics.widthPixels, 0);
    }

    public int getQuestionNumber ()
    {
        return manager.getPointerLocation() + 1;
    }

    public void setQuestionWeight (double weight)
    {
        manager.setIncorrectWeight(weight);
    }

    public void requestSound (boolean wordSound, String soundName)
    {
        Question question = manager.getCurrentQuestion();

        int soundID = DrawableHelper.getResource(question.getNoun().getReferenceID(), false);

        if (!wordSound)
            soundID = DrawableHelper.getResource(soundName, false);

        player.playSound(soundID);
    }
}