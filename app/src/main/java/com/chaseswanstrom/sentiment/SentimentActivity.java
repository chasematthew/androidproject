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
import java.util.ArrayList;

public class SentimentActivity extends AppCompatActivity {


    //Animation points
    private static final float ROTATE1 = -36.0f;
    private static final float ROTATE2 = 36.0f;

    public Boolean isDone = false;
    TwitterManager t = new TwitterManager();
    public static String queryWord1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(4, 0xffffffff);

        final Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setVisibility(View.INVISIBLE);
        final Button battleModeButton = (Button) findViewById(R.id.battleModeButton);
        final Button results = (Button) findViewById(R.id.buttonResults);
        results.setVisibility(View.INVISIBLE);
        final Button sentimentButton = (Button) findViewById(R.id.buttonSentiment);
        sentimentButton.setBackground(gd);
        final ImageView imgSpinner = (ImageView) findViewById(R.id.imgSpinner);
        imgSpinner.setVisibility(View.INVISIBLE);


        if (sentimentButton != null) {
            sentimentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText qw = (EditText) findViewById(R.id.editTextQueryWord);
                    queryWord1 = qw.getText().toString();
                    final Button battleModeButton = (Button) findViewById(R.id.battleModeButton);
                    battleModeButton.setVisibility(View.INVISIBLE);
                    if (isNetworkAvailable() == true) {
                        tweetAsync ta = new tweetAsync();
                        ta.execute(queryWord1);
                        sentimentButton.setVisibility(View.INVISIBLE);
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

        battleModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SentimentActivity.this, BattleActivity.class);
                SentimentActivity.this.startActivity(myIntent);
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SentimentActivity.this, SentimentActivity.class);
                SentimentActivity.this.startActivity(myIntent);
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
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
            imgSpinner.setAnimation(null);
            imgSpinner.setVisibility(View.INVISIBLE);

            Button results = (Button) findViewById(R.id.buttonResults);
            results.setVisibility(View.VISIBLE);
            results.setBackground(gd);

            final ArrayList<String> tweetArray = t.getTweetArray();

            results.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startResultIntent();
                }
            });

            TextView tv = (TextView) findViewById(R.id.textViewScore);
            tv.setText(t.getTotalScoreFinal().toString());
            String score = t.getTotalScoreFinal().toString();
            if (score.length() > 6) {
                score = score.substring(0, 5);
            }
            tv.setText(queryWord1.toString().toUpperCase() + " SCORE IS " + score);
            final Button retryButton = (Button) findViewById(R.id.retryButton);
            retryButton.setVisibility(View.VISIBLE);
            tv.setBackground(gd);
        }
    }

    private void startResultIntent() {
        Intent intent = new Intent(getApplicationContext(), singleModeResults.class);
        intent.putExtra("positive", t.getFirstPosCount() / 3 + "");
        intent.putExtra("neutral", t.getFirstNeutralCount() / 3 + "");
        intent.putExtra("negative", t.getFirstNegCount() / 3 + "");
        intent.putExtra("unPos1", t.getUnPos1());
        intent.putExtra("dnPos1", t.getDnPos1());
        intent.putExtra("imgPos1", t.getImg1BMP());
        intent.putExtra("tweetPos1", t.getTweetPos1());
        intent.putExtra("unNeg1", t.getUnNeg1());
        intent.putExtra("dnNeg1", t.getDnNeg1());
        intent.putExtra("imgNeg1", t.getImg3BMP());
        intent.putExtra("tweetNeg1", t.getTweetNeg1());
        intent.putExtra("query1", queryWord1);
        Bundle extra = new Bundle();
        extra.putSerializable("objects", t.getTweetArray());
        intent.putExtra("tweetArray", t.getTweetArray());
        startActivity(intent);
    }
}



