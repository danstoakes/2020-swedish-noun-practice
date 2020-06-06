package com.example.swedishnounpractice.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.object.Question;
import com.example.swedishnounpractice.utility.ScrollingLayoutManager;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>
{
    private Context context;

    private RecyclerView recyclerView;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount()
    {
        return questions.size();
    }

    class QuestionHolder extends RecyclerView.ViewHolder
    {
        private TextView header;
        private TextView word;

        private Button button;

        public QuestionHolder(@NonNull final View itemView)
        {
            super(itemView);

            header = itemView.findViewById(R.id.textHeader);
            word = itemView.findViewById(R.id.textWord);

            button = itemView.findViewById(R.id.buttonSubmit);
        }

        private void setAttributes (final Question question)
        {
            String headerText = "Skriv svaret på engelska";

            if (question.isToSwedish())
                headerText = "Write the answer in Swedish";

            header.setText(headerText);
            word.setText(question.getQuestion());

            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final ScrollingLayoutManager layoutManager = (ScrollingLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.setHorizontalScrollEnabled(true);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    final int width = displayMetrics.widthPixels;

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
                    {
                        private int last;

                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
                        {
                            super.onScrollStateChanged(recyclerView, newState);

                            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                            {
                                if ((width - last) == 0 || last == 0)
                                {
                                    last = 0;
                                    layoutManager.setHorizontalScrollEnabled(false);
                                } else
                                {
                                    recyclerView.smoothScrollBy(width - last, 0);
                                }
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
                        {
                            super.onScrolled(recyclerView, dx, dy);

                            last += dx;
                        }
                    });
                    recyclerView.smoothScrollBy(width, 0);
                }
            });
        }
    }
}