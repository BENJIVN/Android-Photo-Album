package edu.rutgers.android36;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileHelper implements Serializable {

    private static final String FILENAME = "user_data";

//    private static List<Album> albums;

//    private static ArrayList<Album> albums = new ArrayList<>();
    public static void writeToFile(Context context, List<Album> albumList) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(albumList);
            oos.close();
            fos.close();
            Log.d("FileHelper", "Album list written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Album> readFromFile(Context context) {
        List<Album> albumList = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            albumList = (List<Album>) ois.readObject();
            ois.close();
            fis.close();
            Log.d("FileHelper", "Album list read from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return albumList;
    }

    public static List<Uri> getSelectedImagesUri(Context context, Intent data) {
        List<Uri> selectedImages = new ArrayList<>();

        if (data != null) {
            // If the intent contains data
            if (data.getClipData() != null) {
                // Multiple images are selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Single image is selected
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            }
        }

        return selectedImages;
    }

}