/* Finalised on 08/06/2020 */

package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.swedishnounpractice.adapter.QuestionAdapter;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.object.Noun;
import com.example.swedishnounpractice.object.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionManager implements Parcelable
{
    private static final double CORRECT_WEIGHT = -0.05;
    private static final double INCORRECT_WEIGHT = 0.05;

    private List<Question> questions;
    private List<Question> incorrect;

    private int pointer;

    private int moduleID;

    private DatabaseHelper helper;

    private QuestionAdapter.OnAdapterInteractionListener listener;

    public QuestionManager (Context context, int moduleID)
    {
        if (context instanceof QuestionAdapter.OnAdapterInteractionListener)
            listener = (QuestionAdapter.OnAdapterInteractionListener) context;

        helper = new DatabaseHelper (context);

        questions = new ArrayList<> ();
        incorrect = new ArrayList<> ();

        pointer = 0;

        this.moduleID = moduleID;
    }

    public void initialise ()
    {
        Noun placeholder = new Noun (
                0, moduleID, null, null, null, null);

        List<DatabaseObject> nounObjects = helper.getList (placeholder);

        if (nounObjects.size () == 0)
        {
            listener.onSetupError ();
        } else
        {
            for (int i = 0; i < nounObjects.size (); i++)
            {
                Noun noun = ((Noun) nounObjects.get (i));

                questions.add (new Question (noun, noun.getEnglish (), noun.getSwedish (), true));
                questions.add (new Question (noun, noun.getSwedish (), noun.getEnglish (), false));
            }
            Collections.shuffle (questions);
        }
    }

    public void finalise ()
    {
        for (Question question : getQuestions ())
            helper.update (question.getNoun ());

        Module module = new Module (getCurrentQuestion().getNoun().getModuleID());

        double averageWeight = helper.getModuleWeight (module);

        String difficulty = "Easy";
        if (averageWeight > 0 && averageWeight < 0.2)
        {
            difficulty = "Normal";
        } else if (averageWeight > 0.2)
        {
            difficulty = "Hard";
        }

        module.setDifficulty (difficulty);

        helper.update (module);
    }

    public void setCorrectWeight ()
    {
        getCurrentQuestion().getNoun().setWeight (CORRECT_WEIGHT);
    }

    public void setIncorrectWeight ()
    {
        getCurrentQuestion().getNoun().setWeight (INCORRECT_WEIGHT);
        setIncorrect (getCurrentQuestion ());
    }

    public void setAdditionalWeight (double weight)
    {
        getCurrentQuestion().getNoun().setWeight (weight);
    }

    public void setIncorrect (Question question)
    {
        incorrect.add (question);
    }

    public int getQuestionsSize ()
    {
        return questions.size ();
    }

    public int getIncorrectSize ()
    {
        return incorrect.size ();
    }

    public List<Question> getQuestions ()
    {
        return questions;
    }

    public int getPointerLocation ()
    {
        return pointer;
    }

    public Question getCurrentQuestion ()
    {
        return questions.get (pointer);
    }

    public void loadNextQuestion ()
    {
        pointer++;
    }

    protected QuestionManager (Parcel in)
    {
        if (in.readByte() == 0x01)
        {
            questions = new ArrayList<> ();
            in.readList (questions, Question.class.getClassLoader ());
        } else
        {
            questions = null;
        }

        if (in.readByte() == 0x01)
        {
            incorrect = new ArrayList<> ();
            in.readList(incorrect, Question.class.getClassLoader ());
        } else
        {
            incorrect = null;
        }
        pointer = in.readInt ();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if (questions == null)
        {
            dest.writeByte ((byte) (0x00));
        } else
        {
            dest.writeByte ((byte) (0x01));
            dest.writeList (questions);
        }

        if (incorrect == null)
        {
            dest.writeByte ((byte) (0x00));
        } else
        {
            dest.writeByte ((byte) (0x01));
            dest.writeList (incorrect);
        }
        dest.writeInt (pointer);
    }

    public static final Parcelable.Creator<QuestionManager> CREATOR = new Parcelable.Creator<QuestionManager> ()
    {
        @Override
        public QuestionManager createFromParcel (Parcel in)
        {
            return new QuestionManager(in);
        }

        @Override
        public QuestionManager[] newArray (int size)
        {
            return new QuestionManager[size];
        }
    };
}