package com.xxy.instagramclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxy.instagramclient.R;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class NameLocationView extends RelativeLayout {

  private TextView nameTv;
  private TextView locationTv;

  public NameLocationView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public void setView(String name, String location) {
    if (nameTv == null) {
      LayoutInflater.from(getContext())
          .inflate(R.layout.name_location, this, true);

      nameTv = (TextView) findViewById(R.id.user_name);
      locationTv = (TextView) findViewById(R.id.user_location);
    }
    nameTv.setText(name);
    if (location == null) {
      locationTv.setVisibility(View.GONE);
    } else {
      locationTv.setText(location);
      locationTv.setVisibility(View.VISIBLE);
    }
  }
}
