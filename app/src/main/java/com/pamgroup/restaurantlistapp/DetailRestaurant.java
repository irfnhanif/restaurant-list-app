package com.pamgroup.restaurantlistapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pamgroup.restaurantlistapp.model.Emoji;
import java.io.File;
import static android.widget.Toast.LENGTH_SHORT;

public class DetailRestaurant extends AppCompatActivity implements View.OnClickListener {

    private API emojiAPI;
    private TextView tvName, tvAddress, tvBusinessHour, tvDescription;
    private ImageView ivRestaurant, btn_back;
    private String restaurantName, imgUrl;
    private SpannableString spannableString;

    private Button btnLihatMaps;
    private Bundle restaurantBundle;

    private Double longitude = 0.0;
    private Double latitude = 0.0;

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

        btnLihatMaps = findViewById(R.id.btnLihatMaps);
        btnLihatMaps.setOnClickListener(this);

        restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            restaurantName = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");
            imgUrl = restaurantBundle.getString("imageURL");

            //get long lat
            String lng =  restaurantBundle.getString("longitude");
            String lat = restaurantBundle.getString("latitude");
            if (lng != null) longitude = Double.parseDouble(lng);
            if (lat != null) latitude = Double.parseDouble(lat);
            if (latitude != 0.0 || longitude != 0.0) {
                btnLihatMaps.setVisibility(View.VISIBLE);
            } else {
                btnLihatMaps.setVisibility(View.GONE);
            }

            //API Emoji
            emojiAPI = new API(this.getApplicationContext());
            emojiAPI.getEmoji(new API.DataCallback(){

                @Override
                public void onDataReceived(Emoji emoji) {

                    spannableString = new SpannableString(convertUnicode(emoji.getUniCode()) + " " + restaurantName);
                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
                    spannableString.setSpan(foregroundSpan, 0, emoji.getUniCode().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    String unicode = convertUnicode(emoji.getUniCode());
                    spannableString = new SpannableString(unicode + " " + restaurantName);

                    ForegroundColorSpan foregroundSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.black));

                    int unicodeLength = unicode.length();
                    if (unicodeLength <= spannableString.length()) {
                        spannableString.setSpan(foregroundSpan, 0, unicodeLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

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
                Toast.makeText(this, "Image downloaded", LENGTH_SHORT).show();
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
        switch (view.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.btnLihatMaps:
                String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                Intent moveToMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(moveToMap);
        }
    }

    public String convertUnicode(String uniCode){
        String newUnicode = uniCode.replace("U+", "0x");
        int codepoint = Integer.decode(newUnicode);
        return new String(Character.toChars(codepoint));
    };
}