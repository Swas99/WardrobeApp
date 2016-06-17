package com.swastik.wardrobeapp.modules.wardrobe.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.utils.CommonUtil;
import com.swastik.wardrobeapp.utils.MySQLiteHelper;

import java.util.List;

/**
 * Created by Swastik on 26-04-2016.
 */
public class ImageSlideAdapter extends PagerAdapter {

    int type;
    Context mContext;
    List<String> imageList;

    public ImageSlideAdapter(Context context,int type) {
        mContext = context;
        this.type = type;
        loadImages();
    }

    public void loadImages() {
        if(type == CommonUtil.TOP)
            imageList = MySQLiteHelper.fetchListFromTopsTable(mContext);
        else
            imageList = MySQLiteHelper.fetchListFromBottomsTable(mContext);

        if(imageList.size()==0)
            imageList.add("");
    }

    public String getPicId(int position) {
        return imageList.get(position);
    }
    public int getPositionById (String id) {
        return imageList.indexOf(id);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        container.addView(imageView);

        if(imageList.get(position).isEmpty())
        {
            if(type == CommonUtil.TOP)
                imageView.setImageResource(R.drawable.img_top);
            else
                imageView.setImageResource(R.drawable.img_bottom);
        }else
        {
            if(type == CommonUtil.TOP)
            {
                imageView.setImageBitmap(
                        CommonUtil.decodeBase64(
                                MySQLiteHelper.getPicByIdFromTopsTable(mContext, imageList.get(position))));
            }else
            {
                imageView.setImageBitmap(
                        CommonUtil.decodeBase64(
                                MySQLiteHelper.getPicByIdFromBottomsTable(mContext,imageList.get(position))));
            }

        }

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (View)object);
    }
}