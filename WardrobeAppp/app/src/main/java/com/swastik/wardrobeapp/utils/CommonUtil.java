package com.swastik.wardrobeapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;

/**
 * Created by Swastik on 15-06-2016.
 */
public class CommonUtil
{
    public static final String CAMERA_DIR = "/dcim/";
    public final static int TOP = 900;
    public final static int BOTTOM = 910;
    public final static String FIRST_RUN_FLAG = "first_run";
    public static final int CROP_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 110;
    public static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 120;

    public static boolean isAnimating;

    public static void showAlertMessage(String title, String message,
                                        Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /**
     * Checking device has camera hardware or not
     * */
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }



    public static int ConvertToPx(Context c,int dip)
    {
        Resources r = c.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    public static Point getWindowSize(Activity callingActivity)
    {
        Point windowSize = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            callingActivity.getWindowManager().getDefaultDisplay().getSize(windowSize);
        }
        else
        {
            windowSize.x = callingActivity.getWindowManager().getDefaultDisplay().getWidth();
            windowSize.y = callingActivity.getWindowManager().getDefaultDisplay().getHeight();
        }
        return windowSize;
    }

    public static int getScreenWidth(Activity callingActivity)
    {
        return getWindowSize(callingActivity).x;
    }

    public static int getScreenHeight(Activity callingActivity)
    {
        return getWindowSize(callingActivity).y;
    }

    //Convert Base64-String to Bitmap
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String encodeBitmapToBase64(Bitmap bmp)
    {
        byte[] image_base64;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 99, baos);
        image_base64 = baos.toByteArray();
        return Base64.encodeToString(image_base64, Base64.DEFAULT);
    }


    public static void clearFirstRunFlag(final SharedPreferenceManager preferenceManager) {
        new CountDownTimer(999, 999) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                preferenceManager.saveKeyData(CommonUtil.FIRST_RUN_FLAG,"");
            }
        }.start();
    }
}
