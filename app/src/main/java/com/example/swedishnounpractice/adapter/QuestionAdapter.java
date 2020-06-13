package com.example.swedishnounpractice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.QuestionActivity;
import com.example.swedishnounpractice.layout.ResponsiveEditText;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.helper.PreferenceHelper;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>
{
    private Context context;

    private List<Question> questions;

    public QuestionAdapter (Context context, List<Question> questions)
    {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionHolder holder, int position)
    {
        Question question = questions.get(position);
        holder.setAttributes(question);
    }

    @Override
    public int getItemCount()
    {
        return questions.size();
    }

    class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ProgressBar bar;
        private TextView header;
        private TextView word;
        private ImageButton play;
        private ResponsiveEditText input;
        private Button button;

        public QuestionHolder(@NonNull final View itemView)
        {
            super (itemView);

            bar = itemView.findViewById(R.id.barProgress);

            header = itemView.findViewById(R.id.textHeader);
            word = itemView.findViewById(R.id.textWord);

            play = itemView.findViewById(R.id.imagePlay);

            input = itemView.findViewById(R.id.textInput);

            button = itemView.findViewById(R.id.buttonSubmit);
        }

        @Override
        public void onClick(View v)
        {
            QuestionActivity activity = (QuestionActivity) context;

            switch (v.getId())
            {
                case R.id.imagePlay :
                    activity.requestSound(true, null);
                    break;
                case R.id.buttonSubmit :
                    input.setEnabled(false);
                    button.setVisibility(View.INVISIBLE);
                    activity.updateManager(input.getText().toString());
                    break;
            }
        }

        private void setAttributes (final Question question)
        {
            QuestionActivity activity = (QuestionActivity) context;

            bar.setProgress((int) ((float) activity.getQuestionNumber() / getItemCount () * 100));

            String headerText = "Skriv svaret på engelska";
            if (question.isToSwedish())
                headerText = "Write the answer in Swedish";

            header.setText(headerText);
            word.setText(question.getQuestion());

            play.setOnClickListener(this);

            input.setProperties();
            input.setValidEntryListener(new ResponsiveEditText.ValidEntryListener ()
            {
                @Override
                public void onValidEntryAdded(boolean enabled)
                {
                    button.setEnabled(enabled);
                }
            });

            button.setEnabled(false);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(this);

            if (activity.getQuestionNumber() == 1 &&
                    PreferenceHelper.getSoundPreference(context, R.string.word_sounds_key, true))
                activity.requestSound(true, null);
        }
    }
}