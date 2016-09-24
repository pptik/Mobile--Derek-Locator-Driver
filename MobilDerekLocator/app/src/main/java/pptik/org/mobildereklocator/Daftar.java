package pptik.org.mobildereklocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pptik.org.mobildereklocator.Connection.IConnectionResponseHandler;
import pptik.org.mobildereklocator.Connection.RequestRest;

/**
 * Created by edo on 9/23/2016.
 */

public class Daftar extends AppCompatActivity {
    EditText nama,email,hp,username,password,retypepassword;
    Button daftar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_activity);

        bindingXML();

    }
    private void bindingXML(){
        nama=(EditText)findViewById(R.id.daftar_nama);
        email=(EditText)findViewById(R.id.daftar_email);
        hp=(EditText)findViewById(R.id.daftar_hp);
        username=(EditText)findViewById(R.id.daftar_username);
        password=(EditText)findViewById(R.id.daftar_password);
        retypepassword=(EditText)findViewById(R.id.daftar_retype_password);
        daftar=(Button)findViewById(R.id.daftar_button);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar.setText("");

                checkBeforeSubmit();
            }
        });
    }
    public void checkBeforeSubmit(){
        // Reset errors.
        nama.setError(null);
        email.setError(null);
        hp.setError(null);
        username.setError(null);
        password.setError(null);
        retypepassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nama.getText().toString())) {
            nama.setError("Silahkan Diisi");
            focusView = nama;
            cancel = true;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Silahkan Diisi");
            focusView = email;
            cancel = true;
        }else if (!isEmailValid(email.getText().toString())) {
            email.setError("Email Tidak Valid");
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(hp.getText().toString())) {
            hp.setError("Silahkan Diisi");
            focusView = hp;
            cancel = true;
        } if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("Silahkan Diisi");
            focusView = username;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password.getText().toString()) && !isPasswordValid(password.getText().toString())) {
            password.setError("Password Harus Lebih dari 4 karakter");
            focusView = password;
            cancel = true;
        }
        if (!retypepassword.getText().toString().equals(password.getText().toString())) {
            retypepassword.setError("Password tidak sama");
            focusView =retypepassword;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
         registerUser();
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    private void registerUser(){
        final ProgressDialog dialog = ProgressDialog.show(Daftar.this, null, "Mendaftar....", true);

        RequestRest req = new RequestRest(Daftar.this, new IConnectionResponseHandler(){

            @Override
            public void OnSuccessArray(JSONArray result){
                Log.i("result", result.toString());
                dialog.dismiss();
            }

            @Override
            public void onSuccessJSONObject(String result){
                try {
                    JSONObject obj = new JSONObject(result);
                    Log.i("Test", result);
                    JSONObject jobj=obj.getJSONObject("status");
                    boolean status=jobj.getBoolean("success");
                    dialog.dismiss();
                    if (status){

                        Toast.makeText(Daftar.this, jobj.getString("msg"), Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }else {
                        Toast.makeText(Daftar.this, jobj.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){

                }


            }

            @Override
            public void onFailure(String e){
                Log.i("Test gagal", e);
                dialog.dismiss();
                Toast.makeText(Daftar.this, "Username Sudah Digunakan", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccessJSONArray(String result){
                Log.i("Test", result);
                dialog.dismiss();
            }
        });


        req.registerUser(nama.getText().toString(),email.getText().toString(),hp.getText().toString(),username.getText().toString(),password.getText().toString()
        ,"3");
    }



}

