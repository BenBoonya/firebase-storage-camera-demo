package com.boonya.ben.firebasecamerademo.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.boonya.ben.firebasecamerademo.BuildConfig;
import com.boonya.ben.firebasecamerademo.Const;
import com.boonya.ben.firebasecamerademo.R;
import com.boonya.ben.firebasecamerademo.intent.CameraIntent;
import com.boonya.ben.firebasecamerademo.intent.GalleryIntent;
import com.yalantis.ucrop.UCrop;

import junit.framework.Assert;

import java.io.File;

/**
 * Created by jutikorn on 9/28/2016 AD.
 */
public class PhotoSelectionHelper {

    private static final String STATE_SELECTED_PHOTO_URI = "SelectedPhoto";
    private static final String STATE_CROP_PHOTO = "CroppedPhoto";

    private Uri mSelectedPhotoImageUri;
    private Uri mCapturedPhotoImageUri;
    private Uri mCroppedUri;
    private String mCapturedPhotoPath;
    private Context mContext;
    private Fragment mFragment;
    private CallBack mCallBack;

    public PhotoSelectionHelper(Fragment fragment) {
        this(fragment, null);
    }

    public PhotoSelectionHelper(Context context) {
        this(context, null);
    }

    public PhotoSelectionHelper(Fragment fragment, CallBack callback) {
        this(fragment.getContext(), callback);
        this.mFragment = fragment;
    }

    public PhotoSelectionHelper(Context context, CallBack callback) {
        this.mContext = context;
        this.mCallBack = callback;
        this.mCapturedPhotoPath = "Uhack/food_" + BuildConfig.APPLICATION_ID + ".jpg";
        this.mCapturedPhotoImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mCapturedPhotoPath));
    }

    public void setCallback(CallBack callback) {
        this.mCallBack = callback;
    }

    public void selectPhoto() {
        AlertDialogHelper.createListItemDialog(
                getContext(),
                "Select image from",
                R.array.action_select_image_action, new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                onPhotoFromCameraSelected();
                                break;
                            case 1:
                                onPhotoFromGallerySelected();
                                break;
                        }
                    }
                }).show();
    }

    private void onPhotoFromGallerySelected() {
        GalleryIntent intent = new GalleryIntent(getContext());
        if (mFragment != null) {
            intent.startActivity(mFragment);
        } else {
            intent.startActivity((Activity) getContext());
        }
    }

    private void onPhotoFromCameraSelected() {
        CameraIntent intent = new CameraIntent(getContext(), getCapturePhotoPathUri());
        if (mFragment != null) {
            intent.startActivity(mFragment);
        } else {
            intent.startActivity((Activity) getContext());
        }
    }

    private void crop() {
        File file = new File(getSelectedPhotoPathUri().getPath());
        if (file.exists()) {
            UCrop ucrop = UCrop.of(getSelectedPhotoPathUri(),
                    Uri.fromFile(new File(getContext().getCacheDir(), getCroppedFileName())))
                    .withMaxResultSize(
                            getContext().getResources().getInteger(R.integer.image_size_big),
                            getContext().getResources().getInteger(R.integer.image_size_big));
            if (mFragment != null) {
                ucrop.start(getContext(), mFragment);
            } else {
                ucrop.start((Activity) getContext());
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    mCroppedUri = UCrop.getOutput(data);
                    if (mCallBack != null) {
                        mCallBack.onCroppedSuccess(mCroppedUri);
                    }
                    return;
                case Const.Intent.REQUEST_GALLERY:
                    mSelectedPhotoImageUri = FileHelper.getImageFileUri(getContext(), data);
                    break;
                case Const.Intent.REQUEST_CAMERA:
                    mSelectedPhotoImageUri = getCapturePhotoPathUri();
                    break;
                default:
                    Assert.fail();
                    break;
            }

            crop();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(STATE_CROP_PHOTO, mCroppedUri);
            outState.putParcelable(STATE_SELECTED_PHOTO_URI, mSelectedPhotoImageUri);
        }
    }

    public void onRestoreView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCroppedUri = savedInstanceState.getParcelable(STATE_CROP_PHOTO);
            mSelectedPhotoImageUri = savedInstanceState.getParcelable(STATE_SELECTED_PHOTO_URI);
        }

        if (mCroppedUri != null && mCallBack != null) {
            mCallBack.onCroppedSuccess(mCroppedUri);
        }
    }

    public Uri getCroppedPhotoUri() {
        return mCroppedUri;
    }

    public Uri getCapturePhotoPathUri() {
        return mCapturedPhotoImageUri;
    }

    public Uri getSelectedPhotoPathUri() {
        return mSelectedPhotoImageUri;
    }

    public String getRawPhotoPath() {
        return mCapturedPhotoPath;
    }

    public String getCroppedFileName() {
        return String.valueOf(System.currentTimeMillis()) + "_cropped.jpg";
    }

    private Context getContext() {
        return mContext;
    }

    public interface CallBack {
        void onCroppedSuccess(Uri croppedUri);
    }
}
