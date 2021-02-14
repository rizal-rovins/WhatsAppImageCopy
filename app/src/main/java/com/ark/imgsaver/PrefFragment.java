package com.ark.imgsaver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;


public class PrefFragment extends PreferenceFragmentCompat {

    static ArrayList<Image> images=new ArrayList<Image>();
    static String outputPath= new String();
    ProgressDialog mProgressBar;
    Handler handler;
    static boolean moveFile=false;
    Preference selectImagePref;
    Preference outputFolderSelector;
    public void updateImagesSelected( String msg)
    {
        selectImagePref.setSummary(msg);
    }

    public void updateOutputSelected(String msg){
        outputFolderSelector.setSummary("Path selected: " +msg);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);

        selectImagePref = getPreferenceManager().findPreference("img");
        selectImagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getContext(), "Navigate to /Chat App Images/ Folder", Toast.LENGTH_LONG).show();

                ImagePicker.with(getActivity())
                        .setFolderMode(true)
                        .setFolderTitle("Select Images")
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

        outputFolderSelector = getPreferenceManager().findPreference("output");
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

                if(moveFile)
                    moveFiles.setSummary("Images will be moved.");
                else
                    moveFiles.setSummary("Images will be copied.");
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
                new Worker((MainActivity)getActivity(),images,outputPath,mProgressBar,handler, moveFile).start();
                return true;
            }
        });


    }
}

