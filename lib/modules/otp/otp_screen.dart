import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:otp_text_field/otp_text_field.dart';
import 'package:sms_autofill/sms_autofill.dart';

import '../../Model/app_maintenance.dart';
import '../../Model/firebase_analytics_model.dart';
import '../../Model/logging_model.dart';
import '../../Model/store_user_info_helper.dart';
import '../../Network/auth_service.dart';
import '../../Network/deep_link_handler.dart';
import '../../Network/singular_data_service.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_models/user_info_dm.dart';
import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/footer.dart';
import '../../constants/app_constants.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../home/home.dart';
import 'widgets/enter_otp_widget.dart';
import 'widgets/resend_otp_button.dart';
import 'widgets/timer_widget.dart';

class OtpScreen extends StatefulWidget {
  final String mobileNumber;
  final String requestID;
  final String refercode;
  final bool isNew;
  final String language;
  final bool languageSelected;
  final FToast fToast;
  final String deepLinkAddress;

  OtpScreen({
    this.mobileNumber,
    this.requestID,
    this.refercode,
    this.isNew,
    this.language,
    this.languageSelected,
    this.fToast,
    this.deepLinkAddress = '',
  });

  @override
  State<StatefulWidget> createState() => _OTPScreenState();
}

class _OTPScreenState extends State<OtpScreen> with CodeAutoFill {
  bool isUserSignedIn = false;
  final OtpFieldController _otpFieldController = OtpFieldController();
  String username = StringConstants.emptyString;
  String userId = StringConstants.emptyString;
  String hashString = StringConstants.emptyString;
  Timer _timer;
  int _start = 30;
  bool sendHandleOTPLogin = true;
  bool isTimerFinished = false, isTooManyAttempt = false;

  static const String TAG = "OTPScreen  ";

  @override
  void codeUpdated() async {
    FocusScope.of(context).requestFocus(
      FocusNode(),
    );
    // setState(
    //   () {
    //
    //   },
    // );

    try {
      _otpFieldController.set(
        code.trim().split(""),
      );
    } catch (e) {
      CommonMethods.printLog(TAG, 'e=$e');
    }

    // await Future.delayed(
    //   Duration(
    //     milliseconds: 500,
    //   ),
    // );
    // showProgress();
  }

  @override
  void initState() {
    CommonMethods.printLog(
      "Is a new Registration",
      widget.isNew.toString(),
    );
    startTimer();
    getHashString();
    listenForCode();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        body: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Column(
              children: [
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.only(
                    top: 100.0,
                    left: 14,
                    right: 14,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      //Otp Sent To Number Text
                      Text(
                        'OTP sent to ${widget.mobileNumber}',
                        style: TextStyle(
                          color: ColorConstants.white,
                          fontSize: 20,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                      SizedBox(
                        height: 15,
                      ),
                      //Change Mobile Number Button
                      GestureDetector(
                        onTap: () {
                          Navigator.of(context).pop();
                        },
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(
                              Icons.edit,
                              color: ColorConstants.white,
                              size: 14,
                            ),
                            SizedBox(
                              width: 8,
                            ),
                            Text(
                              "otp_screen.change".tr(),
                              style: TextStyle(
                                color: ColorConstants.white,
                                fontSize: 15,
                                fontWeight: FontWeight.w700,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        height: 50,
                      ),
                      //Enter OTP Widget
                      EnterOtpWidget(
                        onCompleted: (pin) async {
                          FocusScope.of(context).requestFocus(
                            FocusNode(),
                          );
                          showProgress();
                          await handleMobileOTPLogin(
                            source: 'Manual OTP',
                            mobileNumber: widget.mobileNumber,
                            otpNumber: pin,
                            referCode: widget.refercode,
                            language: widget.language,
                            isLanguageSelected: widget.languageSelected,
                            isNew: widget.isNew,
                            fToast: widget.fToast,
                          );
                        },
                        otpFieldController: _otpFieldController,
                      ),
                      SizedBox(
                        height: 50,
                      ),
                      //Timer
                      TimerWidget(
                        isTimerFinished: isTimerFinished,
                        start: _start,
                      ),
                      //Resent OTP Button
                      ResendOtpButton(
                        onTap: () async {
                          setState(
                            () {
                              isTimerFinished = false;
                            },
                          );
                          startTimer();
                          await resendOTP(
                            widget.mobileNumber,
                            widget.requestID,
                            "LOGIN_MOBILE",
                          );
                          autoCaptureOTP();
                        },
                        isTimerFinished: isTimerFinished,
                      )
                    ],
                  ),
                ),
              ],
            ),
            //Footer Icons And Text
            Padding(
              padding: const EdgeInsets.only(
                bottom: 30.0,
              ),
              child: Footer(),
            )
          ],
        ),
      ),
    );
  }

  //METHODS
  void startTimer() {
    _start = 45;
    final oneSec = const Duration(
      seconds: 1,
    );
    cancelTimer();
    _timer = Timer.periodic(
      oneSec,
      (Timer timer) {
        if (_start == 0) {
          setState(
            () {
              if (timer.isActive) timer.cancel();
              isTimerFinished = true;
              isTooManyAttempt = false;
            },
          );
        } else {
          setState(
            () {
              print(_start--);
            },
          );
        }
      },
    );
  }

  void cancelTimer() {
    if (_timer != null && _timer.isActive) {
      _timer.cancel();
    }
  }

  void getHashString() {
    SmsAutoFill().getAppSignature.then(
      (signature) {
        setState(
          () {
            hashString = signature;
          },
        );
      },
    );
  }

  void autoCaptureOTP() {
    CommonMethods.printLog(
      "OTP MESSAGEEEEEEEE",
      "auto complete function call",
    );
  }

  @override
  void dispose() {
    cancelTimer();
    super.dispose();
    SmsAutoFill().unregisterListener();
    cancel();
  }

  Future<void> handleMobileOTPLogin({
    String source,
    String mobileNumber,
    String otpNumber,
    String referCode,
    String language,
    bool isLanguageSelected,
    bool isNew,
    FToast fToast,
  }) async {
    CommonMethods.printLog(
      TAG,
      '-----------handle mobile OTP  login----------',
    );
    String requestID = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.requestId,
    );
    LoggingModel.logging(
      "handleMobileOTPLogin :  ",
      source + "    " + mobileNumber,
      DateTime.now().toString(),
      userId,
    );
    Map<String, Object> result = await AuthService.signInWithMobileOTP(
      mobileNumber,
      referCode,
      Singleton().deviceSize.width.toString(),
      Singleton().deviceSize.height.toString(),
      requestID,
      otpNumber,
      language,
      isLanguageSelected,
      userId,
    );
    sendHandleOTPLogin = true;
    if (result.containsKey('noInternet')) {
      Navigator.of(context).pop();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      Navigator.of(context).pop();
      showDialog(
        context: context,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );

      var eventData;
      if (isNew) {
        eventData = {
          'methodUsed': 'MOBILE',
          'status': 'FAILED',
          'referral_code': referCode
        };
      } else {
        eventData = {
          'methodUsed': 'MOBILE',
          'status': 'FAILED',
        };
      }
      CleverTapPlugin.recordEvent(
        isNew ? "Sign Up" : "Login",
        eventData,
      );
      var eventDataMobile = {
        'mobile_number': mobileNumber,
        'mobile_verified': false
      };
      CleverTapPlugin.recordEvent(
        "Mobile Status",
        eventDataMobile,
      );
      return;
    } else {
      Map data = result['data'];
      CommonMethods.devLog(
        data.toString(),
      );
      if (data.containsKey('error')) {
        Navigator.of(context).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode,
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': "false"
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
      } else if (data['result'] == 'SUCCESS') {
        Navigator.of(context).pop();
        // Saving access token
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.mobileNumber,
          mobileNumber,
        );
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
        CommonMethods.printLog(
          TAG,
          "Saving in Shared preferences ",
        );

        userId = data['user_id'];

        await getWalletUserInfo(
          userInfoDM: UserInfoDM.fromJson(
            data['userInfoResponse'],
          ),
          language: language,
          isLanguageSelected: isLanguageSelected,
        );
        cancelTimer();

        Map<String, dynamic> profile = {
          'Name': username,
          'Identity': userId,
          'Email': StringConstants.emptyString,
          'Phone': mobileNumber,
        };
        CommonMethods.printLog(
          "PROFILE cleverTap",
          profile.toString(),
        );
        await CleverTapPlugin.onUserLogin(
          profile,
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'SUCCESS',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'SUCCESS',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );

        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': true
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );

        await FirebaseAnalyticsModel.analyticsSetUserId(
          userId: userId,
        );

        if (isNew) {
          SingularDataService.appRegister(
            MediaQuery.of(context).size.width.toString(),
            MediaQuery.of(context).size.height.toString(),
            "Mobile",
            referCode,
            userId,
          );
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: REGISTRATION_EVENT_FIREBASE,
          );
        }

        closeMaintenanceWarning(fToast);
        if (widget.deepLinkAddress != null &&
            widget.deepLinkAddress.isNotEmpty) {
          DeepLinkHandler().navigateToLink(
              context: context,
              deepLinkAddress: widget.deepLinkAddress,
              userId: userId);
        } else {
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (BuildContext context) => HomeScreen(
                landingPage: AppConstants.home,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        await sockets.send(
          json.encode(
            {
              "type": WebSocketTopics.initiate,
              "userId": userId,
            },
          ),
        );
      } else if (data['result'] == 'UPS_NOT_REACHABLE') {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Error in validating OTP ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
      } else if (data['result'] == 'DB_ERROR') {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Database Error ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
      } else if (data['result'] == 'AUTH_SERVICE_NOT_REACHABLE') {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Error in validating OTP ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
      } else if (data['result'] == 'USER_LOCKED') {
        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        Navigator.of(context).pop();
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
      } else if (data['result'] == ResponsesKeys.INVALID_OTP) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Please enter a valid OTP.",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.INVALID_REQUEST_ID) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Invalid OTP ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.REQUEST_ID_MISSING) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Internal server error. Please try again. ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "User not Found ",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
          const platform = MethodChannel(
            'com.flutter.fbevents',
          );
          platform.invokeMethod(
            "customEvents",
            {
              "eventName": "REGIS DONE",
            },
          );
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.MOBILE_IN_USE) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Mobile Number already in use",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.REDIS_ERROR) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Internal server error. Please try again.",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.TOO_MANY_ATTEMPTS) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error:
                  "You have made too many attempts. Please wait for some time.",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        setState(
          () {
            isTooManyAttempt = true;
            startTimer();
          },
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.OTP_NOT_VERIFIED) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Please enter a valid OTP.",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else if (data['result'] == ResponsesKeys.OTP_NOT_GENERATED) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Your OTP has been expired. Please resend OTP.",
            );
          },
        );

        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        _otpFieldController.clear();
      } else {
        var eventData;
        if (isNew) {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
            'referral_code': referCode
          };
        } else {
          eventData = {
            'methodUsed': 'MOBILE',
            'status': 'FAILED',
          };
        }
        CleverTapPlugin.recordEvent(
          isNew ? "Sign Up" : "Login",
          eventData,
        );
        var eventDataMobile = {
          'mobile_number': mobileNumber,
          'mobile_verified': false
        };
        CleverTapPlugin.recordEvent(
          "Mobile Status",
          eventDataMobile,
        );
        Navigator.of(context).pop();
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Something went wrong, please try again!",
            );
          },
        );
      }
    }
  }

  Future<void> resendOTP(
    String mobileNumber,
    String requestID,
    String template,
  ) async {
    CommonMethods.printLog("", '-----------RESEND OTP----------');
    CommonMethods.printLog(
      "",
      mobileNumber,
    );

    Map<String, Object> result = await AuthService.requestResendOTPMobile(
      mobileNumber,
      requestID,
      template,
      hashString,
      userId,
    );
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
            error: "Error in Sending OTP ",
          );
        },
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
      } else if (data['result'] == 'TOKEN_EXPIRED') {
        await resendOTP(
          mobileNumber,
          requestID,
          template,
        );
      } else if (data['result'] == 'OTP_RESENT') {
        CommonMethods.printLog(
          "",
          "data['request_id']  :  " + data['request_id'],
        );
        Fluttertoast.showToast(
          msg: "Otp Resent Successfully",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.SNACKBAR,
          timeInSecForIosWeb: 2,
          backgroundColor: ColorConstants.black54,
          textColor: ColorConstants.white,
          fontSize: 16.0,
        );
      } else if (data['result'] == 'DB_ERROR') {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Internal server error. Please try again.",
            );
          },
        );
      } else if (data['result'] == 'REDIS_ERROR') {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Internal server error. Please try again.",
            );
          },
        );
      } else if (data['result'] == 'REQUEST_ID_MISSING') {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Internal server error. Please try again.",
            );
          },
        );
      } else {
        showDialog(
          context: context,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: "Error in resending OTP ",
            );
          },
        );
      }
    }
  }

  Future<void> getWalletUserInfo({
    UserInfoDM userInfoDM,
    String language,
    bool isLanguageSelected,
  }) async {
    CommonMethods.printLog(
      "",
      '-----------WALLET/USER INFO REQUEST----------',
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
        isLanguageSelected,
        language,
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
        isLanguageSelected,
        language,
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
        isLanguageSelected,
        language,
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
        isLanguageSelected,
        language,
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
        isLanguageSelected,
        language,
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
        isLanguageSelected,
        language,
      );
    }
  }

  Future showProgress() {
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return WillPopScope(
          onWillPop: () {
            return Future.delayed(
              Duration.zero,
            ).then(
              (value) => false,
            );
          },
          child: Center(
            child: CircularProgressIndicator(
              backgroundColor: ColorConstants.blue,
            ),
          ),
        );
      },
    );
  }
}
