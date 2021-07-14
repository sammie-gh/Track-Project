package com.gh.sammie.trackproject;

import com.apps.norris.paywithslydepay.core.SlydepayPayment;
import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application {

    public static String MERCHANT_KEY = "1522254751831";

    // email or mobile number associated with the merchant account
    public static String EMAIL_OR_MOBILE_NUMBER = "ofori.d.evans@gmail.com";

    // callback url
    public static String CALLBACK_URL = "https://www.slydepay.com.gh/";
//    public static String CALLBACK_URL = "https://webhook.site/#!/25091d9b-4752-44f0-90bf-d6fde4012423/634cd837-794f-41d7-856f-425388cebfe7/1";
//    public static String CALLBACK_URL = "https://webhook.site/25091d9b-4752-44f0-90bf-d6fde4012423";

    @Override
    public void onCreate() {
        super.onCreate();
        new SlydepayPayment(Application.this).initCredentials(EMAIL_OR_MOBILE_NUMBER, MERCHANT_KEY);
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);

    }
}
