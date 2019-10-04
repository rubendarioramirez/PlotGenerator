package com.plotgen.rramirez.plotgenerator.Common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import com.plotgen.rramirez.plotgenerator.R;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class Tutorial extends Fragment {


    public static void showFabPrompt(View view, Activity activity, String primaryText, String secondaryText, int target)
    {
        MaterialTapTargetPrompt mFabPrompt;
        mFabPrompt = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(view.findViewById(target))
                .setPrimaryText(primaryText)
                .setSecondaryText(secondaryText)
                .setBackButtonDismissEnabled(true)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .create();
        mFabPrompt.show();
    }

    public static void displayDialog(Context context, String title, String body, String positiveBTN){
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public static void checkTutorial(View view, Activity activity){
        Log.v("tutorial", "On boarding value is" + Common.onBoarding);
        if(Common.tutorialMODE) {
            switch (Common.onBoarding) {
                case 1: //First enter
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_1), activity.getString(R.string.onBoarding_1), "Got it!");
                    Common.onBoarding++;
                    break;
                case 2: //Project Detail first time
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_2), activity.getString(R.string.onBoarding_2), "Got it!");
                    Common.onBoarding++;
                    break;
                case 3: //Come back to ProjectList
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_3), activity.getString(R.string.onBoarding_3), "Got it!");
                    Common.onBoarding++;
                    break;
                case 4: //Go to Charlist
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_4), activity.getString(R.string.onBoarding_4), "Got it!");
                    Common.onBoarding++;
                    break;
                case 5: //Go to Create Character
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_5), activity.getString(R.string.onBoarding_5), "Got it!");
                    Common.onBoarding++;
                    break;
                case 6: //Come back to Charlist with a character
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_6), activity.getString(R.string.onBoarding_6), "Got it!");
                    Common.onBoarding++;
                    break;
                case 7: //Go to BIO
                    Tutorial.displayDialog(view.getContext(), activity.getString(R.string.onBoardingTitle_7), activity.getString(R.string.onBoarding_7), "Got it!");
                    Common.onBoarding++;
                    break;
            }
        }
    }
}



