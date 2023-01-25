package in.glg.rummy.CommonMethods;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.models.RummyTable;

/**
 * Created by GridLogic on 26/7/17.
 */

public class RummyStaticValues
{
    public static final String RAW = "raw";

    public static Socket SOCKET = null;
    public static final int SERVERPORT = 4271;
    public static final String SERVER_IP = "180.179.43.250";

    public static String FRAG_MORE = "FRAG_MORE";

    public static String AUTH_OBJECT = "AUTH_OBJECT";
    public static String PLAYER_DETAILS_OBJECT = "PLAYER_DETAILS_OBJECT";
    public static String TABLES_DETAILS_OBJECT = "TABLES_DETAILS_OBJECT";

    public static List<RummyTable> TABLES_LIST = new ArrayList<>();
    public static ArrayList<String> PLAYERS_FILTERS = new ArrayList<>();

    public static Thread thread;
    public static InetAddress serverAddr;
    public static PrintWriter printWriter;
    public static BufferedReader brn;
    public static String s;
    //public static EngineConnection engineConnection;

    // EVENT NAMES
    public static String TABLE_TOSS             = "TABLE_TOSS";
    public static String GET_TABLE_DETAILS      = "get_table_details";
    public static String BALANCE_UPDATE         = "BALANCE_UPDATE";
    public static String PLAYER_QUIT            = "PLAYER_QUIT";
    public static String HEART_BEAT             = "HEART_BEAT";
    public static String GAME_SCHEDULE          = "GAME_SCHEDULE";
    public static String SITTING_SEQUENCE       = "SITTING_SEQ";
    public static String START_GAME             = "START_GAME";
    public static String SEND_STACK             = "SEND_STACK";
    public static String SEND_DEAL              = "SEND_DEAL";
    public static String TURN_UPDATE            = "TURN_UPDATE";
    public static String LIST_GAME_SETTINGS     = "list_gamesettings";
    public static String JOIN_TABLE             = "join_table";
    public static String GAME_SETTING_UPDATE    = "gamesetting_update";
    public static String NEW_GAME_SETTING       = "new_gamesetting";


    /*
        d = diamond     RED
        s = spade
        c = club
        h = heart       RED
     */

}