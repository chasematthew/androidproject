package com.chaseswanstrom.sentiment;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by chaseswanstrom on 4/12/16.
 */
public class TwitterManager extends Application implements Serializable {

    private String tweetText;
    private String cleanQuery;
    private Double totalScoreFinal = 0.0;
    private Double totalScoreFinal2 = 0.0;
    private Double maxPos1 = 0.0;
    private Double maxPos2 = 0.0;
    private String unPos1;
    private String unPos2;
    private String dnPos1;
    private String dnPos2;
    private String tweetPos1;
    private String tweetPos2;
    private String imgPos1;
    private String imgPos2;
    private Double maxNeg1 = 0.0;
    private Double maxNeg2 = 0.0;
    private String unNeg1;
    private String unNeg2;
    private String dnNeg1;
    private String dnNeg2;
    private String tweetNeg1;
    private String tweetNeg2;
    private String imgNeg1;
    private String imgNeg2;
    private int firstPosCount = 0;
    private int secondPosCount = 0;
    private int firstNegCount = 0;
    private int secondNegCount = 0;
    private int firstNeutralCount = 0;
    private int secondNeutralCount = 0;
    private Bitmap img1BMP;
    private Bitmap img2BMP;
    private Bitmap img3BMP;
    private Bitmap img4BMP;

    public Double getTotalScoreFinal() {
        return totalScoreFinal;
    }

    public Double getTotalScoreFinal2() {
        return totalScoreFinal2;
    }

    public String getUnPos1() {
        return unPos1;
    }

    public String getUnPos2() {
        return unPos2;
    }

    public String getDnPos1() {
        return dnPos1;
    }

    public String getDnPos2() {
        return dnPos2;
    }

    public String getTweetPos1() {
        return tweetPos1;
    }

    public String getTweetPos2() {
        return tweetPos2;
    }

    public String getUnNeg1() {
        return unNeg1;
    }

    public String getUnNeg2() {
        return unNeg2;
    }

    public String getDnNeg1() {
        return dnNeg1;
    }

    public String getDnNeg2() {
        return dnNeg2;
    }

    public String getTweetNeg1() {
        return tweetNeg1;
    }

    public String getTweetNeg2() {
        return tweetNeg2;
    }

    public int getFirstPosCount() {
        return firstPosCount;
    }

    public int getSecondPosCount() {
        return secondPosCount;
    }

    public int getFirstNegCount() {
        return firstNegCount;
    }

    public int getSecondNegCount() {
        return secondNegCount;
    }

    public int getFirstNeutralCount() {
        return firstNeutralCount;
    }

    public int getSecondNeutralCount() {
        return secondNeutralCount;
    }

    public Bitmap getImg1BMP() {
        return img1BMP;
    }

    public Bitmap getImg2BMP() {
        return img2BMP;
    }

    public Bitmap getImg3BMP() {
        return img3BMP;
    }

    public Bitmap getImg4BMP() {
        return img4BMP;
    }

    private ArrayList<String> tweetArray = new ArrayList<String>();

    public ArrayList<String> getTweetArray() {
        return tweetArray;
    }


    private int limit = 10; //the number of retrieved tweets
    ConfigurationBuilder cb;
    Twitter twitter;

    public TwitterManager() {
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("OAuthConsumerKey");
        cb.setOAuthConsumerSecret("nOAuthConsumerSecret");
        cb.setOAuthAccessToken("AuthAccessToken");
        cb.setOAuthAccessTokenSecret("OAuthAccessTokenSecret");
        twitter = new TwitterFactory(cb.build()).getInstance();
    }

    public void performQuery(String inQuery) throws InterruptedException, IOException {
        Query query = new Query(inQuery);
        query.setCount(limit);
        try {
            QueryResult r;
            r = twitter.search(query);
            Log.v("Log R>>>", r.toString());
            ArrayList ts = (ArrayList) r.getTweets();
            for (int i = 0; i < limit - 1; ++i) {
                if (ts.get(i) != null) {
                    Status t = (Status) ts.get(i);
                    tweetText = t.getText().replace("&", "");
                    cleanQuery = Uri.encode(tweetText);
                    tweetArray.add(tweetText);
                    Log.v("tweet: ", tweetText);
                }
                URL url;
                try {
                    HttpURLConnection urlConnection = null;
                    String api = "https://api.havenondemand.com/1/api/sync/analyzesentiment/v1?text=" + cleanQuery + "&apikey=yourkey";
                    url = new URL(api);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    String sentimentString = convertStreamToString(in);
                    Log.e("sentiment string", sentimentString);
                    JSONObject jsonObject = new JSONObject(sentimentString);
                    double score = 0;
                    for (int j = 0; j < jsonObject.length(); ++j) {
                        JSONObject sentimentJson = jsonObject.getJSONObject("aggregate");
                        String jsonScoreString = sentimentJson.getString("score");
                        Double jsonScore = Double.parseDouble(jsonScoreString);
                        score += jsonScore;
                        if (score > 0.0) {
                            ++firstPosCount;
                            System.out.println(firstPosCount + "firstposcount");
                        }
                        if (score < 0.0) {
                            ++firstNegCount;
                        }
                        if (score == 0.0) {
                            ++firstNeutralCount;
                        }
                    }
                    if (score > maxPos1) {
                        maxPos1 = score;
                        Status s = (Status) ts.get(i);
                        User u = s.getUser();
                        imgPos1 = u.getProfileImageURL();
                        unPos1 = u.getScreenName();
                        dnPos1 = u.getName();
                        tweetPos1 = s.getText();
                    }

                    if (score < maxNeg1) {
                        maxNeg1 = score;
                        Status s = (Status) ts.get(i);
                        User u = s.getUser();
                        imgNeg1 = u.getProfileImageURL();
                        unNeg1 = u.getScreenName();
                        dnNeg1 = u.getName();
                        tweetNeg1 = s.getText();
                    }

                    double totalScore = score / 3.0;
                    totalScoreFinal += totalScore;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d("final score tweet 1", Double.toString(totalScoreFinal));
        } catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);

        }
    }

    public void performQuery2(String inQuery) throws InterruptedException, IOException {

        Query query = new Query(inQuery);
        query.setCount(limit);
        try {
            QueryResult r;
            r = twitter.search(query);
            ArrayList ts = (ArrayList) r.getTweets();

            for (int i = 0; i < limit - 1; ++i) {
                if (ts.get(i) != null) {
                    Status t = (Status) ts.get(i);
                    tweetText = t.getText().replace("&", "");
                    cleanQuery = Uri.encode(tweetText);
                    Log.v("tweet: ", tweetText);
                }
                URL url;
                try {
                    HttpURLConnection urlConnection = null;
                    String api = "https://api.havenondemand.com/1/api/sync/analyzesentiment/v1?text=" + cleanQuery + "&apikey=yourkey";
                    url = new URL(api);
                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    InputStream in = urlConnection.getInputStream();
                    String sentimentString = convertStreamToString(in);
                    Log.e("sentiment string2", sentimentString);
                    JSONObject jsonObject = new JSONObject(sentimentString);
                    double score = 0;
                    for (int j = 0; j < jsonObject.length(); ++j) {
                        JSONObject sentimentJson = jsonObject.getJSONObject("aggregate");
                        String jsonScoreString = sentimentJson.getString("score");
                        Double jsonScore = Double.parseDouble(jsonScoreString);
                        score += jsonScore;
                        if (score > 0.0) {
                            ++secondPosCount;
                            System.out.println(secondPosCount + "secondposcount");
                        }
                        if (score < 0.0) {
                            ++secondNegCount;
                        }
                        if (score == 0.0) {
                            ++secondNeutralCount;
                        }
                    }

                    if (score > maxPos2) {
                        maxPos2 = score;
                        Status s = (Status) ts.get(i);
                        User u = (User) s.getUser();
                        imgPos2 = u.getProfileImageURL();
                        unPos2 = u.getScreenName();
                        dnPos2 = u.getName();
                        tweetPos2 = s.getText();
                    }

                    if (score < maxNeg2) {
                        maxNeg2 = score;
                        Status s = (Status) ts.get(i);
                        User u = (User) s.getUser();
                        imgNeg2 = u.getProfileImageURL();
                        unNeg2 = u.getScreenName();
                        dnNeg2 = u.getName();
                        tweetNeg2 = s.getText();
                    }
                    double totalScore = score / 3.0;
                    totalScoreFinal2 += totalScore;
                    Log.d("total score", Double.toString(totalScore));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d("final score 2", Double.toString(totalScoreFinal2));
        } catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
        }

        img1BMP = getBitmapFromURL(imgPos1);
        img2BMP = getBitmapFromURL(imgPos2);
        img3BMP = getBitmapFromURL(imgNeg1);
        img4BMP = getBitmapFromURL(imgNeg2);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    //method to return the input stream from sentiment api to a java string
    String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

}
