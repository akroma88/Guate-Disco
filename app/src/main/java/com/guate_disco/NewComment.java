package com.guate_disco;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by akroma on 2/10/15.
 */
public class NewComment extends AppCompatActivity {
    static String idDiscoteca;
    static String nameDiscoteca;

    Button btnCancel;
    Button btnEnviar;
    EditText texto;
    TextView nameDisco;
    RatingBar rt;
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private String idUserFacebook="0";
    String resultado=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.new_comment);

        texto = (EditText)findViewById(R.id.commetText);
        nameDisco = (TextView)findViewById(R.id.nameDiscoComment);
        rt =  (RatingBar)findViewById(R.id.ratingComment);

        nameDisco.setText(nameDiscoteca);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnEnviar = (Button) findViewById(R.id.btn_enviar);
        btnEnviar.setEnabled(false);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setVisibility(View.GONE);
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_birthday");



        try{
            FileInputStream fIn = openFileInput("manejo.txt");
            InputStreamReader isr = new InputStreamReader(fIn);

        /* Prepare a char-Array that will
         * hold the chars we read back in. */
            char[] inputBuffer = new char[25];

            // Fill the Buffer with data from the file
            isr.read(inputBuffer);

            String readString = new String(inputBuffer);
            String result = readString.replaceAll("[^a-zA-Z0-9]", "");
            readString = result;
            Log.i("file","si existe archivo manejo.txt - "+readString );
            idUserFacebook = readString;
            btnEnviar.setEnabled(true);
        }
        catch (IOException io){
            Log.i("file","no existe archivo manejo.txt");
            loginButton.setVisibility(View.VISIBLE);
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            Context context = getApplicationContext();
            @Override
            public void onClick(View v) {
                boolean control = true;
                String a =texto.getText().toString();
                if(a.matches("")){
                    control=false;
                    CharSequence text = "Debes de escribir un comentario primero";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                if(rt.getRating()==0){
                    control=false;
                    CharSequence text = "Debes de elegir la contidad de estrellas";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                if(control==true){
                    String temp =texto.getText().toString()+";"+rt.getRating()+";"+idDiscoteca+";"+idUserFacebook;
                    insertComment(temp);
                }
            }
        });



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.i("idfb", "" + loginResult.getAccessToken().getUserId());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    idUserFacebook = (String) object.get("id");
                                    String name = (String) object.get("name");
                                    String gender = (String) object.get("gender");
                                    String birthday="";
                                    try {
                                        birthday = (String) object.get("birthday");
                                    }
                                    catch (Exception e){
                                        birthday = "null";
                                    }

                                    org.json.JSONObject picture = (org.json.JSONObject) object.get("picture");
                                    org.json.JSONObject data = (org.json.JSONObject) picture.get("data");
                                    String urlPic = (String) data.get("url");
                                    newUser(idUserFacebook + ";" + name + ";" + gender + ";" + birthday+";"+urlPic);
                                    new AsyncTaskWriteFile().execute("manejo.txt");
                                    loginButton.setVisibility(View.GONE);
                                    btnEnviar.setEnabled(true);
                                    Log.i("urlPic: ", "" + urlPic);
                                }
                                catch (Exception r){
                                    Log.i("error ",""+r);
                                }


                            }
                        });
                Bundle parameters = new Bundle();
                //parameters.putString("fields", "id,name,email,gender, birthday");
                parameters.putString("fields", "id,name,picture.type(large),gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                /*info.setText("Login attempt cancelled.");*/
            }

            @Override
            public void onError(FacebookException e) {
                /*info.setText("Login attempt failed.");*/
                Log.i("errfb","Error: "+e);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    Context context = getApplicationContext();
                    CharSequence text="";
                    int duration = Toast.LENGTH_SHORT;
                    if(resultado.equals("exitoso")){
                        text = "Gracias por tu comentario";
                    }
                    else {
                        text = "Ocurrio un error, intenta mas tarde";
                    }

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    //Intent intent = new Intent(NewComment.this, Discoteca.class);
                    //startActivity(intent);
                    onBackPressed();

                    break;
            }
            return false;
        }
    });

    private final void insertComment(final String val) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                resultado = ex.getObtainData(val,"insertComment","cadena");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private final void newUser(final String cad) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                resultado = ex.getObtainData(cad,"NewUser","cadena");
                //handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class AsyncTaskWriteFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... cad) {
            try {

                //write file used to control ID facebook
                final String TESTSTRING = new String(""+idUserFacebook+"");
                FileOutputStream fOut = openFileOutput(cad[0],MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                osw.write(TESTSTRING);
                osw.flush();
                osw.close();
                Log.i("---",""+cad[0]+idUserFacebook);

            } catch (IOException ioe){
                Log.i("IOException", "" + ioe);

            }


            return "";
        }

        @Override
        protected void onPostExecute(String stringFromDoInBackground) {
            loginButton = (LoginButton)findViewById(R.id.login_button);
            loginButton.setVisibility(View.GONE);
        }
    }

}
