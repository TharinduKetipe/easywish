package com.example.fbtest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends Activity {


        public Session ses;
        private String TAG = "MainActivity";
        private TextView lblEmail;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            lblEmail = (TextView) findViewById(R.id.lblEmail);

            LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
            authButton.setOnErrorListener(new OnErrorListener() {

                @Override
                public void onError(FacebookException error) {
                    Log.i(TAG, "Error " + error.getMessage());
                }
            });
            // set permission list, Don't foeget to add email
            authButton.setReadPermissions(Arrays.asList("public_profile","user_friends","email"));
            // session state call back event
            authButton.setSessionStatusCallback(new Session.StatusCallback() {

                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    ses = session;
                    if (session.isOpened()) {
                        Log.i(TAG, "Access Token" + session.getAccessToken());
                        Request.executeMeRequestAsync(session,
                                new Request.GraphUserCallback() {
                                    @Override
                                    public void onCompleted(GraphUser user, Response response) {
                                        if (user != null) {
                                            Log.i(TAG, "User ID " + user.getId());
                                            user.getBirthday();
                                            // Log.i(TAG,"Email "+ user.asMap().get("email"));

                                            lblEmail.setText(user.getFirstName() + "  ID " + user.getId());
                                            new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                                                    .execute("https://graph.facebook.com/"+user.getId()+"/picture?type=large");

                                        }
                                    }
                                });
                    }

                }
            });

            Button b=(Button)findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Back b=new Back(view.getContext());
                    Thread p=new Thread(b);

                    //p.start();
                    //Context context = view.getContext();

                    ImageView imgView;
                    imgView = (ImageView)findViewById(R.id.imageView);

                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }





    public void getfriend(){
        try{
        Facebook mFacebook = new Facebook("1551229735159085");
        AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);
            JSONObject jsonObj = Util.parseJson(mFacebook.request("me/friends"));

        JSONArray jArray = jsonObj.getJSONArray("data");
            Log.v("Main array", jArray.toString());
        for(int i=0;i<jArray.length();i++){

            JSONObject json_data = jArray.getJSONObject(i);
            Log.v("THIS IS DATA", i+" : "+json_data.toString());
        }
        }
        catch(Exception e){
            Log.d("ER", e.toString());
        }
    }


    }

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    } }


