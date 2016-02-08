package com.xxy.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xxy.instagramclient.adapter.InstagramPhotoAdapter;
import com.xxy.instagramclient.object.Comment;
import com.xxy.instagramclient.object.InstagramPhoto;
import com.xxy.instagramclient.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {

  public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
  private ArrayList<InstagramPhoto> photos;
  private InstagramPhotoAdapter aPhotos;

  @Bind(R.id.swipeContainer)
  SwipeRefreshLayout swipeContainer;
  @Bind(R.id.lvPhotos)
  ListView lvPhotos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo);
    ButterKnife.bind(this);

    swipeContainer.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            fetchPopularPhotos();
          }
        }
    );
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

    photos = new ArrayList<>();
    aPhotos = new InstagramPhotoAdapter(this, photos);
    aPhotos.setFragmentManager(getSupportFragmentManager());
    lvPhotos.setAdapter(aPhotos);
    fetchPopularPhotos();
  }

  public void fetchPopularPhotos() {
    /*
    */
    String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray photosJson = null;
        aPhotos.clear();
        try {
          photosJson = response.getJSONArray("data");
          for (int i = 0; i < photosJson.length(); i++) {
            JSONObject photoJson = photosJson.getJSONObject(i);
            InstagramPhoto photo = new InstagramPhoto();
            photo.userName = photoJson.getJSONObject("user").getString("username");
            photo.profileImageUrl = photoJson.getJSONObject("user").getString("profile_picture");
            String captionObjStr = photoJson.getString("caption");
            if (captionObjStr.equals("null")) {
              photo.caption = "";
            } else {
              photo.caption = photoJson.getJSONObject("caption").getString("text");
            }
            photo.likesCount = photoJson.getJSONObject("likes").getInt("count");
            List<Comment> comments = new ArrayList<>();
            JSONArray commentsJson = photoJson.getJSONObject("comments").getJSONArray("data");
            for (int commentIdx = 0; commentIdx < commentsJson.length(); commentIdx++) {
              JSONObject commentJsonObj = commentsJson.getJSONObject(commentIdx);
              JSONObject userJsonObj = commentJsonObj.getJSONObject("from");
              User newuser = new User();
              newuser.userName = userJsonObj.getString("username");
              newuser.fullName = userJsonObj.getString("full_name");
              newuser.profileUrl = userJsonObj.getString("profile_picture");
              Comment newComment = new Comment();
              newComment.user = newuser;
              newComment.created_time = commentJsonObj.getLong("created_time");
              newComment.text = commentJsonObj.getString("text");
              comments.add(newComment);
            }
            Collections.sort(comments, new Comparator<Comment>() {
              @Override
              public int compare(Comment lhs, Comment rhs) {
                if (lhs.created_time < rhs.created_time) {
                  return 1;
                } else if (lhs.created_time > rhs.created_time) {
                  return -1;
                }
                return 0;
              }
            });
            photo.comments = comments;
            photo.commentsCount = photoJson.getJSONObject("comments").getInt("count");
            long createdTime = photoJson.getLong("created_time");
            photo.time = DateUtils.getRelativeTimeSpanString(
                createdTime * 1000,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            ).toString().replace(" ago", "");
            photo.location = photoJson.getString("location");
            if (!photo.location.equals("null")) {
              photo.location = photoJson.getJSONObject("location").getString("name");
            }
            photo.viewType = photoJson.getString("type");
            if (photo.viewType.equals("image")) {
              photo.imageVideoUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
            } else {
              photo.imageVideoUrl = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
            }
            photos.add(photo);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
        swipeContainer.setRefreshing(false);
        aPhotos.notifyDataSetChanged();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      }
    });

  }
}
