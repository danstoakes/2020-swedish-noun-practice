package com.example.swedishnounpractice.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.QuestionActivity;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.utility.PermissionHelper;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>
{
    private Context context;

    private List<Question> questions;

    private PermissionHelper helper;

    public QuestionAdapter (Context context, List<Question> questions)
    {
        this.context = context;
        this.questions = questions;
        this.helper = new PermissionHelper(context);
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

        private EditText input;

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

            input.setText("");
            input.requestFocus();
            input.setImeOptions(EditorInfo.IME_ACTION_DONE);
            input.setRawInputType(InputType.TYPE_CLASS_TEXT);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            input.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s)
                {
                    button.setEnabled(!s.toString().trim().equals(""));
                }
            });

            button.setEnabled(false);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(this);

            if (activity.getQuestionNumber() == 1 && helper.getWordSoundsOn())
                activity.requestSound(true, null);
        }
    }
}