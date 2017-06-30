package com.example.evank.spotifytestapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistBase;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.evank.spotifytestapp.R.id.radio;
import static com.example.evank.spotifytestapp.R.id.time;
import static java.security.AccessController.getContext;
import static java.util.logging.Logger.global;

public class postLogin extends AppCompatActivity {


    Pager<PlaylistTrack> Mtracks;
    String myID;
    HashMap <String, tracks_evan> map = new HashMap<String, tracks_evan>();
    int playlist_size = 0;
    int k = 0;
    SeekBar progress;
    ProgressBar timeline;
    ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        progress = (SeekBar) findViewById(R.id.seekbar);
        timeline = (ProgressBar) findViewById(R.id.determinateBar);

        progbar = (ProgressBar) findViewById(R.id.progbar);
        final TextView number_of_tracks = (TextView) findViewById(R.id.number_of_tracks);
        number_of_tracks.setText("Number of Tracks to Publish: ");
        final PlaylistSimple playlist = getIntent().getParcelableExtra("playlist");
        AuthenticationResponse auth = getIntent().getParcelableExtra("auth");



        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(auth.getAccessToken());
        final SpotifyService spotify = api.getService();
        Log.d("access token", auth.getAccessToken());
        spotify.getPlaylist(playlist.owner.id, playlist.id, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                ArrayList<String> trackList = new ArrayList<String>();
                Pager<PlaylistTrack> tracks = playlist.tracks;
                Mtracks = tracks;
                myID = playlist.owner.id;
                for(int i = 0; i < tracks.total; i++)
                {
                   // Log.d("tracks", tracks.items.get(i).track.name);
                    trackList.add(tracks.items.get(i).track.name);
                }






                final ListView listview = (ListView) findViewById(R.id.listview);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(postLogin.this, android.R.layout.simple_list_item_1, trackList);
                //listview.setBackgroundColor(Color.DKGRAY);
                listview.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                number_of_tracks.setText("Number of Tracks to Publish: " + Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        final Button b1 = (Button) findViewById(R.id.b1);
        final Button b2 = (Button) findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progbar.setVisibility(View.VISIBLE);
                b1.setEnabled(false);

                if(Mtracks.items.size() > 1) {
                        timeline.setMax( Mtracks.items.size() -1);
                        Log.d("MAXofinc", String.valueOf(timeline.getMax()));
                        timeline.setProgress(0);
                        for (k = 0; k < Mtracks.items.size() - 1; k++) {

                            timeline.setProgress(k);
                            spotify.searchPlaylists(Mtracks.items.get(k).track.name, new Callback<PlaylistsPager>() {
                                @Override
                                public void success(PlaylistsPager playlistsPager, Response response) {
                                    ArrayList<Track> results = new ArrayList<Track>();
                                    Log.d("search reults", String.valueOf(playlistsPager.playlists.items.size()));
                                    //LOOP FOR PLAYLIST RESULTS

                                    B:
                                    for (int tr = 0; tr < playlistsPager.playlists.items.size(); tr++) {

                                        //SEARCH FOR PLAYLIST
                                        spotify.getPlaylist(playlistsPager.playlists.items.get(tr).owner.id, playlistsPager.playlists.items.get(tr).id, new Callback<Playlist>() {
                                            @Override
                                            public void success(Playlist playlist, Response response) {
                                                //LOOP FOR TRACKS IN SEARCHED PLAYLIST
                                                C:
                                                for (int searched_tracks = 0; searched_tracks < playlist.tracks.items.size(); searched_tracks++) {

                                                    if (Mtracks.items.get(k).track.name != playlist.tracks.items.get(searched_tracks).track.name) {
                                                        if (!map.containsKey(playlist.tracks.items.get(searched_tracks).track.name)) {
                                                            tracks_evan tmp = new tracks_evan();
                                                            tmp.track = playlist.tracks.items.get(searched_tracks).track;
                                                            tmp.freq = 1;
                                                            map.put(playlist.tracks.items.get(searched_tracks).track.name, tmp);
                                                        } else {
                                                            map.get(playlist.tracks.items.get(searched_tracks).track.name).freq += 1;
                                                        }
                                                    }

                                                }

                                                //SETTING ADAPTER FOR LISTVIEW

                                                ArrayList<tracks_evan> searchedList = new ArrayList<tracks_evan>(map.values());
                                                ;
                                                Collections.sort(searchedList);
                                                final ListView listview = (ListView) findViewById(R.id.listview);
                                                ArrayAdapter<tracks_evan> adapter;
                                                progbar.setVisibility(View.GONE);
                                                timeline.setVisibility(View.VISIBLE);

                                                if (map.size() > 100) {
                                                    adapter = new ArrayAdapter<tracks_evan>(postLogin.this, android.R.layout.simple_list_item_1, searchedList.subList(0, 100));
                                                    progress.setMax(100);

                                                } else {
                                                    adapter = new ArrayAdapter<tracks_evan>(postLogin.this, android.R.layout.simple_list_item_1, searchedList);
                                                    progress.setMax(map.size());

                                                }
                                               // listview.setBackgroundColor(Color.DKGRAY);
                                                listview.setAdapter(adapter);
                                                Log.d("track name", "set adapter");


                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                            }
                                        });

                                        if(tr > 2){
                                            break ;
                                        }

                                    }


                                }

                                @Override
                                public void failure(RetrofitError error) {
                                }

                            });



                        }

                }

                b2.setEnabled(true);
                b1.setText("Playlist Ready");

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> options = new HashMap<>();
                EditText name = (EditText) findViewById(R.id.enter_text);


                options.put("name",  name.getText().toString());
                options.put("public", false);
                Log.d("ID", playlist.owner.id);
                spotify.createPlaylist(myID, options, new Callback<Playlist>() {
                    @Override
                    public void success(Playlist playlist, Response response) {
                        Map<String, Object> p1 = new HashMap<>();
                        Map<String, Object> p2 = new HashMap<>();
                        ArrayList<tracks_evan> searchedList = new ArrayList<tracks_evan>(map.values());
                        String longone = new String();

                        for(int i = 0; i < progress.getProgress(); i++)
                        {
                         longone += searchedList.get(i).track.uri + ",";
                        }
                        p1.put("uris", longone);

                        spotify.addTracksToPlaylist(myID, playlist.id, p1, p2, new Callback<Pager<PlaylistTrack>>() {
                            @Override
                            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                                Log.d("addtrack", "SUCCESS");
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("addtrack", "Failure");

                            }
                        });

                        Log.d("called", "called");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("uncalled", "uncalled");

                    }
                });
            }
        });

    }



}
