package com.sps.candle;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class AddArticleActivity extends Activity {

    String session_name;
    String session_id;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_article);

        Button btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                new addArticleTask().execute();

            }
        });
    }


    private class addArticleTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(GlobalVariables._Servername + GlobalVariables._Service_endpoint + GlobalVariables._RESOURCE_NODE_CREATE);
           
            try {

                EditText etTitle = (EditText) findViewById(R.id.etTitle);
                EditText etBody = (EditText) findViewById(R.id.etBody);

                String title = etTitle.getText().toString().trim();
                String body = etBody.getText().toString().trim();

                JSONObject json = new JSONObject();
                json.put("type", "article");
                json.put("title", title);

                
                // Body field in drupal is diffrent from title. we must fill body field as follow:
                // "body" : { "und" : { "value" : etbody } }
                
                JSONArray und = new JSONArray();
                JSONObject undtag = new JSONObject();
                JSONObject value = new JSONObject();         
  
                value.put("value", body); 
                und.put(value);                
                undtag.put("und", und);
                
                // Finally we add body to our json request.
                json.put("body", undtag);
                
                
                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));                
                httppost.setEntity(se);
                httppost.setHeader("X-CSRF-Token", GlobalVariables._Token);
                httppost.setHeader("Cookie", GlobalVariables._Cookie);
                httpclient.execute(httppost);
                
                //BasicHttpContext mHttpContext = new BasicHttpContext();
                //CookieStore mCookieStore = new BasicCookieStore();
                //BasicClientCookie cookie = new BasicClientCookie(session_name, session_id);
                //cookie.setVersion(0);
                //cookie.setDomain("http://imankiani.ir");
                //cookie.setPath("/");
                //mCookieStore.addCookie(cookie);
                //mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
                //httpclient.execute(httppost, mHttpContext);
                
                // After saving the new article, we must see the list of our articles.
                Intent intent = new Intent(AddArticleActivity.this, ListArticlesActivity.class);
                startActivity(intent);
                
                

            }
            catch (Exception e) {
                Log.v("Error adding article", e.getMessage());

            } finally {
			} //end of try - catch

            return 0;
        }
    } // end of thread class
}
