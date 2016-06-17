package com.swastik.wardrobeapp.modules;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.modules.get_started.GetStarted;
import com.swastik.wardrobeapp.modules.notification_helper.MyReceiver;
import com.swastik.wardrobeapp.modules.wardrobe.PreviewPic;
import com.swastik.wardrobeapp.modules.wardrobe.Wardrobe;
import com.swastik.wardrobeapp.utils.CommonUtil;
import com.swastik.wardrobeapp.utils.MySQLiteHelper;
import com.swastik.wardrobeapp.utils.SharedPreferenceManager;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    public Dialog dialog;
    public int picType;
    public boolean isLandscape;
    public String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOrientation();
        if(savedInstanceState!=null)
        {
            loadWardrobeScreen();
        }
        else{
            loadGetStartedScreen();
            setRepeatingAlarm();
        }
    }

    private void setOrientation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            isLandscape = true;
        }else
            isLandscape = false;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            isLandscape = true;
//        } else // if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//         {
//            isLandscape = false;
//        }
//    }

    public void loadGetStartedScreen() {
        GetStarted objGetStarted = new GetStarted(new WeakReference<>(this));
        objGetStarted.init();
    }

    public void loadWardrobeScreen() {
        Wardrobe objWardrobe = new Wardrobe(new WeakReference<>(this));
        objWardrobe.init();
    }

    public void loadScreen(int id)
    {
//        View current_screen = findViewById(R.id.rootView);
        LayoutInflater inflater = getLayoutInflater();
        View next_screen = inflater.inflate(id,null);

//        current_screen.animate().alpha(0);
        setContentView(next_screen);
        next_screen.setAlpha(0);
        next_screen.animate().alpha(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem menuItem = menu.add(Menu.NONE, R.id.menu_clear_data, 0, "Clear my collection");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_pick_of_the_day, 0, "Load pick of the day");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                CommonUtil.showAlertMessage("Coming soon", "Coming soon!", MainActivity.this);
                return true;
            case R.id.menu_clear_data:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("Clear data?");

                alertDialogBuilder.setMessage("This will delete all data from your collection").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                MySQLiteHelper.clearAllData(getApplicationContext());
                                loadWardrobeScreen();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.menu_pick_of_the_day:
                SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                preferenceManager.saveKeyData(CommonUtil.FIRST_RUN_FLAG,"1");
                loadWardrobeScreen();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Dialog getDialog(int id)
    {
        if(dialog==null)
        {
            dialog = new AlertDialog.Builder(MainActivity.this).show();
            dialog.setCancelable(false);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(id, null, true);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(!backPressFlag)
                setBackPressFlag();
            else
            {
                finish();
            }
        }
        else
            super.onKeyDown(keyCode, event);

        return false;
    }

    boolean backPressFlag;
    public void setBackPressFlag()
    {
        try {
            backPressFlag = true;
            Toast.makeText(getApplicationContext(), "Tap again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2700, 2700) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    backPressFlag = false;
                }
            }.start();
        }
        catch (Exception ex){
            //Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("picType", picType);
        outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        picType = savedInstanceState.getInt("picType");
        mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CommonUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                handlePic();
            }
            return;
        }else
        if (requestCode == CommonUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE  && null != data) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                mCurrentPhotoPath = selectedImage.getPath();
                handlePic();
            }
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void handlePic() {
        if (mCurrentPhotoPath != null) {
            PreviewPic mPreviewPic = new PreviewPic();
            mPreviewPic.previewCapturedImageWithUploadOption(this, mCurrentPhotoPath);
        }

    }



    public void setRepeatingAlarm() {
        Calendar calendar = GregorianCalendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)>6)
            calendar.add(Calendar.HOUR,24);
        calendar.set(Calendar.HOUR_OF_DAY,6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.AM_PM,Calendar.AM);

        Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
    }
}
