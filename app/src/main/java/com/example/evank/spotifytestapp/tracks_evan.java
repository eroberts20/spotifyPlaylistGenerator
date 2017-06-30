package com.example.evank.spotifytestapp;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by evank on 6/24/2017.
 */

public class tracks_evan implements Comparable<tracks_evan> {
    public Track track;
    public Integer freq = 1;

    public String toString() {
        return String.valueOf(freq) + " : " + track.name;
    }

    public int compareTo(tracks_evan compareFruit) {

        int compareQuantity = ((tracks_evan) compareFruit).freq;


        return compareQuantity - this.freq;

    }
}

