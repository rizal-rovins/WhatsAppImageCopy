package com.ark.whatsappimagecopy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.preference.Preference;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutlin, new PrefFragment()).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(resultCode + "res code");
        System.out.println(requestCode + "req code");
        if (data==null)
            super.onActivityResult(requestCode, resultCode, data);

        else if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
             ArrayList<Image> images = ImagePicker.getImages(data);
             if(images.size()==0)
                 Toast.makeText(getApplicationContext(),"No Images Selected!",Toast.LENGTH_SHORT).show();

             else

                 Toast.makeText(getApplicationContext(),String.valueOf(images.size())+" Images Selected!",Toast.LENGTH_SHORT).show();
            // Do stuff with image's path or id. For example:
            for (Image image : images) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d("MainActivityQ",image.getPath());
                    PrefFragment.images.add(image);
                } else {
                    PrefFragment.images.add(image);
                }
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
            Log.d("MainActivityFolder",outputPath);
            PrefFragment.outputPath=outputPath;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}
