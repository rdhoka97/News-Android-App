package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private final MainActivity mainActivity;
    private final ArrayList<Article> articlesList;

    public ArticleAdapter(MainActivity mainActivity, ArrayList<Article> articlesList) {
        this.mainActivity = mainActivity;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.article_entry, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articlesList.get(position);

        Log.d("", "onBindViewHolder: " + article.toString());

        if(!article.getTitle().trim().equalsIgnoreCase("null")
                && !article.getTitle().isEmpty()) {
            holder.heading.setText(article.getTitle());
            holder.heading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(article.getUrl());
                }
            });
        } else {
            makeItInvisible(holder.heading);
        }

        SimpleDateFormat givenFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        SimpleDateFormat reqFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm", Locale.US);
        String goodPublishedDate="";
        try {
            goodPublishedDate = reqFormat.format(givenFormat.parse(article.getPublishedAt()));
            holder.publishedAt.setText(goodPublishedDate);
        } catch (ParseException e) {
            givenFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            try {
                goodPublishedDate = reqFormat.format(givenFormat.parse(article.getPublishedAt()));
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            if(!goodPublishedDate.trim().equalsIgnoreCase("null")
                    && !goodPublishedDate.isEmpty()) {
                holder.publishedAt.setText(goodPublishedDate);
            }

            e.printStackTrace();
        }

        if(!article.getAuthor().trim().equalsIgnoreCase("null")
                && !article.getAuthor().isEmpty()) {
            holder.author.setText(article.getAuthor());
        } else {
            makeItInvisible(holder.author);
        }


        if(!article.getContent().trim().equalsIgnoreCase("null")
                && !article.getContent().isEmpty()) {
            holder.content.setText(article.getDescription());
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(article.getUrl());
                }
            });
        } else {
            makeItInvisible(holder.content);
        }

        holder.pageNum.setText(String.format(
                Locale.getDefault(),"%d of %d", (position+1), articlesList.size()));

        if(!article.getUrlToImage().trim().equalsIgnoreCase("null")
                && !article.getUrlToImage().isEmpty()) {
            Picasso.get().load(article.getUrlToImage())
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.loading)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(article.getUrl());
                }
            });
        } else {
            makeItInvisible(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    private void click(String uri) {
        if (!uri.isEmpty() && !uri.equalsIgnoreCase("null")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            mainActivity.startActivity(intent);
        }
    }

    private void makeItInvisible(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }

    private void makeItInvisible(TextView textView) {
        textView.setVisibility(View.GONE);
    }
}
