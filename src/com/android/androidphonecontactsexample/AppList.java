package com.android.androidphonecontactsexample;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppList {

	String appname = "";
    String pname = "";
    String versionName = "";
    int versionCode = 0;
    Drawable icon;
    void prettyPrint() {
        Log.v(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode,"");
    }
}
