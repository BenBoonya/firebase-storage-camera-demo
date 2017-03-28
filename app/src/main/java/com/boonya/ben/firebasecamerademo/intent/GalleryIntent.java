package com.boonya.ben.firebasecamerademo.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.boonya.ben.firebasecamerademo.Const;
import com.boonya.ben.firebasecamerademo.R;
import com.boonya.ben.firebasecamerademo.helper.AlertDialogHelper;

import junit.framework.Assert;


/**
 * GalleryIntent
 * An encapsulated Intent for specifically open the Eatigo Consumer app playstore
 * on 5/16/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public class GalleryIntent extends BaseIntent {

    private Context mContext;
    private int REQUEST_CODE = Const.Intent.REQUEST_GALLERY;

    public GalleryIntent(Context context) {
        Assert.assertNotNull(context);
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            addCategory(Intent.CATEGORY_OPENABLE);
            setType("image/*");
            setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            setType("image/*");
            setAction(Intent.ACTION_GET_CONTENT);
        }
    }

    @Override
    public void startActivity(Fragment fragment) {
        if (IntentHelper.resolveActivity(mContext, this)) {
            if (fragment != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    fragment.startActivityForResult(this, REQUEST_CODE);
                } else {
                    fragment.startActivityForResult(Intent.createChooser(this,
                            "Select Picture"), REQUEST_CODE);
                }
            }
        } else {
            AlertDialogHelper.createErrorDialog(mContext,
                    mContext.getString(R.string.label_error_no_supported_action))
                    .show();
        }
    }

    @Override
    public void startActivity(Activity activity) {
        if (IntentHelper.resolveActivity(mContext, this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.startActivityForResult(this, REQUEST_CODE);
            } else {
                activity.startActivityForResult(Intent.createChooser(this,
                        "Select Picture"), REQUEST_CODE);
            }
        } else {
            AlertDialogHelper.createErrorDialog(mContext,
                    mContext.getString(R.string.label_error_no_supported_action))
                    .show();
        }
    }
}