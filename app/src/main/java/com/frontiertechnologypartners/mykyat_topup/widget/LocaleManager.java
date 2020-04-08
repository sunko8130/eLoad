package com.frontiertechnologypartners.mykyat_topup.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.frontiertechnologypartners.mykyat_topup.util.PreferenceUtils;
import com.frontiertechnologypartners.mykyat_topup.util.Util;
import java.util.Locale;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.LANGUAGE_KEY;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYANMAR;

public class LocaleManager {
    private PreferenceUtils prefs;

    public LocaleManager(Context context) {
        prefs = new PreferenceUtils(context);
    }

    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    private String getLanguage() {
        return prefs.fromPreference(LANGUAGE_KEY, MYANMAR);
    }

    public void persistLanguage(String language) {
        prefs.toPreference(LANGUAGE_KEY, language);
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Util.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
}