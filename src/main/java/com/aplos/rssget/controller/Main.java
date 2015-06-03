package com.aplos.rssget.controller;

import java.sql.SQLException;
import java.util.Map;

import com.aplos.rssget.Constants;
import com.aplos.rssget.services.RssDAO;
import com.aplos.rssget.services.SqlDAO;
import com.sun.syndication.feed.synd.SyndFeed;


/**
 * Project: Aplos RSSGet
 * Created by Michael Hobbs on 4/4/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class Main {
    // test
    public static void main(String[] args) {


        SqlDAO sqlDAO = null;
        Map<Integer, String> RSSFeedList = null;
        try {
            sqlDAO = new SqlDAO(Constants.DB_DRIVER);
            sqlDAO.open(Constants.CONNECTION);
            sqlDAO.initialize();

            RSSFeedList = sqlDAO.getRSSFeedList(Constants.DB_RSS_QUERY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1045) {
                System.out.println("Bad user name or password for SQL DB!");
            } else {
                e.printStackTrace();
            }
            sqlDAO.close();
            System.exit(-1);
        }
        catch (Exception e) {
            e.printStackTrace();
            sqlDAO.close();
            System.exit(-1);
        }

        System.out.println("Processing " + RSSFeedList.size() + " feeds using " + Constants.RSS_THREADS + " threads.");
        Map<Integer, SyndFeed> RSSFeeds = RssDAO.getFeeds(RSSFeedList, Constants.RSS_THREADS);

        System.out.println("Inserting " + RSSFeeds.size() + " feeds into DB.");
        try {
            for (Map.Entry<Integer, SyndFeed> entry : RSSFeeds.entrySet()) {
                sqlDAO.insertFeed(Constants.CONNECTION, entry.getKey(), entry.getValue());
            }
        }
        catch (Exception ex) {
            System.out.println("ERROR: "+ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Done.");

        sqlDAO.close();
    }
}
