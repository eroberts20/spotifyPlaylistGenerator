package com.example.evank.spotifytestapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wrapper.spotify.Api;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.attr.offset;
import static android.R.id.list;
import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {


    String CLIENT_ID = "a111623aa760425fbea3290a301ecf90"; // Your client id
    String clientSecret = "6bee9fa0ca314e08a620d1c77ec1bb35"; // Your secret
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/callback/q";
    AuthenticationResponse responeAuth;
    ArrayList<playlist_evan> playList = new ArrayList<playlist_evan>();
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    Context c;
    SpotifyService spotify_scopeless;
    SpotifyApi api_scopeless = new SpotifyApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("log1", "FIRST CHECK !");
        c = this;


        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming", "user-top-read", "playlist-read-private", "playlist-modify-private", "playlist-modify-public"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        final ListView listview = (ListView) findViewById(R.id.listview);
        final TextView text1 = (TextView) findViewById(R.id.text1);




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playlist_evan tmp = (playlist_evan) listview.getItemAtPosition(position);
                PlaylistSimple tmp_playlist = tmp.playlist;

                if(!tmp_playlist.equals(null)) {
                    Intent intent = new Intent(MainActivity.this, postLogin.class);
                    intent.putExtra("playlist", tmp_playlist);
                    intent.putExtra("auth", responeAuth);
                    startActivity(intent);
                    Log.d("playlist uri", tmp_playlist.tracks.href);
                }
            }
        });



        Log.d("Album success", "EVAN ROBERTS");


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            responeAuth = response;
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.i("tag1", "successful");
                    //Intent intent2 = new Intent(this,postLogin.class);
                    //startActivity(intent2);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.i("tag1", "unsuccessful");
                    Log.d("MainActivity", response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }


        api_scopeless.setAccessToken(responeAuth.getAccessToken());

        final SpotifyService spotify = api_scopeless.getService();
        spotify_scopeless = spotify;
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, 50);
        spotify.getMyPlaylists(options, new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                Log.d("p1", "playlists recieved");
                Log.d("PLAYLIST SIZE", String.valueOf(playlistSimplePager.items.size()));
                for(int i = 0; i < playlistSimplePager.items.size(); i++)
                {
                    Log.d("playlistname", playlistSimplePager.items.get(i).name);
                    playlist_evan tmp = new playlist_evan();
                    tmp.playlist =  playlistSimplePager.items.get(i);
                    tmp.playlist_name =  playlistSimplePager.items.get(i).name;
                    tmp.uri = playlistSimplePager.items.get(i).uri;
                    playList.add( tmp);

                }





                final ListView listview = (ListView) findViewById(R.id.listview);
                ArrayAdapter<playlist_evan> adapter = new ArrayAdapter<playlist_evan>(c, android.R.layout.simple_list_item_1, playList );
                listview.setAdapter(adapter);


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("p2", "palylist not revieced");
            }
        });



    }


    }








