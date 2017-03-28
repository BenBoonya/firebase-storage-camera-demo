package com.boonya.ben.firebasecamerademo.intent;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.boonya.ben.firebasecamerademo.Const;
import com.boonya.ben.firebasecamerademo.R;
import com.boonya.ben.firebasecamerademo.helper.AlertDialogHelper;

import junit.framework.Assert;

import java.io.File;

/**
 * CameraIntent
 * on 5/16/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public class CameraIntent extends BaseIntent {

    private Context mContext;
    private int REQUEST_CODE = Const.Intent.REQUEST_CAMERA;


    public CameraIntent(Context context, String filePath) {
        this(context, new File(Environment.getExternalStorageDirectory(), filePath));
    }

    public CameraIntent(Context context, File file) {
        this(context, Uri.fromFile(file));
    }

    public CameraIntent(Context context, Uri uri) {
        super(MediaStore.ACTION_IMAGE_CAPTURE);
        Assert.assertNotNull(context);
        Assert.assertNotNull(uri);
        putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.mContext = context;
    }

    @Override
    public void startActivity(Fragment fragment) {
        if (IntentHelper.resolveActivity(mContext, this)) {
            if (fragment != null) {
                fragment.startActivityForResult(this, REQUEST_CODE);
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
            if (activity != null) {
                activity.startActivityForResult(this, REQUEST_CODE);
            }
        } else {
            AlertDialogHelper.createErrorDialog(mContext,
                    mContext.getString(R.string.label_error_no_supported_action))
                    .show();
        }
    }

    public Uri getUri() {
        return this.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
    }
}
