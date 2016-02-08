package com.xxy.instagramclient.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.xxy.instagramclient.CommentDialogFragment;
import com.xxy.instagramclient.R;
import com.xxy.instagramclient.object.InstagramPhoto;
import com.xxy.instagramclient.util.CustomStyle;
import com.xxy.instagramclient.view.LikesView;
import com.xxy.instagramclient.view.TopView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/3/16.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

  private FragmentManager fragmentManager;

  public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
    super(context, android.R.layout.simple_list_item_1, objects);
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final InstagramPhoto photo = getItem(position);
    viewHolder.topView.setView(photo);

    int darkblue = ContextCompat.getColor(getContext(), R.color.darkblue);
    viewHolder.likesView.setText(photo.likesCount, darkblue);

    if (photo.commentsCount == 0) {
      viewHolder.tvComments.setVisibility(View.GONE);
      viewHolder.lvComments.setVisibility(View.GONE);
    } else if (photo.commentsCount == 1) {
      viewHolder.tvComments.setText(photo.comments.get(0).text);
      CommentsViewAdapter adapter =
          new CommentsViewAdapter(getContext(), photo.comments);
      viewHolder.lvComments.setAdapter(adapter);
    } else {
      viewHolder.tvComments.setText(
          String.format("view all %s comments",
              NumberFormat.getNumberInstance(Locale.US).format(photo.commentsCount)
          )
      );
      viewHolder.tvComments.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              CommentDialogFragment dialogFragment =
                  CommentDialogFragment.newInstance(photo.comments);
              dialogFragment.show(fragmentManager, "view");
            }
          }
      );
      CommentsViewAdapter adapter = new CommentsViewAdapter(
          getContext(), photo.comments.subList(0, 2)
      );
      adapter.setMaxLines(2);
      viewHolder.lvComments.setAdapter(adapter);
      setListViewHeightBasedOnChildren(viewHolder.lvComments);
    }

    viewHolder.tvCaption.setText(
        CustomStyle.stylizeFirstPart(photo.userName, photo.caption, darkblue));

    setImageVideoView(photo, viewHolder.imageView, viewHolder.videoView);

    return convertView;
  }

  public void setImageVideoView(
      InstagramPhoto photo,
      ImageView mImageView,
      VideoView mVideoView
  ) {
    if (photo.viewType.equals("image")) {
      mVideoView.setVisibility(View.GONE);
      mImageView.setVisibility(View.VISIBLE);
      mImageView.setImageResource(0);
      Picasso.with(getContext())
          .load(photo.imageVideoUrl)
          .placeholder(R.drawable.image_placeholder)
          .into(mImageView);
    } else if (photo.viewType.equals("video")) {
      mImageView.setVisibility(View.GONE);
      mVideoView.setVisibility(View.VISIBLE);
      mVideoView.setZOrderOnTop(true);
      mVideoView.setVideoPath(photo.imageVideoUrl);
      MediaController mediaController = new MediaController(getContext());
      mediaController.setAnchorView(mVideoView);
      mVideoView.setMediaController(mediaController);
      mVideoView.requestFocus();
      mVideoView.seekTo(1);
      mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        // Close the progress bar and play the video
        public void onPrepared(MediaPlayer mp) {
        }
      });
    } else {
      throw new RuntimeException(
          String.format("the view type %s is not supported", photo.viewType)
      );
    }
  }

  public static void setListViewHeightBasedOnChildren(ListView listView) {
    if (listView == null) return;
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }
    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 10;
    listView.setLayoutParams(params);
  }

  static class ViewHolder {
    @Bind(R.id.topView)
    TopView topView;
    @Bind(R.id.likes)
    LikesView likesView;
    @Bind(R.id.comment_example)
    ListView lvComments;
    @Bind(R.id.comments)
    TextView tvComments;
    @Bind(R.id.tvCaption)
    TextView tvCaption;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.videoView)
    VideoView videoView;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
