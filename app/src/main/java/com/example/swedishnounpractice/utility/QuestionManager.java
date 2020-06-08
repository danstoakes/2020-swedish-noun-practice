package com.example.swedishnounpractice.utility;

import com.example.swedishnounpractice.object.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager
{
    private List<Question> questions;
    private List<Question> incorrect;

    private int pointer;

    public QuestionManager (List<Question> questions)
    {
        this.questions = questions;
        incorrect = new ArrayList<>();

        pointer = 0;
    }

    public Question getCurrentQuestion ()
    {
        return questions.get(pointer);
    }

    public Question getNextQuestion ()
    {
        return questions.get(++pointer);
    }

    public void addIncorrect (Question question)
    {
        incorrect.add(question);
    }

    public List<Question> getQuestions ()
    {
        return questions;
    }

    public int getQuestionSize ()
    {
        return questions.size();
    }

    public int getCorrect ()
    {
        return questions.size() - incorrect.size();
    }
}