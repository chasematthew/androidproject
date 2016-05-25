package com.chaseswanstrom.sentiment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.Serializable;

public class singleModeResults extends AppCompatActivity implements Serializable {

    BattleActivity b = new BattleActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mode_results);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(4, 0xffffffff);

        Intent intent = getIntent();
        TextView query1 = (TextView) findViewById(R.id.textViewQuery1term);
        query1.setBackground(gd);
        query1.setText(b.getQueryWord1().toUpperCase());
        TextView results1 = (TextView) findViewById(R.id.textView1results);
        results1.setText("Positive Tweets: " + intent.getStringExtra("positive").toString() + "\n"
                + "Neutral Tweets: " + intent.getStringExtra("neutral").toString() + "\n" + "Negative Tweets: " + intent.getStringExtra("negative").toString());

        Button topMashers = (Button) findViewById(R.id.buttonTM);
        topMashers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTopIntent();
            }
        });

    }

    private void startTopIntent() {
        TwitterManager t = new TwitterManager();
        Intent myIntent = new Intent(singleModeResults.this, TopMashersActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("objects", t.getTweetArray());
        myIntent.putExtra("tweetArray", t.getTweetArray());
        myIntent.putExtra("dnPos1", getIntent().getExtras().getString("dnPos1"));
        myIntent.putExtra("unPos1", getIntent().getExtras().getString("unPos1"));
        myIntent.putExtra("tweetPos1", getIntent().getExtras().getString("tweetPos1"));
        myIntent.putExtra("imgPos1", getIntent().getParcelableExtra("imgPos1"));
        myIntent.putExtra("dnNeg1", getIntent().getExtras().getString("dnNeg1"));
        myIntent.putExtra("unNeg1", getIntent().getExtras().getString("unNeg1"));
        myIntent.putExtra("tweetNeg1", getIntent().getExtras().getString("tweetNeg1"));
        myIntent.putExtra("imgNeg1", getIntent().getParcelableExtra("imgNeg1"));
        myIntent.putExtra("query1", getIntent().getExtras().getString("query1"));
        myIntent.putExtra("isSingle", "yes");
        singleModeResults.this.startActivity(myIntent);
    }
}
