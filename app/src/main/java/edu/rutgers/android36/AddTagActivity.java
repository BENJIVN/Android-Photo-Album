package edu.rutgers.android36;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddTagActivity extends AppCompatActivity {

    private static final String[] tagTypes = {"location", "person"};
    Photo currentPhoto;
    Album currentAlbum;
    TextView tagValue;
    String tagType;

    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        // Retrieve the currentPhoto from the intent's extras
        currentPhoto = (Photo) getIntent().getSerializableExtra("currentPhoto");
        currentAlbum = (Album) getIntent().getSerializableExtra("currentAlbum");
//        currentAlbum = getIntent().getParcelableExtra("currentAlbum");
        // Now you can use the currentPhoto object in this activity
        tagValue = findViewById(R.id.tagValue);
        submit = findViewById(R.id.submitTag);

        Spinner spinner = (Spinner)findViewById(R.id.tagTypeSelection);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(AddTagActivity.this,
                android.R.layout.simple_spinner_item,tagTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tagType = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the tagValue from the TextView
                String tagVal = tagValue.getText().toString();

                System.out.println(tagType+tagVal);

                currentPhoto.addTag(tagType, tagVal);

                System.out.println(currentPhoto);

                FileHelper.writeToFile(AddTagActivity.this, MainActivity.albums);
                // Create an intent to return the currentAlbum
                Intent intent = new Intent();
                intent.putExtra("currentAlbumName", currentAlbum.getName());

                // Set the result to indicate success and include the intent
                setResult(RESULT_OK, intent);
                finish(); // Finish the AddTagActivity
            }
        });
        String tags = tagValue.getText().toString();


    }



}
