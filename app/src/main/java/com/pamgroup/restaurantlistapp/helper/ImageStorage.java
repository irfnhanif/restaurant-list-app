package com.pamgroup.restaurantlistapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImageStorage {

    private FirebaseStorage storage;
    private StorageReference storageRef, imageRef;
    private Uri file;
    private String imageURL;

    public ImageStorage() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public String uploadImage(String filePath) {
        file = Uri.fromFile(new File(filePath));
        imageRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        uploadTask.addOnFailureListener(exception -> {
            Log.e("ERROR-UPLOAD", exception.getMessage());
        }).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageURL = uri.toString();
                Log.d("IMAGEURL", imageURL);
            }).addOnFailureListener(exception -> {
                Log.e("ERROR-GET-IMAGE-URL", exception.getMessage());
            });
        });

        return imageURL;
    }


}
