package edu.rutgers.android36;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAlbumActivity extends AppCompatActivity {

    private EditText albumNameEditText;
    private Button createAlbumButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);

        // Initialize UI components
        albumNameEditText = findViewById(R.id.albumNameEditText);
        createAlbumButton = findViewById(R.id.createAlbumButton);

        // Set click listener for createAlbumButton
        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered album name
                String albumName = albumNameEditText.getText().toString().trim();
                // Check if the album name is not empty
                if (!albumName.isEmpty()) {
                    MainActivity.albums.add(new Album(albumName));
                    FileHelper.writeToFile(CreateAlbumActivity.this, MainActivity.albums);

                    returnToMainActivity();
                } else {
                    // Inform the user to enter a valid album name
                    albumNameEditText.setError("Please enter a valid album name");
                }
            }
        });
    }

    private void returnToMainActivity() {
        // Return to MainActivity
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
