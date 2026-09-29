package uz.mahmud.ruschaozbekchalugat2026.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {

    private static final String PREFS_NAME = "LanguagePrefs";
    private static final String KEY_LANGUAGE = "language";

    public static Context setLocale(Context context, String language) {

        saveLanguage(context, language);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration =
                context.getResources().getConfiguration();

        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    public static String getLanguage(Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        return preferences.getString(
                KEY_LANGUAGE,
                "uz"
        );
    }

    private static void saveLanguage(
            Context context,
            String language
    ) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        preferences.edit()
                .putString(KEY_LANGUAGE, language)
                .apply();
    }

    public static Context applyLanguage(Context context) {

        String language = getLanguage(context);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration =
                context.getResources().getConfiguration();

        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }
}