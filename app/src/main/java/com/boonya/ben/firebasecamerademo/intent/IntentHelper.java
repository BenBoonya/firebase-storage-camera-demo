package com.boonya.ben.firebasecamerademo.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import junit.framework.Assert;

/**
 * IntentHelper
 * on 4/27/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public class IntentHelper {

    private IntentHelper(){
        throw new IllegalAccessError("Utility class");
    }

    public static boolean resolveActivity(Context context, Intent intent){
        if(context != null && intent != null){
            return intent.resolveActivity(context.getPackageManager()) != null;
        } else {
            return false;
        }
    }

    public static void startActivity(Context context, String action, String url){
        if(!TextUtils.isEmpty(url)) {
            startActivity(context, action, Uri.parse(url));
        } else {
            Assert.fail();
        }
    }

    public static void startActivity(Context context, String action, Uri uri){
        if(context != null && uri != null && !TextUtils.isEmpty(action)){
            Intent intent = new Intent();
            intent.setData(uri);
            intent.setAction(action);
            // Verify that there are applications registered to handle this intent
            if(resolveActivity(context, intent)) {
                context.startActivity(intent);
            }
        } else {
            Assert.fail();
        }

    }
}
