package com.example.newsaggregator;

import java.io.Serializable;

/**
 * id : "abc-news" ,
 * name : "ABC News" ,
 * description : "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com." ,
 * url : " https://abcnews.go.com " ,
 * category : "general" ,
 * language : "en" ,
 * country : "us"
 */
public class Source implements Serializable {
    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String languageCode;
    private String countryCode;
    private String language;
    private String country;

    public Source() {
    }

    public Source(String id, String name, String description, String url, String category, String languageCode, String countryCode, String language, String country) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.language = language;
        this.country = country;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
