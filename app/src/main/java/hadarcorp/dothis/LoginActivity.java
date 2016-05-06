package hadarcorp.dothis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        final TextView mTextView = (TextView) findViewById(R.id.response_text) ;
        RequestQueue queue = Volley.newRequestQueue(this);
        String base_url = "http://10.10.20.114:8000";
        String url = base_url + "/get_or_create_user";
        System.out.println("Calling url " + url);
        Intent intent = new Intent(this, MainActivity.class);

        EditText editText = (EditText) findViewById(R.id.username_text);
        String user_name = editText.getText().toString();
        try {
            JSONObject json_body = new JSONObject("{\"user_name\": \"" + user_name + "\"}");
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json_body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("IN ON RESPONSE!!!!!!!!");
                            try {
                                String user_id = response.getString("user_id");
                                mTextView.setText("Received user id: "+ user_id);
                            } catch (Exception e) {
                                System.out.println("Bad JSON Response");
                                mTextView.setText("ERROR ");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("IN ON ERROR RESPONSE!!!!!!!!");
                    mTextView.setText("That didn't work!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        } catch (JSONException e) {
            System.out.println("Failed to create json object");
        }

    }
}