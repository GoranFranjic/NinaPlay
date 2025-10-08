package com.nina.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nina.playlist.models.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter adapter;
    private List<Song> songs = new ArrayList<>();
    private EditText searchEditText;
    private Button searchButton, playButton, pauseButton, stopButton;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupWebView();
        setupClickListeners();

        // Dodaj neke test pjesme
        addSampleSongs();
    }

    private void initializeViews() {
        playlistRecyclerView = findViewById(R.id.playlistRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        webView = findViewById(R.id.webView);
    }

    private void setupRecyclerView() {
        adapter = new PlaylistAdapter(songs, this::playSong);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        playlistRecyclerView.setAdapter(adapter);
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Load YouTube music
        webView.loadUrl("https://music.youtube.com");
    }

    private void setupClickListeners() {
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchSongs(query);
            }
        });

        playButton.setOnClickListener(v -> {
            startMusicService();
        });

        pauseButton.setOnClickListener(v -> {
            // Pauziraj WebView audio
            webView.onPause();
        });

        stopButton.setOnClickListener(v -> {
            stopMusicService();
            webView.loadUrl("about:blank"); // Zaustavi sve
        });
    }

    private void addSampleSongs() {
        // Dodaj neke test pjesme
        songs.add(new Song("dQw4w9WgXcQ", "Never Gonna Give You Up", "Rick Astley", ""));
        songs.add(new Song("9bZkp7q19f0", "Gangnam Style", "PSY", ""));
        songs.add(new Song("JGwWNGJdvx8", "Shape of You", "Ed Sheeran", ""));
        adapter.notifyDataSetChanged();
    }

    private void searchSongs(String query) {
        // Pretraži direktno u YouTube Music WebView
        String searchUrl = "https://music.youtube.com/search?q=" + query.replace(" ", "+");
        webView.loadUrl(searchUrl);

        // Dodaj u playlistu (simulacija)
        songs.add(new Song("search_" + System.currentTimeMillis(), query, "Rezultat pretrage", ""));
        adapter.notifyDataSetChanged();
    }

    private void playSong(Song song) {
        if (song.getId().startsWith("search_")) {
            // Ako je search rezultat, ostani na trenutnoj stranici
            return;
        }
        // Play specific song u WebView
        String youtubeUrl = "https://www.youtube.com/watch?v=" + song.getId();
        webView.loadUrl(youtubeUrl);
    }

    private void startMusicService() {
        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);
    }

    private void stopMusicService() {
        Intent serviceIntent = new Intent(this, MusicService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}