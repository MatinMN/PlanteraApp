package com.example.planteraapp.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class AttributeConverters {
    @TypeConverter
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        b = b.length > 500000 ? Compress(b) : b;
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @TypeConverter
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    private static byte[] Compress(byte[] img) {
        while (img.length > 900000) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            img = stream.toByteArray();
        }
        return img;
    }
}
