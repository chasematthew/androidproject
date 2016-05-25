package com.chaseswanstrom.sentiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.Serializable;

public class BattleActivity extends AppCompatActivity implements Serializable {

    TwitterManager t = new TwitterManager();
    private static final float ROTATE1 = -36.0f;
    private static final float ROTATE2 = 36.0f;
    private static String queryWord1 = "";
    private static String queryWord2 = "";

    public static String getQueryWord1() {
        return queryWord1;
    }

    public static String getQueryWord2() {
        return queryWord2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(4, 0xffffffff);

        final Button singleModeButton = (Button) findViewById(R.id.sentimentButtonMode);
        final Button retryButton = (Button) findViewById(R.id.retryButton);
        final Button results = (Button) findViewById(R.id.buttonResults);
        final Button sentimentButton = (Button) findViewById(R.id.buttonSentiment);
        final ImageView imgSpinner = (ImageView) findViewById(R.id.imgSpinner);
        retryButton.setVisibility(View.INVISIBLE);
        results.setVisibility(View.INVISIBLE);
        sentimentButton.setBackground(gd);
        imgSpinner.setVisibility(View.INVISIBLE);

        if (sentimentButton != null) {
            sentimentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText qw = (EditText) findViewById(R.id.editTextQueryWord);
                    final EditText qw2 = (EditText) findViewById(R.id.editTextQueryWord2);
                    final Button singleModeButton = (Button) findViewById(R.id.sentimentButtonMode);
                    singleModeButton.setVisibility(View.INVISIBLE);
                    if (isNetworkAvailable() == true) {
                        tweetAsync ta = new tweetAsync();
                        queryWord1 = qw.getText().toString();
                        queryWord2 = qw2.getText().toString();
                        ta.execute();
                        sentimentButton.setVisibility(View.INVISIBLE);
                        imgSpinner.setVisibility(View.VISIBLE);
                        RotateAnimation squash = new RotateAnimation(ROTATE1, ROTATE2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        squash.setDuration(500);
                        squash.setRepeatMode(Animation.REVERSE);
                        squash.setRepeatCount(Animation.INFINITE);
                        imgSpinner.startAnimation(squash);
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not connect, please check Internet Connectivity",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        singleModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(BattleActivity.this, SentimentActivity.class);
                BattleActivity.this.startActivity(myIntent);
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(BattleActivity.this, BattleActivity.class);
                BattleActivity.this.startActivity(myIntent);
            }
        });
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, BattleActivity.class);
        startActivity(intent);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public class tweetAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    t.performQuery(queryWord1);
                    t.performQuery2(queryWord2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(5);
            gd.setStroke(4, 0xffffffff);

            ImageView imgSpinner = (ImageView) findViewById(R.id.imgSpinner);
            Button results = (Button) findViewById(R.id.buttonResults);
            imgSpinner.setAnimation(null);
            results.setVisibility(View.VISIBLE);
            results.setBackground(gd);
            results.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startResultIntent();

                }
            });

            TextView tv = (TextView) findViewById(R.id.textViewScore);
            final Button retryButton = (Button) findViewById(R.id.retryButton);
            retryButton.setVisibility(View.VISIBLE);
            imgSpinner.setVisibility(View.INVISIBLE);
            tv.setText(t.getTotalScoreFinal().toString());

            if (t.getTotalScoreFinal() > t.getTotalScoreFinal2()) {
                tv.setText(queryWord1.toString().toUpperCase() + " WINS!");
                tv.setBackground(gd);
            } else {
                tv.setText(queryWord2.toString().toUpperCase() + " WINS!");
                tv.setBackground(gd);
            }
        }

    }

    private void startResultIntent() {
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra("positive", t.getFirstPosCount() / 3 + "");
        intent.putExtra("neutral", t.getFirstNeutralCount() / 3 + "");
        intent.putExtra("negative", t.getFirstNegCount() / 3 + "");
        intent.putExtra("positive2", t.getSecondPosCount() / 3 + "");
        intent.putExtra("neutral2", t.getSecondNeutralCount() / 3 + "");
        intent.putExtra("negative2", t.getSecondNegCount() / 3 + "");
        intent.putExtra("unPos1", t.getUnPos1());
        intent.putExtra("dnPos1", t.getDnPos1());
        intent.putExtra("imgPos1", t.getImg1BMP());
        intent.putExtra("unPos2", t.getUnPos2());
        intent.putExtra("dnPos2", t.getDnPos2());
        intent.putExtra("imgPos2", t.getImg2BMP());
        intent.putExtra("tweetPos1", t.getTweetPos1());
        intent.putExtra("tweetPos2", t.getTweetPos2());
        intent.putExtra("unNeg1", t.getUnNeg1());
        intent.putExtra("dnNeg1", t.getUnNeg1());
        intent.putExtra("imgNeg1", t.getImg3BMP());
        intent.putExtra("unNeg2", t.getUnNeg2());
        intent.putExtra("dnNeg2", t.getDnNeg2());
        intent.putExtra("imgNeg2", t.getImg4BMP());
        intent.putExtra("tweetNeg1", t.getTweetNeg1());
        intent.putExtra("tweetNeg2", t.getTweetNeg2());
        intent.putExtra("query1", queryWord1);
        intent.putExtra("query2", queryWord2);
        intent.putExtra("isSingle", false);
        Bundle extra = new Bundle();
        extra.putSerializable("objects", t.getTweetArray());
        intent.putExtra("tweetArray", t.getTweetArray());
        startActivity(intent);
    }
}


