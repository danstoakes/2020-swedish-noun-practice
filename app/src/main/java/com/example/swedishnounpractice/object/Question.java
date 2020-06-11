package com.example.swedishnounpractice.object;

import android.os.Parcelable;

public class Question
{
    private Noun noun;

    private String question;
    private String answer;

    private boolean toSwedish;

    public Question (Noun noun, String question, String answer, boolean toSwedish)
    {
        this.noun = noun;
        this.question = question;
        this.answer = answer;
        this.toSwedish = toSwedish;
    }

    public Noun getNoun ()
    {
        return noun;
    }

    public String getQuestion ()
    {
        return question;
    }

    public String getAnswer ()
    {
        return answer;
    }

    public boolean isToSwedish ()
    {
        return toSwedish;
    }
}