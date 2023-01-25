import 'dart:convert';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_sign_in/google_sign_in.dart';

import '../../../Model/app_maintenance.dart';
import '../../../Model/firebase_analytics_model.dart';
import '../../../Model/store_user_info_helper.dart';
import '../../../Network/auth_service.dart';
import '../../../Network/singular_data_service.dart';
import '../../../Network/web_socket_helper_service.dart';
import '../../../common_models/user_info_dm.dart';
import '../../../common_widgets/base_error_dialog.dart';
import '../../../constants/methods/common_methods.dart';
import '../../../constants/methods/flavor_info.dart';
import '../../../constants/shared_pref_keys.dart';
import '../../../constants/string_constants.dart';
import '../../../constants/web_socket_topics.dart';
import '../../../network_new/constants/response_status.dart';
import '../../../network_new/constants/responses_keys.dart';
import '../../../utils/shared_pref_service.dart';
import '../../../utils/singleton.dart';
import '../../home/home.dart';
import 'important_terms_ui.dart';

class ImportantTermsGoogle extends StatefulWidget {
  final String socialToken, referralCode, language;
  final bool languageSelected;
  final GoogleSignIn googleSignIn;
  final user;
  final FToast fToast;

  const ImportantTermsGoogle({
    Key key,
    @required this.user,
    @required this.socialToken,
    @required this.googleSignIn,
    @required this.referralCode,
    @required this.language,
    @required this.languageSelected,
    @required this.fToast,
  }) : super(key: key);

  @override
  _ImportantTermsGoogleState createState() => _ImportantTermsGoogleState();
}

class _ImportantTermsGoogleState extends State<ImportantTermsGoogle> {
  FirebaseAuth auth;
  bool isUserSignedIn = false;
  static const String TAG = "CustomDialogBoxSocial";

  String userName = StringConstants.emptyString;
  String userId = StringConstants.emptyString;

  void initApp() async {
    auth = FirebaseAuth.instance;
    checkIfUserIsSignedIn();
  }

  @override
  void initState() {
    super.initState();
    initApp();
  }

  void checkIfUserIsSignedIn() async {
    var userSignedIn = await widget.googleSignIn.isSignedIn();
    setState(
      () {
        isUserSignedIn = userSignedIn;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return ImportantTermsUI(
      iAgree: () async {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (BuildContext context) {
            return WillPopScope(
              onWillPop: () {
                return Future.delayed(Duration.zero).then(
                  (value) => false,
                );
              },
              child: Center(
                child: CircularProgressIndicator(
                  backgroundColor: Colors.blue,
                ),
              ),
            );
          },
        );
        await handleSocialLogin(
          widget.socialToken,
          "GOOGLE",
          widget.user,
        );
      },
    );
  }

  //METHODS
  handleSocialLogin(
    String socialLogin,
    socialLoginMethod,
    var user,
  ) async {
    CommonMethods.printLog(
      TAG,
      '-----------handle social login----------',
    );
    Map<String, Object> result = await AuthService.signInWithSocialLogin(
      socialLogin,
      socialLoginMethod,
      user,
      MediaQuery.of(context).size.width.toString(),
      MediaQuery.of(context).size.height.toString(),
      widget.referralCode,
      widget.language,
      widget.languageSelected,
      userId,
    );
    Navigator.of(context).pop();
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      showDialog(
        context: context,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );

      var eventData = {
        'methodUsed': 'GOOGLE',
        'status': 'FAILED',
        'referral_code': widget.referralCode
      };
      CleverTapPlugin.recordEvent(
        "Sign Up",
        eventData,
      );
      return;
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.userID,
          data['user_id'],
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.accessToken,
          data['access_token'],
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.refreshToken,
          data['refresh_token'],
        );
        await SharedPrefService.addIntToSharedPref(
          SharedPrefKeys.expireAt,
          data['expire_at'],
        );

        userId = data['user_id'];
        await sockets.send(
          json.encode(
            {
              "type": WebSocketTopics.initiate,
              "userId": userId,
            },
          ),
        );
        await getWalletUserInfo(
          UserInfoDM.fromJson(
            data['userInfoResponse'],
          ),
        );

        Map<String, dynamic> profile = {
          'Name': userName,
          'Identity': userId,
          'Email': user.email,
          'Phone': StringConstants.emptyString
        };
        CommonMethods.printLog(
          "PROFILE cleverTap",
          profile.toString(),
        );
        await CleverTapPlugin.onUserLogin(profile);

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'SUCCESS',
          'referral_code': widget.referralCode
        };

        //CLEVERTAP
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );

        // FACEBOOK
        const platform = MethodChannel(
          'com.flutter.fbevents',
        );
        platform.invokeMethod(
          "customEvents",
          {
            "eventName": "REGIS DONE",
          },
        );

        //SINGULAR

        SingularDataService.appRegister(
          Singleton().deviceSize.width.toString(),
          Singleton().deviceSize.height.toString(),
          "Google",
          widget.referralCode,
          userId,
        );
        //FIREBASE
        await FirebaseAnalyticsModel.analyticsSetUserId(
          userId: userId,
        );
        await FirebaseAnalyticsModel.analyticsLogEvent(
          eventName: REGISTRATION_EVENT_FIREBASE,
        );

        closeMaintenanceWarning(
          widget.fToast,
        );
        Navigator.pushAndRemoveUntil(
          context,
          CupertinoPageRoute(
            builder: (BuildContext context) => HomeScreen(
              landingPage: 0,
              routeDetail: StringConstants.emptyString,
              userId: userId,
            ),
          ),
          (route) => false,
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.databaseError,
            );
          },
        );

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
        //
      } else if (data['result'] == ResponsesKeys.UPS_NOT_REACHABLE) {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: 'Error in loading profile ',
            );
          },
        );

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
      } else if (data['result'] == ResponsesKeys.SOCIAL_AUTHENTICATION_FAILED) {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: 'Socail Login Failed',
            );
          },
        );

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
      } else if (data['result'] == 'USER_LOCKED') {
        Navigator.of(context).pop();
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error:
                  '${StringConstants.yourAccountHasBeenLocked}${FlavorInfo.supportEmail}',
            );
          },
        );
      } else {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: 'Some Technical Error',
            );
          },
        );

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
          'referral_code': widget.referralCode
        };
        CleverTapPlugin.recordEvent(
          "Sign Up",
          eventData,
        );
      }
    }
  }

  Future<void> getWalletUserInfo(
    UserInfoDM userInfoDM,
  ) async {
    CommonMethods.printLog(
      "",
      '-----------WALLET/USER INFO STORING----------',
    );

    if (userInfoDM.result == ResponseStatus.success) {
      storeUserInfo(userInfoDM);
      storeWalletInfo(
        userInfoDM.walletInfo[0].amount.toString(),
        userInfoDM.walletInfo[1].amount.toString(),
        userInfoDM.playChips.toString(),
        userInfoDM.walletInfo[3].amount.toString(),
        userInfoDM.walletInfo[2].amount.toString(),
        userInfoDM.walletInfo[4].amount.toString(),
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    } else if (userInfoDM.result == ResponseStatus.dbError) {
      storeWalletInfo(
        "0.0",
        "0.0",
        "10000.0",
        "0.0",
        "0.0",
        "0.0",
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    } else if (userInfoDM.result == ResponseStatus.userNotFound) {
      storeWalletInfo(
        "0.0",
        "0.0",
        "10000.0",
        "0.0",
        "0.0",
        "0.0",
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    } else if (userInfoDM.result == ResponseStatus.upsNotReachable) {
      storeWalletInfo(
        "0.0",
        "0.0",
        "10000.0",
        "0.0",
        "0.0",
        "0.0",
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    } else if (userInfoDM.result == ResponseStatus.walletServiceNotReachable) {
      storeWalletInfo(
        "0.0",
        "0.0",
        "10000.0",
        "0.0",
        "0.0",
        "0.0",
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    } else if (userInfoDM.result == ResponseStatus.walletDoesNotExist) {
      storeUserInfo(userInfoDM);
      storeWalletInfo(
        "0.0",
        "0.0",
        "10000.0",
        "0.0",
        "0.0",
        "0.0",
      );
      storingMandatoryInfo(
        userInfoDM,
        true,
        widget.language,
      );
    }
  }
}
