package com.stackroute.muzicx.repository;

import com.stackroute.muzicx.domain.Track;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends CrudRepository<Track,Integer> {

    @Query("SELECT t FROM Track t WHERE t.name = ?1")
    List<Track> getTrackbyName(String name);
}
