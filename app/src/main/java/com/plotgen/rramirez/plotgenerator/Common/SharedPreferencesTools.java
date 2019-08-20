package com.plotgen.rramirez.plotgenerator.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.plotgen.rramirez.plotgenerator.items.item_character_list;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesTools {
    private static final String USER_SETTINGS_PREFERENCES_NAME = "UserSettings";
    private static final String ALL_ITEMS_LIST = "AllItemsList";
    private static Gson gson = new GsonBuilder().create();

    public static List<item_character_list> getOrderedItems(Context context) {
        String stringValue = getUserSettings(context).getString(ALL_ITEMS_LIST, "");
        Type collectionType = new TypeToken<List<item_character_list>>() {
        }.getType();
        List<item_character_list> result = gson.fromJson(stringValue, collectionType);
        return (result == null) ? new ArrayList<item_character_list>() : result;
    }

    public static void setOrderedItems(Context context, List<item_character_list> items) {
        String stringValue = gson.toJson(items);

        SharedPreferences sharedPreferences = getUserSettings(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALL_ITEMS_LIST, stringValue);
        editor.apply();
    }

    static SharedPreferences getUserSettings(Context context) {
        return context.getSharedPreferences(USER_SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
