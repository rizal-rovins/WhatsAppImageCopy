package com.ark.whatsappimagecopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class Worker extends Thread {
    ArrayList<Image> images;
    String outputPath;
    ProgressDialog progressDialog;
    Handler handler;
    MainActivity activity;
    boolean moveFile;
    public Worker(MainActivity activity,ArrayList<Image> images, String outputPath, ProgressDialog progressDialog, Handler handler,boolean moveFile)
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
        System.out.println("Image copying thread is running...");
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
                        activity.onFinishCopy();
                    }
                    else {
                        progressDialog.setProgress(temp * 100 / images.size());

                    }

                }
            });
        }

        PrefFragment.images.clear();// clear the images array after copying
        PrefFragment.outputPath="";

        System.out.println("Image copying thread has ended...");


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


            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));
            System.out.println(" Sent intent to scan: "+ newFile.getName());


        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }
}
