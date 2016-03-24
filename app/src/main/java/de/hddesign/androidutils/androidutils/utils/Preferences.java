package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hddesign.androidutils.androidutils.model.MainItem;

public class Preferences {

    private static final String MAIN_ITEMS = "MAIN_ITEMS";
    private static final String PALETTE_QUERY = "PALETTE_QUERY";

    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public ArrayList<MainItem> getMainItems() {
        ArrayList<MainItem> mainItems;

        Type listType = new TypeToken<ArrayList<MainItem>>() {
        }.getType();
        mainItems = new Gson().fromJson(sharedPreferences.getString(MAIN_ITEMS, ""), listType);

        return mainItems;
    }

    public void setMainItems(ArrayList<MainItem> mainItems) {
        sharedPreferences.edit().putString(MAIN_ITEMS, new Gson().toJson(mainItems)).apply();
    }

    public void setPaletteQuery(Set<String> paletteQuery) {
        sharedPreferences.edit().putStringSet(PALETTE_QUERY, paletteQuery).apply();
    }

    public Set<String> getPaletteQuery() {
        Set<String> defaultSet = new HashSet<>();
        defaultSet.add("cat");

        return sharedPreferences.getStringSet(PALETTE_QUERY, defaultSet);
    }
}
