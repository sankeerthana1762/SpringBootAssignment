package com.stackroute.muzicx.service;

import com.stackroute.muzicx.domain.Track;
import com.stackroute.muzicx.exception.TrackAlreadyExistsException;
import com.stackroute.muzicx.exception.TrackNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrackService {

    public Track saveTrack(Track track) throws TrackAlreadyExistsException;

    public List<Track>getAllTracks() throws TrackNotFoundException;

    public int deleteTrack(long id);

    public Track getTrackById(int id) throws TrackNotFoundException;

    public List<Track> getTrackbyName(String name);

    void getTopTracks();

    public Track UpdateTrack(int id, Track track)throws TrackNotFoundException;


}
