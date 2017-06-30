# Spotify Playlist Generator
### An Android App for generating new playlist based off of a users current playlists from other users playlists with similar music

This app uses Spotify's Android authorization sdk to get access to read and write permissions for a users playlists.


<p align="center">
  
  <img src="https://github.com/eroberts20/spotifyPlaylistGenerator/blob/master/screenshots/start_screen.PNG">
</p>

Once the Playlists have been gathered to user selects one and is taken to a new activity to generate a new playlist.


<p align="center">
  
  <img src="https://github.com/eroberts20/spotifyPlaylistGenerator/blob/master/screenshots/playlist.PNG">
</p>


Based off of the tracks in the user provided playlist and using spotify's web API, the app searchs for other users playlists with similar tracks to the given playlist, excluding duplicate tracks.


<p align="center">
  
  <img src="https://github.com/eroberts20/spotifyPlaylistGenerator/blob/master/screenshots/playlist_ready.PNG">
</p>


The found tracks are organized into frequency of occurance. The user can then select how many tracks to put into the new playlist, name the playlist, and then publish it to their account.


<p align="center">
  
  <img src="https://github.com/eroberts20/spotifyPlaylistGenerator/blob/master/screenshots/spotify.PNG">
</p>

