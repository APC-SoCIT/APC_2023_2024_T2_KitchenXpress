package com.kitchenxpress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText email , password ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }


    public void register(View view) {
        Intent homeIntent = new Intent(login.this, register.class);
        startActivity(homeIntent);
        finish();
    }

    public void login(View view) {
        String ema = email.getText().toString();
        String pas = password.getText().toString();

        ProgressDialog pd = new ProgressDialog(login.this);
        pd.setMessage("Logging in");
        pd.setCancelable(false);
        pd.show();


        if(!ema.equals("") && !pas.equals("")){
            StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://eminder.website/kxlogin.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject responseJson = new JSONObject(response);
                        String status = responseJson.getString("status");

                        if (status.equals("success")) {
                            pd.hide();
                            startActivity(new Intent(login.this, main.class));
                            finish();
                        } else if (status.equals("failure")) {
                            pd.hide();
                            Snackbar snackbar = Snackbar
                                    .make(view, "Invalid Email or Password", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.statusCode == 404) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            //use this json as you want
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email.getText().toString());
                    data.put("password", password.getText().toString());
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            pd.hide();
            Snackbar snackbar = Snackbar
                    .make(view, "Fields can not be empty!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

    }
}