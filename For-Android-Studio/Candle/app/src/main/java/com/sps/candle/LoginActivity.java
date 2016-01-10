package com.sps.candle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button b = (Button) findViewById(R.id.btnLogin);
        b.setOnClickListener(this);
    }

    String session_name;
    String session_id;
    String token;


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        new drupalLogin().execute();



    }


    @SuppressLint("ShowToast")
	private class drupalLogin extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GlobalVariables._Servername + GlobalVariables._Service_endpoint + GlobalVariables._RESOURCE_USER_LOGIN);
            Log.i("debug", "i am in doInBackground");
            try {
                EditText username = (EditText) findViewById(R.id.etUsername);
                EditText password = (EditText) findViewById(R.id.etPassword);

                JSONObject json = new JSONObject();
                json.put("username", username.getText().toString().trim());
                json.put("password", password.getText().toString().trim());

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);

                HttpResponse response = httpClient.execute(httpPost);
                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONObject jsonObj = new JSONObject(jsonResponse);
                session_name = jsonObj.getString("session_name");
                session_id = jsonObj.getString("sessid");
                GlobalVariables._Cookie = session_name + "=" + session_id;
                Log.i("Debug: session id is: ", session_id);
                GlobalVariables._Token = jsonObj.getString("token");
                return 1;
            }
            catch (Exception e) {
                Log.i("error", e.getMessage());
            }
            return 0;
        } // end of doInBackground


        @Override
        protected void onPostExecute(Integer result) {
        	if(result ==1 ){
        		Intent intent = new Intent(LoginActivity.this, ListArticlesActivity.class);
        		startActivity(intent);        		
        	}
        	else{
        		
        		Toast.makeText(LoginActivity.this, "Username or password is incorrect!", 3000).show();
        	}

        } // end of post execute

    } // end of inner class (AsyncTask)

}