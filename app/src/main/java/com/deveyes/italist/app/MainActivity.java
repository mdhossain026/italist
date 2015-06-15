package com.deveyes.italist.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.deveyes.italist.Constants;
import com.deveyes.italist.model.Product;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    //private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
           @author hossain
           changed for toolbar instead of action bar
         */
        getActionBarToolbar();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.actionbar_logo));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cek", "home selected");


            }
        });

        //mTitle = getTitle();
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up the drawer.Toolbar reference
        // passing t
        mNavigationDrawerFragment.setUp(
                mActionBarToolbar,
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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

                setmActionBarToolbar(mActionBarToolbar);
                setSupportActionBar(mActionBarToolbar);
                mActionBarToolbar.setNavigationIcon(R.drawable.actionbar_logo);
               //getSupportActionBar().setHomeButtonEnabled(false);
               //getSupportActionBar().setDisplayShowHomeEnabled(true);
                //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
              // getSupportActionBar().setDisplayShowTitleEnabled(false);
               // mActionBarToolbar.setNavigationIcon(R.drawable.actionbar_logo);


            }
        }
        return mActionBarToolbar;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position)
        {
            case 0:
                /*
                fragmentManager.beginTransaction()
                        .replace(R.id.container, QRCodeScannerFragment.newInstance(), "cameraFragment")
                        .commit();*/
                QRCodeScannerFragment fragment = (QRCodeScannerFragment) fragmentManager.findFragmentByTag("cameraFragment");
                if (fragment == null)
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, QRCodeScannerFragment.newInstance(), "cameraFragment")
                            .commit();
                } else {
                    final View helpView = fragment.getView().findViewById(R.id.llHelp);
                    final View startButton = fragment.getView().findViewById(R.id.bStart);

                    if (helpView.getVisibility() == View.VISIBLE) {
                        fragment.mScannerView.stopCamera();

                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setInterpolator(new AccelerateInterpolator());
                        fadeIn.setDuration(1000);

                        fadeIn.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                helpView.setVisibility(View.VISIBLE);
                                startButton.setVisibility(View.VISIBLE);
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                            }
                        });

                        helpView.startAnimation(fadeIn);
                        startButton.startAnimation(fadeIn);
                    }
                }
                break;
            case 1:
                //PreferenceManager.getDefaultSharedPreferences(this).edit().putString("userID", null).commit();
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("password", null).commit();

                Intent loginIntent = new Intent(this, LoginActivity.class);
                //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(loginIntent);
                break;
        }

    }

    /*
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Scansione codice";
                break;
            case 2:
                mTitle = "Login";
                break;
        }

        supportInvalidateOptionsMenu();
    }
    */

    public void setmActionBarToolbar(Toolbar mActionBarToolbar) {
        this.mActionBarToolbar = mActionBarToolbar;
    }


    public Toolbar getmActionBarToolbar() {
        return mActionBarToolbar;
    }
    public void restoreActionBar() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
        setSupportActionBar(mActionBarToolbar);
       // getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mActionBarToolbar.setNavigationIcon(R.drawable.actionbar_logo);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

        Log.e("mToolbar::::::::",""+ getmActionBarToolbar());



//            setSupportActionBar(getmActionBarToolbar());
//           // getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//         //  mActionBarToolbar.setNavigationIcon(R.drawable.actionbar_logo);
//            getmActionBarToolbar().setNavigationIcon(R.drawable.actionbar_logo);
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

//    public static class LoginFragment extends Fragment {
//           public static LoginFragment newInstance() {
//               LoginFragment fragment = new LoginFragment();
//
//               return fragment;
//           }
//
//        public LoginFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
//            if (rootView != null) {
//                Button bLogin = (Button) rootView.findViewById(R.id.bLogin);
//                bLogin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final String username = ((EditText) getView().findViewById(R.id.etUsername)).getText().toString();
//                        final String password = ((EditText) getView().findViewById(R.id.etPassword)).getText().toString();
//
//                        Ion.with(getActivity(), Constants.API_LOGIN_URL)
//                                .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:italist".getBytes(), 0))
//                                .setBodyParameter("user", username)
//                                .setBodyParameter("pwd", password)
//                                .asJsonObject()
//                                .setCallback(new FutureCallback<JsonObject>() {
//                                      @Override
//                                        public void onCompleted(Exception e, JsonObject jsonObject) {
//                                            Log.d(Constants.TAG, "Response " + jsonObject);
//                                            if (jsonObject.get("error").getAsInt() == 0) {
//                                            getActivity().getPreferences(MODE_PRIVATE).edit().putString("userID", username).commit();
//                                            getActivity().getPreferences(MODE_PRIVATE).edit().putString("password", password).commit();
//
//                                            getActivity().getSupportFragmentManager().beginTransaction()
//                                                    .replace(R.id.container, QRCodeScannerFragment.newInstance(), "cameraFragment")
//                                                    .commit();
//                                        } else {
//                                            new AlertDialog.Builder(getActivity()).setTitle("Errore").setMessage("Impossibile accedere a Italist.\nVerifica le credenziali di accesso e riprova.").setPositiveButton("OK", null).show();
//                                        }
//
//                                    }
//                                });
//                    }
//                });
//
//                String mStoredUserID = getActivity().getPreferences(MODE_PRIVATE).getString("userID", null);
//                String mStoredPassword = getActivity().getPreferences(MODE_PRIVATE).getString("password", null);
//
//                ((EditText)rootView.findViewById(R.id.etUsername)).setText(mStoredUserID);
//                ((EditText)rootView.findViewById(R.id.etPassword)).setText(mStoredPassword);
//            }
//            return rootView;
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            //((MainActivity) activity).onSectionAttached(2);
//        }
//    }

    public static class QRCodeScannerFragment extends Fragment implements ZBarScannerView.ResultHandler {

        private String mLoginKey;

        public static QRCodeScannerFragment newInstance() {
            QRCodeScannerFragment fragment = new QRCodeScannerFragment();

            return fragment;
        }

        public QRCodeScannerFragment() {
        }

        public ZBarScannerView mScannerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            if (rootView != null) {
                mScannerView = (ZBarScannerView) rootView.findViewById(R.id.scanner_view);
                /*
                ViewGroup parent = (ViewGroup)mScannerView.getParent();
                int index = parent.indexOfChild(mScannerView);
                parent.removeViewAt(index);
                mScannerView = new ZBarScannerView(getActivity());
                parent.addView(mScannerView, index);*/
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                mScannerView.stopCamera();

                Button bStart = (Button) rootView.findViewById(R.id.bStart);
                bStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Nascondiamo il tutorial e il bottone per avviare la scansione
                        final View helpView = rootView.findViewById(R.id.llHelp);
                        final View startButton = rootView.findViewById(R.id.bStart);

                        Animation fadeOut = new AlphaAnimation(1, 0);
                        fadeOut.setInterpolator(new AccelerateInterpolator());
                        fadeOut.setDuration(1000);

                        fadeOut.setAnimationListener(new Animation.AnimationListener()
                        {
                            public void onAnimationEnd(Animation animation)
                            {
                                helpView.setVisibility(View.GONE);
                                startButton.setVisibility(View.GONE);

                                mScannerView.startCamera();
                            }
                            public void onAnimationRepeat(Animation animation) {}
                            public void onAnimationStart(Animation animation) {}
                        });

                        helpView.startAnimation(fadeOut);
                        startButton.startAnimation(fadeOut);

                        // Attiviamo la scansione
                        //onResume();
                    }
                });

                // Recuperiamo la login key

                mLoginKey = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("loginKey", null);
                if (mLoginKey == null) {
                    String mStoredUserID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userID", null);
                    String mStoredPassword = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("password", null);

                    if (mStoredUserID == null || mStoredPassword == null) {
                    /*
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, LoginFragment.newInstance())
                            .commit();
                            */
                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(loginIntent);

                    }
//
//                    else {
//                        Ion.with(getActivity(), Constants.API_LOGIN_URL)
//                                .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:eFfFYFAdp3".getBytes(), 0))
//                                .setBodyParameter("user", "diego.mastalli@gmail.com")
//                                .setBodyParameter("pwd", "lorenzo")
//                                .asJsonObject()
//                                .setCallback(new FutureCallback<JsonObject>() {
//                                    @Override
//                                    public void onCompleted(Exception e, JsonObject jsonObject) {
//                                        if (e != null) {
//                                            Log.e(Constants.TAG, "Errore di Login: " + e.getMessage());
//                                        } else {
//                                            Log.d(Constants.TAG, "Response " + jsonObject);
//                                            if (jsonObject.get("error").getAsInt() == 0) {
//                                                mLoginKey = jsonObject.get("login_key").getAsString();
//                                            } else {
//                                                new AlertDialog.Builder(getActivity()).setTitle("Errore55").setMessage("Impossibile accedere a Italist.\nVerifica le credenziali di accesso e riprova.").setPositiveButton("OK", null).show();
//                                            }
//                                        }
//                                    }
//                                });
//                    }
                }
                /*
                rootView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            mScannerView.stopCamera();
                        } else {
                            mScannerView.startCamera();
                        }
                        return false;
                    }
                });
                */
            }
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            //mScannerView.setResultHandler(this);
            //mScannerView.startCamera();

            if (getView().findViewById(R.id.llHelp).getVisibility() == View.GONE) {
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            mScannerView.stopCamera();           // Stop camera on pause
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            //((MainActivity) activity).onSectionAttached(1);
        }

        @Override
        public void handleResult(final Result rawResult) {
            if (getView().findViewById(R.id.llHelp).getVisibility() == View.GONE) {
                mScannerView.stopCamera();

                Log.d(Constants.TAG, "Result: " + rawResult.getContents());
                Intent showDetailActivity = new Intent(getActivity(), ProductDetailActivity.class);
                String  loginKey = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(("loginKey"),"DEFAULT");
                showDetailActivity.putExtra("loginkey", loginKey);
                showDetailActivity.putExtra("qrcode", rawResult.getContents());

                startActivity(showDetailActivity);
            } else {
                mScannerView.startCamera();
            }

            /*
            Ion.with(getActivity(), Constants.API_PRODUCT_INFO_URL)
                    .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:italist".getBytes(), 0))
                    .setBodyParameter("key", mLoginKey)
                    .setBodyParameter("qrcode", rawResult.getContents())
                    .as(new TypeToken<Product>() {
                    })
                    .setCallback(new FutureCallback<Product>() {
                        @Override
                        public void onCompleted(Exception e, Product product) {
                            if (e != null)
                            {
                                Log.e(Constants.TAG, "Errore Scansione " + e.getMessage());
                            } else {
                                new AlertDialog.Builder(getActivity()).setTitle("Codice rilevato").setMessage("Dati prodotto:\nModello: " + product.getModel() + "(" + product.getModel_number() +
                                        ")\nStore: " + product.getStore() + "\nMarca: " + product.getMarca() + "\nColore: " + product.getColor() + "\nDimensione: " + product.getSize() + "(" + product.getUnit_system() +
                                        ")\nDisponibili: " + product.getNr_available() + " (" + product.getNr_available_store() + " in negozio)").setPositiveButton("Scala disponibilità", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Ion.with(getActivity(), Constants.API_PRODUCT_DEDUCT_STOCK_URL)
                                                .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:italist".getBytes(), 0))
                                                .setBodyParameter("key", mLoginKey)
                                                .setBodyParameter("qrcode", rawResult.getContents())
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject jsonObject) {
                                                        if (jsonObject.get("error").getAsInt() == 0)
                                                            Toast.makeText(getActivity(), "Disponibilità scalata", Toast.LENGTH_SHORT).show();
                                                        else
                                                            Toast.makeText(getActivity(), jsonObject.get("error_description").getAsString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).setNegativeButton("Annulla", null).show();
                            }
                        }
                    });
        */
        }
    }

}





