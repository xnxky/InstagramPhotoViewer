package com.xxy.instagramclient.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxy.instagramclient.R;
import com.xxy.instagramclient.object.Comment;
import com.xxy.instagramclient.util.CustomStyle;
import com.xxy.instagramclient.util.LoadImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class CommentsViewAdapter extends ArrayAdapter<Comment> {

  private boolean hasMaxLines = false;
  private int maxLines;

  public CommentsViewAdapter(Context context, List<Comment> objects) {
    super(context, android.R.layout.simple_list_item_1, objects);
  }

  public void setMaxLines(int maxLines) {
    hasMaxLines = true;
    this.maxLines = maxLines;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    Comment comment = getItem(position);
    LoadImageView.loadProfile(
        viewHolder.ivUserProfile, getContext(), comment.user.profileUrl);

    int darkblue = ContextCompat.getColor(getContext(), R.color.darkblue);
    viewHolder.tvComment.setText(
        CustomStyle.stylizeFirstPart(comment.user.userName, comment.text, darkblue));
    if (hasMaxLines) {
      viewHolder.tvComment.setMaxLines(maxLines);
      viewHolder.tvComment.setEllipsize(TextUtils.TruncateAt.END);
    }
    return convertView;
  }

  static class ViewHolder {
    @Bind(R.id.user_profile)
    ImageView ivUserProfile;
    @Bind(R.id.comment)
    TextView tvComment;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

}
