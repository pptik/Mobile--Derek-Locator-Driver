package pptik.org.mobildereklocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pptik.org.mobildereklocator.Connection.IConnectionResponseHandler;
import pptik.org.mobildereklocator.Connection.RequestRest;
import pptik.org.mobildereklocator.Setup.ApplicationConstants;

/**
 * Created by GIGABYTE on 22/09/2016.
 */
public class Login extends AppCompatActivity {

    SharedPreferences prefs;
    EditText username,password;
    TextView fab;
    Button login;
    RequestParams params = new RequestParams();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        prefs = getSharedPreferences(ApplicationConstants.USER_PREFS_NAME,
                Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String userid = prefs.getString(ApplicationConstants.USER_ID, "");
        if (!TextUtils.isEmpty(userid)) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
       bindingXML();
    }
    private void bindingXML(){
        username=(EditText) findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loginCheck();
            }
        });

        fab = (TextView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Daftar.class);
                startActivity(i);
            }
        });
    }
    private void loginCheck(){

        username.setError(null);
        password.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("Silahkan Diisi");
            focusView = username;
            cancel = true;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Silahkan Diisi");
            focusView = password;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            attemptLogin();
        }
    }

    private void    attemptLogin(){
        final ProgressDialog dialog = ProgressDialog.show(Login.this, null, "Silahkan Tunggu...", true);


        params.put("user", username.getText().toString());
        params.put("pass", password.getText().toString());
        Log.i("user",username.getText().toString());
        Log.i("pass",password.getText().toString());
        // Make RESTful webservice call using AsyncHttpClient object
        final int DEFAULT_TIMEOUT = 400 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);

        client.addHeader("content-type", "application/x-www-form-urlencoded");
        client.post(ApplicationConstants.HTTP_URL+"login", params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response login : ", response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.i("response",response.toString());
                            JSONObject childjobj=jObj.getJSONObject("status");
                            boolean status=childjobj.getBoolean("success");
                            if (status) {
                                JSONObject data = jObj.getJSONObject("data");
                                String __id=data.getString("_id");
                                String __name=data.getString("name");
                                String __email=data.getString("email");
                                String __nomorHP=data.getString("nomor_telepon");
                                String __username=data.getString("user");
                                //store to sharedpreference
                                storeRegIdinSharedPref(getApplicationContext(),__id,__name,__email,__nomorHP,__username );

                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {

                                String errorMsg = childjobj.getString("msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Hide Progress Dialog
                        dialog.hide();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        dialog.hide();
                        Log.i("error",error.toString());
                        Log.i("error2",content.toString());
                        Log.i("error3",String.valueOf(statusCode));
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        // When Http response code is '404'
                        if (statusCode == 400) {
                            Toast.makeText(getApplicationContext(),
                                    "Username atau Password salah",
                                    Toast.LENGTH_LONG).show();
                        }
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void storeRegIdinSharedPref(Context context,String id,String name,String email,String nohp,String username){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstants.USER_ID, id);
        editor.putString(ApplicationConstants.FULLNAME, name);
        editor.putString(ApplicationConstants.EMAIL_ID, email);
        editor.putString(ApplicationConstants.NOMOR_HP, nohp);
        editor.putString(ApplicationConstants.USERNAME, username);
        editor.commit();
    }
}
