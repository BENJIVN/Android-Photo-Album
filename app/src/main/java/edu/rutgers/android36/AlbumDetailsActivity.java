package edu.rutgers.android36;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsActivity extends AppCompatActivity {
    private static final int PICK_IMAGES_REQUEST = 1;
    public static List<Bitmap> selectedImageBitmaps = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private Album currentAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_details);

        recyclerView = findViewById(R.id.recyclerView);

        String albumName = getIntent().getStringExtra("albumName");
        currentAlbum = MainActivity.getAlbum(albumName);
        System.out.println(albumName);




        if (currentAlbum != null) {
            for (Photo photo : currentAlbum.getPhotos()) {
                // Retrieve file path from the Photo object
                String filePath = photo.getPath();
                if (filePath != null && !filePath.isEmpty()) {
                    // Load bitmap from file using the file path
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap != null) {
                        // Add bitmap to list
                        selectedImageBitmaps.add(bitmap);
                    }
                }
            }
        }




        // Initialize and set up RecyclerView with selectedImageBitmaps
        assert currentAlbum != null;
        imageAdapter = new ImageAdapter(selectedImageBitmaps, currentAlbum.getPhotos(), currentAlbum);
        imageAdapter.setCurrentAlbum(currentAlbum);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);


        // Set up button click listener
        Button selectImageButton = findViewById(R.id.selectPhotosButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });


        // Set up button click listener
        Button deleteAlbum = findViewById(R.id.deleteAlbum);
        deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlbum();
            }
        });

        Button renameAlbum = findViewById(R.id.renameAlbum);
        renameAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameAlbum();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadImages();
    }
    private void deleteAlbum() {
        MainActivity.albums.remove(currentAlbum);
        FileHelper.writeToFile(AlbumDetailsActivity.this, MainActivity.albums);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void renameAlbum() {
        // Example: Show a dialog with an input field for entering the new album name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename Album");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the new album name from the input field
                String newAlbumName = input.getText().toString();

                // Perform the rename operation
                if (!TextUtils.isEmpty(newAlbumName)) {
                    renameCurrentAlbum(newAlbumName);
                } else {
                    // Show a message if the input field is empty
                    Toast.makeText(AlbumDetailsActivity.this, "Please enter a valid album name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void loadImages() {
        selectedImageBitmaps.clear(); // Clear existing images
        if (currentAlbum != null) {
            for (Photo photo : currentAlbum.getPhotos()) {
                String filePath = photo.getPath();
                if (filePath != null && !filePath.isEmpty()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap != null) {
                        selectedImageBitmaps.add(bitmap);
                    }
                }
            }
        }
        imageAdapter.notifyDataSetChanged(); // Notify RecyclerView adapter of data change
    }

    private void renameCurrentAlbum(String newAlbumName) {
        // Implement the logic to rename the current album
        if (currentAlbum != null) {
            currentAlbum.setName(newAlbumName);
            FileHelper.writeToFile(AlbumDetailsActivity.this, MainActivity.albums);
        }
    }


    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        String filename = "image_" + System.currentTimeMillis() + ".png";
                        String filePath = ImageStorageUtils.saveImageToInternalStorage(this, bitmap, filename); // Save image to internal storage
                        selectedImageBitmaps.add(bitmap);
                        currentAlbum.addPhoto(new Photo(filePath)); // Store the file path in the Photo object
                        FileHelper.writeToFile(AlbumDetailsActivity.this, MainActivity.albums);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String filename = "image_" + System.currentTimeMillis() + ".png";
                    String filePath = ImageStorageUtils.saveImageToInternalStorage(this, bitmap, filename); // Save image to internal storage
                    selectedImageBitmaps.add(bitmap);
                    currentAlbum.addPhoto(new Photo(filePath)); // Store the file path in the Photo object
                    FileHelper.writeToFile(AlbumDetailsActivity.this, MainActivity.albums);
                }
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

}


