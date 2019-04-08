package com.example.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.weight.ImgPagerAdapter;
import com.example.demo.weight.ImgViewPager;

import java.util.ArrayList;

/**
 * Created by bob on 2017/12/6.
 */

public class ImageShowActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ArrayList<String> imgList;
    private TextView indexView, countView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_show_image);
        imgList = (ArrayList<String>) getIntent().getSerializableExtra("imgList");
        if (null != imgList && imgList.size() > 0) {
            indexView = findViewById(R.id.tv_index);
            countView = findViewById(R.id.tv_count);
            int currentPosition = getIntent().getIntExtra("position", -1);
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ImgViewPager viewPager = findViewById(R.id.view_pager);
            ImgPagerAdapter mPagerAdapter = new ImgPagerAdapter(this, imgList);
            viewPager.setAdapter(mPagerAdapter);
            viewPager.setCurrentItem(currentPosition);
            viewPager.setOnPageChangeListener(this);
            String indexCount = String.valueOf((currentPosition + 1));
            indexView.setText(indexCount);
            countView.setText("/" + imgList.size());
        } else {
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (null != imgList && imgList.size() > 0) {
            String indexCount = String.valueOf((position + 1));
            indexView.setText(indexCount);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
