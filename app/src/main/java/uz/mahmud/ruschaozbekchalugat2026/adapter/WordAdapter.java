package uz.mahmud.ruschaozbekchalugat2026.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import uz.mahmud.ruschaozbekchalugat2026.R;

public class WordAdapter
        extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private final ArrayList<HashMap<String, String>> wordList;

    private final OnWordClickListener listener;

    public interface OnWordClickListener {
        void onWordClick(
                HashMap<String, String> word
        );
    }

    public WordAdapter(
            ArrayList<HashMap<String, String>> wordList,
            OnWordClickListener listener
    ) {
        this.wordList = wordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.word_item,
                                parent,
                                false
                        );

        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull WordViewHolder holder,
            int position
    ) {

        HashMap<String, String> word =
                wordList.get(position);

        holder.txtRussian.setText(
                word.get("ru")
        );

        holder.txtUzbek.setText(
                word.get("uz")
        );

        holder.itemView.setOnClickListener(v ->
                listener.onWordClick(word)
        );
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class WordViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtRussian;
        TextView txtUzbek;

        public WordViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtRussian =
                    itemView.findViewById(
                            R.id.txtRussian
                    );

            txtUzbek =
                    itemView.findViewById(
                            R.id.txtUzbek
                    );
        }
    }
}