package com.example.swedishnounpractice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.QuestionAdapter;
import com.example.swedishnounpractice.dialog.CloseDialog;
import com.example.swedishnounpractice.dialog.FeedbackDialog;
import com.example.swedishnounpractice.dialog.HintDialog;
import com.example.swedishnounpractice.helper.CheckAnswerHelper;
import com.example.swedishnounpractice.helper.ConstantHelper;
import com.example.swedishnounpractice.helper.DrawableHelper;
import com.example.swedishnounpractice.dialog.ErrorDialog;
import com.example.swedishnounpractice.listener.OnAdapterInteractionListener;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.dialog.SnackbarFactory;
import com.example.swedishnounpractice.helper.FlagHelper;
import com.example.swedishnounpractice.helper.PreferenceHelper;
import com.example.swedishnounpractice.utility.QuestionManager;
import com.example.swedishnounpractice.layout.ScrollingLayoutManager;
import com.example.swedishnounpractice.layout.ScrollingRecyclerView;
import com.example.swedishnounpractice.utility.SoundPlayer;
import com.example.swedishnounpractice.helper.VibrationHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class QuestionActivity extends AppCompatActivity implements OnAdapterInteractionListener, SnackbarFactory.SnackbarOptionSelectedListener
{
    private QuestionManager manager;

    private SnackbarFactory factory;

    private SoundPlayer player;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoBar);
        setContentView(R.layout.activity_question);

        if (savedInstanceState != null)
            manager = savedInstanceState.getParcelable("questionManager");

        FlagHelper.setFlags(this);

        initialise();
    }

    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable ("questionManager", manager);
    }

    @Override
    protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed ()
    {
        CloseDialog dialog = new CloseDialog (QuestionActivity.this);
        dialog.show ();
    }

    private void initialise ()
    {
        if (getIntent().getIntExtra ("moduleID", 0) == 0)
        {
            onSetupError ();
        } else
        {
            player = new SoundPlayer (this);

            setupView ();
        }
    }

    private void setupView ()
    {
        ScrollingRecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new ScrollingLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false, false));
        recyclerView.setScrollCompletedListener(new ScrollingRecyclerView.ScrollCompletedListener()
        {
            @Override
            public void onScrollCompleted ()
            {
                FlagHelper.setRotationUnlocked (QuestionActivity.this);

                playSound (manager.getCurrentQuestion().getNoun().getReferenceID(), R.string.word_sounds_key);
            }
        });
        recyclerView.setAdapter (setupAdapter ());
    }

    private QuestionAdapter setupAdapter ()
    {
        if (manager == null)
            return new QuestionAdapter (this, setQuestions(getIntent().getIntExtra("moduleID", 0)));

        return new QuestionAdapter (this, manager.getQuestions());
    }

    private List<Question> setQuestions (int moduleID)
    {
        manager = new QuestionManager (this, moduleID);
        manager.initialise ();

        return manager.getQuestions ();
    }

    @Override
    public void onAdapterLoaded ()
    {
        boolean wordSounds = PreferenceHelper.getSoundPreference(this, R.string.word_sounds_key, true);

        if (getQuestionNumber () == 1 && wordSounds)
            playSound (manager.getCurrentQuestion().getNoun().getReferenceID(), R.string.word_sounds_key);
    }

    public void playSound (String soundID, int preferenceKey)
    {
        if (PreferenceHelper.getSoundPreference(this, preferenceKey, ConstantHelper.SOUND_DEFAULT))
        {
            try
            {
                player.playSound (DrawableHelper.getResource(soundID, false));
            } catch (Resources.NotFoundException e)
            {
                Toast.makeText(this, "Error playing sound", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAdapterItemClick (View view, int moduleID, @Nullable String text)
    {
        switch (view.getId ())
        {
            case R.id.imagePlay :
                playSound (manager.getCurrentQuestion().getNoun().getReferenceID(), R.string.word_sounds_key);
                break;
            case R.id.imageHint:
                HintDialog dialog = new HintDialog (this);
                dialog.setAttributes (manager.getCurrentQuestion());
                dialog.show();
                break;
            case R.id.buttonSubmit :
                handleUserInput (text);
                break;
        }
    }

    private void handleUserInput (String answer)
    {
        FlagHelper.setRotationLocked (this);

        int code = CheckAnswerHelper.evaluateAnswer(answer, manager.getCurrentQuestion().getAnswer());

        if (code == ConstantHelper.CORRECT || code == ConstantHelper.CORRECT_TYPO)
        {
            manager.setCorrectWeight ();
            playSound ("correct", R.string.correct_sounds_key);
        } else
        {
            manager.setIncorrectWeight ();
            playSound ("incorrect", R.string.error_sounds_key);
        }
        VibrationHelper.vibrate(this, true, code);

        handleOutput (code);
    }

    private void handleOutput (int code)
    {
        factory = new SnackbarFactory (this);
        factory.setSnackbarOptionSelectedListener(this);
        factory.make(code, manager.getCurrentQuestion().getAnswer()).addCallback(new Snackbar.Callback()
        {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event)
            {
                super.onDismissed(transientBottomBar, event);

                handleNextAction (factory.isDialogShowing ());
            }
        });
        factory.show ();
    }

    @Override
    public void onSnackbarOptionSelected (String header)
    {
        FeedbackDialog dialog = new FeedbackDialog (QuestionActivity.this);
        dialog.setHeader(header);
        dialog.setOnDialogOptionSelectedListener(new FeedbackDialog.DialogOptionSelectedListener ()
        {
            @Override
            public void onDialogOptionSelected()
            {
                factory.setDialogShowing (false);

                handleNextAction (factory.isDialogShowing());
            }
        });
        dialog.show();
    }

    private void handleNextAction (boolean dialogShowing)
    {
        if (!dialogShowing)
        {
            if (manager.getPointerLocation() == manager.getQuestionsSize() - 1)
            {
                manager.finalise ();

                Intent resultIntent = new Intent(QuestionActivity.this, ResultActivity.class)
                        .putExtra("score", (manager.getQuestionsSize() - manager.getIncorrectSize()))
                        .putExtra("total", manager.getQuestionsSize());

                startActivity(resultIntent);
            } else
            {
                moveToNextQuestion();
            }
        }
    }

    private void moveToNextQuestion ()
    {
        manager.loadNextQuestion ();

        ScrollingRecyclerView recyclerView = findViewById(R.id.recyclerView);

        ScrollingLayoutManager layoutManager = (ScrollingLayoutManager) recyclerView.getLayoutManager();

        if (layoutManager != null)
            layoutManager.setHorizontalScrollEnabled(true);

        recyclerView.smoothScrollBy(getResources().getDisplayMetrics().widthPixels, 0);
    }

    @Override
    public void onSetupError()
    {
        ErrorDialog dialog = new ErrorDialog (QuestionActivity.this);
        dialog.show ();
    }

    @Override
    public int getQuestionNumber()
    {
        return manager.getPointerLocation() + 1;
    }

    @Override
    public boolean isViewVisible (View view)
    {
        if (view.getId() == R.id.imageHint)
            return PreferenceHelper.getStandardPreference(this, R.string.hint_key, false);

        return true;
    }
}