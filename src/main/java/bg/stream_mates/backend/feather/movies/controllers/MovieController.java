package bg.stream_mates.backend.feather.movies.controllers;

import bg.stream_mates.backend.feather.commonData.dtos.CinemaRecRequestDto;
import bg.stream_mates.backend.feather.movies.models.entities.Movie;
import bg.stream_mates.backend.feather.movies.services.MovieService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @CrossOrigin(origins = "http://localhost:5173") // Позволява заявки от localhost:5173
    @PostMapping("/get-movies")
    public List<Movie> getMovies(@RequestBody CinemaRecRequestDto cinemaRecRequestDto) throws IOException, InterruptedException {
        List<Movie> movies = new ArrayList<>();
        if (cinemaRecRequestDto.getRecordName().trim().isEmpty()) return movies;

        return this.movieService.getMovies(cinemaRecRequestDto.getRecordName());
    }


    @CrossOrigin(origins = "http://localhost:5173") // Позволява заявки от localhost:5173
    @PostMapping("/search-movies")
    public String searchMovies(@RequestBody CinemaRecRequestDto cinemaRecRequestDto) throws IOException, InterruptedException {
        this.movieService.searchForMovies(cinemaRecRequestDto.getRecordName());
        return "";
    }
}
