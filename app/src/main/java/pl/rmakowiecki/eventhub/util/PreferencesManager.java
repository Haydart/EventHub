package pl.rmakowiecki.eventhub.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

import static android.content.Context.MODE_PRIVATE;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_CATEGORIES_KEY;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_CATEGORY_IMAGE_KEY;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_INTERESTS_KEY;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_KEY;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_PREFERENCE_LOCALE_KEY;
import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_SUBCATEGORIES_KEY;

public class PreferencesManager {

    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
    }

    public Set<String> getCategories() {
        return sharedPreferences.getStringSet(SHARED_PREFERENCES_CATEGORIES_KEY, new HashSet<>());
    }

    public Set<String> getSubCategories(String categoryName) {
        return sharedPreferences.getStringSet(SHARED_PREFERENCES_SUBCATEGORIES_KEY + categoryName.toUpperCase(), new HashSet<>());
    }

    public Set<String> getInterests(String categoryName) {
        return sharedPreferences.getStringSet(SHARED_PREFERENCES_INTERESTS_KEY + categoryName.toUpperCase(), new HashSet<>());
    }

    public String getImageResourceName(String categoryName) {
        return sharedPreferences.getString(SHARED_PREFERENCES_CATEGORY_IMAGE_KEY + categoryName.toUpperCase(), "");
    }

    public List<PreferenceCategory> getPreferenceCategoryList() {
        List<PreferenceCategory> preferenceCategories = new ArrayList<>();
        Set<String> categories = getCategories();
        for (String categoryName : categories) {
            String imageResourceName = getImageResourceName(categoryName);
            List<String> subCategories = new ArrayList<>();
            subCategories.addAll(getSubCategories(categoryName));
            preferenceCategories.add(new PreferenceCategory(categoryName, imageResourceName, subCategories));
        }

        return preferenceCategories;
    }

    public void saveCategories(List<Preference> preferenceList) {
        Set<String> categories = new HashSet<>();
        for (Preference preference : preferenceList) {
            categories.add(preference.getName());
            Set<String> subCategories = new HashSet<>();
            subCategories.addAll(preference.getSubCategories());
            sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_SUBCATEGORIES_KEY + preference.getName().toUpperCase(), subCategories).commit();
            sharedPreferences.edit().putString(SHARED_PREFERENCES_CATEGORY_IMAGE_KEY + preference.getName().toUpperCase(), preference.getImageUrl()).commit();
        }

        sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_CATEGORIES_KEY, categories).commit();
    }

    public void saveInterests(List<Interest> interests) {
        if (!interests.isEmpty()) {
            for (Interest interest : interests) {
                Set<String> subCategories = new HashSet<>();
                subCategories.addAll(interest.getSubCategories());
                sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_INTERESTS_KEY + interest.getName().toUpperCase(), subCategories).commit();
            }
        }
    }

    public void saveSubCategories(String categoryName, Set<String> subCategories) {
        sharedPreferences.edit().remove(SHARED_PREFERENCES_INTERESTS_KEY + categoryName.toUpperCase());
        sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_INTERESTS_KEY + categoryName.toUpperCase(), subCategories).commit();
    }

    public void saveLocales(List<PreferenceLocale> localesList) {
        for (PreferenceLocale locale : localesList) {
            String categoryName = locale.getName();
            String localeName = locale.getPreferenceLocaleString();
            Map<String, Object> map = locale.getLocaleNamesMap();
            if (!map.isEmpty()) {
                for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                    sharedPreferences.edit().putString(SHARED_PREFERENCES_PREFERENCE_LOCALE_KEY +
                            localeName.toUpperCase() + categoryName.toUpperCase() + mapEntry.getKey().toUpperCase(), mapEntry.getValue().toString()).commit();
                }
            }
        }
    }

    public List<PreferenceCategory> getInterestsDisplayList() {
        List<PreferenceCategory> categoryList = getPreferenceCategoryList();
        List<PreferenceCategory> displayList = new ArrayList<>();

        for (PreferenceCategory category : categoryList) {
            String categoryName = category.getTitle();
            Set<String> interests = getInterests(categoryName);

            if (!interests.isEmpty()) {
                LocaleUtils utils = new LocaleUtils();
                if (utils.hasLocale()) {
                    categoryName = getNameOrLocaleName(utils.getLocaleString(), category.getTitle(), category.getTitle());
                    List<String> translatedSubcategories = new ArrayList<>();
                    for (String subCategory : interests) {
                        translatedSubcategories.add(getNameOrLocaleName(utils.getLocaleString(), category.getTitle(), subCategory));
                    }
                    displayList.add(new PreferenceCategory(categoryName, "", translatedSubcategories));
                }
                else {
                    List<String> subcategories = new ArrayList<>();
                    subcategories.addAll(interests);
                    displayList.add(new PreferenceCategory(categoryName, "", subcategories));
                }
            }
        }

        return displayList;
    }

    public String getNameOrLocaleName(String localeName, String categoryName, String defaultName) {
        return sharedPreferences.getString(SHARED_PREFERENCES_PREFERENCE_LOCALE_KEY + localeName.toUpperCase() + categoryName.toUpperCase() + defaultName.toUpperCase(), defaultName);
    }
}
