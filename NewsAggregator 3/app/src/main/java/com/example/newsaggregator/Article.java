package com.example.newsaggregator;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * source: {
 * id: "abc-news",
 * name: "ABC News"
 * },
 * author: "ALAN FRAM Associated Press",
 * title: "It's not just Manchin: Dems' $2T bill faces Senate gauntlet",
 * description: "",
 * url: "https://abcnews.go.com/Politics/wireStory/manchin-dems-2t-bill-faces-senate-gauntlet-81366189",
 * urlToImage: "https://s.abcnews.com/images/Politics/WireAP_d362d87f79d049baac266dc2b2aa5bbb_16x9_992.jpg",
 * publishedAt: "2021-11-24T05:15:12Z",
 * content: "WASHINGTON -- It took half a year but Democrats have driven President Joe Bidens $2 trillion package of social and climate initiatives through the House. It gets no easier in the Senate, where painfu… [+6443 chars]"
 */

class ArticleSource implements Serializable{
    String id;
    String name;

    public ArticleSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class Article implements Serializable {
    private ArticleSource source;
    private String author;
    private String title;
    private String url;
    private String description;
    private String urlToImage;
    private String publishedAt;
    private String content;
    private transient Drawable drawable;

    public Article() {
    }

    public Article(ArticleSource source, String author, String title, String url, String description, String urlToImage, String publishedAt, String content, Drawable draw) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.url = url;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
        this.drawable = draw;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArticleSource getSource() {
        return source;
    }

    public void setSource(ArticleSource source) {
        this.source = source;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                ", drawable=" + drawable +
                '}';
    }
}
