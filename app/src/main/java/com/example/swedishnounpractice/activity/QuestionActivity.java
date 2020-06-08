package com.example.swedishnounpractice.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.ImageButton;

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

    private boolean halted;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initialise();
    }

    @Override
    public void onBackPressed()
    {
        CloseDialog dialog = new CloseDialog(QuestionActivity.this);
        dialog.show();
    }

    public void initialise ()
    {
        int moduleID = 0;
        if (getIntent().getIntExtra("moduleID", 0) != 0)
            moduleID = getIntent().getIntExtra("moduleID", 0);

        ScrollingRecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new ScrollingLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false, false));

        QuestionAdapter adapter = new QuestionAdapter(this, setQuestions(moduleID));
        recyclerView.setAdapter(adapter);
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

    public void addToManager (final Question question, String answer, final int progress)
    {
        halted = false;

        String actual = question.getAnswer().toLowerCase();
        answer = answer.toLowerCase();

        int difference = Math.abs(answer.length() - actual.length());

        String output = getString(R.string.app_answer_incorrect);
        if (answer.contains(actual) && difference <= 1)
        {
            output = getString(R.string.app_answer_correct);

            if (difference == 1)
                output = getString(R.string.app_answer_correct_spelling, question.getAnswer());

            SnackbarFactory factory = new SnackbarFactory(QuestionActivity.this);
            factory.make(output, Snackbar.LENGTH_SHORT, R.color.correctAnswer, false);
            factory.show();
        } else
        {
            SnackbarFactory factory = new SnackbarFactory(QuestionActivity.this);
            factory.make(output, Snackbar.LENGTH_SHORT, R.color.incorrectAnswer, true);
            factory.setHeader(question.getAnswer());
            factory.show();
        }
    }

    public QuestionManager getQuestionManager ()
    {
        return manager;
    }

    public void loadNextQuestion (Question question)
    {
        ScrollingRecyclerView view = findViewById(R.id.recyclerView);
        QuestionAdapter adapter = (QuestionAdapter) view.getAdapter();

        adapter.nextQuestion(manager.getNextQuestion());
    }

    public void playSound (Question question, ImageButton button)
    {
        SoundPlayer player = new SoundPlayer(QuestionActivity.this, button); // needs to be global
        try
        {
            String reference = question.getNoun().getReferenceID();
            int id = R.raw.class.getField(reference).getInt(null);

            if (button != null)
                button.setEnabled(false);

            player.playSound(id);
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
}