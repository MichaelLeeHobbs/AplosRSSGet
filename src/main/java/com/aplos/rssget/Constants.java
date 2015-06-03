package com.aplos.rssget;

import com.aplos.utils.PropertiesUtil;
import com.sun.org.apache.bcel.internal.util.*;
import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Project: aplosrssget
 * Created by Michael Hobbs on 4/4/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class Constants {
    private Constants(){}

    // Will only download one RSS feed for testing
    public static final boolean TESTING = false;

    static ClassLoader classLoader = new ClassLoader();
    static PropertiesUtil propertiesUtil = new PropertiesUtil();
    static Properties config;

    public static int RSS_READ_TIME_OUT;
    public static int RSS_THREADS;


    // config
    public static String DB_DRIVER;
    public static String DB_CONNECTION_TYPE;
    public static String DB_TYPE;
    public static String DB_HOST;
    public static String DB_SCHEMA;
    public static final String DB_APLOS_RSS_SCHEMA = "AplosRSS";

    private static String DB_USER;
    private static String DB_PASS_WORD;

    public static String DB_RSS_QUERY;

    // load from properties file
    static {
        try {
            //config = propertiesUtil.loadProperties("aplosrss.cfg");

            System.out.println("Looking for aplosrss.cfg in " + System.getProperty("user.dir"));
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/aplosrss.cfg");//classLoader.getResourceAsStream(System.getProperty("user.dir") + "/aplosrss.cfg");

            config = propertiesUtil.loadPropertiesExternal(inputStream);


            RSS_READ_TIME_OUT = Integer.parseInt(config.getProperty("RSS_READ_TIME_OUT"));
            RSS_THREADS = Integer.parseInt(config.getProperty("RSS_THREADS"));

            DB_HOST = config.getProperty("DB_HOST");
            DB_SCHEMA = config.getProperty("DB_SCHEMA");
            DB_USER = config.getProperty("DB_USER");
            DB_PASS_WORD = config.getProperty("DB_PASS_WORD");

            //##DB Table to store in
            //DB_STORE_TABLE = config.getProperty("DB_STORE_TABLE");

            //##DB Query strings
            DB_RSS_QUERY = config.getProperty("DB_RSS_QUERY");


            //##DB Connection Type -- Do not change unless you know what you are doing
            DB_DRIVER = config.getProperty("DB_DRIVER");
            DB_CONNECTION_TYPE = config.getProperty("DB_CONNECTION_TYPE");
            DB_TYPE = config.getProperty("DB_TYPE");

        } catch (IOException e) {
            System.out.println("Fatal error! aplosrss.cfg not found.");
            System.exit(255);
        }
    }

    // DB connection string
    //"jdbc:mysql://localhost/amntwk_test?user=%s&password=%s";
    private static final String CONNECTION_FORMAT = "%s:%s://%s/%s?user=%s&password=%s";
    public static final String CONNECTION =
            String.format(
                CONNECTION_FORMAT,
                DB_CONNECTION_TYPE,
                DB_TYPE,
                DB_HOST,
                DB_SCHEMA,
                DB_USER,
                DB_PASS_WORD
            );


    // DB Initialize script
    public static final String DB_CREATE_SCRIPT = "dbCreate.sql";

    // main tables
    public static final String SQL_INSERT_RSS_FEED = "INSERT INTO `AplosRSS`.`RSS_Feed` (`idRSS_Feed`, `Author`, `Copyright`, `Desc`, `DescEx`, `Type`, `ImgTitle`, `ImgDesc`, `ImgLink`, `ImgURI`, `Lang`, `PubDate`, `Title`, `TitleEx`, `URI`, `Link`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `Author`=`Author`;";

    public static final String SQL_SELECT_ENTRY_BY_TITLE =  "SELECT `idEntries` FROM `AplosRSS`.`Entries` WHERE `Title` = ?;";
    public static final String SQL_INSERT_ENTRIES = "INSERT INTO `AplosRSS`.`Entries` (`Feed_Ent_Num`, `Title`, `Author`, `TitleEx`, `Desc`, `URI`, `Link`, `PubDate`, `UpdateDate`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // sub tables
    public static final String SQL_SELECT_AUTHORS = "SELECT `idAuthors` FROM `AplosRSS`.`Authors` WHERE `Author` = ?;";
    public static final String SQL_INSERT_AUTHORS = "INSERT INTO `AplosRSS`.`Authors` (`Author`) VALUES (?) ON DUPLICATE KEY UPDATE `Author`=`Author`;";

    public static final String SQL_SELECT_CATEGORIES = "SELECT `idCategories` FROM `AplosRSS`.`Categories` WHERE `Category` = ?;";
    public static final String SQL_INSERT_CATEGORIES = "INSERT INTO `AplosRSS`.`Categories` (`Category`) VALUES (?) ON DUPLICATE KEY UPDATE `Category`=`Category`;";

    public static final String SQL_SELECT_CONTRIBUTORS = "SELECT `idContributors` FROM `AplosRSS`.`Contributors` WHERE `Contributor` = ?;";
    public static final String SQL_INSERT_CONTRIBUTORS = "INSERT INTO `AplosRSS`.`Contributors` (`Contributor`) VALUES (?) ON DUPLICATE KEY UPDATE `Contributor`=`Contributor`;";

    public static final String SQL_SELECT_LINKS = "SELECT `idLinks` FROM `AplosRSS`.`Links` WHERE `Link` = ?;";
    public static final String SQL_INSERT_LINKS = "INSERT INTO `AplosRSS`.`Links` (`Link`) VALUES (?) ON DUPLICATE KEY UPDATE `Contributor`=`Contributor`;";

    // has tables for RSS_FEED
    public static final String SQL_INSERT_RSS_FEED_HAS_AUTHORS = "INSERT INTO `AplosRSS`.`RSS_Feed_has_Authors` (`RSS_Feed_idRSS_Feed`, `Authors_idAuthors`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `RSS_Feed_idRSS_Feed`=`RSS_Feed_idRSS_Feed`;";
    public static final String SQL_INSERT_RSS_FEED_HAS_CATEGORIES = "INSERT INTO `AplosRSS`.`RSS_Feed_has_Categories` (`RSS_Feed_idRSS_Feed`, `Categories_idCategories`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `RSS_Feed_idRSS_Feed`=`RSS_Feed_idRSS_Feed`;";
    public static final String SQL_INSERT_RSS_FEED_HAS_CONTRIBUTORS = "INSERT INTO `AplosRSS`.`RSS_Feed_has_Contributors` (`RSS_Feed_idRSS_Feed`, `Contributors_idContributors`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `RSS_Feed_idRSS_Feed`=`RSS_Feed_idRSS_Feed`;";
    public static final String SQL_INSERT_RSS_FEED_HAS_LINKS = "INSERT INTO `AplosRSS`.`RSS_Feed_has_Links` (`RSS_Feed_idRSS_Feed`, `Links_idLinks`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `RSS_Feed_idRSS_Feed`=`RSS_Feed_idRSS_Feed`;";
    public static final String SQL_INSERT_RSS_FEED_HAS_ENTRIES = "INSERT INTO `AplosRSS`.`RSS_Feed_has_Entries` (`RSS_Feed_idRSS_Feed`, `Entries_idEntries`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `RSS_Feed_idRSS_Feed`=`RSS_Feed_idRSS_Feed`;";

    // has tables for ENTRIES
    public static final String SQL_INSERT_ENTRIES_HAS_AUTHORS = "INSERT INTO `AplosRSS`.`Entries_has_Authors` (`Entries_idEntries`, `Authors_idAuthors`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Entries_idEntries`=`Entries_idEntries`;";
    public static final String SQL_INSERT_ENTRIES_HAS_CATEGORIES = "INSERT INTO `AplosRSS`.`Entries_has_Categories` (`Entries_idEntries`, `Categories_idCategories`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Entries_idEntries`=`Entries_idEntries`;";
    public static final String SQL_INSERT_ENTRIES_HAS_CONTRIBUTORS = "INSERT INTO `AplosRSS`.`Entries_has_Contributors` (`Entries_idEntries`, `Contributors_idContributors`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Entries_idEntries`=`Entries_idEntries`;";
    public static final String SQL_INSERT_ENTRIES_HAS_LINKS = "INSERT INTO `AplosRSS`.`Entries_has_Links` (`Entries_idEntries`, `Links_idLinks`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Entries_idEntries`=`Entries_idEntries`;";


    // Validators
    public static final String DB_DRIVER_VALIDATOR =  "[A-Za-z0-9_\\.]+[A-Za-z]+";
    public static final String DB_CONNECTION_VALIDATOR = "(?:[\\w\\d]+:){2}\\/(\\/\\w+){2}\\?user=.+&password=.+";
    public static final String DB_QUERY_SELECT_VALIDATOR = "SELECT.+as ID.+ as RSSUrl.+FROM.+WHERE.+;";

    // String Messages
    public static final String MSG_STRING_ILLEGAL_ARGUMENT_EXCEPTION = "'%s' failed validation: %s";
    public static final String MSG_ILLEGAL_STATE_RSS_DAO_SQL = "Must call open(String connection) before getRSSFeedList";
    public static final String MSG_PROCESSING_FEEDS = "Processing: id=%s URL=%s";

}
