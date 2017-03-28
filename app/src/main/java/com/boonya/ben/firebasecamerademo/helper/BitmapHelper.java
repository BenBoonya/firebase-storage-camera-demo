package com.boonya.ben.firebasecamerademo.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import junit.framework.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * BitmapHelper
 * on 4/21/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public class BitmapHelper {


    private BitmapHelper(){
        throw new IllegalAccessError("Utility class");
    }

    public static Bitmap getBitmap(ImageView imageView){
        Assert.assertNotNull(imageView);
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    public static Bitmap getBitmapFromDrawable(Context con, int resId) {
        if(con != null){
            return BitmapFactory.decodeResource(con.getResources(), resId);
        } else {
            Assert.fail();
            return null;
        }
    }

    public static Bitmap getBitmapFromAssets(Context con, String fileName) {
        if(con != null){
            AssetManager assetManager = con.getAssets();

            InputStream istr = null;
            try {
                istr = assetManager.open(fileName);
            } catch(IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(istr);

            return bitmap;
        } else {
            Assert.fail();
        }
        return null;
    }

    public static Bitmap reScaleBitmap(Bitmap bmp, int width, int height, boolean filter) {
        if(bmp != null) {
            return Bitmap.createScaledBitmap(bmp, width, height, filter);
        }else {
            Assert.fail();
            return null;
        }
    }

    public static Bitmap loadBitmapFromView(View view) {
         if(view instanceof ImageView){
            return getBitmap((ImageView) view);
         } else if(view instanceof View){
             return loadBitmapFromView(view, view.getWidth(), view.getHeight());
         } else {
             Assert.fail();
         }
        return null;
    }

    public static Bitmap loadBitmapFromView(View view, int width, int height) {
        if(view != null &&
                width > 0 &&
                height > 0) {
            Bitmap b;
            try {
                b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }

            Canvas c = new Canvas(b);
            view.draw(c);
            return b;
        } else {
            Assert.fail();
        }

        return null;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float downScaleFactor) {

       if(bitmap != null){
           int width = (int) (bitmap.getWidth() / downScaleFactor);
           int height = (int) (bitmap.getHeight() / downScaleFactor);

           return Bitmap.createScaledBitmap(bitmap, width, height, false);
       } else {
           Assert.fail();
       }
        return null;
    }

    public static void recycle(Bitmap bitmap) {
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void recycle(ImageView imageview) {
        if(imageview != null) {
            Drawable drawable = imageview.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                recycle(bitmap);
            }
        } else {
            Assert.fail();
        }
    }

    public static Bitmap getBlurImageBitmap(Context context,
                                            View view,
                                            float downScaleFactor,
                                            int blurRadius) {

       if(view != null && context != null){
           return getBlurImageBitmap(context, loadBitmapFromView(view), downScaleFactor, blurRadius, true);
       } else {
           return null;
       }
    }

    public static Bitmap getBlurImageBitmap(Context context,
                                            Bitmap bitmap,
                                            float downScaleFactor,
                                            int blurRadius,
                                            boolean reusable) {
        if(context != null && bitmap != null){
            return fastBlur(context, scaleBitmap(bitmap, downScaleFactor), blurRadius, reusable);
        } else {
            Assert.fail();
            return null;
        }
    }

    public static Bitmap getCircularBitmapImage(Bitmap source) {

       if(source != null){
           int size = Math.min(source.getWidth(), source.getHeight());
           int x = (source.getWidth() - size) / 2;
           int y = (source.getHeight() - size) / 2;
           Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
           if (squaredBitmap != source) {
               source.recycle();
           }
           Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
           Canvas canvas = new Canvas(bitmap);
           Paint paint = new Paint();
           BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
           paint.setShader(shader);
           paint.setAntiAlias(true);
           float r = size / 2f;
           canvas.drawCircle(r, r, r, paint);
           squaredBitmap.recycle();
           return bitmap;
       } else {
           Assert.fail();
           return null;
       }
    }


    public static Bitmap fastBlur(Context context, Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        if(context != null && sentBitmap != null){
            Bitmap u84Bitmap;
            if(sentBitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                u84Bitmap = sentBitmap;
            } else {
                u84Bitmap = sentBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            if(Build.VERSION.SDK_INT > 16) {
                Bitmap bitmap = Bitmap.createBitmap(u84Bitmap.getWidth(), u84Bitmap.getHeight(), u84Bitmap.getConfig());

                final RenderScript rs = RenderScript.create(context);
                final Allocation input = Allocation.createFromBitmap(rs,
                        u84Bitmap,
                        Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);

                final Allocation output = Allocation.createTyped(rs, input.getType());
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, output.getElement() /*Element.U8_4(rs)*/);
                script.setRadius(radius /* e.g. 3.f */);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap);
                rs.destroy();
                return bitmap;
            } else {
                // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
                Bitmap bitmap;
                if(canReuseInBitmap) {
                    bitmap = u84Bitmap;
                } else {
                    bitmap = u84Bitmap.copy(u84Bitmap.getConfig(), true);
                }

                if(radius < 1) {
                    return (null);
                }

                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int[] pix = new int[w * h];
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);

                int wm = w - 1;
                int hm = h - 1;
                int wh = w * h;
                int div = radius + radius + 1;

                int r[] = new int[wh];
                int g[] = new int[wh];
                int b[] = new int[wh];
                int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
                int vmin[] = new int[Math.max(w, h)];

                int divsum = (div + 1) >> 1;
                divsum *= divsum;
                int dv[] = new int[256 * divsum];
                for(i = 0; i < 256 * divsum; i++) {
                    dv[i] = (i / divsum);
                }

                yw = yi = 0;

                int[][] stack = new int[div][3];
                int stackpointer;
                int stackstart;
                int[] sir;
                int rbs;
                int r1 = radius + 1;
                int routsum, goutsum, boutsum;
                int rinsum, ginsum, binsum;

                for(y = 0; y < h; y++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    for(i = -radius; i <= radius; i++) {
                        p = pix[yi + Math.min(wm, Math.max(i, 0))];
                        sir = stack[i + radius];
                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);
                        rbs = r1 - Math.abs(i);
                        rsum += sir[0] * rbs;
                        gsum += sir[1] * rbs;
                        bsum += sir[2] * rbs;
                        if(i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                    }
                    stackpointer = radius;

                    for(x = 0; x < w; x++) {

                        r[yi] = dv[rsum];
                        g[yi] = dv[gsum];
                        b[yi] = dv[bsum];

                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;

                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];

                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];

                        if(y == 0) {
                            vmin[x] = Math.min(x + radius + 1, wm);
                        }
                        p = pix[yw + vmin[x]];

                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);

                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];

                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;

                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[(stackpointer) % div];

                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];

                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];

                        yi++;
                    }
                    yw += w;
                }
                for(x = 0; x < w; x++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    yp = -radius * w;
                    for(i = -radius; i <= radius; i++) {
                        yi = Math.max(0, yp) + x;

                        sir = stack[i + radius];

                        sir[0] = r[yi];
                        sir[1] = g[yi];
                        sir[2] = b[yi];

                        rbs = r1 - Math.abs(i);

                        rsum += r[yi] * rbs;
                        gsum += g[yi] * rbs;
                        bsum += b[yi] * rbs;

                        if(i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }

                        if(i < hm) {
                            yp += w;
                        }
                    }
                    yi = x;
                    stackpointer = radius;
                    for(y = 0; y < h; y++) {
                        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;

                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];

                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];

                        if(x == 0) {
                            vmin[y] = Math.min(y + r1, hm) * w;
                        }
                        p = x + vmin[y];

                        sir[0] = r[p];
                        sir[1] = g[p];
                        sir[2] = b[p];

                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];

                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;

                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer];

                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];

                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];

                        yi += w;
                    }
                }

                bitmap.setPixels(pix, 0, w, 0, 0, w, h);
                return (bitmap);
            }
        } else {
            Assert.fail();
            return null;
        }
    }

}
