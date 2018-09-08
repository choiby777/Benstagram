package com.cby.benstagram.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageManager {
    private static final String TAG = "ImageManager";

    public static Bitmap getBitmap(String imgUrl){
        File imageFile = new File(imgUrl);
        FileInputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getBitmap: " + e.getMessage() );
        }finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "getBitmap: " + e.getMessage() );
            }

        }
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , quality , stream);
        return stream.toByteArray();
    }
}
