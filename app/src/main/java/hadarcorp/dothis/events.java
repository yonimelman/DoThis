package hadarcorp.dothis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class events extends AppCompatActivity {

    String user_id;
    String[] ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_events);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        populate_list_views();
        registerClickCallback();
    }

    private void populate_list_views() {

        String[] my_events = {"event_1\n55.5 km\n4 people", "event_3\n55.5 km\n4 people","event_3\n55.5 km\n4 people"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.row_event,
                my_events
        );
        ListView list = (ListView) findViewById(R.id.mobile_list);
        list.setAdapter(adapter);
    }


    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.mobile_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
//                TextView textView = (TextView) viewClicked;
//                String msg = "You have joined to" + textView.getText().toString();
//                Toast.makeText(events.this, msg, Toast.LENGTH_LONG).show();

                TextView textView = (TextView) viewClicked;
                RequestQueue queue = Volley.newRequestQueue(this);
                String base_url = "http://10.10.20.114:8000";
                String url = base_url + "/join_activity";
                System.out.println("Calling url " + url);
                // TODO: CHANGE THIS
//                final Intent intent = new Intent(this, MainActivity.class);
                String json_str = "{";
                json_str += "\"user_id\": \"" + user_id + "\",";
                json_str += "\"activity_id\": \"" + ids[position] + "\"";
                json_str += "}";

                try {
                    JSONObject json_body = new JSONObject(json_str);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json_body,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("IN ON RESPONSE!!!!!!!!");
                                    try {
                                        String status = response.getString("status");
                                        System.out.println("activity_id: " + status);
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
        });
    }

}
