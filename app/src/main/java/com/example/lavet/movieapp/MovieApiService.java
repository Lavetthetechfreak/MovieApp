package com.example.lavet.movieapp;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by lavet on 4/14/17.
 */

public interface MovieApiService {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);
}
