/* Finalised on 05/06/2020 */

package com.example.swedishnounpractice.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable
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

    protected Question (Parcel in)
    {
        noun = (Noun) in.readValue (Noun.class.getClassLoader ());
        question = in.readString ();
        answer = in.readString ();
        toSwedish = in.readByte () != 0x00;
    }

    @Override
    public int describeContents ()
    {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags)
    {
        dest.writeValue (noun);
        dest.writeString (question);
        dest.writeString (answer);
        dest.writeByte ((byte) (toSwedish ? 0x01 : 0x00));
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question> ()
    {
        @Override
        public Question createFromParcel (Parcel in)
        {
            return new Question(in);
        }

        @Override
        public Question [] newArray (int size)
        {
            return new Question [size];
        }
    };
}