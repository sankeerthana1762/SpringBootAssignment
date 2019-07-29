package com.stackroute.muzicx.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.stackroute.muzicx.domain.Track;
import com.stackroute.muzicx.exception.TrackAlreadyExistsException;
import com.stackroute.muzicx.exception.TrackNotFoundException;
import com.stackroute.muzicx.repository.TrackRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    TrackRepository trackRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track saveTrack(Track track) throws TrackAlreadyExistsException {

        if (trackRepository.existsById(track.getId())) {
            throw new TrackAlreadyExistsException("Track already exists");
        }
        Track track1 = trackRepository.save(track);
        if (track1 == null) {
            throw new TrackAlreadyExistsException("Track already exists");

        }
        return track1;
    }

    @Override
    public List<Track> getAllTracks() throws TrackNotFoundException {

        return (List<Track>) trackRepository.findAll();
    }

    @Override
    public int deleteTrack(long id) {
        System.out.println(id);

        Long l = new Long(id);
        int i = l.intValue();
        Track track = new Track();
        try {
            trackRepository.deleteById(i);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    //method to get a single track by id
    @Override
    public Track getTrackById(int id) throws TrackNotFoundException {
        //check if track exists
        if (!trackRepository.existsById(id)) {
            //throw custom exception
            throw new TrackNotFoundException("Track Not Found!");
        }
        //otherwise get the track
        return trackRepository.findById(id).orElse(null);
    }

    @Override
    public List<Track> getTrackbyName(String name) {

        return trackRepository.getTrackbyName(name);
    }

    @Override
    public Track UpdateTrack(int id, Track track) throws TrackNotFoundException {

        Track track1;
        if (trackRepository.existsById(id)) {
            track1 = trackRepository.save(track);
        } else {
            throw new TrackNotFoundException("No track found with id:" + id);

        }
        return track1;

    }
//
//    public ResponseEntity<?> gettrackss(){
//        final String uri = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=spain&api_key=90aaa37a824cb860430cb9851bdb7902&format=json";
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
//        re
//    }

    public void getTopTracks()
    {
        final String uri = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=spain&api_key=90aaa37a824cb860430cb9851bdb7902&format=json";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);


        //Object Mapper to access the JSON from the response entity
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;

            //read the response body to get JSON object
        try {
            root = mapper.readTree(result.getBody());
            ArrayNode arrayNode = (ArrayNode) root.path("tracks").path("track");

            //iterate the JSON array
            for (int i = 0; i < arrayNode.size(); i++) {
                //get a new Track object and fill it with data using setters
                Track track = new Track();
                track.setName(arrayNode.get(i).path("name").asText());
                track.setComment(arrayNode.get(i).path("artist").path("name").asText());
                //save the track to database
                trackRepository.save(track);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
