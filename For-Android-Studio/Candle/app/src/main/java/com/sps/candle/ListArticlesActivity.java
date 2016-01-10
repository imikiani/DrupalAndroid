package com.sps.candle;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ListArticlesActivity extends Activity {

    String session_name;
    String session_id;
    String token;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_articles);


        new FetchItems().execute();

        Button btn = (Button) findViewById(R.id.btnAddArticle);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ListArticlesActivity.this, AddArticleActivity.class);
                startActivity(intent);

            }
        });

        Button btnFollow = (Button) findViewById(R.id.btnFollow);
        btnFollow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("debug", "clicked");
                new FollowTask().execute();

            }
        });

    }

    private class FollowTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GlobalVariables._Servername + GlobalVariables._Service_endpoint + GlobalVariables._RESOURCE_FLAG_FLAG );
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Cookie", GlobalVariables._Cookie);
            httpPost.setHeader("X-CSRF-Token", GlobalVariables._Token);


            try{
                JSONObject json = new JSONObject();
                json.put("flag_name", "follow_user");
                json.put("entity_id", 2);
                json.put("action", "flag");

                StringEntity se = new StringEntity(json.toString());
                httpPost.setEntity(se);

                httpClient.execute(httpPost);
            }
            catch (Exception e){

            }
            return null;
        }
    }

    private class FetchItems extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            // TODO Auto-generated method stub

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(GlobalVariables._Servername + GlobalVariables._Service_endpoint + GlobalVariables._RESOURCE_NODES_INDEX);
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-Type", "application/json");
            
            // There is no need to use authentication for fetching list of nodes (contents), therefore we dont have to send cookie or also token.
            // By default anonymous users can view list of nodes.
            // If you set some permissions for viewing content, Authentication will be required. Therefore we will need to use the 2 following lines that commented:
            // httpget.setHeader("X-CSRF-Token", GlobalVariables._Token);
            // httpget.setHeader("Cookie", GlobalVariables._Cookie);
            
            JSONArray json = new JSONArray();

            try {
                HttpResponse response = httpclient.execute(httpget);
                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                return json;
            }
            catch (Exception e) {
                Log.v("Error adding article", e.getMessage());

            } // end of try catch
            return json;
        }// end of doInBackground


        @Override
        protected void onPostExecute(JSONArray result) {
            ListView lst = (ListView) findViewById(R.id.lvArticles);
            ArrayList<String> listItems = new ArrayList<String>();
            for (int i = 0; i < result.length(); i++) {
                try {
                    listItems.add(result.getJSONObject(i).getString("title").toString());
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.v("Error adding articles", e.getMessage());
                }
            } // end of for

            ArrayAdapter ad = new ArrayAdapter<String>(ListArticlesActivity.this, android.R.layout.simple_list_item_1, listItems);
            lst.setAdapter(ad);

        }

    } // end of AsyncTask
} // end of ListArticleActivity class
