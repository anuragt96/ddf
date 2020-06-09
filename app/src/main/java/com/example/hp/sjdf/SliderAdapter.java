package com.example.hp.sjdf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ANURAG TIWARI on 04-01-2018.
 */

public class SliderAdapter extends PagerAdapter
{
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context= context;
    }

    //Arrays

    public int[] slideimages =
    {
            R.drawable.f,
            R.drawable.event,
            R.drawable.votes
    };
    public String[] slide_headings =
    {
            "FAST",
            "CREATE EVENT",
            "CREATE POLL"
    };
    public String[] slide_description =
    {
            " Our App is Fast and Reliant, with Easy to        use UI ",
            "Create Multiple Events & Chat with Tagged Users",
            "             Create Poll and generate Result"
    };

    @Override
    public int getCount()
    {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView imageView=(ImageView)view.findViewById(R.id.slider_imageview);
        TextView textView1 =(TextView) view.findViewById(R.id.slider_heading_text);
        TextView textView2 =(TextView) view.findViewById(R.id.slider_description);

        imageView.setImageResource(slideimages[position]);
        textView1.setText(slide_headings[position]);
        textView2.setText(slide_description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }
}
