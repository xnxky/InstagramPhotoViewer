package com.xxy.instagramclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxy.instagramclient.R;
import com.xxy.instagramclient.object.InstagramPhoto;
import com.xxy.instagramclient.util.LoadImageView;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class TopView extends RelativeLayout {

  private ImageView userProfileIv;
  private ImageView clockIv;
  private TextView creationTimeTv;
  private NameLocationView nameLocationView;

  public TopView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public void setView(InstagramPhoto photo) {

    if (userProfileIv == null) {
      LayoutInflater.from(getContext())
          .inflate(R.layout.top_view, this, true);

      userProfileIv = (ImageView) findViewById(R.id.user_profile);
      clockIv = (ImageView) findViewById(R.id.clock);
      creationTimeTv = (TextView) findViewById(R.id.created_time);
      nameLocationView =
          (NameLocationView) findViewById(R.id.name_location);
    }

    LoadImageView.loadProfile(userProfileIv, getContext(), photo.profileImageUrl);

    clockIv.setImageResource(R.drawable.clock);
    creationTimeTv.setText(photo.time);

    String location = photo.location.equals("null") ? null : photo.location;
    nameLocationView.setView(photo.userName, location);
  }

}
