package edu.rutgers.android36;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageStorageUtils {

    public static String saveImageToInternalStorage(Context context, Bitmap bitmap, String filename) {

        File directory = context.getFilesDir();
        File file = new File(directory, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath(); // Return the file path
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap loadImageFromInternalStorage(Context context, String filePath) {
        Bitmap bitmap = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null; // File does not exist, return null
            }
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
