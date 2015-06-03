package com.aplos.rssget.services;

import com.aplos.rssget.Constants;
import com.aplos.utils.Validators;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: aplosrss
 * Created by Michael Hobbs on 4/4/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class SqlDAO {
    static final Logger logger = LogManager.getLogger(SqlDAO.class.getName());

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private boolean isOpen = false;

    /**
     * Constructs a DAO using an SQL driver for RSS feed listing.
     *
     * @param DBDriver SQL driver
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
    public SqlDAO(String DBDriver) throws ClassNotFoundException {
        logger.entry("SqlDao(" + DBDriver + ")");
        // throws exception if fails
        Validators.RegexValidator(DBDriver,
                Constants.DB_DRIVER_VALIDATOR,
                String.format(Constants.MSG_STRING_ILLEGAL_ARGUMENT_EXCEPTION, "DBDriver", Constants.DB_DRIVER_VALIDATOR)
        );
        // This will load the driver, each DB has its own driver
        // throws ClassNotFoundException
        Class.forName(DBDriver);
        logger.exit("SqlDao");

    }

    /**
     * Opens a <code>connectionStr</code> to the DataBase
     *
     * @param connectionStr connectionStr string for DB
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public void open(String connectionStr) throws SQLException {
        logger.entry("open(" + connectionStr + ")");
        if (isOpen) {
            logger.exit("Connection already open. Close connection before open.");
            return;
        }

        // throws exception if fails
        Validators.RegexValidator(connectionStr,
                Constants.DB_CONNECTION_VALIDATOR,
                String.format(Constants.MSG_STRING_ILLEGAL_ARGUMENT_EXCEPTION, "connectionStr", Constants.DB_CONNECTION_VALIDATOR)
        );

        // Setup the connectionStr with the DB
        this.connection = DriverManager.getConnection(connectionStr);
        isOpen = true;
        logger.exit("Connection opened to: " + connectionStr);

    }


    /**
     * Opens a connection using <code>connectionStr</code> to the DataBase
     * Returns a <code>Map<Integer, String></code> of the RSS feed URLs where:
     * <p>Integer = DB_ID of feed</p>
     * <p>String = URL of feed</p>
     *
     * @param RSSSelectQuery query that will return the RSS ID field as ID and URL as RSSUrl
     *                       // todo fix return, does not render correctly
     * @return <code>Map<Integer, String></code> where Integer = DB_ID of feed, String = URL of feed
     * @throws Exception
     */
    public Map<Integer, String> getRSSFeedList(String RSSSelectQuery) throws Exception {
        logger.entry("getRSSFeedList(" + RSSSelectQuery + ")");

        // validate connection is open and throws exception if not
        validateIsOpen();

        // throws exception if fails
        Validators.booleanValidator(isOpen, true, new IllegalStateException(Constants.MSG_ILLEGAL_STATE_RSS_DAO_SQL));

        // throws exception if fails
        Validators.RegexValidator(RSSSelectQuery,
                Constants.DB_QUERY_SELECT_VALIDATOR,
                String.format(Constants.MSG_STRING_ILLEGAL_ARGUMENT_EXCEPTION,
                        RSSSelectQuery,
                        Constants.DB_QUERY_SELECT_VALIDATOR)
        );

        // Map results will be returned
        Map<Integer, String> results = new HashMap<>();

        try {
            // issue SQL queries to the database
            statement = this.connection.createStatement();

            // result of the SQL query
            resultSet = statement.executeQuery(RSSSelectQuery);

            // build results
            while (resultSet.next()) {
                results.put(resultSet.getInt("ID"), resultSet.getString("RSSUrl"));
            }

        } catch (SQLException e) {
            logger.error("SQLException while trying to execute: " + RSSSelectQuery);
            logger.catching(e);
        }
        logger.exit(results.size() + " feeds downloaded");
        return results;

    }

    @SuppressWarnings("unchecked")
    public void insertFeed(String connection, int feedID, SyndFeed syndFeed) throws SQLException, IOException {
        logger.entry("insertFeed(" + connection + ", " + feedID + ", " + "feedObject" + ")");

        // validate connection is open and throws exception if not
        validateIsOpen();

        // create schema if needed
        initialize();

        // insert RSS feed into DB

        // insert feed and get id
        int feedId = insertFeed(feedID, syndFeed);

        // insert entries
        int[] ids = insertEntries(syndFeed);
        // -- insert into has table
        insertHasItem(feedId, ids, Constants.SQL_INSERT_RSS_FEED_HAS_ENTRIES);

        // insert authors
        ids = insertList(syndFeed.getAuthors(), Constants.SQL_INSERT_AUTHORS, Constants.SQL_SELECT_AUTHORS);//insertAuthors(syndFeed.getAuthors());
        // -- insert into has table
        insertHasItem(feedId, ids, Constants.SQL_INSERT_RSS_FEED_HAS_AUTHORS);

        // insert categories
        ids = insertCategories(syndFeed.getCategories());
        // -- insert into has table
        insertHasItem(feedId, ids, Constants.SQL_INSERT_RSS_FEED_HAS_CATEGORIES);

        // insert contributors
        ids = insertList(syndFeed.getContributors(), Constants.SQL_INSERT_CONTRIBUTORS, Constants.SQL_SELECT_CONTRIBUTORS);//insertContributors(syndFeed.getContributors());
        // -- insert into has table
        insertHasItem(feedId, ids, Constants.SQL_INSERT_RSS_FEED_HAS_CONTRIBUTORS);

        // insert links
        ids = insertList(syndFeed.getLinks(), Constants.SQL_INSERT_LINKS, Constants.SQL_SELECT_LINKS);//insertLinks(syndFeed.getLinks());
        // -- insert into has table
        insertHasItem(feedId, ids, Constants.SQL_INSERT_RSS_FEED_HAS_LINKS);

        logger.exit();

    }

    private void validateIsOpen() {
        if(!isOpen) {
            logger.error("Connection is not open! Cannot get RSS Feed List");
            throw new InvalidStateException("Connection not open!");
        }
    }

    @SuppressWarnings("unchecked")
    private int[] insertEntries(SyndFeed feed){
        int[] entryIds = new int[feed.getEntries().size()];

        for (int i = 0; i < entryIds.length; i++){
            try {
                SyndEntry syndEntry = (SyndEntry)feed.getEntries().get(i);
                // process entry
                entryIds[i] = insertEntry(syndEntry, i);
                // todo if id = -1 handle

                // process authors
                int ids[] = insertList(syndEntry.getAuthors(), Constants.SQL_INSERT_AUTHORS, Constants.SQL_SELECT_AUTHORS);//insertAuthors(syndEntry.getAuthors());
                insertHasItem(entryIds[i], ids, Constants.SQL_INSERT_ENTRIES_HAS_AUTHORS);
                
                // process categories
                ids = insertCategories(syndEntry.getCategories());
                insertHasItem(entryIds[i], ids, Constants.SQL_INSERT_ENTRIES_HAS_CATEGORIES);
                
                // process contributors
                ids = insertList(syndEntry.getContributors(), Constants.SQL_INSERT_CONTRIBUTORS, Constants.SQL_SELECT_CONTRIBUTORS);//insertContributors(syndEntry.getContributors());
                insertHasItem(entryIds[i], ids, Constants.SQL_INSERT_ENTRIES_HAS_CONTRIBUTORS);
                
                // process links
                ids = insertList(syndEntry.getLinks(), Constants.SQL_INSERT_LINKS, Constants.SQL_SELECT_LINKS);//insertLinks(syndEntry.getLinks());
                insertHasItem(entryIds[i], ids, Constants.SQL_INSERT_ENTRIES_HAS_LINKS);


            } catch (SQLException e) {
                // todo handle errors
                e.printStackTrace();
            }

            /* todo
            List entEnclosures = syndEntry.getEnclosures();  // list - why does each entry have a list of enclosures?
            // ex: index 0
            // url: http://www.castmate.fm/play.mp3?u=johndoe&i=7815
            // type: audio/mpeg
            // length: 36187136 bytes
            */
        }

       return entryIds;
    }

    private int[] insertList(List<String> list, String insert, String select){
        int[] ids = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            try {
                ids[i] = insertUnique(list.get(i), insert, select);
            } catch (SQLException e) {
                ids[i] = -1;
                e.printStackTrace(); //todo
            }
        }
        return ids;
    }


    private int[] insertCategories(List<SyndCategory> categories){
        int[] ids = new int[categories.size()];
        for (int i = 0; i < categories.size(); i++){
            try {
                ids[i] = insertUnique(categories.get(i).getName(), Constants.SQL_INSERT_CATEGORIES, Constants.SQL_SELECT_CATEGORIES);
            } catch (SQLException e) {
                ids[i] = -1;
                e.printStackTrace(); //todo
            }
        }
        return ids;
    }

    private int insertUnique(String item, String insert, String select) throws SQLException {
        ResultSet rs;

        // see if the item already exist
        PreparedStatement psSelect = this.connection.prepareStatement(select);
        psSelect.setString(1, item);
        rs = psSelect.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }

        // try to insert item
        PreparedStatement psInsert = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        psInsert.setString(1, item);
        psInsert.executeUpdate();

        rs = psInsert.getGeneratedKeys();
        if (rs.next()){
            return rs.getInt(1);
        }

        return -1;
    }
    private void insertHasItem(int firstId, int secondId[], String insert) throws SQLException {
        logger.entry("insertHasItem");
        PreparedStatement psInsert = this.connection.prepareStatement(insert);
        psInsert.setInt(1, firstId);

        for (int aSecondId : secondId) {
            psInsert.setInt(2, aSecondId);

            try {
                psInsert.executeUpdate();
            } catch (SQLException e) {
                System.out.println("error " + e.getErrorCode() + ": Cannot add or update a child row: a foreign key constraint fails.");
                System.out.println("-->" + psInsert.toString());
                //e.printStackTrace();
            }
        }
    }

    private int insertFeed(int feedID, SyndFeed feed) throws SQLException {
        PreparedStatement psFeed = this.connection.prepareStatement(Constants.SQL_INSERT_RSS_FEED, Statement.RETURN_GENERATED_KEYS);

        psFeed.setInt(1, feedID);
        psFeed.setString(2, feed.getAuthor());                       // string ex: johndoe@joe.com (John Doe)
        psFeed.setString(3, feed.getCopyright());                    // string ex: Copyright 2012 John Doe, Doe Audio
        psFeed.setString(4, feed.getDescription());                  // string ex: "Blah blah blah..." long string of text

        if (feed.getDescription() != null) {
            psFeed.setString(5, feed.getDescriptionEx().getValue());     // string longer version of desc
        } else {
            psFeed.setNull(5, Types.LONGVARCHAR);
        }
        psFeed.setString(6, feed.getFeedType());                     // string ex: rss_2.0

        if (feed.getImage() != null) {
            psFeed.setString(7, feed.getImage().getTitle());             // string
            psFeed.setString(8, feed.getImage().getDescription());       // string
            psFeed.setString(9, feed.getImage().getLink());              // string
            psFeed.setString(10, feed.getImage().getUrl());              // string
        } else {
            psFeed.setNull(7, Types.VARCHAR);
            psFeed.setNull(8, Types.VARCHAR);
            psFeed.setNull(9, Types.LONGVARCHAR);
            psFeed.setNull(10, Types.LONGVARCHAR);
        }

        psFeed.setString(11, feed.getLanguage());                    // string ex: en-us or en or whatever they felt like

        if (feed.getPublishedDate() != null) {
            psFeed.setDate(12, new Date(feed.getPublishedDate().getTime()));    //date
        } else {
            psFeed.setNull(12, Types.DATE);
        }
        psFeed.setString(13, feed.getTitle());                       // string ex: John's Pod Cast

        if (feed.getTitle() != null){
            psFeed.setString(14, feed.getTitleEx().getValue());          // string longer version of title
        } else {
            psFeed.setNull(14, Types.VARCHAR);
        }

        psFeed.setString(15, feed.getUri());                         // string ex: often null
        psFeed.setString(16, feed.getLink());                        // String ex: http//johndoe.castmate.fm

        try {
            psFeed.executeUpdate();
        } catch (SQLException e){
            System.out.println("SQL insert failed: " + psFeed.toString());
        }


        ResultSet rs = psFeed.getGeneratedKeys();
        if (rs.next()){
            return rs.getInt(1);
        }

        return feedID;
    }

    private int insertEntry(SyndEntry syndEntry, int entryNum) throws SQLException {
        ResultSet rs;

        PreparedStatement psSelectEntry = this.connection.prepareStatement(Constants.SQL_SELECT_ENTRY_BY_TITLE);
        psSelectEntry.setString(1, syndEntry.getTitle());
        rs = psSelectEntry.executeQuery();
        if (rs.next()){
            return rs.getInt(1);
        }


        PreparedStatement psEntry = this.connection.prepareStatement(Constants.SQL_INSERT_ENTRIES, Statement.RETURN_GENERATED_KEYS);

        psEntry.setInt(1, entryNum);
        psEntry.setString(2, syndEntry.getTitle());                             // string ex: The Pod Cast
        psEntry.setString(3, syndEntry.getAuthor());                            // string ex: Joe

        if (syndEntry.getTitleEx() != null) {
            psEntry.setString(4, syndEntry.getTitleEx().getValue());                // string ex: The Pod Cast - Show 17
        } else {
            psEntry.setNull(4, Types.VARCHAR);
        }

        if (syndEntry.getDescription() != null) {
            psEntry.setObject(5, syndEntry.getDescription().getValue());            // string ex: Blah blah blah
        } else {
            psEntry.setNull(5, Types.LONGVARCHAR);
        }

        psEntry.setString(6, syndEntry.getUri());                               // string ex: http://www.castmate.fm/play.mp3?u=johndoe&i=7815 this is not always the case ex: 96efa5c996a38e144b0d5ef2eec805ff
        psEntry.setString(7, syndEntry.getLink());                              // string ex: http//johndoe.castmate.fm/?id=7815 or ex: http://traffic.libsyn.com/jimhart/LMME051.mpe

        if (syndEntry.getPublishedDate() != null) {
            psEntry.setDate(8, new Date(syndEntry.getPublishedDate().getTime()));   // date
        } else {
            psEntry.setNull(8, Types.DATE);
        }
        if (syndEntry.getUpdatedDate() != null) {
            psEntry.setDate(9, new Date(syndEntry.getUpdatedDate().getTime()));     // date - often null
        } else {
            psEntry.setNull(9, Types.DATE);
        }
        psEntry.executeUpdate();

        rs = psEntry.getGeneratedKeys();
        if (rs.next()){
            return rs.getInt(1);
        }
        
        return -1;
    }

    public void initialize() throws SQLException, IOException {
        logger.entry("initialize");

        // validate connection is open and throws exception if not
        validateIsOpen();

        DatabaseMetaData dmd = this.connection.getMetaData();
        this.resultSet = dmd.getCatalogs();
        while (resultSet.next()){
            if (resultSet.getString(1).equals(Constants.DB_APLOS_RSS_SCHEMA)){
                logger.info("DB exist");
                return;
            }
        }

        // setup script runner
        ScriptRunner runner = new ScriptRunner(this.connection);

        // read sql script in
        ClassLoader classLoader = getClass().getClassLoader();
        File sqlScript = new File(classLoader.getResource(Constants.DB_CREATE_SCRIPT).getFile());

        try (InputStreamReader scriptReader = new InputStreamReader(new FileInputStream(sqlScript))){
            // run script
            logger.info("runScript(" + Constants.DB_CREATE_SCRIPT + ")");
            runner.runScript(scriptReader);
        }

        logger.exit();

    }


    /**
     * Close the connection.
     */
    public void close() {
        logger.entry("close");
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            logger.catching(e);
        } finally {
            logger.exit();
        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        this.close();
    }
}
