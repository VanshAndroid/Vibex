package com.crescentek.cryptocurrency.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.BannerImage;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Rakesh on 29/08/15.
 */
public class SlidingImage_Adapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<BannerImage> bannerImageList;

    public SlidingImage_Adapter(Context context, List<BannerImage> bannerImageList) {
        this.context = context;
        this.bannerImageList=bannerImageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerImageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

        Log.d("ImageUrlPager>>>",bannerImageList.get(position).getBannerimageurl());
        try {
            //Picasso.with(context).load(bannerImageList.get(position).getBannerimageurl()).into(imageView);
            //imageView.setImageResource(bannerImageList.get(position).getImageId());
            Glide.with(context).load(bannerImageList.get(position).getImageId())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        catch (Exception e)
        {
            Log.d("ImageParsingError",e.getMessage());
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
