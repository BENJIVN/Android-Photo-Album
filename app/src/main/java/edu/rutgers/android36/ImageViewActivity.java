// ImageDetailActivity.java
package edu.rutgers.android36;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageViewActivity extends AppCompatActivity {

    private Album currentAlbum;
    private Photo currentPhoto;

    private TextView tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        // Retrieve the image bitmap from the intent extras
        byte[] byteArray = getIntent().getByteArrayExtra("imageBitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        // Retrieve the current album name and the file path of the current photo from the intent extras
        String albumName = getIntent().getStringExtra("albumName");
        String photoPath = getIntent().getStringExtra("photoPath");

        currentAlbum = MainActivity.getAlbum(albumName);


        currentPhoto = gp(photoPath);

        // Display the image bitmap in an ImageView
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        tags = findViewById(R.id.tags);

        System.out.println(currentPhoto.getTags());

        if (currentPhoto.getTags() != null && !currentPhoto.getTags().isEmpty()) {
            StringBuilder tagBuilder = new StringBuilder();
            for (String tag : currentPhoto.getTags()) {
                tagBuilder.append(tag).append(",");
            }
            // Set the text to the TextView
            tags.setText("Tags: " + tagBuilder.toString());
        } else {
            // If no tags exist, set an appropriate message
            tags.setText("No tags available");
        }

        // Find the "Add Tag" button
        Button addTagButton = findViewById(R.id.addTag);

        // Set OnClickListener for the "Add Tag" button
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag();
                Intent intent = new Intent(ImageViewActivity.this, AddTagActivity.class);
                intent.putExtra("currentPhoto", currentPhoto);
                intent.putExtra("currentAlbum", currentAlbum);
                startActivity(intent);
            }
        });

        // Set up button click listener
        Button deletePhoto = findViewById(R.id.removePhoto);
        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoto();
            }
        });
    }


    public Photo gp(String photoPath) {
        for (Photo p : currentAlbum.getPhotos()) {
            if (p.getPath().equals(photoPath)) {
                return p;
            }
        }
        return null;
    }

    private void removePhoto() {
        if (currentAlbum != null && currentPhoto != null) {
            currentAlbum.removePhoto(currentPhoto);
//            AlbumDetailsActivity.selectedImageBitmaps.remove(currentPhoto.getPath());
            FileHelper.writeToFile(ImageViewActivity.this, MainActivity.albums);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish(); // Finish the ImageViewActivity
        }
    }

    private void addTag() {
            FileHelper.writeToFile(ImageViewActivity.this, MainActivity.albums);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish(); // Finish the ImageViewActivity
    }
}
