/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawlertwitter;

import datastore.Csv;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author ahmadluky
 */
public class TwitterDataStream {
    
    
    private final String TWITTER_CONSUMER_KEY = "DirV4f3gp3vIUVZDZH5W3k6mT";
    private final String TWITTER_CONSUMER_SECRET = "GYPvZahLYb0JKw1etZlByBEtXZulpQnt8TcQfGtk0d7uFkjw6q";
    private final String OAUTH_TOKEN = "58413351-7sWPSdF3cXvRdq7DwLWfnAgABX6bFJWNpC3LXWUyB";
    private final String OAUTH_SECRET = "fUUwbYJmnDMAtHSLBuYycKzx9RvLKvcKK9Bke1WoqHljo";
    
    private TwitterStream twitterStream; 
    private String[] keywords;
    FileOutputStream fos;
    Csv c;
    
    public TwitterDataStream(){
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
          .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
          .setOAuthAccessToken(OAUTH_TOKEN)
          .setOAuthAccessTokenSecret(OAUTH_SECRET);
        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
    }
    
    public void startTwitter() {
 
        c                   = new Csv();
//        String keywordString = prop.getProperty("@jokowi_do2, @Pak_JK");
//        keywords = keywordString.split(",");
//        for (int i = 0; i < keywords.length; i++) {
//            keywords[i] = keywords[i].trim();
//        }
        String keywords[] = {"@jokowi_do2", "@Pak_JK"};
        twitterStream.addListener(listener);
        System.out.println("Starting down Twitter sample stream...");
        FilterQuery query = new FilterQuery().track(keywords);
        twitterStream.filter(query);
 
    }
    
    public void stopTwitter() {
 
        System.out.println("Shutting down Twitter sample stream...");
        twitterStream.shutdown();
 
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    StatusListener listener = new StatusListener() {
 
        Format formatter    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public void onStatus(Status status) {
            System.out.println(status.getUser().getScreenName() + ": " + status.getText() + " > " + formatter.format(status.getCreatedAt()));
            System.out.println("timestamp : "+ String.valueOf(status.getCreatedAt().getTime()));
            try {
                c.writeCsvFile("stream_opinion",
                                String.valueOf(status.getId()),
                                status.getUser().getScreenName(),
                                status.getText(),
                                formatter.format(status.getCreatedAt()),
                                ""+status.getGeoLocation()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
        public void onScrubGeo(long userId, long upToStatusId) {}
        public void onException(Exception ex) {}
        public void onStallWarning(StallWarning warning) {}
    };
}
