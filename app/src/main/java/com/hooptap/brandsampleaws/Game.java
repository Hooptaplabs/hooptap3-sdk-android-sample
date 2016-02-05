package com.hooptap.brandsampleaws;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hooptap.sdkbrandclub.Interfaces.HooptapJSInterface;
import com.hooptap.sdkbrandclub.Models.HooptapGameStatus;
import com.hooptap.sdkbrandclub.Models.HooptapWebView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 12/01/16.
 */
public class Game extends AppCompatActivity {


    private HooptapWebView mWebView;
    private JSONObject game;
    private String urlCupon;
    private String reward_id;
    private String image;
    private boolean landscape;


    @Bind(R.id.bg)
    ImageView bg;


    protected void onDestroy() {
        //Metodo para eliminar cualquier Webview y evitar que siga funcionando en background (Se puede omitir esta funcion)
        mWebView.clearCache(true);
        if (mWebView != null) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.RootView);
            rl.removeView(mWebView);
            mWebView.removeAllViews();

            mWebView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.htwebviewsample);
        ButterKnife.bind(this);


       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            game = (JSONObject) bundle.getSerializable("item");
        } else {
            finish();
        }*/



        /*HooptapApi.getItemDetail("5694c3df891c81963be4c7a1","5579aaab16721c325c6ae396",  new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                   launchGame(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(getApplicationContext(), responseError.getReason(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
*/

        mWebView = new HooptapWebView(this);


        mWebView = (HooptapWebView) findViewById(R.id.webview);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        launchGame();

        //Añadimos la interface donde recibiremos el resultado del juego
        mWebView.addInterface(new HooptapJSInterface() {
            @Override
            public void gameDidFinish(HooptapGameStatus gameStatus) {
                Log.e("gameStatus", gameStatus.getScore() + "**" + gameStatus.getResult() + "**" + gameStatus.getRewards());
                Toast.makeText(Game.this, "Has ganado " + gameStatus.getScore() + "**", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Game.this.finish();
                    }
                }, 2000);
                /* HooptapApi.play(HTApplication.getPathConfig(), game.getId(), gameStatus.getScore(), HTApplication.getTinydb().getString("userId"), new HooptapCallback<HashMap<String, Object>>() {
                            @Override
                            public void onSuccess(HashMap<String, Object> stringObjectHashMap) {
                                if (landscape) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                            mWebView.setBackgroundColor(Color.WHITE);
                                        }
                                    });

                                }
                                Log.e("respuesta", stringObjectHashMap.toString());
                                Boolean res = ((Boolean) stringObjectHashMap.get("passed")).booleanValue();
                                if (res) {
                                    if (!isFinishing())
                                        UtilsFuctions.notaFin(Game.this, true, game);

                                } else {
                                    Log.e("entroError", res + "");
                                    if (!isFinishing())
                                        UtilsFuctions.notaFin(Game.this, false, game);
                                    //notaFin(false);
                                }


                            }

                            @Override
                            public void onError(ResponseError responseError) {
                                Toast.makeText(Game.this, responseError.getReason(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
*/
            }

            @Override
            public void gameDidStart() {
                Toast.makeText(Game.this, "El juego está apunto de empezar, ¡Preparate!", Toast.LENGTH_LONG).show();
            }


        });

        mWebView.setWebChromeClient(new WebChromeClient());
        //Definimos un cliente para oculta el loader cuando el juego se ha cargado o para finalizar la activity si da error al cargar (Este método no es obligatorio)
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(android.webkit.WebView view, String url) {
            }

            public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Game.this, "Se ha producido un error " + errorCode + " : " + description, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onReceivedSslError(android.webkit.WebView view, @NonNull SslErrorHandler handler, SslError error) {

                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

                return false;
            }
        });

    }

    private void launchGame() {
        try {

            urlCupon = "http://comun-ht.s3.amazonaws.com/html5/7Differences/game/index.html?bucket=hooptap&configId=557a911d9dc0212c2eeb5ddb&app=46576686f6f707461702e627";//game.getUrl_game();
            if (!urlCupon.contains("typeGame=videoquiz")) {
                Log.e("webview", "entro url contiene video quiz");
                urlCupon = urlCupon + "&noBackground=true";
            }
            image = "http://comun-ht.s3.amazonaws.com/html5/7Differences/assets/background.jpg";
            if ("portrait".equals("landscape")) {
                landscape = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Picasso.with(this).load(image).into(bg);

        mWebView.loadUrl("http://comun-ht.s3.amazonaws.com/html5/7Differences/game/index.html?bucket=hooptap&configId=557a911d9dc0212c2eeb5ddb&app=46576686f6f707461702e627");

        if (!urlCupon.contains("typeGame=videoquiz")) {
            mWebView.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setLayerType(android.webkit.WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void launchGame(JSONObject itemGame) {
        try {
            game = itemGame;
            urlCupon = game.getString("url_game");//game.getUrl_game();
            if (!urlCupon.contains("typeGame=videoquiz")) {
                Log.e("webview", "entro url contiene video quiz");
                urlCupon = urlCupon + "&noBackground=true";
            }
            JSONObject json_extra = itemGame;//getExtra_data();
            image = json_extra.getString("game_bg");
            if (game.getString("orientation").equals("landscape")) {
                landscape = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Picasso.with(this).load(image).into(bg);

        mWebView.loadUrl(urlCupon);

        if (!urlCupon.contains("typeGame=videoquiz")) {
            mWebView.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setLayerType(android.webkit.WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }
/*
    private void notaFin(boolean result) {
        LayoutInflater inflater = (HTWebViewSample.this).getLayoutInflater();
        View layout = inflater.inflate(R.layout.nota_fin, (ViewGroup) (HTWebViewSample.this).findViewById(R.id.layout_root));
        final Dialog alertDialog = new Dialog(HTWebViewSample.this);

        TextView resultado = (TextView) layout.findViewById(R.id.resultado);
        TextView titulo = (TextView) layout.findViewById(R.id.titulo);

        final ImageHooptap img = (ImageHooptap) layout.findViewById(R.id.photo_reto);
        final Button boton1 = (Button) layout.findViewById(R.id.boton1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                alertDialog.dismiss();
            }
        });
        final Button boton2 = (Button) layout.findViewById(R.id.boton2);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getOrientation().equals("landscape")) {
                    landscape = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                if (!urlCupon.contains("typeGame=videoquiz")) {
                    mWebView.setBackgroundColor(Color.TRANSPARENT);
                    mWebView.setLayerType(android.webkit.WebView.LAYER_TYPE_SOFTWARE, null);
                }
                mWebView.loadUrl(game.getUrl_game());
                alertDialog.dismiss();
            }
        });

        try {
            JSONObject strings = new JSONObject(config.getStrings());
            if (result) {
                img.setImageDrawable(getResources().getDrawable(R.drawable.ic_superada));
                titulo.setText(strings.getString("win_message_title"));
                if (game.getEnd_msgOK().equals("")) {
                    resultado.setText(strings.getString("win_message"));
                } else {
                    resultado.setText(game.getEnd_msgOK());
                }
                boton1.setText(strings.getString("win_message_button_close"));
                boton2.setText(strings.getString("win_message_button_again"));

            } else {
                img.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_superada));

                titulo.setText(strings.getString("lose_message_title"));
                if (game.getEnd_msgOK().equals("")) {
                    resultado.setText(strings.getString("lose_message"));
                } else {
                    resultado.setText(game.getEnd_msgNO());
                }
                boton1.setText(strings.getString("win_message_button_close"));
                boton2.setText(strings.getString("lose_message_button_again"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setContentView(layout);

        alertDialog.show();
    }*/

    /*private void notaFinPremio(final HooptapGameStatus gameStatus) {
        String reward_img = "";
        try {
            JSONObject reward = (JSONObject) gameStatus.getRewards().get(0);
            reward_id = reward.getString("redeem_code");
            reward_img = reward.getJSONObject("reward").getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (HTWebViewSample.this).getLayoutInflater();
        View layout = inflater.inflate(R.layout.nota_fin, (ViewGroup) (HTWebViewSample.this).findViewById(R.id.layout_root));
        final Dialog alertDialog = new Dialog(HTWebViewSample.this);
        TextView resultado = (TextView) layout.findViewById(R.id.resultado);
        TextView titulo = (TextView) layout.findViewById(R.id.titulo);
        TextView text_name = (TextView) layout.findViewById(R.id.text_name);
        TextView text_mail = (TextView) layout.findViewById(R.id.text_mail);
        final EditText edit_name = (EditText) layout.findViewById(R.id.edit_name);
        final EditText edit_mail = (EditText) layout.findViewById(R.id.edit_mail);
        LinearLayout ll_premio = (LinearLayout) layout.findViewById(R.id.ll_premio);
        ll_premio.setVisibility(View.VISIBLE);
        final ImageView img = (ImageView) layout.findViewById(R.id.photo_reto);
        final Button boton1 = (Button) layout.findViewById(R.id.boton1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_name.getText().toString().trim().equals("") || !edit_mail.getText().toString().trim().equals("")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(edit_mail.getText().toString()).matches()) {
                        Log.e("REWARD_ID", reward_id);
                        HooptapApi.redem(HTApplication.getPathConfig(), reward_id, edit_name.getText().toString(), edit_mail.getText().toString(), new HooptapCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                try {
                                    JSONObject strings = new JSONObject(config.getStrings());
                                    Toast.makeText(getBaseContext(), strings.getString("email_sent"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.mail_enviado), Toast.LENGTH_LONG).show();
                                }
                                alertDialog.dismiss();
                                finish();
                            }
                            @Override
                            public void onError(ResponseError responseError) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mail_incorrecto), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.rellena_campos), Toast.LENGTH_LONG).show();
                }
            }
        });
        final Button boton2 = (Button) layout.findViewById(R.id.boton2);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                alertDialog.dismiss();
            }
        });
        try {
            Picasso.with(this).load(reward_img).transform(new CircleTransform()).into(img);
            //img.setImageDrawable(getResources().getDrawable(R.drawable.gift));
            JSONObject strings = new JSONObject(config.getStrings());
            text_name.setText(strings.getString("reward_box_name"));
            text_mail.setText(strings.getString("reward_box_email"));
            titulo.setText(strings.getString("reward_box_title"));
            resultado.setText(strings.getString("reward_box_text"));
            boton1.setText(strings.getString("reward_box_button"));
            boton2.setText(strings.getString("win_message_button_close"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setContentView(layout);
        alertDialog.show();
    }*/


    class MyChromeClient extends WebChromeClient {


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

        }

        @Override
        public void onHideCustomView() {

        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

            Toast.makeText(Game.this, "Se ha producido un error " + consoleMessage.sourceId() + " : " + consoleMessage.message(), Toast.LENGTH_LONG).show();
            finish();
            return super.onConsoleMessage(consoleMessage);
        }
    }
}
