package com.kitchenxpress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_verify extends AppCompatActivity {
    EditText code_one,code_two,code_three,code_four;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_verify);


        code_one = findViewById(R.id.code_one);
        code_two = findViewById(R.id.code_two);
        code_three = findViewById(R.id.code_three);
        code_four = findViewById(R.id.code_four);

        SharedPreferences sh = getSharedPreferences("OTP", MODE_PRIVATE);

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "  https://eminder.website/kxregister_otp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.statusCode == 404) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("otp",  sh.getString("otp", ""));
                data.put("email",  sh.getString("email", ""));
                return data;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest1);


    }

    public void submit(View view) {

        String one = code_one.getText().toString();
        String two = code_two.getText().toString();
        String three = code_three.getText().toString();
        String four = code_four.getText().toString();


        ProgressDialog pd = new ProgressDialog(register_verify.this);
        pd.setMessage("Validating OTP");
        pd.setCancelable(false);
        pd.show();

        SharedPreferences sh = getSharedPreferences("OTP", MODE_PRIVATE);

        String code = one + two + three + four ;


        if(code.matches(sh.getString("otp", ""))){

            Intent homeIntent = new Intent(register_verify.this, login.class);
            startActivity(homeIntent);
            finish();

            }else{
                pd.hide();
                Snackbar snackbar = Snackbar
                        .make(view, "Invalid OTP", Snackbar.LENGTH_LONG);
                snackbar.show();

            }

        }

    }
