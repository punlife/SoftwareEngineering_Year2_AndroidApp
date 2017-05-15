package com.com2027.group03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    //  declare field to edit username and password
    private EditText editTextEmail, editTextPassword;
    // declare button to login
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private boolean isConnectedToTheInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        // Not connected to the internet and the user did not logged in previously
        else if(!isConnectedToTheInternet()){
            Log.e("LoginActivity", "No internet access, logging in as anonymous");
            SharedPreferences.Editor editor = getPreferences(0).edit();
            //editor.putBoolean(Constants.IS_LOGGED_IN,true);
            editor.putString(Constants.EMAIL, "anonymous@anonymous.com");
            editor.putString(Constants.NICKNAME, "anonymous");
            editor.putString(Constants.UNIQUE_ID, "anonymous");
            editor.apply();

            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        setContentView(R.layout.activity_login);

        // inflate the edit texts
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        // progress dialog which will ask the user to wait whilst it is working
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);

    }

    private void userLogin(){
        // get the username and password typed by the user and converting it into a string
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        // display the progress dialog
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();

                if(resp.getResult().equals(Constants.SUCCESS)){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.putString(Constants.EMAIL, resp.getUser().getEmail());
                    editor.putString(Constants.NICKNAME, resp.getUser().getNickname());
                    editor.putString(Constants.UNIQUE_ID, resp.getUser().getUnique_id());
                    editor.apply();

                    SharedPreferences viewer = sharedPreferences;
                    Log.d(TAG, "Is logged in: " + viewer.getBoolean(Constants.IS_LOGGED_IN, false));
                    Log.d(TAG, "Email: " + viewer.getString(Constants.EMAIL, "null"));
                    Log.d(TAG, "Nickneme: " + viewer.getString(Constants.NICKNAME, "null"));
                    Log.d(TAG, "Unique ID: " + viewer.getString(Constants.UNIQUE_ID, "null"));
                    Log.d(TAG, "Starting menu activity...");

                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Log.e(TAG, "Login failed! Error: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "Login failed! Error: " + t.getLocalizedMessage());
            }
        });


    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        }
    }
}
