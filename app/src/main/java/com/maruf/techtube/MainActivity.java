package com.maruf.techtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listview;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listview = findViewById(R.id.listview);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = "http://192.168.1.107/apps/techvideo.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {

                try {
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        String title = jsonObject.getString("title");
                        String video_id = jsonObject.getString("video_id");
                        Log.d("serverresponse", "" + title + "\n" + "" + video_id);

                        HashMap hashMap = new HashMap();
                        hashMap.put("title", title);
                        hashMap.put("video_id", video_id);
                        arrayList.add(hashMap);

                    }
                    MyAdapter myAdapter = new MyAdapter();
                    listview.setAdapter(myAdapter);

                    listview.setOnItemClickListener((parent, view, position, id) -> {
                        HashMap<String, String> hashMap = arrayList.get(position);
                        String videoId = hashMap.get("video_id");

                        PlayerActivity.videoURL = "https://www.youtube.com/watch?v=" + videoId;

                        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);

                        startActivity(intent);

                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.d("serverresponse", "" + volleyError);

            }
        });

        queue.add(jsonArrayRequest);

    }
    //**********************************************************************************************
    //**********************************************************************************************
    //**********************************************************************************************

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View myview = layoutInflater.inflate(R.layout.video_item, parent, false);

            ImageView thambnailimage = myview.findViewById(R.id.thambnail);
            TextView video_title = myview.findViewById(R.id.video_title);

            HashMap<String, String> hashMap = arrayList.get(position);
            String vtitle = hashMap.get("title");
            String vimage_id = hashMap.get("video_id");

            video_title.setText(vtitle);
            Picasso.get().load("http://img.youtube.com/vi/" + vimage_id + "/0.jpg").placeholder(R.drawable.blank_image).into(thambnailimage);

            return myview;
        }

    }

}