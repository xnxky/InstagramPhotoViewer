package com.xxy.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {

  public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
  private ArrayList<InstagramPhoto> photos;
  private InstagramPhotoAdapter aPhotos;

  private SwipeRefreshLayout swipeContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo);

    swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
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
    ListView lv = (ListView)findViewById(R.id.lvPhotos);
    lv.setAdapter(aPhotos);
    fetchPopularPhotos();
  }

  public void fetchPopularPhotos() {
    /*
    */
    String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray photosJson = null;
        aPhotos.clear();
        try {
          photosJson = response.getJSONArray("data");
          for(int i=0; i<photosJson.length(); i++) {
            JSONObject photoJson = photosJson.getJSONObject(i);
            InstagramPhoto photo = new InstagramPhoto();
            photo.userName = photoJson.getJSONObject("user").getString("username");
            photo.caption = photoJson.getJSONObject("caption").getString("text");
            photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
            photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
            photo.likesCount = photoJson.getJSONObject("likes").getInt("count");
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
