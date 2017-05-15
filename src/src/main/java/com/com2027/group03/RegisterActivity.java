package com.com2027.group03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Akhil on 23/04/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // fields to edit the user name, email and password
    private EditText editTextUsername, editTextEmail, editTextPassword;
    // declare the register button
    private Button buttonRegister;
    private ProgressDialog progressDialog;
    // declare field to display the login message
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        // inflate the email text view
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        // inflate the user name text view
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        // inflate the password text view
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        // inflate the login text view
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        // inflate the register button
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        // declare the progress dialog
        progressDialog = new ProgressDialog(this);
        // declare listeners for the register button and login text view
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //todo implement based on victor's server



        Retrofit retrofit = new


        /*progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), "Message2: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Error2: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);*/


    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister)
            registerUser();
        if(view == textViewLogin)
            startActivity(new Intent(this, LoginActivity.class));
    }
}
