package edu.rutgers.android36;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Album> adapter;
    public static ArrayList<Album> albums;

    Button createAlbumButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        List<Album> albumList = new ArrayList<>();
//        albumList.add(new Album("John"));
//        albumList.add(new Album("Alice"));
//
//        // Write the user list to file
//        FileHelper.writeToFile(this, albumList);

        //Initialize the listview + adapter
        listView = findViewById(R.id.albumsListView);
        albums = loadAlbums();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albums);
        listView.setAdapter(adapter);

        // Set click listener for listview items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected album
            Album selectedAlbum = albums.get(position);
            // Open AlbumDetailsActivity with the name of the selected album
            openAlbum(selectedAlbum);
        });

        Button createAlbum = findViewById(R.id.createAlbum);
        createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAlbum();
            }
        });

        Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void search() {

        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        albums.clear();
        albums.addAll(loadAlbums());
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Album> loadAlbums() {
        List<Album> loadedAlbumList = FileHelper.readFromFile(this);
        if (loadedAlbumList == null) {
            Log.d(TAG, "FAILED TO LOAD ALBUMS");
            return new ArrayList<>(); // Return an empty list if loadedAlbumList is null
        }
        Iterator<Album> iterator = loadedAlbumList.iterator();
        while (iterator.hasNext()) {
            Album album = iterator.next();
            Log.d(TAG, "Loaded : " + album.getName() + ", " + album.getPhotos());
        }
        return (ArrayList<Album>) loadedAlbumList;
    }




    private ActivityResultLauncher<Intent> startForResultEdit;
    private ActivityResultLauncher<Intent> startForResultAdd;
    private void addNewAlbum(){
        //I think we use intent here, check Movies.zip again
        Intent intent = new Intent(MainActivity.this, CreateAlbumActivity.class);
        startActivity(intent);
    }

    private void openAlbum(Album album){
        //intent
        Intent intent = new Intent(MainActivity.this, AlbumDetailsActivity.class);
        intent.putExtra("albumName", album.getName());
        startActivity(intent);
    }

    public static Album getAlbum(String name) {
        for (Album a : albums) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }
}