package com.pamgroup.restaurantlistapp;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pamgroup.restaurantlistapp.model.Emoji;

import java.io.File;

public class DetailRestaurant extends AppCompatActivity implements View.OnClickListener {

    private API emojiAPI;
    private TextView tvName, tvAddress, tvBusinessHour, tvDescription;
    private ImageView ivRestaurant, btn_back;
    private String restaurantName, imgUrl;
    private SpannableString spannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        tvName = findViewById(R.id.tvRestoran);
        tvAddress = findViewById(R.id.tvAlamat);
        tvBusinessHour = findViewById(R.id.tvJam);
        tvDescription = findViewById(R.id.tvDeskripsi);
        ivRestaurant = findViewById(R.id.ivDetailRestoran);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            restaurantName = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");
            imgUrl = restaurantBundle.getString("imageURL");

            //API Emoji
            emojiAPI = new API(this.getApplicationContext());
            emojiAPI.getEmoji(new API.DataCallback(){

                @Override
                public void onDataReceived(Emoji emoji) {
                    spannableString = new SpannableString(convertUnicode(emoji.getUniCode()) + " " + restaurantName);
                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.purple_700));
                    spannableString.setSpan(foregroundSpan, 0, emoji.getUniCode().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvName.setText(spannableString);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ERROR-EMOJI-FETCH", e.getMessage());
                }
            });
            


            if (imgUrl != null) {
                downloadImage();
            }

//            tvName.setText(restaurantName);
            tvAddress.setText(address);
            tvBusinessHour.setText(businessHour);
            tvDescription.setText(description);

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_restaurant)
                    .error(R.drawable.ic_error);

            Glide.with(this)
                    .load(imgUrl)
                    .apply(requestOptions)
                    .into(ivRestaurant);
        }
    }
   // Download image
    private void downloadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        try {
            // Create storage reference
            StorageReference storageRef = storage.getReferenceFromUrl(imgUrl);

            File localFile;
            File storagePath = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "");

            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            if(storagePath.exists()) {
                localFile = storagePath;
            } else {
                localFile = getApplication().getExternalFilesDir("images");
            }

            final File myFile = new File(localFile, restaurantName + ".jpg");

            storageRef.getFile(myFile).addOnSuccessListener(taskSnapshot -> {
                // Local file berhasil created
                Toast.makeText(this, "File downloaded", LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // Handle errors
                Toast.makeText(getBaseContext(), "Download failed. Try again!", LENGTH_SHORT).show();
                exception.printStackTrace();
            });


            new Handler().postDelayed(() -> {
                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.DISPLAY_NAME, restaurantName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, myFile.getAbsolutePath());
                getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }, 1000);
            ContentValues values = new ContentValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String convertUnicode(String uniCode){
        String newUnicode = uniCode.replace("U+", "0x");
        int codepoint = Integer.decode(newUnicode);
        return new String(Character.toChars(codepoint));
    };
}