package com.boonya.ben.firebasecamerademo.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;

import com.boonya.ben.firebasecamerademo.helper.BitmapHelper;

import junit.framework.Assert;

/**
 * BaseIntent
 * on 4/19/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public abstract class BaseIntent extends Intent {

    protected BaseIntent() {
        super();
    }

    protected BaseIntent(String action) {
        super(action);
    }

    protected BaseIntent(String action, Uri uri) {
        super(action, uri);
    }

    protected BaseIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public void startActivityWithThumbnailScaleUp(Fragment fragment,
                                                  View source){
        startActivityWithThumbnailScaleUp(fragment,
                source, BitmapHelper.loadBitmapFromView(source));
    }

    public void startActivityWithThumbnailScaleUp(Activity activity,
                                                  View source){
        startActivityWithThumbnailScaleUp(activity,
                source, BitmapHelper.loadBitmapFromView(source));
    }

    public void startActivityForResultWithThumbnailScaleUp(Fragment fragment,
                                                              View source, int requestCode){
        startActivityForResultWithThumbnailScaleUp(fragment,
                source, BitmapHelper.loadBitmapFromView(source), requestCode);
    }

    public void startActivityForResultWithThumbnailScaleUp(Activity activity,
                                                              View source, int requestCode){
        startActivityForResultWithThumbnailScaleUp(activity,
                source, BitmapHelper.loadBitmapFromView( source), requestCode);
    }


    public void startActivityWithThumbnailScaleUp(Fragment fragment,
                                                     View source,
                                                     Bitmap thumbnail) {
        if(fragment != null){
            if(source != null && thumbnail != null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fragment.startActivity(this ,
                            ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, 0, 0)
                                    .toBundle());
                } else {
                    fragment.startActivity(this);
                }
            } else {
                fragment.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityWithThumbnailScaleUp(Activity activity,
                                                     View source,
                                                     Bitmap thumbnail){
        if(activity != null){
            if(source != null && thumbnail != null){
                ActivityCompat.startActivity(activity, this,
                        ActivityOptionsCompat
                                .makeThumbnailScaleUpAnimation(source, thumbnail, 0, 0)
                                .toBundle());
            } else {
                activity.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityForResultWithThumbnailScaleUp(Fragment fragment,
                                                              View source,
                                                              Bitmap thumbnail,
                                                              int requestCode) {

           if(fragment != null){
               if(source != null && thumbnail != null){
                   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                       fragment.startActivityForResult(this , requestCode,
                               ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, 0, 0)
                                       .toBundle());
                   } else {
                       fragment.startActivityForResult(this, requestCode);
                   }
               } else {
                   fragment.startActivityForResult(this, requestCode);
               }
           } else {
               Assert.fail();
           }
    }


    public void startActivityForResultWithThumbnailScaleUp(Activity activity,
                                                              View source,
                                                              Bitmap thumbnail,
                                                              int requestCode){
        if(activity != null){
            if(source != null && thumbnail != null){
                ActivityCompat.startActivityForResult(activity, this, requestCode,
                        ActivityOptionsCompat
                                .makeThumbnailScaleUpAnimation(source, thumbnail, 0, 0)
                                .toBundle());
            } else {
                activity.startActivityForResult(this, requestCode);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityWithSceneTransition(Activity activity,
                                                    Pair<View, String>... pairs){
        if(activity != null){
            if(pairs != null ){
                ActivityCompat.startActivity(activity, this,
                        ActivityOptionsCompat
                                .makeSceneTransitionAnimation(activity, pairs)
                                .toBundle());
            } else {
                activity.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityWithSceneTransition(Fragment fragment,
                                                    Pair<View, String>... pairs){
        if(fragment != null){
            if(pairs != null ){
                fragment.startActivity(this, ActivityOptionsCompat
                        .makeSceneTransitionAnimation(fragment.getActivity(), pairs)
                        .toBundle());
            } else {
                fragment.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityForResultWithSceneTransition(Activity activity,
                                                             int requestCode,
                                                             Pair<View, String>... pairs){
        if(activity != null){
            if(pairs != null ){
                ActivityCompat.startActivityForResult(activity, this, requestCode,
                        ActivityOptionsCompat
                                .makeSceneTransitionAnimation(activity, pairs)
                                .toBundle());
            } else {
                activity.startActivityForResult(this, requestCode);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityForResultWithSceneTransition(Fragment fragment,
                                                             int requestCode,
                                                             Pair<View, String>... pairs){
        if(fragment != null){
            if(pairs != null ){
                fragment.startActivityForResult(this, requestCode, ActivityOptionsCompat
                        .makeSceneTransitionAnimation(fragment.getActivity(), pairs)
                        .toBundle());
            } else {
                fragment.startActivityForResult(this, requestCode);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityWithSceneTransition(Activity activity,
                                                    View shareElementView,
                                                    String shareElementName){
        if(activity != null){
            if(shareElementView != null && !TextUtils.isEmpty(shareElementName)){
                ActivityCompat.startActivity(activity, this,
                        ActivityOptionsCompat
                                .makeSceneTransitionAnimation(activity, shareElementView, shareElementName)
                                .toBundle());
            } else {
                activity.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }


    public void startActivityWithSceneTransition(Fragment fragment,
                                                    View shareElementView,
                                                    String shareElementName){
        if(fragment != null){
            if(shareElementView != null && !TextUtils.isEmpty(shareElementName) ){
                fragment.startActivity(this, ActivityOptionsCompat
                        .makeSceneTransitionAnimation(fragment.getActivity(), shareElementView, shareElementName)
                        .toBundle());
            } else {
                fragment.startActivity(this);
            }
        } else {
            Assert.fail();
        }
    }


    public void startActivityForResultWithSceneTransition(Activity activity,
                                                             View shareElementView,
                                                             String shareElementName,
                                                             int requestCode){
        if(activity != null){
            if(shareElementView != null && !TextUtils.isEmpty(shareElementName)){
                ActivityCompat.startActivityForResult(activity, this, requestCode,
                        ActivityOptionsCompat
                                .makeSceneTransitionAnimation(activity, shareElementView, shareElementName)
                                .toBundle());
            } else {
                activity.startActivityForResult(this, requestCode);
            }
        } else {
            Assert.fail();
        }
    }

    public void startActivityForResultWithSceneTransition(Fragment fragment,
                                                             View shareElementView,
                                                             String shareElementName,
                                                             int requestCode){
        if(fragment != null){
            if(shareElementView != null && !TextUtils.isEmpty(shareElementName) ){
                fragment.startActivityForResult(this, requestCode, ActivityOptionsCompat
                        .makeSceneTransitionAnimation(fragment.getActivity(), shareElementView, shareElementName)
                        .toBundle());
            } else {
                fragment.startActivityForResult(this, requestCode);
            }
        } else {
            Assert.fail();
        }
    }

    public abstract void startActivity(Fragment fragment);
    public abstract void startActivity(Activity activity);
}
