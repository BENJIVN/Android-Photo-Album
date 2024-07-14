package edu.rutgers.android36;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
public class Album implements Serializable {
    private String name;
    private List<Photo> photos;

    public Album(String name){
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Photo> getPhotos(){
        return photos;
    }

    public void addPhoto(Photo p){
        if(!photos.contains(p)){
            photos.add(p);
        }
    }

    public boolean removePhoto(Photo p){
       photos.remove(p);
       return true;
    }

    @Override
    public String toString(){
        return name + " (" + photos.size() + " photos)";
    }
}
