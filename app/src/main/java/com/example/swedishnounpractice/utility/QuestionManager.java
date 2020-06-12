package com.example.swedishnounpractice.utility;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.swedishnounpractice.object.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager implements Parcelable
{
    /*
     * Needs refining at a later date.
     *
     * Consider re-ordering methods and changing access levels. Also change the parcelable
     * methods to be more in line with style.
     */

    private List<Question> questions;
    private List<Question> incorrect;

    private int pointer;

    public QuestionManager (List<Question> questions)
    {
        this.questions = questions;
        incorrect = new ArrayList<>();

        pointer = 0;
    }

    public int getPointerLocation ()
    {
        return pointer;
    }

    public void loadNextQuestion ()
    {
        pointer++;
    }

    public Question getCurrentQuestion ()
    {
        return questions.get(pointer);
    }

    public List<Question> getQuestions ()
    {
        return questions;
    }

    protected QuestionManager(Parcel in) {
        if (in.readByte() == 0x01) {
            questions = new ArrayList<>();
            in.readList(questions, Question.class.getClassLoader());
        } else {
            questions = null;
        }
        if (in.readByte() == 0x01) {
            incorrect = new ArrayList<>();
            in.readList(incorrect, Question.class.getClassLoader());
        } else {
            incorrect = null;
        }
        pointer = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (questions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questions);
        }
        if (incorrect == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(incorrect);
        }
        dest.writeInt(pointer);
    }

    public static final Parcelable.Creator<QuestionManager> CREATOR = new Parcelable.Creator<QuestionManager>() {
        @Override
        public QuestionManager createFromParcel(Parcel in) {
            return new QuestionManager(in);
        }

        @Override
        public QuestionManager[] newArray(int size) {
            return new QuestionManager[size];
        }
    };
}