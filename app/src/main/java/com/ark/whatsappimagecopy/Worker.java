package com.ark.whatsappimagecopy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class Worker extends Thread {
    ArrayList<Image> images;
    String outputPath;
    ProgressDialog progressDialog;
    Handler handler;
    Activity activity;
    boolean moveFile;
    public Worker(Activity activity,ArrayList<Image> images, String outputPath, ProgressDialog progressDialog, Handler handler,boolean moveFile)
    {
        this.images= new ArrayList<Image>(images);
        this.outputPath=outputPath;
        this.handler=handler;
        this.progressDialog=progressDialog;
        this.activity=activity;
        this.moveFile=moveFile;
    }

    public void run(){
        int count=0;
        System.out.println("thread is running...");
        System.out.println(images.size());
        for (Image x : images) {
            count++;
            final int temp=count;

                File inputFile= new File(x.getPath());
                File outputDir=new File(outputPath);

            try {
                moveFile(inputFile,outputDir);
            } catch (IOException e) {
                e.printStackTrace();
            }



            //Update the value background thread to UI thread
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(temp==images.size()) {
                        progressDialog.dismiss();
                    }
                    else
                    progressDialog.setProgress(temp*100/images.size());

                }
            });
        }

        PrefFragment.images.clear();// clear the images array after copying
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(outputPath))));

        PrefFragment.outputPath="";

        System.out.println("thread run method has ended...");
    }

    private void moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        if(newFile.exists()) {
            System.out.println("File exists, skipping "+file.getName());
            return;
        }

        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            if(moveFile)
                file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }
}
