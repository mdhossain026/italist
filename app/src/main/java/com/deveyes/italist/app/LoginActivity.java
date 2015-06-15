package com.deveyes.italist.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.deveyes.italist.Constants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }

      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

            /*
           @author hossain
           changed for toolbar instead of action bar
         */
        getActionBarToolbar();
    }

            /*
        @author hossain
       @mthod  getActionBarToolbar()
     */

    private Toolbar mActionBarToolbar;
    private Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            Log.e("mActionBarToolbar::::",""+mActionBarToolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
               // getSupportActionBar().setDisplayShowHomeEnabled(false);

                //mActionBarToolbar.setLogo(R.drawable.actionbar_logo);
               // getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        return mActionBarToolbar;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public static class LoginFragment extends Fragment {
        public static LoginFragment newInstance() {
            LoginFragment fragment = new LoginFragment();

            return fragment;
        }

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            if (rootView != null) {
                Button bLogin = (Button) rootView.findViewById(R.id.bLogin);
                bLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String username = ((EditText) getView().findViewById(R.id.etUsername)).getText().toString();
                        final String password = ((EditText) getView().findViewById(R.id.etPassword)).getText().toString();

                        Ion.with(getActivity(), Constants.API_LOGIN_URL)
                                // comment for bug
                                .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:eFfFYFAdp3".getBytes(), 0))
                                .setBodyParameter("user", username)
                                .setBodyParameter("pwd", password)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject jsonObject) {
                                      //  Log.d(Constants.TAG, "Exception " + e);
                                        Log.d(Constants.TAG, "Response " + jsonObject);
                                        if (jsonObject.get("error").getAsInt() == 0) {
                                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("userID", username).commit();
                                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("password", password).commit();
                                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("loginKey", jsonObject.get("login_key").getAsString()).commit();

                                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            getActivity().finish();
                                            startActivity(mainIntent);

                                        } else {
                                            new AlertDialog.Builder(getActivity()).setTitle("Errore").setMessage("Impossibile accedere a Italist.\nVerifica le credenziali di accesso e riprova.").setPositiveButton("OK", null).show();
                                        }

                                    }
                                });
                    }
                });

                String mStoredUserID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userID", null);
                String mStoredPassword = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("password", null);
                ((EditText)rootView.findViewById(R.id.etUsername)).setText(mStoredUserID);
                ((EditText)rootView.findViewById(R.id.etPassword)).setText(mStoredPassword);

                /** Test **/
                ((EditText)rootView.findViewById(R.id.etUsername)).setText("italist@deveyesgroup.com");
                ((EditText)rootView.findViewById(R.id.etPassword)).setText("it4d3v@1");
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //((MainActivity) activity).onSectionAttached(2);
        }
    }
}
