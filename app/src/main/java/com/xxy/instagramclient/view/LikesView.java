package com.xxy.instagramclient.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxy.instagramclient.R;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by xiangyang_xiao on 2/6/16.
 */
public class LikesView extends RelativeLayout {

  private TextView textView;

  public LikesView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    LayoutInflater.from(context)
        .inflate(R.layout.likes, this, true);
    ImageView imageView = (ImageView) findViewById(R.id.likesImageView);
    imageView.setImageResource(R.drawable.likes);
    textView = (TextView) findViewById(R.id.likesCount);
  }

  public void setText(long count, int textColor) {
    String countString = NumberFormat.getNumberInstance(Locale.US).format(count);
    final SpannableStringBuilder sb = new SpannableStringBuilder(
        countString + " likes"
    );

    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    final ForegroundColorSpan fcs = new ForegroundColorSpan(textColor);
    sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    sb.setSpan(fcs, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    textView.setText(sb);
  }
}
