package com.deveyes.italist.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deveyes.italist.Constants;
import com.deveyes.italist.model.Product;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


public class ProductDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_product_detail);

         /*
           @author hossain
           changed for toolbar instead of action bar
         */
        getActionBarToolbar();

        String qrCode = getIntent().getStringExtra("qrcode");
        String loginKey = getIntent().getStringExtra("loginkey");
        Log.d("qrCode:: ",qrCode+":: loginKey:: "+loginKey);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ProductDetailFragment.newInstance(qrCode, loginKey))
                    .commit();
        }

       // getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

            /*
        @author hossain
       @mthod  getActionBarToolbar()
     */

    private Toolbar mActionBarToolbar;
    private Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            Log.e("mActionBarToolbar::::", "" + mActionBarToolbar);
            if (mActionBarToolbar != null) {

                setSupportActionBar(mActionBarToolbar);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                mActionBarToolbar.setLogo(R.drawable.actionbar_logo);

            }
        }
        return mActionBarToolbar;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ProductDetailFragment extends Fragment {

        public static ProductDetailFragment newInstance(String qrCode, String loginKey)
        {
            ProductDetailFragment fragment = new ProductDetailFragment();

            fragment.mQRCode = qrCode;
            fragment.mLoginKey = loginKey;

            return fragment;
        }

        private String mQRCode;
        private String mLoginKey;

        public ProductDetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);

            Button bCancel = (Button) rootView.findViewById(R.id.bCancel);
            bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //getActivity().finish();
                    startActivity(mainIntent);
                }
            });

            Log.d(Constants.TAG, "Test: " + mQRCode + ", " + mLoginKey);
            Ion.with(getActivity(), Constants.API_PRODUCT_INFO_URL)
                    .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:eFfFYFAdp3".getBytes(), 0))
                    .setBodyParameter("key", mLoginKey)
                    .setBodyParameter("qrcode", mQRCode)
                    .as(new TypeToken<Product>() {
                    })
                    .setCallback(new FutureCallback<Product>() {
                        @Override
                        public void onCompleted(Exception e, Product product) {
                            if (e != null) {
                                Log.d("ProductDetailActivity:","Is called1..");
                               // Log.e(Constants.TAG, "Errore Scansione " + e.getMessage());
                            } else {
                                if (product.getError() == 0) {
                                    Log.d("ProductDetailActivity:","Is called2..");
                                    // Non dovrebbe essere necessario, ma giusto in caso
                                    getView().findViewById(R.id.llInvalidQRCodeError).setVisibility(View.GONE);

                                    final ImageView ivProductPhoto = (ImageView) rootView.findViewById(R.id.ivProductPhoto);
                                    ImageLoader.getInstance().displayImage(product.getProduct_image(), ivProductPhoto, new ImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                            ivProductPhoto.setImageResource(R.drawable.photo_error);
                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            // Impostiamo il center crop per evitare stiramenti
                                            ivProductPhoto.setBackgroundColor(Color.WHITE);
                                            ivProductPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {

                                        }
                                    });

                                    TextView tvBrandName = (TextView) rootView.findViewById(R.id.tvBrandName);
                                    TextView tvProductName = (TextView) rootView.findViewById(R.id.tvProductName);
                                    TextView tvProductInfo = (TextView) rootView.findViewById(R.id.tvProductInfo);

                                    tvBrandName.setText(product.getMarca());
                                    tvProductName.setText(product.getModel());
                                    tvProductInfo.setText(Html.fromHtml("<img src='bullet.png' /> <font color='#767676'>Disponibilità:</font> " + product.getNr_available() + "<br />\n" +
                                            "<img src='bullet.png' /> <font color='#767676'>Taglia:</font> " + product.getSize() + "<br />\n" +
                                            "<img src='bullet.png' /> <font color='#767676'>Colore:</font> " + product.getColor() + "<br />\n" +
                                            "<img src='bullet.png' /> <font color='#767676'>Codice prodotto:</font> " + product.getModel_number(), new Html.ImageGetter() {
                                        @Override
                                        public Drawable getDrawable(String s) {
                                            Drawable bulletIcon = getResources().getDrawable(R.drawable.bullet_point);
                                            bulletIcon.setBounds(0, -bulletIcon.getIntrinsicHeight(), bulletIcon.getIntrinsicWidth(), bulletIcon.getIntrinsicHeight() / 2);
                                            return bulletIcon;
                                        }
                                    }, null));

                                    Button bDeductStock = (Button) rootView.findViewById(R.id.bDeductStock);

                                    // Se il prodotto è disponibile attiviamo il bottone, altrimenti avvisiamo
                                    if (Integer.valueOf(product.getNr_available()) == 0)
                                    {
                                        new AlertDialog.Builder(getActivity()).setTitle("Attenzione").setMessage("Il prodotto non è disponibile nel tuo catalogo italist.").setPositiveButton("OK", null).show();
                                    } else {
                                        bDeductStock.setEnabled(true);

                                        // E impostiamo il Listener
                                        bDeductStock.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Ion.with(getActivity(), Constants.API_PRODUCT_DEDUCT_STOCK_URL)
                                                        .setHeader("Authorization", "Basic " + Base64.encodeToString("italist:eFfFYFAdp3".getBytes(), 0))
                                                        .setBodyParameter("key", mLoginKey)
                                                        .setBodyParameter("qrcode", mQRCode)
                                                        .asJsonObject()
                                                        .setCallback(new FutureCallback<JsonObject>() {
                                                            @Override
                                                            public void onCompleted(Exception e, JsonObject jsonObject) {
                                                                if (jsonObject.get("error").getAsInt() == 0) {
                                                                    Toast.makeText(getActivity(), "Disponibilità scalata", Toast.LENGTH_SHORT).show();
                                                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    //getActivity().finish();
                                                                    startActivity(mainIntent);
                                                                } else
                                                                    Toast.makeText(getActivity(), jsonObject.get("error_description").getAsString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                                    }

                                } else {
                                    switch (product.getError())
                                    {
                                        case 2:
                                            //Codice QR non valido
                                            getView().findViewById(R.id.llInvalidQRCodeError).setVisibility(View.VISIBLE);
                                            Button bOkay = (Button) getView().findViewById(R.id.bOkay);
                                            bOkay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    //getActivity().finish();
                                                    startActivity(mainIntent);
                                                }
                                            });
                                            break;
                                        default:
                                            // Altro errore
                                            Toast.makeText(getActivity(), "Debug: Errore " + product.getError() + ": " + product.getError_description(), Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }

                                /*
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
                                }).setNegativeButton("Annulla", null).show();*/
                            }
                        }
                    });

            return rootView;
        }
    }
}
