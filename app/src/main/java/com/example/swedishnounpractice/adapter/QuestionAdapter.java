/* Finalised on 17/06/2020 */

package com.example.swedishnounpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.layout.ResponsiveEditText;
import com.example.swedishnounpractice.object.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>
{
    private OnAdapterInteractionListener listener;

    private List<Question> questions;

    public QuestionAdapter (List<Question> questions)
    {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from (
                parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull QuestionAdapter.QuestionHolder holder, int position)
    {
        Question question = questions.get (position);
        holder.setAttributes (question);
    }

    @Override
    public int getItemCount ()
    {
        return questions.size();
    }

    public void setAdapterInteractionListener (OnAdapterInteractionListener listener)
    {
        this.listener = listener;
    }

    public interface OnAdapterInteractionListener extends ModuleAdapter.OnAdapterEventListener
    {
        @Override
        void onAdapterItemClick(View view, int moduleID, @Nullable String text);

        void onSetupError ();

        void onAdapterLoaded (int position);

        int getQuestionNumber ();

        boolean isViewVisible (View view);
    }

    class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ResponsiveEditText.ValidEntryListener
    {
        private final ProgressBar bar;
        private final TextView header;
        private final TextView word;
        private final ImageButton play;
        private final ResponsiveEditText input;
        private final ImageButton hint;
        private final Button button;

        public QuestionHolder(@NonNull final View itemView)
        {
            super (itemView);

            bar = itemView.findViewById(R.id.barProgress);
            header = itemView.findViewById(R.id.textHeader);
            word = itemView.findViewById(R.id.textWord);
            play = itemView.findViewById(R.id.imagePlay);
            input = itemView.findViewById(R.id.textInput);
            hint = itemView.findViewById(R.id.imageHint);
            button = itemView.findViewById(R.id.buttonSubmit);
        }

        @Override
        public void onClick (View v)
        {
            if (v.getId () == button.getId ())
            {
                input.setEnabled (false);
                button.setEnabled (false);
            }

            if (input.getText () != null)
                listener.onAdapterItemClick (v, 0, input.getText().toString());
        }

        @Override
        public void onValidEntryAdded (boolean enabled)
        {
            button.setEnabled (enabled);
        }

        public String getHeader (Question question)
        {
            String headerText = "Skriv svaret på engelska";
            if (question.isToSwedish ())
                headerText = "Write the answer in Swedish";

            return headerText;
        }

        private void setAttributes (final Question question)
        {
            bar.setProgress((int) ((float) listener.getQuestionNumber() / getItemCount () * 100));

            header.setText(getHeader(question));
            word.setText(question.getQuestion());

            play.setOnClickListener(this);

            input.setProperties ();
            input.setValidEntryListener(this);

            if (listener.isViewVisible(hint))
            {
                hint.setVisibility (View.VISIBLE);
                hint.setOnClickListener (this);
            }

            button.setEnabled (false);
            button.setOnClickListener (this);

            listener.onAdapterLoaded (listener.getQuestionNumber ());
        }
    }
}