package com.pamgroup.restaurantlistapp.helper;

import android.net.Uri;

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

    public ImageStorage(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public String uploadImage(String filePath) {
        String imageURL = "";
        uploadImageTask(filePath).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String URL) {
                // Handle successful image upload
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful image upload
            }
        });
    }

    public Task<String> uploadImageTask(String filePath) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        file = Uri.fromFile(new File(filePath));
        imageRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                taskCompletionSource.setException(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        taskCompletionSource.setResult(imageURL);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setException(e);
                    }
                });
            }
        });

        return taskCompletionSource.getTask();
    }

}
