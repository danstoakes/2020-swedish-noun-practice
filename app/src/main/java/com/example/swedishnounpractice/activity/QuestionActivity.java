package com.example.swedishnounpractice.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.QuestionAdapter;
import com.example.swedishnounpractice.object.CloseDialog;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Noun;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.object.SnackbarFactory;
import com.example.swedishnounpractice.utility.DatabaseHelper;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initialise();
    }

    public void initialise ()
    {
        if (getIntent().getIntExtra("moduleID", 0) == 0)
        {
            // throw error page. can make use of the old no connection error page
        } else
        {
            player = new SoundPlayer(this);

            int moduleID = getIntent().getIntExtra("moduleID", 0);

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new ScrollingLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false, false));

            QuestionAdapter adapter = new QuestionAdapter(this, setQuestions (moduleID));
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed()
    {
        CloseDialog dialog = new CloseDialog(QuestionActivity.this);
        dialog.show ();
    }

    public List<Question> setQuestions (int moduleID)
    {
        List<Question> questions = new ArrayList<>();

        DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());

        Noun placeholder = new Noun (
                0, moduleID, null, null, null, null);

        List<DatabaseObject> nounObjects = helper.getList(placeholder);
        for (int i = 0; i < nounObjects.size(); i++)
        {
            Noun noun = ((Noun) nounObjects.get(i));

            questions.add(new Question(noun, noun.getEnglish(), noun.getSwedish(), true));
            questions.add(new Question(noun, noun.getSwedish(), noun.getEnglish(), false));
        }
        Collections.shuffle(questions);

        manager = new QuestionManager (questions);

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
        } else
        {
            factory.make(output, Snackbar.LENGTH_SHORT, R.color.incorrectAnswer, true);
            factory.setHeader(question.getAnswer ());
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
        return manager.getPointerLocation();
    }

    public void requestSound (@Nullable Question question)
    {
        if (question == null)
            question = manager.getCurrentQuestion();

        if (!player.isPlaying())
        {
            try
            {
                int soundID = R.raw.class.getField(
                        question.getNoun().getReferenceID()).getInt(null);

                player.playSound(soundID);
            } catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
    }
}