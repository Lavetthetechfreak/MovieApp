package com.example.lavet.movieapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addEncodedQueryParam("api_key", "YOUR_API_KEY");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MovieApiService service = restAdapter.create(MovieApiService.class);
        service.getPopularMovies(new Callback<Movie.MovieResult>() {
            public void success(Movie.MovieResult movieResult, Downloader.Response response) {
                mAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void success(Movie.MovieResult movieResult, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            movies.add(new Movie());
        }
        mAdapter.setMovieList(movies);

    }
    }
    class MovieViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public MovieViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
    class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder>
    {

        private List<Movie> mMovieList;
        private LayoutInflater mInflater;
        private Context mContext;

        public MoviesAdapter(Context context)
        {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.mMovieList = new ArrayList<>();
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = mInflater.inflate(R.layout.row_movie, parent, false);
            MovieViewHolder viewHolder = new MovieViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position)
        {
            Movie movie = mMovieList.get(position);

            // This is how we use Picasso to load images from the internet.
            Picasso.with(mContext)
                    .load(movie.getPoster())
                    .placeholder(R.color.colorAccent)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount()
        {
            return (mMovieList == null) ? 0 : mMovieList.size();
        }

        public void setMovieList(List<Movie> movieList)
        {
            this.mMovieList.clear();
            this.mMovieList.addAll(movieList);
            // The adapter needs to know that the data has changed. If we don't call this, app will crash.
            notifyDataSetChanged();
        }
    }


    /**
     * Created by lavet on 4/14/17.
     */



