package hadarcorp.dothis;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEventActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public String category, location;
    private GoogleApiClient mGoogleApiClient;
    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).enableAutoManage(this, this).build();
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("ERROR");
    }


    public void sportClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked)
            category = "SPRT";
    }

    public void foodClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked)
            category = "FOOD";
    }

    public void otherClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked)
            category = "OTHR";
    }


    public void addEventClick(View view) {

        System.out.println("IN Add Event Click!!!!!!!!");

        String description = ((TextView)findViewById(R.id.description)).getText().toString();
        String min_participants = ((TextView)findViewById(R.id.min_participants)).getText().toString();
        String max_participants = ((TextView)findViewById(R.id.max_participants)).getText().toString();
        String lifetime = ((TextView)findViewById(R.id.lifetime)).getText().toString();

        String json_str = "{";
        json_str += "\"owner\": \"" + user_id + "\",";
        json_str += "\"description\": \"" + description + "\",";
        json_str += "\"min_participants\": \"" + min_participants + "\",";
        json_str += "\"max_participants\": \"" + max_participants + "\",";
        json_str += "\"lifetime\": \"" + lifetime + "\",";
        json_str += "\"category\": \"" + category + "\",";
        json_str += "\"location\": \"" + location + "\"";
        json_str += "}";

        RequestQueue queue = Volley.newRequestQueue(this);
        String base_url = "http://10.10.20.114:8000";
        String url = base_url + "/add_activity";

        try {
            JSONObject json_body = new JSONObject(json_str);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json_body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("IN ON RESPONSE!!!!!!!!");
                            try {
                                String activity_id = response.getString("activity_id");
                                System.out.println("activity_id: " + activity_id);
                            } catch (Exception e) {
                                System.out.println("Bad JSON Response");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("IN ON ERROR RESPONSE!!!!!!!!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        } catch (JSONException e) {
            System.out.println("ERROR: " + e);
        }

    }
}



