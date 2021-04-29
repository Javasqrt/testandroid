package com.anton.testtagsoft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.URLUtil;
import android.widget.ImageView;

public class TableRowView {
    String name;
    Bitmap image1;
    String image, status,species, gender;
    public TableRowView(String name, Bitmap image1,  String status, String species, String gender){
        this.name = name;
        this.image1 = image1;
        this.status = status;
        this.species = species;
        this.gender = gender;
    }

    public Bitmap getImage() {
        return image1;
    }


    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }



    public String getSpecies() {
        return species;
    }



    public String getGender() {
        return gender;
    }



}
