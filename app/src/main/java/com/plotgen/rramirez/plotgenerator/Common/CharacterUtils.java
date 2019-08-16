package com.plotgen.rramirez.plotgenerator.Common;

import android.app.Fragment;
import android.content.Context;

import com.plotgen.rramirez.plotgenerator.R;

public class CharacterUtils extends Fragment {

    public static String addCharNameOnChallenge(String question, Context context){
        String result = "";
        String short_name = "??";
        String gender = "";
        if (Common.currentCharacter !=null){
            short_name = Common.currentCharacter.getName().split(" ")[0];
            gender = Common.currentCharacter.getGender();
        }
        String withName = question.replace("$char_name$", short_name);
        switch (gender) {
            case "Female":
            case "Femenino":
                result = withName.replace("$gender$", context.getString(R.string.gender_female_article));
                break;
            case "Male":
            case "Masculino":
                result = withName.replace("$gender$", context.getString(R.string.gender_male_article));
                break;
            case "No-Binario":
            case "Transgenero Masculino":
            case "Transgenero Femenino":
            case "Transgender Male":
            case "Transgender Female":
            case "Transgender":
            case "Transgenero":
            case "Non-Binary":
                result = withName.replace("$gender$", context.getString(R.string.gender_nonbinary_article));
                break;
            default:
                result = withName;
        }
        return result;
    }
}
