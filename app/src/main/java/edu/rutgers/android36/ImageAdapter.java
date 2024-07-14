package edu.rutgers.android36;

import static edu.rutgers.android36.ImageStorageUtils.loadImageFromInternalStorage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Bitmap> imageBitmaps;
    private List<Photo> photos;

    private Album currentAlbum;
    private String currentPhoto;

    public ImageAdapter(List<Bitmap> imageBitmaps, List<Photo> photos, Album currentAlbum) {
        this.imageBitmaps = imageBitmaps;
        this.photos = photos;
    }

    public interface OnImageClickListener {
        void onImageClick(Bitmap bitmap, String filePath);
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }


    private List<String> selectedImageFilePaths = new ArrayList<>();
    private List<Bitmap> selectedBitmaps = new ArrayList<>();
    private void initialize() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;


        for (Photo photo : currentAlbum.getPhotos()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPath(), options);
            String filePath = photo.getPath();
            if (bitmap != null && filePath != null) {
                selectedBitmaps.add(bitmap);
                selectedImageFilePaths.add(filePath);
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Bitmap bitmap = imageBitmaps.get(position);
        holder.imageView.setImageBitmap(bitmap);

        // Set up click listener to open image detail activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current photo
                for (Photo p : currentAlbum.getPhotos()) {
                    if (bitmap.sameAs(loadImageFromInternalStorage(v.getContext(), p.getPath()))) {
                        currentPhoto = p.getPath();
                        break;
                    }
                }

                // Create intent to open ImageViewActivity
                Intent intent = new Intent(v.getContext(), ImageViewActivity.class);

                // Pass the image bitmap as a byte array to the activity
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("imageBitmap", byteArray);

                // Pass the current album name and the file path of the current photo to the activity
                intent.putExtra("albumName", currentAlbum.getName());
                intent.putExtra("photoPath", currentPhoto);

                // Start the activity
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageBitmaps.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public void setCurrentPath(String path) {
        this.currentPhoto = path;
    }
}

