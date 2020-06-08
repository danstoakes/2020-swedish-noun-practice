package com.example.swedishnounpractice.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.QuestionActivity;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.utility.ScrollingLayoutManager;
import com.example.swedishnounpractice.utility.ScrollingRecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>
{
    private Context context;

    private ScrollingRecyclerView recyclerView;

    private ScrollingLayoutManager manager;

    private List<Question> questions;

    public int holderPosition;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        this.recyclerView = (ScrollingRecyclerView) recyclerView;
        manager = (ScrollingLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public int getItemCount()
    {
        return questions.size();
    }

    public void setAdapterPosition (int holderPosition)
    {
        this.holderPosition = holderPosition;
    }

    public int getAdapterPosition ()
    {
        return holderPosition;
    }

    public void nextQuestion (Question question)
    {
        manager.setHorizontalScrollEnabled(true);
        manager.setHorizontalScrollPosition (getAdapterPosition());

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int index = questions.indexOf(question);

        recyclerView.setQuestion (questions.get(index));
        recyclerView.smoothScrollBy(metrics.widthPixels, 0);
    }

    class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Question question;

        private ProgressBar bar;

        private TextView header;
        private TextView word;

        private ImageButton play;

        private EditText input;

        private Button button;

        public QuestionHolder(@NonNull final View itemView)
        {
            super(itemView);

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
            switch (v.getId())
            {
                case R.id.imagePlay :
                    ((QuestionActivity) context).playSound(question, null);
                    break;
                case R.id.buttonSubmit :
                    Log.i("HELP", "OH DEAR");
                    ((QuestionActivity) context).addToManager(
                            question, input.getText().toString(), bar.getProgress());
                    break;
            }
        }

        private void setAttributes (final Question question)
        {
            this.question = question;

            bar.setProgress((int) ((float) getLayoutPosition () / getItemCount () * 100));

            String headerText = "Skriv svaret på engelska";

            if (question.isToSwedish())
                headerText = "Write the answer in Swedish";

            header.setText(headerText);
            word.setText(question.getQuestion());

            play.setOnClickListener(this);

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
                    button.setEnabled(!s.toString().equals(""));
                }
            });

            button.setOnClickListener(this);

            setAdapterPosition(getAdapterPosition());

            if (getAdapterPosition() == 0 && manager.getHorizontalScrollPosition() == 0)
                ((QuestionActivity) context).playSound(question, play);
        }
    }
}