package com.example.demo.weight;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.example.demo.utils.HTMLParseUtil;
import com.example.demo.weight.photoview.PhotoView;

import java.util.List;

/**
 * 图片查看与缓存
 *
 * @author: Bob
 * @date :2019/3/28 10:10
 */
public class ImgPagerAdapter extends PagerAdapter {
    private List<String> mImgList;
    private Context mContext;

    public ImgPagerAdapter(Context context, List<String> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((PhotoView) object);
    }

    /**
     * 区分路径加载图片
     *
     * @param imgUrl    图片url
     * @param photoView 要显示图片的ImageView
     */
    private void loadImage(final String imgUrl, PhotoView photoView) {
        if (imgUrl.contains(HTMLParseUtil.HTTP) || imgUrl.contains(HTMLParseUtil.HTTPS)) {
            Glide.with(mContext).load(imgUrl).placeholder(R.drawable.default_image).into(photoView);
        } else {
            //本地文件是否存在
            if (HTMLParseUtil.fileIsExists(imgUrl)) {
                //加载本地图片
                Glide.with(mContext).load(imgUrl).placeholder(R.drawable.default_image).into(photoView);
            } else {
                //图片不存在直接加载替换图片
                Glide.with(mContext).load(R.drawable.default_image).into(photoView);
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = mImgList.get(position);
        PhotoView photoView = new PhotoView(mContext);
        loadImage(imgUrl, photoView);
        container.addView(photoView);
        return photoView;
    }
}
