package com.anton.testtagsoft;

import android.graphics.Bitmap;

public interface ClickItemListener {
    void onItemClick(int position, String name, Bitmap image, String status, String species, String gender);
}
