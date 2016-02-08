package com.xxy.instagramclient.util;

import android.content.Context;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.xxy.instagramclient.R;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class LoadImageView {

  public static void loadProfile(
      ImageView userProfileIv, Context context, String url) {
    Transformation transformation = new RoundedTransformationBuilder()
        .borderWidthDp(0)
        .cornerRadiusDp(30)
        .oval(false)
        .build();

    Picasso.with(context)
        .load(url)
        .placeholder(R.drawable.profile_placeholder)
        .fit()
        .transform(transformation)
        .into(userProfileIv);
  }

}
