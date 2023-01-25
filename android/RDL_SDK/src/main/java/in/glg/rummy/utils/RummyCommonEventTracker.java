package in.glg.rummy.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;



import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import in.glg.rummy.models.RummyLoginResponseRest;

public class RummyCommonEventTracker {

    // event tracker type
    private static boolean IsFirebaseEnabled = true;

    private static boolean IsFaceBookTrackerEnabled = false;
    private static boolean IsMoEngageEnabled = true;
    private static String dateFormate = "yyyy-MM-ddTHH:mm:ss";

    public static final String WE_LOG = "tracker_log";

    public static final String USER_LOGGED_IN = "User Login";
    public static final String USER_LOGGED_IN_PROFILE = "User Login Profile";
    public static final String USER_LOGGED_OUT = "User Logged Out";
    public static final String USER_REGISTERED = "User Registered";
    public static final String PAYMENT_PAGE_VISITED = "PaymentPageVisited";
    public static final String PAYMENT_FAILED = "Deposit Failed";
    public static final String PAYMENT_SUCCESS = "Deposit Success";
    public static final String WITHDRAW_Request = "Withdrawal Request";
    public static final String KYC_UPDATE = "KYC Update";
    public static final String MOBILE_VERIFIED = "Mobile Verified";
    public static final String PROFILE_UPDATED = "Profle Update";
    public static final String SUPPORT_QUERY = "SupportQueryRaised";
    public static final String REPORT_BUG = "ReportBug";
    public static final String REFER_FRIEND = "RAFRequest";
    public static final String EMAIL_UPDATE = "Email Update";
    public static final String MOBILE_UPDATE = "Mobile Update";
    public static final String PROMOTION_CHECKED = "Promotion Checked";
    public static final String Tournament_Register = "Tournament Register";
    public static final String Tournament_DE_Register = "Tournament DeRegister";
    public static final String Add_Cash = "Add Cash";
    public static final String Change_Password = "Change Password";


    public static final String BONUS_CODE = "bonuscode";
    public static final String MODE_OF_DEPOSIT = "modeofdeposit";
    public static final String FIRST_DEPOSIT = "firstdeposit";
    public static final String USER_ID = "userId";
    public static final String Tournament_id = "tournament_id";
    public static final String DEVICE_TYPE = "device_type";
    public static final String CLIENT_TYPE = "client_type";
    public static final String AMOUNT = "amount";
    public static final String WE_EMAIL = "we_email";
    public static final String Type = "Type";
    public static final String WE_PHONE = "we_phone";
    public static final String WE_GENDER = "we_gender";
    public static final String EMAIL_VERIFICATION_STATUS = "EmailVerificationStatus";
    public static final String MOBILE_VERIFICATION_STATUS = "MobileVerificationStatus";
    public static final String UniqueTxnID = "UniqueTxnID";
    public static final String Deposit_Number = "depositNumber";
    public static final String First_Deposit_Success = "First Deposit Success";
    public static final String Second_Deposit_Success = "Second Deposit Success";
    public static final String Third_Deposit_Success = "Third Deposit Success";
    public static final String Cash_Game_Click = "Cash Game Click";
    public static final String Free_Game_Click = "Free Game Click";
    public static final String Cash_Game_Played = "Cash Game Played";
    public static final String Free_Game_Played = "Free Game Played";


    // Screen Tags
    public static final String LOGIN_SCREEN = "Login Screen";
    public static final String REGISTRATION_SCREEN = "Registration Screen";
    public static final String PRE_LOBBY_SCREEN = "Pre-lobby Screen";
    public static final String LOBBY_SCREEN = "Lobby Screen";
    public static final String PROFILE_SCREEN = "Profile Screen";
    public static final String KYC_SCREEN = "KYC Screen";
    public static final String RAF_SCREEN = "RAF Screen";
    public static final String PREFERENCES_SCREEN = "Preferences Screen";
    public static final String CHANGE_PASS_SCREEN = "Change Password Screen";
    public static final String ACC_OVERVIEW_SCREEN = "Account Overview Screen";
    public static final String DEPOSIT_SCREEN = "Deposit Screen";
    public static final String WITHDRAW_SCREEN = "Withdraw Screen";
    public static final String SUPPORT_SCREEN = "Support Screen";
    public static final String GAME_TABLE_SCREEN = "Game Table Screen";
    public static final String TOURNEY_LOBBY_SCREEN = "Tourney Lobby Screen";
    public static final String TOURNEY_DETAILS_SCREEN = "Tourney Details Screen";

    /// keys

    public static String Key_UserId = "userID";
    public static String Key_mobile_hash = "userMobileHash";
    public static String Key_email_hash = "userEmailHash";
    public static String Key_Type = "Type";
    public static String Key_device_type = "device_type";
    public static String Key_client_type = "client_type";
    public static String Key_amount = "Amount";
    public static String Key_bonus_code = "bonusCode";
    public static String Key_mode_of_deposit = "modeofdeposit";
    public static String Key_payout_type = "payoutType";
    public static String Key_game_type = "GameType";
    public static String Key_table_id = "tableID";
    public static String Key_game_id = "gameID";
    public static String Key_bet = "bet";
    public static String Key_mobile_no = "mobile_no";
    public static String Key_email_id = "email_id";
    public static String Key_gender_male = "male";
    public static String Key_gender_female = "female";
    public static String Key_gender_other = "other";
    public static String Key_dob = "dob";
    public static String Key_first_name = "firsname";
    public static String Key_last_name = "lastname";
    public static String Key_city = "city";
    public static String Key_state = "state";
    public static String Key_pincode = "pincode";
    public static String Key_RegisterationIP = "RegisterationIP";
    public static String Key_firstDepositDate = "firstDepositDate";
    public static String Key_EmailVerificationStatus = "EmailVerificationStatus";
    public static String Key_MobileVerificationStatus = "MobileVerificationStatus";
    public static String Key_KYC_STATUS = "KYC_STATUS";
    public static String Key_networkoverlap = "networkoverlap";
    public static String Key_LastLoginDate = "LastLoginDate";
    public static String Key_RegisterationDate = "RegisterationDate";
    public static String Key_RegistrationDeviceType = "RegistrationDeviceType";
    public static String Key_RegistrationClientType = "RegistrationClientType";
    public static String Key_LastDepositDate = "LastDepositDate";
    public static String Key_FirstWithdrawalDate = "FirstWithdrawalDate";
    public static String Key_LastWithdrawalDate = "LastWithdrawalDate";
    public static String Key_InactiveDays = "InactiveDays";
    public static String Key_totalNumberOfDeposits = "totalNumberOfDeposits";
    public static String Key_totalDeposits = "totalDeposits";
    public static String Key_totalNumberOfWithdrawals = "totalNumberOfWithdrawals";
    public static String Key_totalWithdrawals = "totalWithdrawals";


    public static String Key_accountBalance = "account_balance";


    public static void UpdateBalaceProperty(Context context, String realchips)
    {
      /*  if(IsFirebaseEnabled)
        {
            try {
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
                firebaseAnalytics.setAnalyticsCollectionEnabled(true);

                if(realchips != null && !realchips.equalsIgnoreCase(""))
                {
                    firebaseAnalytics.setUserProperty(Key_accountBalance, realchips);

                }
            }
            catch (Exception e)
            {

            }
        }

        if(IsMoEngageEnabled)
        {
            try {
                if(realchips != null && !realchips.equalsIgnoreCase(""))
                {
                   // MoEHelper.getInstance(context).setUserAttribute(Key_accountBalance,Float.parseFloat(realchips));
                }
            }
            catch (Exception e)
            {

            }
        }

        if(Utils.FLAG_WEB_ENGAGE)
        {
            User weUser = WebEngage.get().user();

            if (realchips != null && !realchips.equalsIgnoreCase(""))
                weUser.setAttribute(Key_accountBalance, Float.parseFloat(realchips));
            else
                weUser.setAttribute(Key_accountBalance, 0);
        }*/
    }

    public static void trackUserProfile(RummyLoginResponseRest response, Context context) {
       /* if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Log.e(WE_LOG, "doWebEngageLogin");
                User weUser = WebEngage.get().user();
                weUser.login(String.valueOf(response.getPlayerid()));


                if (response.getEmail() != null) {
                    weUser.setEmail(response.getEmail());
                    //weUser.setAttribute("we_email", response.getEmail());
                }


                if (response.getGender() != null) {
                    if (response.getGender().equalsIgnoreCase("male")) {
                        weUser.setGender(Gender.MALE);
                        //weUser.setAttribute("we_gender", Gender.MALE.toString());
                    } else if (response.getGender().equalsIgnoreCase("female")) {
                        weUser.setGender(Gender.FEMALE);
                        //weUser.setAttribute("we_gender", Gender.FEMALE.toString());
                    } else {
                        weUser.setGender(Gender.OTHER);
                        //weUser.setAttribute("we_gender", Gender.OTHER.toString());
                    }

                }

                if (response.getMobile() != null) {
                    weUser.setPhoneNumber(response.getMobile().toString());
                    //weUser.setAttribute("we_phone", response.getMobile());
                }


                if (response.getDob() != null) {
                    weUser.setBirthDate(response.getDob().toString());
                    //weUser.setAttribute("we_birth_date", response.getDob().toString());
                }


                if (response.getFirstname() != null) {
                    weUser.setFirstName(response.getFirstname());
                    //weUser.setAttribute("we_first_name", response.getFirstname());
                }


                if (response.getLastname() != null) {
                    weUser.setLastName(response.getLastname().toString());
                    //weUser.setAttribute("we_last_name", response.getLastname());
                }


                if (response.getCity() != null)
                    weUser.setAttribute("city", response.getCity());

                if (response.getState() != null)
                    weUser.setAttribute("State", response.getState());

                if (response.getZipcode() != null)
                    weUser.setAttribute("pincode", response.getZipcode());

                if (response.getCreationip() != null)
                    weUser.setAttribute("RegisterationIP", response.getCreationip());

                if (response.getFirstDepositDate() != null)
                    weUser.setAttribute("firstDepositDate", getDateObject(response.getFirstDepositDate().toString()));

                if (response.getVerified() != null)
                    weUser.setAttribute("EmailVerificationStatus", response.getVerified());

                if (response.getMobileverified() != null)
                    weUser.setAttribute("MobileVerificationStatus", response.getMobileverified());

                if (response.getKycverified() != null)
                    weUser.setAttribute("KYC_STATUS", response.getKycverified());
            *//*if(response.getKycverified()!=null)
                weUser.setAttribute("KYC_STATUS", response.getKycverified());*//*

                if (response.getNetworkOverlap() != null)
                    weUser.setAttribute("networkoverlap", response.getNetworkOverlap().toString());

                if (response.getLastLogin() != null)
                    weUser.setAttribute("LastLoginDate", getDateObject(response.getLastLogin().toString()));

                if (response.getLastLoginDays() != null)
                    weUser.setAttribute("InactiveDays", response.getLastLoginDays());

           *//* if(response.getRegisteredon()!=null)
                weUser.setAttribute("RegisterationDate", getDateObject(response.getRegisteredon()));*//*

                if (response.getRegisteredon() != null)
                    weUser.setAttribute("RegistrationDate", getDateObject(response.getRegisteredon()));

                if (response.getRegisterDeviceType() != null)
                    weUser.setAttribute("RegistrationDeviceType", response.getRegisterDeviceType());

                if (response.getRegisterClientType() != null)
                    weUser.setAttribute("RegistrationClientType", response.getRegisterClientType());

                if (response.getLastDepositAt() != null)
                    weUser.setAttribute("LastDepositDate", getDateObject(response.getLastDepositAt().toString()));

                if (response.getFirstWithdrawalAt() != null)
                    weUser.setAttribute("FirstWithdrawalDate", getDateObject(response.getFirstWithdrawalAt().toString()));

                if (response.getLastWithdrawalAt() != null)
                    weUser.setAttribute("LastWithdrawalDate", getDateObject(response.getLastWithdrawalAt().toString()));

                if (response.getLastWithdrawalAt() != null)
                    weUser.setAttribute("InactiveDays", response.getLastLoginDays());

                weUser.setAttribute("device_type", "Mobile");
                weUser.setAttribute("client_type", Utils.CLIENT_TYPE);

                if (response.getDeposits() != null)
                    weUser.setAttribute("totalNumberOfDeposits", response.getDeposits());
                else
                    weUser.setAttribute("totalNumberOfDeposits", 0);

                if (response.getSumdeposit() != null)
                    weUser.setAttribute("totalDeposits", Float.parseFloat(response.getSumdeposit()));
                else
                    weUser.setAttribute("totalDeposits", 0);

                if (response.getWithdrawls() != null)
                    weUser.setAttribute("totalNumberOfWithdrawals", response.getWithdrawls());
                else
                    weUser.setAttribute("totalNumberOfWithdrawals", 0);

                if (response.getSumwithdrawl() != null)
                    weUser.setAttribute("totalWithdrawals", Float.parseFloat(response.getSumwithdrawl()));
                else
                    weUser.setAttribute("totalWithdrawals", 0);

                weUser.setUserProfile(new UserProfile.Builder()
                        .build());

          *//*  Map<String, Object> customAttributes = new HashMap<>();
            weUser.setAttributes(customAttributes);*//*
                Log.e(CommonEventTracker.WE_LOG, "User attributes are set for user: " + response.getPlayerid());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(WE_LOG, e + "@185");
            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {

            try {
                String email = "", fName = "", lName = "", phoneNumber = "", bDate = "", gender = "", city = "", state = "", zipCode = "", country = "in";
                if (response.getEmail() != null)
                    email = response.getEmail().toLowerCase();

                if (response.getGender() != null) {
                    gender = response.getGender();
                    if (gender.equalsIgnoreCase("male"))
                        gender = "m";
                    else if (gender.equalsIgnoreCase("female"))
                        gender = "f";
                }

                if (response.getMobile() != null)
                    phoneNumber = response.getMobile().toString();

                if (response.getDob() != null) {
                    bDate = response.getDob().toString();
                    bDate = bDate.replace("-", "");
                    Log.e("facebook", "bday " + bDate);
                }

                if (response.getFirstname() != null)
                    fName = response.getFirstname().toLowerCase();

                if (response.getLastname() != null)
                    lName = response.getLastname().toLowerCase();

                if (response.getCity() != null)
                    city = response.getCity().toLowerCase();

                if (response.getState() != null)
                    state = response.getState().toLowerCase();

                if (response.getZipcode() != null)
                    zipCode = response.getZipcode().toString();


                AppEventsLogger.setUserData(email, fName, lName, phoneNumber, bDate, gender, city, state, zipCode, country);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }*/
    }

    public static void trackLogoutUser(Context context) {
     /*   if (Utils.FLAG_WEB_ENGAGE) {
            try {
                WebEngage.get().user().logout();

                Log.e(WE_LOG, "Web Engage user logged out");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                String userId = PrefManager.getString(context, RummyConstants.PLAYER_USER_ID, "0");
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString(Key_UserId, userId);
                logger.logEvent(USER_LOGGED_OUT, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void trackEvent(String eventName, Map<String, Object> map, Context context, Bundle params) {
       /* if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Analytics weAnalytics = WebEngage.get().analytics();

                if (map == null)
                    weAnalytics.track(eventName);
                else
                    weAnalytics.track(eventName, map);

                Log.w(CommonEventTracker.WE_LOG, "WebEngage event tracked: " + eventName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                if (params != null) {
                    params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                    logger.logEvent(eventName, params);
                } else
                    logger.logEvent(eventName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   */ }


    public static void trackPurchaseEvent(String eventName, Map<String, Object> map, Context context, Bundle params, double amount) {
       /* if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Analytics weAnalytics = WebEngage.get().analytics();

                if (map == null)
                    weAnalytics.track(eventName);
                else
                    weAnalytics.track(eventName, map);

                Log.w(CommonEventTracker.WE_LOG, "WebEngage event tracked: " + eventName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                if (params != null) {
                    params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                    logger.logPurchase(BigDecimal.valueOf(amount), Currency.getInstance("INR"), params);
                } else
                    logger.logPurchase(BigDecimal.valueOf(0), Currency.getInstance("INR"), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void trackLoginEvent(String event_name, String userId, Context context) {
      /*  if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put(USER_ID, userId);
                map.put(DEVICE_TYPE, "Mobile");
                map.put(CLIENT_TYPE, Utils.CLIENT_TYPE);

                Analytics weAnalytics = WebEngage.get().analytics();
                weAnalytics.track(event_name, map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString(Key_UserId, userId);
                params.putString(DEVICE_TYPE, "Mobile");
                params.putString(CLIENT_TYPE, Utils.CLIENT_TYPE);
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                logger.logEvent(event_name, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void trackRegisterEvent(JSONObject object, String userId, String type, Context context) {
     /*   if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put(CommonEventTracker.USER_ID, userId);
                map.put(CommonEventTracker.DEVICE_TYPE, "Mobile");
                map.put(CommonEventTracker.CLIENT_TYPE, Utils.CLIENT_TYPE);
                map.put(CommonEventTracker.WE_EMAIL, object.getString("email"));
                map.put(CommonEventTracker.Type, type);
                map.put(CommonEventTracker.WE_PHONE, object.getString("phone"));
                map.put(CommonEventTracker.WE_GENDER, object.getString("gender").toLowerCase());

                Analytics weAnalytics = WebEngage.get().analytics();
                weAnalytics.track(USER_REGISTERED, map);
            } catch (Exception e) {

            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString(CommonEventTracker.USER_ID, userId);
                params.putString(CommonEventTracker.DEVICE_TYPE, "Mobile");
                params.putString(CommonEventTracker.CLIENT_TYPE, Utils.CLIENT_TYPE);
                //params.putString(CommonEventTracker.WE_EMAIL, object.getString("email"));
                params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, type);
                //params.putString(CommonEventTracker.WE_PHONE, object.getString("phone"));
                params.putString(CommonEventTracker.WE_GENDER, object.getString("gender").toLowerCase());
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");

                logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
            } catch (Exception e) {

            }
        }*/
    }


    public static void trackScreenName(String screen, Context context) {
      /*  if (Utils.FLAG_WEB_ENGAGE) {
            Analytics weAnalytics = WebEngage.get().analytics();

            Map<String, Object> screenData = new HashMap<String, Object>();
            screenData.put(CommonEventTracker.USER_ID, PrefManager.getString(context, RummyConstants.PLAYER_USER_ID, "0"));

            weAnalytics.setScreenData(screenData);
            weAnalytics.screenNavigated(screen, screenData);
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            AppEventsLogger logger = AppEventsLogger.newLogger(context);
            Bundle params = new Bundle();
            params.putString(CommonEventTracker.USER_ID, PrefManager.getString(context, RummyConstants.PLAYER_USER_ID, "0"));
            logger.logEvent(screen, params);
        }*/
    }

    public static Date getDateObject(String dateStr) {
        Date date = null;
        try {
            dateStr = dateStr.replace("T", " ");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateStr);
        } catch (Exception e) {
            Log.e(WE_LOG, "EXP: getDateObject->> " + e.getMessage());
        }
        return date;
    }


    public static void trackTournamentRegisterDeRegister(String event_name, String user_id, String tourneyId, String tournament_type, Context context) {
//        if (Utils.FLAG_WEB_ENGAGE) {
//            try {
//                Map<String, Object> map = new HashMap<>();
//                map.put(CommonEventTracker.USER_ID, user_id);
//                map.put(CommonEventTracker.Tournament_id, tourneyId);
//                map.put("tournament_type", tournament_type);
//
//                Analytics weAnalytics = WebEngage.get().analytics();
//                weAnalytics.track(event_name, map);
//            } catch (Exception e) {
//
//            }
//        }
//
//        if (Utils.FLAG_FACEBOOK_EVENTS) {
//
//            try {
//                AppEventsLogger logger = AppEventsLogger.newLogger(context);
//                Bundle params = new Bundle();
//                params.putString(CommonEventTracker.USER_ID, user_id);
//                params.putString(CommonEventTracker.Tournament_id, tourneyId);
//                params.putString("tournament_type", tournament_type);
//                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
//                logger.logEvent(event_name, params);
//            } catch (Exception e) {
//
//            }
//        }
    }


    public static void trackAddCashClickEvent(String event_name, String user_id, Context context) {
//        if (Utils.FLAG_WEB_ENGAGE) {
//            try {
//                Map<String, Object> map = new HashMap<>();
//                map.put(CommonEventTracker.USER_ID, user_id);
//
//                Analytics weAnalytics = WebEngage.get().analytics();
//                weAnalytics.track(event_name, map);
//            } catch (Exception e) {
//
//            }
//        }
//
//        if (Utils.FLAG_FACEBOOK_EVENTS) {
//            try {
//                AppEventsLogger logger = AppEventsLogger.newLogger(context);
//                Bundle params = new Bundle();
//                params.putString(CommonEventTracker.USER_ID, user_id);
//                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
//                logger.logEvent(event_name, params);
//            } catch (Exception e) {
//
//            }
//        }
    }


    public static void trackChangePasswordEvent(String event_name, String user_id, Context context) {

      /*  if (Utils.FLAG_WEB_ENGAGE) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put(CommonEventTracker.USER_ID, user_id);

                Analytics weAnalytics = WebEngage.get().analytics();
                weAnalytics.track(event_name, map);
            } catch (Exception e) {

            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString(CommonEventTracker.USER_ID, user_id);
                logger.logEvent(event_name, params);
            } catch (Exception e) {

            }
        }*/
    }


    public static void trackGameJoinEvent(Context context, String event_name, String user_id, String bet, String buyInAmount, String game_name, String game_type, String max_player) {
       /* if (Utils.FLAG_WEB_ENGAGE) {
            try {
                float betInt = 0, buyInInt = 0;
                int max_playerInt = 0;
                if (bet != null && bet.length() > 0)
                    betInt = Float.parseFloat(bet);
                if (buyInAmount != null && buyInAmount.length() > 0)
                    buyInInt = Float.parseFloat(buyInAmount);
                if (max_player != null && max_player.length() > 0)
                    max_playerInt = Integer.parseInt(max_player);

                Map<String, Object> map = new HashMap<>();
                map.put(CommonEventTracker.USER_ID, user_id);
                map.put("bet", betInt);//
                map.put("buyinamount", buyInInt);//
                map.put("game_name", game_name);
                map.put("game_type", game_type);
                map.put("max_players", max_playerInt);//

                Analytics weAnalytics = WebEngage.get().analytics();
                weAnalytics.track(event_name, map);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("gopal", "My Exept " + e.getMessage());
            }
        }

        if (Utils.FLAG_FACEBOOK_EVENTS) {
            try {
                float betInt = 0, buyInInt = 0;
                int max_playerInt = 0;
                if (bet != null && bet.length() > 0)
                    betInt = Float.parseFloat(bet);
                if (buyInAmount != null && buyInAmount.length() > 0)
                    buyInInt = Float.parseFloat(buyInAmount);
                if (max_player != null && max_player.length() > 0)
                    max_playerInt = Integer.parseInt(max_player);

                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString(CommonEventTracker.USER_ID, user_id);

                params.putString(CommonEventTracker.USER_ID, user_id);
                params.putFloat("bet", betInt);//
                params.putFloat("buyinamount", buyInInt);//
                params.putString("game_name", game_name);
                params.putString("game_type", game_type);
                params.putInt("max_players", max_playerInt);//
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");

                logger.logEvent(event_name, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void trackGamePlayedEvent(String event_name, String user_id, String bet, String game_type) {

      /*  if (Utils.FLAG_WEB_ENGAGE) {
            try {
                float betInt = 0;
                if (bet != null && bet.length() > 0)
                    betInt = Float.parseFloat(bet);

                Map<String, Object> map = new HashMap<>();
                map.put(CommonEventTracker.USER_ID, user_id);
                map.put("bet", betInt);//
                map.put("game_type", game_type);


                Analytics weAnalytics = WebEngage.get().analytics();
                weAnalytics.track(event_name, map);

                Log.e("gopal", "Event tracked " + event_name);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("gopal", "My Exept " + e.getMessage());
            }
        }*/
    }

}
