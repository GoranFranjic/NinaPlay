package com.nina.playlist;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView playlistView;
    private PlaylistAdapter adapter;
    private List<Song> songs = new ArrayList<>();
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupYouTubePlayer();
        loadPlaylist();
        startMusicService();
    }

    private void initializeViews() {
        playlistView = findViewById(R.id.playlistView);
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        adapter = new PlaylistAdapter(this, songs);
        playlistView.setAdapter(adapter);
    }

    private void setupYouTubePlayer() {
        youTubePlayerView.initialize("YOUR_API_KEY",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer player, boolean wasRestored) {
                        youTubePlayer = player;
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult error) {
                        // Handle error
                    }
                });
    }

    private void loadPlaylist() {
        // Učitaj playlistu iz SharedPreferences ili baze
        songs.addAll(PlaylistManager.loadPlaylist(this));
        adapter.notifyDataSetChanged();
    }

    private void startMusicService() {
        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);
    }
}