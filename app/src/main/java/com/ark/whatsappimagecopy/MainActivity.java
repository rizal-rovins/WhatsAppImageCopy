package com.ark.whatsappimagecopy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    PrefFragment fragment;

    void onFinishCopy()
    {
        Toast.makeText(this,"Finished copying images!",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this);

       getSupportFragmentManager().beginTransaction().replace(R.id.layoutlin2, new PrefFragment(),"FRAGMENT").commit();
       //load the ads

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("C3C2620790FE540E8D008CF76CE8B71D")
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        fragment =  (PrefFragment) getSupportFragmentManager().findFragmentByTag("FRAGMENT");


        System.out.println(resultCode + "res code");
        System.out.println(requestCode + "req code");
        if (data==null)
            super.onActivityResult(requestCode, resultCode, data);

        else if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
             ArrayList<Image> images = ImagePicker.getImages(data);
             if(images.size()==0) {
                 fragment.updateImagesSelected("Please select at least one image.");
             }
             else {
                 fragment.updateImagesSelected(images.size() + " images selected.");
             }
                 // Do stuff with image's path or id. For example:
            for (Image image : images) {
                    Log.d("MainActivityQ",image.getPath());
                    PrefFragment.images.add(image);
                }
        }

        else
        {
            Uri uri = data.getData();
            Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                    DocumentsContract.getTreeDocumentId(uri));

            String outputPath= null;
            try {
                outputPath = ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(),docUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            fragment.updateOutputSelected(outputPath);
            Log.d("MainActivityFolder",outputPath);
            PrefFragment.outputPath=outputPath;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}
