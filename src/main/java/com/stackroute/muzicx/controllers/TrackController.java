package com.stackroute.muzicx.controllers;


import com.stackroute.muzicx.domain.Track;
import com.stackroute.muzicx.exception.TrackAlreadyExistsException;
import com.stackroute.muzicx.exception.TrackNotFoundException;
import com.stackroute.muzicx.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "api/v1")
@ControllerAdvice(basePackages="com.stackroute.muzicx")
public class TrackController {


    TrackService trackService;

    @Value("${exceptionMsg}")
    String exp;

    @Value("${Savesuccessmsg}")
    String success;
    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }


    @ExceptionHandler(TrackAlreadyExistsException.class)
    @PostMapping("track")
    public ResponseEntity<?> saveTrack( Track track){

        ResponseEntity responseEntity;
        trackService.getTopTracks();
        try{
            trackService.saveTrack(track);
            responseEntity = new ResponseEntity<String>(success, HttpStatus.CREATED);


        }catch(TrackAlreadyExistsException e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

            e.printStackTrace();
        }
        return  responseEntity;

    }

    @GetMapping("track")
    public  ResponseEntity<?> getallTracks() throws TrackNotFoundException {
        ResponseEntity responseEntity;
       // trackService.getAllTracks();
            try {
                responseEntity = new ResponseEntity <List<Track>>(trackService.getAllTracks(),HttpStatus.OK);
            } catch (TrackNotFoundException e) {
                responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

                e.printStackTrace();
            }
        return  responseEntity;
    }

//    @GetMapping("trackk")
//    public  ResponseEntity<?> getTopTracks(){
//
//        return trackService.getTopTracks();
//    }

    @DeleteMapping("track/{id}")
    public 	ResponseEntity<?> delete(@PathVariable(value = "id") long id){

        ResponseEntity responseEntity;

        try{
           int result =  trackService.deleteTrack(id);
            System.out.println(result);
            if(result == 1){
                responseEntity = new ResponseEntity<String>("Succesfully deleted", HttpStatus.CREATED);

            }else{
                responseEntity = new ResponseEntity<String>(exp, HttpStatus.CONFLICT);

            }


        }catch(Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

        }

        return  responseEntity;

    }

    @ExceptionHandler(TrackNotFoundException.class)
    @PutMapping("track/{id}")
    public ResponseEntity<?> updateTrack(@PathVariable(value = "id") int id,Track track){

        ResponseEntity responseEntity;

        try{
            trackService.UpdateTrack(id,track);
            responseEntity = new ResponseEntity<String>("Succesfully updated", HttpStatus.CREATED);


        }catch(TrackNotFoundException e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

        }
        return  responseEntity;

    }

    @GetMapping("track/{name}")
    public ResponseEntity<?> getTrackbyName(@PathVariable(value = "name") String name) {

        ResponseEntity responseEntity;

        try {
            responseEntity = new ResponseEntity<List<Track>>(trackService.getTrackbyName(name), HttpStatus.CREATED);


        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

        }
        return responseEntity;

    }

    @GetMapping("track/{id}")
    //handler to get a track by id
    public ResponseEntity<?> getTrack(@PathVariable String id) {
        try {
            return new ResponseEntity<>(trackService.getTrackById(Integer.parseInt(id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
