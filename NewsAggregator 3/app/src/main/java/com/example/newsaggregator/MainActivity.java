package com.example.newsaggregator;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private HashMap<String, ArrayList<Source>> topicsToSource = new HashMap<>();
    private HashMap<String, ArrayList<Source>> languagesToSource = new HashMap<>();
    private HashMap<String, ArrayList<Source>> countriesToSource = new HashMap<>();
    private ArrayList<String> sourcesDisplayed = new ArrayList<>();
    private HashMap<String, String> sourcesHashMap = new HashMap<>();
    private List<Article> articlesArrayList = new ArrayList<>();
    private List<Source> allSourcesList = new ArrayList<>();
    private List<Source> filteredSourcesList = new ArrayList<>();
    private int[] colors;
    private Map<String, Integer> topicsToColor = new HashMap<>();
    private int currentSourcePointer;
    private int currentArticlePointer;
    private boolean stateFlag;

    private final Filter filter = new Filter();

    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArticleAdapter articleAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ViewPager2 viewPager2;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                    currentSourcePointer = position;
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,            /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        colors = new int[] {
                Color.BLACK,
                getColor(R.color.red),
                getColor(R.color.pink),
                getColor(R.color.purple),
                getColor(R.color.cyan),
                getColor(R.color.teal),
                getColor(R.color.blue),
                getColor(R.color.green),
                getColor(R.color.orange),
                getColor(R.color.brown)
        };

        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(articleAdapter);


        if (savedInstanceState == null )
            new Thread(new NewsAPISourcesRunnable(this)).start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        MakeLayout layoutRestore = new MakeLayout();
        layoutRestore.setArticlesList(articlesArrayList);
        layoutRestore.setSourcesList(allSourcesList);
        layoutRestore.setFilteredSourcesList(filteredSourcesList);

        layoutRestore.setCountriesToSource(countriesToSource);
        layoutRestore.setLanguagesToSource(languagesToSource);
        layoutRestore.setTopicsToSource(topicsToSource);

        layoutRestore.setCurrentSource(currentSourcePointer);
        layoutRestore.setCurrentArticle(viewPager2.getCurrentItem());

        layoutRestore.setTopicsToColor(topicsToColor);

        layoutRestore.setSourcesDisplayed(sourcesDisplayed);
        layoutRestore.setSourcesHashMap(sourcesHashMap);

        outState.putSerializable("state", layoutRestore);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MakeLayout layoutRestore = (MakeLayout) savedInstanceState.getSerializable("state");
        stateFlag = true;

        articlesArrayList = layoutRestore.getArticlesList();
        allSourcesList = layoutRestore.getSourcesList();
        filteredSourcesList = layoutRestore.getFilteredSourcesList();

        countriesToSource = layoutRestore.getCountriesToSource();
        languagesToSource = layoutRestore.getLanguagesToSource();
        topicsToSource = layoutRestore.getTopicsToSource();

        currentSourcePointer = layoutRestore.getCurrentSource();
        currentArticlePointer = layoutRestore.getCurrentArticle();

        topicsToColor = layoutRestore.getTopicsToColor();

        sourcesDisplayed = layoutRestore.getSourcesDisplayed();
        sourcesHashMap = layoutRestore.getSourcesHashMap();

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sourcesDisplayed) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(topicsToColor.get(textView.getText().toString()));

                return textView;
            }
        });

        setViewPager(articlesArrayList, currentArticlePointer);

        setTitle(String.format("%s (%s)", getString(R.string.app_name), filteredSourcesList.size()));
    }

    private void selectItem(int position) {
        String sourceName = sourcesDisplayed.get(position);
        String source = sourcesHashMap.get(sourceName);
        setTitle(sourceName);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new NewsAPIArticlesRunnable(this, source)).start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        List<Source> sources = null;

        if (item.getTitle().toString().equalsIgnoreCase("all")) {
            sources = new ArrayList<>(topicsToSource.get("all"));
            sourcesDisplayed.clear();
            filteredSourcesList = new ArrayList<>(sources);
            setTitle(String.format("%s (%s)", getString(R.string.app_name), sources.size()));
            for (Source source : sources) {
                sourcesDisplayed.add(source.getName());
                sourcesHashMap.put(source.getName(), source.getId());
            }
            mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sourcesDisplayed) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextColor(topicsToColor.get(textView.getText().toString()));
                    return textView;
                }
            });
            return super.onOptionsItemSelected(item);
        } else {
            sources = new ArrayList<>(filteredSourcesList);
        }

        if (!item.hasSubMenu()) {
            int id = item.getItemId();
            if (id == 0) {
                String topic = item.getTitle().toString();
                filter.setSelectedTopic(topic);
                sources = filteredSourcesList.stream()
                        .filter(source -> source.getCategory().equalsIgnoreCase(topic))
                        .collect(Collectors.toList());

                sourcesDisplayed.clear();
                Log.d(TAG, "onOptionsItemSelected: " + sources);
            } else if (id == 1) {
                String language = item.getTitle().toString();
                filter.setSelectedLanguage(language);
                sources = filteredSourcesList.stream()
                        .filter(source -> source.getLanguage().equalsIgnoreCase(language))
                        .collect(Collectors.toList());
                sourcesDisplayed.clear();
                Log.d(TAG, "onOptionsItemSelected: " + sources);
            } else if (id == 2) {
                String country = item.getTitle().toString();
                filter.setSelectedCountry(country);
                sources = filteredSourcesList.stream()
                        .filter(source -> source.getCountry().equalsIgnoreCase(country))
                        .collect(Collectors.toList());
                sourcesDisplayed.clear();
                Log.d(TAG, "onOptionsItemSelected: " + sources);
            }
            filteredSourcesList = new ArrayList<>(sources);
            setTitle(String.format("%s (%s)", getString(R.string.app_name), sources.size()));
            for (Source source : sources) {
                sourcesDisplayed.add(source.getName());
                sourcesHashMap.put(source.getName(), source.getId());
            }
        }

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sourcesDisplayed) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(topicsToColor.get(textView.getText().toString()));

                return textView;
            }
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        if(stateFlag){
            setOptionsMenu(allSourcesList);
        }
        return true;
    }

    public void updateSourcesData(List<Source> sourcesList) {
        allSourcesList.addAll(sourcesList);
        setOptionsMenu(sourcesList);
        filteredSourcesList.clear();
        sourcesDisplayed.clear();
        ArrayList<Source> sources = topicsToSource.get("all");
        for (Source source : sources) {
            filteredSourcesList.add(source);
            sourcesDisplayed.add(source.getName());
            sourcesHashMap.put(source.getName(), source.getId());
        }
    }

    public void setOptionsMenu(List<Source> sourcesList) {
        topicsToSource.put("all", new ArrayList<>(sourcesList));
        languagesToSource.put("All", new ArrayList<>(sourcesList));
        countriesToSource.put("All", new ArrayList<>(sourcesList));

        for (Source source : sourcesList) {
            String topic = source.getCategory();
            String language = source.getLanguage();
            String country = source.getCountry();

            if (!topicsToSource.containsKey(topic))
                topicsToSource.put(topic, new ArrayList<>());
            Objects.requireNonNull(topicsToSource.get(topic)).add(source);

            if (!languagesToSource.containsKey(language))
                languagesToSource.put(language, new ArrayList<>());
            Objects.requireNonNull(languagesToSource.get(language)).add(source);

            if (!countriesToSource.containsKey(country))
                countriesToSource.put(country, new ArrayList<>());
            Objects.requireNonNull(countriesToSource.get(country)).add(source);
        }

        ArrayList<String> topicsList = new ArrayList<>(topicsToSource.keySet());
        Collections.sort(topicsList);
        ArrayList<String> languagesList = new ArrayList<>(languagesToSource.keySet());
        Collections.sort(languagesList);
        ArrayList<String> countriesList = new ArrayList<>(countriesToSource.keySet());
        Collections.sort(countriesList);

        SubMenu topicsSubMenu = opt_menu.addSubMenu(0, 0, 0, "Topics");
        SubMenu languagesSubMenu = opt_menu.addSubMenu(0, 1, 0, "Languages");
        SubMenu countriesSubMenu = opt_menu.addSubMenu(0, 2, 0, "Countries");

        for (int i = 0; i < topicsList.size(); i++) {
            topicsSubMenu.add(Menu.NONE, 0, i, topicsList.get(i));
        }

        for (int i = 0; i < languagesList.size(); i++) {
            languagesSubMenu.add(Menu.NONE, 1, i, languagesList.get(i));
        }

        for (int i = 0; i < countriesList.size(); i++) {
            countriesSubMenu.add(Menu.NONE, 2, i, countriesList.get(i));
        }

        for (int i = 0; i < topicsSubMenu.size(); i++) {
            MenuItem menuItem = topicsSubMenu.getItem(i);
            SpannableString spannableString = new SpannableString(menuItem.getTitle().toString());
            spannableString.setSpan(new ForegroundColorSpan(colors[i]), 0, spannableString.length(), 0);
            if (!menuItem.getTitle().toString().equalsIgnoreCase("all")) {
                for (int j = 0; j < topicsToSource.get(menuItem.getTitle().toString()).size(); j++) {
                    topicsToColor.put(topicsToSource.get(menuItem.getTitle().toString()).get(j).getName(), colors[i]);
                }
            }
            menuItem.setTitle(spannableString);
        }

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sourcesDisplayed) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(topicsToColor.get(textView.getText().toString()));

                return textView;
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void downloadSourcesFailed() {
        sourcesDisplayed.clear();
        allSourcesList.clear();
        filteredSourcesList.clear();
        sourcesHashMap.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    public void updateArticlesData(List<Article> articlesList) {
        progressBar.setVisibility(View.GONE);
        articlesArrayList = articlesList;
        setViewPager(articlesList, 0);
    }

    public void setViewPager(List<Article> articlesList, int currentArticle) {
        articleAdapter = new ArticleAdapter(this, new ArrayList<>(articlesList));

        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(articleAdapter);
        articleAdapter.notifyItemRangeChanged(0, articlesList.size());
        viewPager2.setCurrentItem(currentArticle);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void downloadArticlesFailed() {
        progressBar.setVisibility(View.GONE);
        allSourcesList.clear();
        articleAdapter.notifyDataSetChanged();
    }
}