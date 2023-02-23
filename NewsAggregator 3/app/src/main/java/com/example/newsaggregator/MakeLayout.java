package com.example.newsaggregator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeLayout implements Serializable {
    private List<Article> articlesList;
    private List<Source> sourcesList;
    private List<Source> filteredSourcesList;
    private int currentSource;
    private int currentArticle;
    private HashMap<String, ArrayList<Source>> topicsToSource;
    private HashMap<String, ArrayList<Source>> languagesToSource;
    private HashMap<String, ArrayList<Source>> countriesToSource;
    private ArrayList<String> sourcesDisplayed;
    private HashMap<String, String> sourcesHashMap;
    private Map<String, Integer> topicsToColor;

    public List<Article> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<Article> articlesList) {
        this.articlesList = articlesList;
    }

    public List<Source> getSourcesList() {
        return sourcesList;
    }

    public void setSourcesList(List<Source> sourcesList) {
        this.sourcesList = sourcesList;
    }

    public int getCurrentSource() {
        return currentSource;
    }

    public void setCurrentSource(int currentSource) {
        this.currentSource = currentSource;
    }

    public int getCurrentArticle() {
        return currentArticle;
    }

    public void setCurrentArticle(int currentArticle) {
        this.currentArticle = currentArticle;
    }

    public HashMap<String, ArrayList<Source>> getTopicsToSource() {
        return topicsToSource;
    }

    public void setTopicsToSource(HashMap<String, ArrayList<Source>> topicsToSource) {
        this.topicsToSource = topicsToSource;
    }

    public HashMap<String, ArrayList<Source>> getLanguagesToSource() {
        return languagesToSource;
    }

    public void setLanguagesToSource(HashMap<String, ArrayList<Source>> languagesToSource) {
        this.languagesToSource = languagesToSource;
    }

    public HashMap<String, ArrayList<Source>> getCountriesToSource() {
        return countriesToSource;
    }

    public void setCountriesToSource(HashMap<String, ArrayList<Source>> countriesToSource) {
        this.countriesToSource = countriesToSource;
    }

    public ArrayList<String> getSourcesDisplayed() {
        return sourcesDisplayed;
    }

    public void setSourcesDisplayed(ArrayList<String> sourcesDisplayed) {
        this.sourcesDisplayed = sourcesDisplayed;
    }

    public HashMap<String, String> getSourcesHashMap() {
        return sourcesHashMap;
    }

    public void setSourcesHashMap(HashMap<String, String> sourcesHashMap) {
        this.sourcesHashMap = sourcesHashMap;
    }

    public Map<String, Integer> getTopicsToColor() {
        return topicsToColor;
    }

    public void setTopicsToColor(Map<String, Integer> topicsToColor) {
        this.topicsToColor = topicsToColor;
    }

    public List<Source> getFilteredSourcesList() {
        return filteredSourcesList;
    }

    public void setFilteredSourcesList(List<Source> filteredSourcesList) {
        this.filteredSourcesList = filteredSourcesList;
    }
}
