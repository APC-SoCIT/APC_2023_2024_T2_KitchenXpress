package com.kitchenxpress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class register extends AppCompatActivity {
    EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void Login(View view) {
        Intent homeIntent = new Intent(register.this, login.class);
        startActivity(homeIntent);
        finish();
    }

    public void register(View view) {

        String nam = name.getText().toString();
        String ema = email.getText().toString();
        String pas = password.getText().toString();

        String nam1 = name.getText().toString();
        String ema1 = email.getText().toString();
        String pas1 = password.getText().toString();

        String student_validator = "@student.apc.edu.ph";
        String faculty_validator = "@apc.edu.ph";


        Pattern student_pattern = Pattern.compile(student_validator);
        Matcher student_matcher = student_pattern.matcher(email.getText().toString());

        Pattern faculty_pattern = Pattern.compile(faculty_validator);
        Matcher faculty_matcher = faculty_pattern.matcher(email.getText().toString());

        if (email.getText().toString().isEmpty()) {
              Snackbar snackbar = Snackbar
                    .make(view, "Fields can not be empty!", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else if (!student_matcher.find()) {

            Snackbar snackbar = Snackbar
                    .make(view, "Invalid university email...", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {
//
//            Snackbar snackbar = Snackbar
//                    .make(view, "valid university email", Snackbar.LENGTH_LONG);
//            snackbar.show();


            ProgressDialog pd = new ProgressDialog(register.this);
            pd.setMessage("Creating student account...");
            pd.setCancelable(false);
            pd.show();

            if (!nam.equals("") && !ema.equals("") && !pas.equals("")) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "  https://eminder.website/kxregister.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success")) {
                            pd.hide();
                            Random rand = new Random();
                            String otp = String.valueOf(rand.nextInt(10000));
                            SharedPreferences sharedPreferences = getSharedPreferences("OTP",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("otp",otp);
                            myEdit.putString("email",email.getText().toString());
                            myEdit.commit();
                            startActivity(new Intent(register.this, register_verify.class));
                            finish();
                           }else {
                            pd.hide();
                            Snackbar snackbar = Snackbar
                                    .make(view, "Somthing went wrong", Snackbar.LENGTH_LONG);
                            snackbar.show();
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
                        data.put("name", name.getText().toString());
                        data.put("email", email.getText().toString());
                        data.put("level", "user");
                        data.put("password", password.getText().toString());
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


            }

        } if (!faculty_matcher.find()) {
            Snackbar snackbar = Snackbar
                    .make(view, "Invalid faculty email...", Snackbar.LENGTH_LONG);
            snackbar.show();

        }else {

//            Snackbar snackbar = Snackbar
//                    .make(view, "valid faculty email", Snackbar.LENGTH_LONG);
//

            ProgressDialog pd = new ProgressDialog(register.this);
            pd.setMessage("Creating faculty account...");
            pd.setCancelable(false);
            pd.show();
            if (!nam1.equals("") && !ema1.equals("") && !pas1.equals("")) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "  https://eminder.website/kxregister.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success")) {
                            pd.hide();
                            Random rand = new Random();
                            String otp = String.valueOf(rand.nextInt(10000));
                            SharedPreferences sharedPreferences = getSharedPreferences("OTP", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("otp", otp);
                            myEdit.putString("email", email.getText().toString());
                            myEdit.commit();
                            startActivity(new Intent(register.this, register_verify.class));
                            finish();
                        }else {

                            Snackbar snackbar = Snackbar
                                    .make(view, "Something went wrong", Snackbar.LENGTH_LONG);
                            snackbar.show();

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
                        data.put("name", name.getText().toString());
                        data.put("email", email.getText().toString());
                        data.put("level", "faculty");
                        data.put("password", password.getText().toString());
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }
    }
}