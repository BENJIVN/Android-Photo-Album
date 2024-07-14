package edu.rutgers.android36;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class Photo implements Serializable {

    private String path;
    private List<String> tags;

    public Photo(String filePath){
        this.path = filePath;
        this.tags = new ArrayList<>();
    }

    public String getPath(){
        return path;
    }

    public void setPath(String p){
        this.path = p;
    }

    public void addTag(String type, String tag){
       tags.add(type+"="+tag);
    }

    public List<String> getTags() {
        return tags;
    }

    public void deleteTag(String tag) {
        for (String s : tags) {
            if (s.equals(tag)) {
                tags.remove(s);
            }
        }
    }
    //check if a photo has specific tag
//    public boolean hasTag(String type, String tag) {
//        return tags.containsKey(type) && tags.get(type).contains(tag.toLowerCase());
//    }

    //getter for all tags
//    public Set<String> getTagsByType(String type){
//        return tags.getOrDefault(type, new HashSet<>());
//    }

//    @Override
//    public String toString(){
//        return "Photo"; //need to finish this
//    }
}


