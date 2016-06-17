package com.swastik.wardrobeapp.modules.wardrobe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.modules.MainActivity;
import com.swastik.wardrobeapp.modules.wardrobe.adapters.ImageSlideAdapter;
import com.swastik.wardrobeapp.utils.CommonUtil;
import com.swastik.wardrobeapp.utils.MySQLiteHelper;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by Swastik on 15-06-2016.
 */
public class Wardrobe
{
    MainActivity objMainActivity;
    View.OnClickListener click_listener;
    ViewPager.OnPageChangeListener pageChangeListener;

    public Wardrobe(WeakReference<MainActivity> _MainActivityWeakReference)
    {
        objMainActivity = _MainActivityWeakReference.get();
        init();
    }

    public void init()
    {
        objMainActivity.loadScreen(R.layout.screen_wardrobe);
        initializeClickListener();
        initializePageChangeListener();
        initializeImageAdapters();

        //set click listeners
        int ids[] = { R.id.btnShuffle,R.id.btnAddBottom,R.id.btnAddTop,R.id.btnFav};
        for(int id:ids)
            objMainActivity.findViewById(id).setOnClickListener(click_listener);

    }

    private void initializePageChangeListener() {
        pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
                ImageSlideAdapter mImageSlideAdapterTop = (ImageSlideAdapter)mViewPagerTop.getAdapter();

                ViewPager mViewPagerBottom = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
                ImageSlideAdapter mImageSlideAdapterBottom = (ImageSlideAdapter)mViewPagerBottom.getAdapter();

                String topId =mImageSlideAdapterTop.getPicId(mViewPagerTop.getCurrentItem());
                String bottomId = mImageSlideAdapterBottom.getPicId(mViewPagerBottom.getCurrentItem());

                if(isFav(topId,bottomId))
                    objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_checked);
                else
                    objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_unchecked);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void initializeImageAdapters() {

        String pickOfTheDay = MySQLiteHelper.getPickOfTheDay(objMainActivity);

        ImageSlideAdapter mImageSlideAdapterTop = new ImageSlideAdapter(objMainActivity, CommonUtil.TOP);
        ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
        mViewPagerTop.setAdapter(mImageSlideAdapterTop);
        mViewPagerTop.addOnPageChangeListener(pageChangeListener);

        ImageSlideAdapter mImageSlideAdapterBottom = new ImageSlideAdapter(objMainActivity, CommonUtil.BOTTOM);
        ViewPager mViewPagerBottom = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
        mViewPagerBottom.setAdapter(mImageSlideAdapterBottom);
        mViewPagerBottom.addOnPageChangeListener(pageChangeListener);

        if(pickOfTheDay.length()>2)
        {
            int top_pos = mImageSlideAdapterTop.getPositionById(pickOfTheDay.split("_")[0]);
            int bottom_pos = mImageSlideAdapterBottom.getPositionById(pickOfTheDay.split("_")[1]);
            if(top_pos>=0 && bottom_pos>=0)
            {
                mViewPagerTop.setCurrentItem(top_pos);
                mViewPagerBottom.setCurrentItem(bottom_pos);
            }
        }

        String topId =mImageSlideAdapterTop.getPicId(mViewPagerTop.getCurrentItem());
        String bottomId = mImageSlideAdapterBottom.getPicId(mViewPagerBottom.getCurrentItem());
        if(isFav(topId,bottomId))
            objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_checked);
        else
            objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_unchecked);
    }

    private void initializeClickListener() {

        click_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btnShuffle:
                        shuffleCombo(v);
                        break;
                    case R.id.btnFav:
                        toggleFav(v);
                        break;
                    case R.id.btnAddTop:
                        addTop();
                        break;
                    case R.id.btnAddBottom:
                        addBottom();
                        break;
                }
            }
        };
    }

    private void toggleFav(View btnFav) {
        if(CommonUtil.isAnimating)
            return;

        CommonUtil.isAnimating = true;
        btnFav.animate()
                .setDuration(207)
                .scaleX(1.2f)
                .scaleY(1.2f);

        ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
        ImageSlideAdapter mImageSlideAdapterTop = (ImageSlideAdapter)mViewPagerTop.getAdapter();

        ViewPager mViewPagerBottom = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
        ImageSlideAdapter mImageSlideAdapterBottom = (ImageSlideAdapter)mViewPagerBottom.getAdapter();

        String topId =mImageSlideAdapterTop.getPicId(mViewPagerTop.getCurrentItem());
        String bottomId = mImageSlideAdapterBottom.getPicId(mViewPagerBottom.getCurrentItem());

        if(isFav(topId,bottomId))
        {
            deleteFav(topId, bottomId);
            objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_unchecked);
        }
        else
        {
            setAsFav(topId, bottomId);
            objMainActivity.findViewById(R.id.btnFav).setBackgroundResource(R.drawable.btn_fav_checked);
        }

        new CountDownTimer(209, 209) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                CommonUtil.isAnimating = false;
                View btnFav = null;
                if(objMainActivity!=null)
                    btnFav = objMainActivity.findViewById(R.id.btnFav);
                if(btnFav!=null)
                btnFav.animate().setInterpolator(new LinearInterpolator())
                        .setDuration(207)
                        .scaleX(1)
                        .scaleY(1);
            }
        }.start();
    }

    private void deleteFav(String topId, String bottomId) {
        MySQLiteHelper.deleteRowFromFavoritesTable(objMainActivity, topId, bottomId);
    }

    private boolean isFav(String topId, String bottomId) {
        return MySQLiteHelper.checkForEntryInFavoritesTable(objMainActivity,topId,bottomId);
    }

    private void setAsFav(String topId,String bottomId)
    {
        MySQLiteHelper.insertRowToFavoritesTable(objMainActivity,topId,bottomId);
    }

    private void shuffleCombo(View btnShuffle) {
        if(CommonUtil.isAnimating)
            return;
        CommonUtil.isAnimating=true;

        btnShuffle.setRotation(0);
        btnShuffle.animate().setDuration(1080)
                .setInterpolator(new DecelerateInterpolator());
        btnShuffle.animate().rotation(720f);


        ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
        mViewPagerTop.setRotationY(0);
        mViewPagerTop.animate().setDuration(1080).setInterpolator(new AccelerateInterpolator());
        mViewPagerTop.animate().rotationY(720f).alpha(0);

        ViewPager mViewPagerBottom = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
        mViewPagerBottom.setRotationY(0);
        mViewPagerBottom.animate().setDuration(1080).setInterpolator(new AccelerateInterpolator());
        mViewPagerBottom.animate().rotationY(-720f).alpha(0);


        new CountDownTimer(1090, 1090) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewPager mViewPagerTop = (ViewPager) objMainActivity.findViewById(R.id.vpTop);
                if (mViewPagerTop != null) {
                    mViewPagerTop.setRotationY(0);
                    Random rand = new Random();
                    ImageSlideAdapter mImageSlideAdapterTop = (ImageSlideAdapter) mViewPagerTop.getAdapter();
                    int randomItem = rand.nextInt(mImageSlideAdapterTop.getCount());
                    mViewPagerTop.setCurrentItem(randomItem, false);
                    mViewPagerTop.setAlpha(0);
                    mViewPagerTop.animate().setDuration(207).alpha(1).rotationY(360);

                }

                ViewPager mViewPagerBottom = (ViewPager) objMainActivity.findViewById(R.id.vpBottom);
                if (mViewPagerBottom != null) {
                    mViewPagerBottom.setRotationY(0);
                    Random rand = new Random();
                    ImageSlideAdapter mImageSlideAdapterBottom = (ImageSlideAdapter) mViewPagerBottom.getAdapter();
                    int randomItem = rand.nextInt(mImageSlideAdapterBottom.getCount());
                    mViewPagerBottom.setCurrentItem(randomItem, false);
                    mViewPagerBottom.setAlpha(0);
                    mViewPagerBottom.animate().setDuration(207).alpha(1).rotationY(-360);
                }
            }
        }.start();
        new CountDownTimer(1090, 1090) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                CommonUtil.isAnimating=false;
            }
        }.start();

    }

    private void addBottom() {
        showImagePickerDialog(CommonUtil.BOTTOM);
    }

    private void addTop() {
        showImagePickerDialog(CommonUtil.TOP);
    }

    /*
     * Capturing Camera Image will launch camera app request image capture
     */
    public void captureImage() {
        // Checking camera availability
        if (!CommonUtil.isDeviceSupportCamera(objMainActivity)) {
            Toast.makeText(objMainActivity,
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = setUpPhotoFile();
            objMainActivity.mCurrentPhotoPath = f.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            objMainActivity.startActivityForResult(intent, CommonUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            objMainActivity.mCurrentPhotoPath = null;
            CommonUtil.showAlertMessage("Oops..",
                    "Could not create file to save pic data",objMainActivity);
        }

    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File (
                    Environment.getExternalStorageDirectory()
                            + CommonUtil.CAMERA_DIR
                            + "wardrobe"
            );

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "awesome_i_am";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        objMainActivity.mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void pickImageFromGallery()
    {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        objMainActivity.startActivityForResult(i, CommonUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
    }


    public void showImagePickerDialog(final int fileType) {

        objMainActivity.picType = fileType;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(objMainActivity);

        alertDialogBuilder.setTitle("Select source");

        alertDialogBuilder.setMessage("Select image source").setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        captureImage();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        pickImageFromGallery();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
