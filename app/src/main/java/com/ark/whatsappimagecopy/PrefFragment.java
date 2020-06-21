package com.ark.whatsappimagecopy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.zaphlabs.filechooser.KnotFileChooser;
import com.zaphlabs.filechooser.Sorter;
import com.zaphlabs.filechooser.utils.FileType;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;


public class PrefFragment extends PreferenceFragmentCompat {

    static ArrayList<Image> images=new ArrayList<Image>();
    static String outputPath= new String();
    ProgressDialog mProgressBar;
    Handler handler;
    static boolean moveFile=false;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);

        Preference selectImagePref = getPreferenceManager().findPreference("img");
        selectImagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getContext(), "Navigate to /WhatsApp Images/ Folder", Toast.LENGTH_LONG).show();

                ImagePicker.with(getActivity())
                        .setFolderMode(true)
                        .setFolderTitle("Select Images")
                        .setRootDirectoryName(Config.CREATOR.getROOT_DIR_DCIM())
                        .setDirectoryName("Image Picker")
                        .setMultipleMode(true)
                        .setShowNumberIndicator(true)
                        .setShowCamera(false)
                        .setMaxSize(100)
                        .setLimitMessage("You can select up to 100 images")
                        .setRequestCode(100)
                        .start();
                return true;
            }
        });

        Preference outputFolderSelector = getPreferenceManager().findPreference("output");
        outputFolderSelector.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                   Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                   i.addCategory(Intent.CATEGORY_DEFAULT);
                   startActivityForResult(Intent.createChooser(i, "Choose directory"), 21);

                return true;
            }
        });
        final Preference moveFiles = getPreferenceManager().findPreference("move");
        moveFiles.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                moveFile=(boolean)newValue;
                System.out.println(moveFile);
                return true;
            }
        });
        moveFile=moveFiles.getSharedPreferences().getBoolean("move",false);
        System.out.println(moveFiles.getSharedPreferences().getAll());
        Preference starCopy = getPreferenceManager().findPreference("start");
        starCopy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(images.isEmpty()) {
                    Toast.makeText(getContext(), "No images selected!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(outputPath.isEmpty()) {
                    Toast.makeText(getContext(), "Output path not selected!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                mProgressBar= new ProgressDialog(getContext());
                mProgressBar.setMax(100);
                mProgressBar.setCancelable(false);
                mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressBar.show();
                handler=new Handler();
                new Worker(getActivity(),images,outputPath,mProgressBar,handler, moveFile).start();
                return true;
            }
        });


    }
}

