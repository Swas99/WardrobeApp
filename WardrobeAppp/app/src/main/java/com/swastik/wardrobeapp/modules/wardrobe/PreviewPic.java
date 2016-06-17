package com.swastik.wardrobeapp.modules.wardrobe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.swastik.wardrobeapp.modules.MainActivity;
import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.modules.wardrobe.adapters.ImageSlideAdapter;
import com.swastik.wardrobeapp.utils.CommonUtil;
import com.swastik.wardrobeapp.utils.MySQLiteHelper;

/**
 * Created by Swastik on 17-06-2016.
 */
public class PreviewPic
{

    /*
     * Display image from a path to ImageView
     */
    public void previewCapturedImageWithUploadOption(final MainActivity objMainActivity,String path) {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btnAdd:
                        savePicToCollection(objMainActivity);
                        objMainActivity.dialog.cancel();
                        break;
                    case R.id.btnCancel:
                        objMainActivity.dialog.cancel();
                        break;
                }
            }
        };
        objMainActivity.dialog = objMainActivity.getDialog(R.layout.dialog_captured_photo);
        ImageView imgPreview = (ImageView) objMainActivity.dialog.findViewById(R.id.imgPreview);
        Point ScreenSize = CommonUtil.getWindowSize(objMainActivity);
        int height = ScreenSize.y - CommonUtil.ConvertToPx(objMainActivity,163);
        int width =ScreenSize.x - CommonUtil.ConvertToPx(objMainActivity,54);
        imgPreview.getLayoutParams().height = height;
        imgPreview.getLayoutParams().width = width;
        imgPreview.setImageBitmap(getBitmap(path,width/4,height/4));
        objMainActivity.dialog.findViewById(R.id.btnAdd).setOnClickListener(clickListener);
        objMainActivity.dialog.findViewById(R.id.btnCancel).setOnClickListener(clickListener);
        objMainActivity.dialog.show();
    }

    private void savePicToCollection(MainActivity objMainActivity) {
        try {
            Point ScreenSize = CommonUtil.getWindowSize(objMainActivity);
            int height = Math.max(ScreenSize.y, ScreenSize.x)/5;
            int width = Math.max(ScreenSize.y,ScreenSize.x)/5;

            Bitmap pic = getBitmap(objMainActivity.mCurrentPhotoPath, width, height);
            if(objMainActivity.picType == CommonUtil.BOTTOM)
            {
                MySQLiteHelper.insertRowToBottomsTable(objMainActivity, CommonUtil.encodeBitmapToBase64(pic));
                ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
                ImageSlideAdapter imageSlideAdapter = (ImageSlideAdapter)mViewPagerTop.getAdapter();
                imageSlideAdapter.loadImages();
                objMainActivity.loadWardrobeScreen();
            }
            else
            {
                MySQLiteHelper.insertRowToTopsTable(objMainActivity,CommonUtil.encodeBitmapToBase64(pic));
                ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
                ImageSlideAdapter imageSlideAdapter = (ImageSlideAdapter)mViewPagerTop.getAdapter();
                imageSlideAdapter.loadImages();
                objMainActivity.loadWardrobeScreen();
            }
        }catch (Exception e)
        {
            Toast.makeText(objMainActivity,"Oops..code phat gaya saala",Toast.LENGTH_SHORT);
        }
    }

    private Bitmap getBitmap(String path, int targetW,int targetH) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */


		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(path, bmOptions);
    }
}
