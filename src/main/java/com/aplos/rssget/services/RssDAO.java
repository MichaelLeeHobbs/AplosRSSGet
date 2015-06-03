package com.aplos.rssget.services;

import com.aplos.rssget.Constants;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: Aplos RSS Get
 * Created by Michael Hobbs on 4/7/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class RssDAO implements Runnable {
    static final Logger logger = LogManager.getLogger(RssDAO.class.getName());
    static Queue<Pair<Integer, String>>  work;
    static Map<Integer, SyndFeed> result;

    static {
        work = new ConcurrentLinkedQueue<Pair<Integer, String>>();
        result = new ConcurrentHashMap<Integer, SyndFeed>(new HashMap<Integer, SyndFeed>());
    }

    public static Map<Integer, SyndFeed> getFeeds(Map<Integer, String> feedMap, int threadCount){

        for (Map.Entry entry : feedMap.entrySet()){
            work.add(new Pair(entry.getKey(), entry.getValue()));
            if (Constants.TESTING) break;
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++){
            executor.execute(new RssDAO());
        }
        executor.shutdown();
        while (!executor.isTerminated()) { /* wait for work to be done */ }
        return result;

    }

    public SyndFeed getFeed(URLConnection feedUrl) throws FeedException, IOException {
        logger.entry("Reading RSS feed.");


        // get feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        logger.exit();
        return feed;
    }

    @Override
    public void run() {
        Logger badFeeds = LogManager.getLogger("bad.feeds");

        while (!work.isEmpty()){
            Pair<Integer, String> myWork = work.remove();

            //logger.debug("String to Url: " + feedURL);
            try {
                URLConnection urlConnection = new URL(myWork.getValue()).openConnection();
                urlConnection.setConnectTimeout(Constants.RSS_READ_TIME_OUT);
                SyndFeed feed = getFeed(urlConnection);
                result.put(myWork.getKey(), feed);
            } catch (FeedException e) {
                String msg = "Feed error: FeedException: " + e.getMessage();
                logger.error(msg);
                logger.catching(e);
                System.out.println(msg);
            } catch (IOException e) {
                String msg = "Feed error: IOException: " + e.getMessage();
                badFeeds.error(myWork.getValue());
                logger.error(msg);
                logger.catching(e);
                System.out.println(msg);
            }
        }
        logger.exit();

    }
}
