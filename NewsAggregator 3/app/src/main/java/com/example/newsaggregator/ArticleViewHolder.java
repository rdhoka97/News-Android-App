package com.example.newsaggregator;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView heading;
    TextView publishedAt;
    TextView author;
    ImageView imageView;
    TextView content;
    TextView pageNum;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        heading = itemView.findViewById(R.id.heading);
        publishedAt = itemView.findViewById(R.id.publishedAt);
        author = itemView.findViewById(R.id.author);
        imageView = itemView.findViewById(R.id.imageView);
        content = itemView.findViewById(R.id.content);
        pageNum = itemView.findViewById(R.id.pageNum);
        content.setMovementMethod(new ScrollingMovementMethod());
    }
}
