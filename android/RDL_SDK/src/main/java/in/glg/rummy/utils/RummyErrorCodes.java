package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */


import androidx.core.view.InputDeviceCompat;

import cz.msebera.android.httpclient.HttpStatus;

public class RummyErrorCodes {
    public static int ALREADY_IN_WAITING_LIST_FOR_TOURNAMENT = 7006;
    public static int ALREADY_REGISTERED_FOR_TOURNAMENT = 7005;
    public static int ALREADY_REQUESTED_EXTRATIME = 409;
    //public static int ALREADY_SPLIT_REQUESTED = ERR_PASSWORD_MIN;
    public static int ATTRIB_MISSING = 470;
    //public static int BELOW_18 = EMAIL_UNRIGISTERED;
    public static int CANNOT_DEREGISTER_FOR_TOURNAMENT = 7009;
    public static int CANT_DO_RELOAD = 901;
    public static int CANT_DO_THIS_NO_MATCHING_CONDITION = 408;
    public static int CAN_NOT_DELETE_TOURNAMENT = 7019;
    public static int CAN_NOT_EDIT_TOURNAMENT = 7020;
    public static int CAN_NOT_LOGOUT = 412;
    public static int CONNECTION_WAS_NOT_ESTABLISHED_TO_DBSLAYER = 463;
    public static int CORRECT_MOBILE_NUMBER = 710;
    public static int DATABASE_ERROR = 460;
    public static int DATABASE_MAINTENANCE = 477;
    public static int DEREGISTRATIN_TIME_OVER_FOR_TOURNAMENT = 7013;
    public static final int EMAIL_ALREADY_REGISTERED = 712;
    public static int EMAIL_MOBILE_NOT_VERIFIED = 7046;
    public static int EMAIL_NOT_VERIFIED = 7042;
    public static final int EMAIL_UNRIGISTERED = 708;
    public static final int ERR_AGE_LESS_THAN_18 = 708;
    public static int EMPTY_SET_FROM_DATABASE = 464;
    public static int ENGINE_RESTARTED = 472;
    public static int ENGINE_SET_FOR_MAINTANANCE = 100;
    public static int ERROR_IN_GAP = 465;
    public static int ERROR_IN_GETTING_LEVEL_DETAILS = 2028;
    public static int ERROR_IN_GETTING_LEVEL_TIMER = 7018;
    public static int ERROR_IN_PLAYERADD = 802;
    public static int ERR_ADDING_TOURNEY = 7001;
    public static int ERR_DELETEING_SCHEDULE = 402;
    public static int ERR_DELETE_TOURNEY = 7003;
    public static int ERR_EDITING_TOURNEY = 7002;
    public static final int ERR_ONLY_ALPHABETS = 701;
    public static final int ERR_PASSWORD_MIN = 703;
    public static final int ERR_USERNAME_CHARACTERS = 702;
    public static final int ERR_VALID_EMAIL = 704;
    public static int FALSE_SPLIT_CONDITION = ERR_ONLY_ALPHABETS;
    public static int FIELD_MISSING = 501;
    public static int FIRST_TIME_DEPOSIT_FAIL = 7044;
    public static int FORBIDDEN = 101;
    public static int FUNDED_CONDITION_FALSE = 481;
    public static int FUNDED_DAYS_CONDITION_FALSE = 7040;
    public static int FUNDED_VALUE_CONDITION_FALSE = 7031;
    public static int GAP_RECONNECTED = 471;
    public static int IMPROPER_CONTIDITION_FOR_REQUEST = 509;
    public static int INTERNAL_ERROR = 500;
    public static final int INVALID_EMAIL = 707;
    public static int INVALID_PASSWORD = 462;
    public static int INVALID_SEAT_IN_REQUEST = 804;
    public static int INVALID_USER_NAME = 461;
    public static int LEVEL_END_IS_TRIGGERED = 7015;
    public static int LEVEL_NOT_RUNNING = 7017;
    public static int LEVEL_REBUYIN_DENIED = 7014;
    public static int LEVEL_REBUYIN_TIMEOUT = 7016;
    public static int LOGINS_NOT_ALLOWED = 476;
    public static int LOW_BALANCE = 501;
    public static int MELD_ALREADY_PLACED = 512;
    public static int MELD_HAS_WRONG_CARD_COUNT = InputDeviceCompat.SOURCE_DPAD;
    public static int MIDDLE_JOIN_ALREADY_REQUESTED = 508;
    public static int MOBILE_ALREADY_REG = 709;
    public static int MOBILE_NOT_VERIFIED = 7043;
    public static int MORE_THEN_MAXBUYIN = 503;
    public static int NON_DEPOSIT_FAIL = 7045;
    public static int NOT_ACCEPTABLE = 406;
    public static int NOT_PROPER_TABLE_TYPE = 506;
    public static int NOT_REGISTERED_FOR_TOURNAMENT = 7008;
    public static int NO_APPROPRIATE_CHIPS = 511;
    public static int NO_MIDDLE_JOIN = 510;
    public static int NO_RESULT_YET = 2029;
    public static int NO_SCHEDULES = 473;
    public static int NO_SLOT_FOR_THIS_TABLE = 411;
    public static int NO_SUCH_CHATROOM = 405;
    public static int NO_SUCH_EDITINGVALUES = 407;
    public static int NO_SUCH_PLAYER = 401;
    public static int NO_SUCH_SCHEDULE = 403;
    public static int NO_SUCH_TABLE = 404;
    public static int NO_SUFF_AMOUNT = HttpStatus.SC_INSUFFICIENT_STORAGE;
    public static int NO_TABLE_WITH_SAME_CONSTRAINS = 410;
    public static int NO_TOURNAMENT_SCHEDULED = 7004;
    public static int OK = 200;
    public static int PLAYER_ALREADY_INMIDDLE = 514;
    public static int PLAYER_ALREADY_INPLAY = 504;
    public static int PLAYER_ALREADY_INVIEW = 505;
    public static int PLAYER_CANCEL_SPLIT = ERR_USERNAME_CHARACTERS;
    public static int PLAYER_CANNOTJOIN_INTO_THIS_TABLE = 469;
    public static int PLAYER_DISABLE = 479;
    public static int PLAYER_DISABLE_OR_LOCKED = 480;
    public static int PLAYER_HAS_CHIPS_MORE_THAN_MINIMUN = 466;
    public static int PLAYER_LOCKED = 478;
    public static int PLAYER_NOT_ELIGIBLE_FOR_TOURNAMENT = 7007;
    public static int PLAYER_NOT_ELIGIBLE_FOR_TOURNAMENT_GENDER = 7032;
    public static int PLAYER_NOT_ELIGIBLE_FOR_TOURNAMENT_LEVEL = 7033;
    public static int PLAYER_NOT_ELIGIBLE_FOR_TOURNAMENT_LOYALTY = 7034;
    public static int PLAYER_NOT_VERIFIED = 475;
    public static int READ_TOU = 711;
    public static int REGISTRATIN_TIME_OVER_FOR_TOURNAMENT = 7012;
    public static int REGISTRATION_FULL_FOR_TOURNAMENT = 7010;
    public static int REGISTRATION_NOT_START_FOR_TOURNAMENT = 7011;
    public static final int REG_FAILED = 713;
    public static final int REG_SUCCESS = 200;
    public static int REQUEST_FAILED = 502;
    public static int SEARCH_JOIN_ALREADY_RECIEVED = 413;
    public static int SEAT_ALREADY_TAKEN = 801;
    public static int SEAT_RESERVED_TO_TAKE = 803;
    public static int SELECT_GENDER = 705;
    public static int SELECT_VALID_REF = 714;
    public static int SERVER_MAINTENANCE = 477;
    public static int SUCCESS = 200;
    public static int TABLE_FULL = 502;
    public static int THERE_ARE_NO_SCHEDULES = 470;
    public static int TOURNAMENT_ALREADY_PRESENT = 7022;
    public static int TOURNAMENT_ELEMINATE_CONDITION_CHECK_ERROR = 7104;
    public static int TOURNAMENT_ELEMINATE_LAST_LEVEL = 7102;
    public static int TOURNAMENT_ELEMINATE_NOT_ENOUGH_CHIPS = 7103;
    public static int TOURNAMENT_ELEMINATE_NO_REASON_GIVEN = 7100;
    public static int TOURNAMENT_ELEMINATE_STILL_IN_AUTOPLAY = 7101;
    public static int TOURNAMENT_NOT_PRESENT = 7023;
    public static int TOURNAMENT_NOT_RUNNING = 7027;
    public static int USER_ALREADY_LOGGEDIN = 467;
    public static int USER_CANNOT_PLAY = 468;
    public static final int USER_NOT_AVAILABLE = 715;
    public static int VALID_DOB = 706;
    public static int VIPCODE_REGISTRATION_CODE_WRONG = 7026;
    public static int VIPCODE_REGISTRATION_FULL = 7025;
    public static int VIPCODE_REGISTRATION_NOT_ENABLED = 7024;
    public static int WRONG_CARDS = 515;
    public static int WRONG_PASSWORD = 462;
    public static int WRONG_SESSION_ID = 474;
    public static int WRONG_TIME = 805;
    public static int WRONG_USERNAME = 461;
    public static int ERR_SELECT_STATE = 707;

}