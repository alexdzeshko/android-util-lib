package com.sickfutre.android.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alex Dzeshko on 02-Feb-16.
 */
public class Countries {
    public static List<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        List<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        return countries;
    }
}
