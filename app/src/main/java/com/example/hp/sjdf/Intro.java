package com.example.hp.sjdf;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Intro extends AppCompatActivity {

    private ViewPager mViewpager;
    private LinearLayout dotslayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button nextbtn,pre_btn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mViewpager=(ViewPager)findViewById(R.id.slideview_pager);
        dotslayout=(LinearLayout)findViewById(R.id.dots_layout);
        nextbtn=(Button)findViewById(R.id.next_button);
        pre_btn=(Button)findViewById(R.id.prev_button);

        sliderAdapter = new SliderAdapter(this);

        mViewpager.setAdapter(sliderAdapter);

        adddotsIndicators(0);

        mViewpager.addOnPageChangeListener(viewListener);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewpager.setCurrentItem(mCurrentPage + 1);
            }
        });
        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewpager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void adddotsIndicators(int position)
    {
        mDots = new TextView[3];
        dotslayout.removeAllViews();  //if this is not written it creates multiple dots
        for (int i=0;i< mDots.length;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentwhite));

            dotslayout.addView(mDots[i]);
        }

        if(mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorwhite));
        }
    }

    //This method show the dots changing animation when we slide a page
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            adddotsIndicators(position);
            mCurrentPage = position;

            if(position == 0)
            {
                //nextbtn.setEnabled(true);
                //pre_btn.setEnabled(false);
                //pre_btn.setVisibility(View.INVISIBLE);

                //nextbtn.setText("NEXT");
                //pre_btn.setText("");
            }
            else if(position == mDots.length - 1)
            {
                //nextbtn.setEnabled(true);
                //pre_btn.setEnabled(true);
//                pre_btn.setVisibility(View.VISIBLE);

                nextbtn.setText("FINISH");
                nextbtn.setVisibility(View.VISIBLE);
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),LginActivity.class);
                        startActivity(i);
                    }
                });
                //pre_btn.setText("BACK");
            }
            else
            {
                //              nextbtn.setEnabled(true);
                //            pre_btn.setEnabled(true);
                //          pre_btn.setVisibility(View.VISIBLE);

                //nextbtn.setText("NEXT");
                //pre_btn.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };
}
