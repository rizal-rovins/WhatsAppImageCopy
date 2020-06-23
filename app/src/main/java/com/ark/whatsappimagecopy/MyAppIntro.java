package com.ark.whatsappimagecopy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

import org.jetbrains.annotations.Nullable;

public class MyAppIntro extends AppIntro2 {

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setImmersiveMode();
        setImmersiveMode();
        setBackgroundResource(R.drawable.bs2);
        addSlide(AppIntroFragment.newInstance(
                "Welcome!",
                "Copy/Move WhatsApp Images to other directories easily.",
                R.drawable.appstore,R.drawable.bs1));



        addSlide(AppIntroFragment.newInstance(
                "Step 1",
                "Navigate to /WhatsApp Images/ and select images to be copied.",
                R.drawable.select_wa,R.drawable.bs2));


        addSlide(AppIntroFragment.newInstance(
                "Step 2",
                "Select the destination folder. For example /storage/DCIM/Camera/ , /storage/Pictures/ or any folder of your choice!",
                R.drawable.select_od,R.drawable.bs2));


        addSlide(AppIntroFragment.newInstance(
                "Step 3",
                "Check the box to Move and UnCheck the box to Copy images.",
                R.drawable.select_move,R.drawable.bs2));

        addSlide(AppIntroFragment.newInstance(
                "Step 4",
                "Start Copying!",
                R.drawable.select_start,R.drawable.bs2));

        setTransformer(AppIntroPageTransformerType.Zoom.INSTANCE);




    }
}
