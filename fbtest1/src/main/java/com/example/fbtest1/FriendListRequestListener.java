package com.example.fbtest1;

/**
 * Created by NRV on 3/5/15.
 */


import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendListRequestListener  {

    public void onComplete(final String response) {
        String _error = null;

        try {
            JSONObject json = Util.parseJson(response);
            final JSONArray friends = json.getJSONArray("data");

            /*FacebookActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Do stuff here with your friends array,
                    // which is an array of JSONObjects.
                }
            });*/

        } catch (JSONException e) {
            _error = "JSON Error in response";
        } catch (FacebookError e) {
            _error = "Facebook Error: " + e.getMessage();
        }

        if (_error != null)
        {
            Log.d("Frnd", _error);
        }
    }
}