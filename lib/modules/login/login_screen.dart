import 'dart:async';
import 'dart:convert';
import 'dart:developer';
import 'dart:ui';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:sizer/sizer.dart';
import 'package:sms_autofill/sms_autofill.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/auth_service.dart';
import '../../Network/deep_link_handler.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/footer.dart';
import '../../constants/app_constants.dart';
import '../../constants/app_dimens.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/methods/maintenance_methods.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/methods/shared_pref_methods.dart';
import '../../constants/methods/validators.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../home/home.dart';
import '../otp/otp_screen.dart';
import '../terms_and_conditions/terms_and_conditions_screen.dart';
import 'widgets/change_language_button.dart';
import 'widgets/enter_referral_code_widget.dart';
import 'widgets/important_terms_google.dart';
import 'widgets/important_terms_otp.dart';
import 'widgets/logo_and_cards_image.dart';
import 'widgets/or_widget.dart';

class LoginScreen extends StatefulWidget {
  LoginScreen({
    this.deepLinkAddress = "",
  });
  final String deepLinkAddress;

  @override
  State<StatefulWidget> createState() => new LoginScreenState();
}

class LoginScreenState extends State<LoginScreen> {
  bool codeAppliedRefer = false;
  String finalReferCode = '';

  String userName = '';
  String userId = '';
  String hashString = '';
  bool isLangChanged = false;
  TextEditingController _mobileController = TextEditingController();
  TextEditingController _referCodeController = TextEditingController();
  bool languageSelected = false;
  FToast fToast = FToast();

  bool isAbove18 = true;

  //Default Language
  String language = 'English';
  //List Of Languages
  List<String> languageType = [
    'English',
    'Bengali',
    'Gujrati',
    'Hindi',
    'Marathi',
    'Tamil',
  ];

  @override
  void initState() {
    initApp();
    SmsAutoFill().getAppSignature.then(
      (signature) {
        setState(
          () {
            hashString = signature;
          },
        );
      },
    );
    super.initState();
    fToast.init(context);
  }

  void initApp() async {
    CommonMethods.printLog(
      "TAG",
      "initApp",
    );
    //Show language Bottom Sheet
    showLanguageBottomSheet(
      language,
      languageType,
      context,
    );

    // Getting Refer Code From Shared Preferences
    finalReferCode = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.referralCode,
        ) ??
        "";
    if (finalReferCode.isNotEmpty) {
      setState(
        () {
          codeAppliedRefer = true;
        },
      );
    }

    CommonMethods.printLog(
      "LOGIN SCREEN CODES REFER",
      finalReferCode,
    );

    //Saving Refer Code To Shared Preferences
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.referralCode,
      StringConstants.emptyString,
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        body: Padding(
          padding: const EdgeInsets.symmetric(
            horizontal: 14,
            vertical: 19,
          ),
          child: SingleChildScrollView(
            child: Column(
              children: [
                //Change Language Button
                Align(
                  alignment: Alignment.topRight,
                  child: ChangeLanguageButton(
                    onTap: () async {
                      await SharedPrefService.clear();
                      showLanguageBottomSheet(
                        language,
                        languageType,
                        context,
                      );
                    },
                  ),
                ),
                //Logo And Card Image With Text
                LogoAndCardsImage(),
                // Login Signup Text
                Text(
                  StringConstants.loginSignup,
                  style: TextStyle(
                    color: ColorConstants.white,
                    fontWeight: FontWeight.w700,
                    fontSize: 14,
                  ),
                ),
                SizedBox(
                  height: 10,
                ),
                //Enter Your Mobile Number Text
                Text(
                  StringConstants.enterYourMobileNumberToGetStarted,
                  style: TextStyle(
                    color: ColorConstants.white.withOpacity(
                      0.75,
                    ),
                    fontWeight: FontWeight.w400,
                    fontSize: 11,
                  ),
                ),
                SizedBox(
                  height: 14,
                ),
                //Enter Mobile Number Widget
                TextFormField(
                  keyboardType: TextInputType.phone,
                  controller: _mobileController,
                  validator: Validators.mobileNumberValidator,
                  maxLength: 10,
                  inputFormatters: [
                    FilteringTextInputFormatter.allow(
                      RegExpMethods.digits,
                    ),
                  ],
                  onChanged: (value) {
                    if (value.length == 10) {
                      FocusScope.of(context).unfocus();
                    }
                  },
                  decoration: InputDecoration(
                    contentPadding: EdgeInsets.only(
                      left: 8.0.sp,
                      top: 13.5.sp,
                    ),
                    border: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: ColorConstants.purpleBorder,
                      ),
                    ),
                    filled: true,
                    counterText: StringConstants.emptyString,
                    fillColor: ColorConstants.white,
                    hintText: 'login_screen.enter_your_number'.tr(),
                    hintStyle: TextStyle(
                      color: ColorConstants.lightGreyText,
                      fontSize: 13,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
                if (FlavorInfo.isPS)
                  SizedBox(
                    height: 4,
                  ),
                if (FlavorInfo.isPS)
                  Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      SizedBox(
                        width: 24,
                        height: 24,
                        child: Checkbox(
                          materialTapTargetSize:
                              MaterialTapTargetSize.shrinkWrap,
                          checkColor: ColorConstants.black,
                          fillColor: MaterialStateProperty.resolveWith(
                            (state) => ColorConstants.white,
                          ),
                          value: this.isAbove18,
                          onChanged: (bool value) {
                            setState(
                              () {
                                this.isAbove18 = value;
                              },
                            );
                          },
                        ),
                      ),
                      SizedBox(
                        width: 5,
                      ),
                      RichText(
                        text: TextSpan(
                          style: textStyleCheckBox(
                            fontWeight: FontWeight.w300,
                          ),
                          text: StringConstants.above18AgreeMsg,
                          children: <TextSpan>[
                            TextSpan(
                              text: ' ',
                            ),
                            TextSpan(
                              text: StringConstants.termsConditions,
                              recognizer: TapGestureRecognizer()
                                ..onTap = _onTapTnc,
                              style: textStyleCheckBox(
                                decoration: TextDecoration.underline,
                              ),
                            ),
                          ],
                        ),
                      ),
                      // Text(StringConstants.above18Msg, style: textStyleCheckBox(ColorConstants.white),),
                    ],
                  ),
                SizedBox(
                  height: 11,
                ),
                //Get Started Button
                CustomButton(
                  height: 45,
                  fontSize: 16,
                  fontWeight: FontWeight.w700,
                  gradient: ColorConstants.goldGradientLight,
                  buttonText: StringConstants.getStarted,
                  isEnabled: FlavorInfo.isPS ? isAbove18 : true,
                  onTap: () async {
                    if (FlavorInfo.isPS && !isAbove18) {
                      Fluttertoast.showToast(
                        msg: StringConstants.above18Msg,
                      );
                    } else if (_mobileController.text.length < 10 ||
                        _mobileController.text.length > 10) {
                      Fluttertoast.showToast(
                        msg: StringConstants.enterValidMobileNumber,
                      );
                    } else {
                      CommonMethods.printLog(
                        StringConstants.emptyString,
                        _mobileController.text.toString(),
                      );
                      _mobileController.text = _mobileController.text.trim();
                      //Loading Indicator
                      showDialog(
                        context: context,
                        barrierDismissible: false,
                        builder: (_) {
                          return WillPopScope(
                            onWillPop: () {
                              return Future.delayed(Duration.zero).then(
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
                      Map<String, Object> result =
                          await AuthService.verifyLogin(
                        _mobileController.text,
                        true,
                        userId,
                      );
                      if (result.containsKey(
                        'noInternet',
                      )) {
                        Navigator.of(context).pop();
                        CommonMethods.showSnackBar(
                          context,
                          StringConstants.noInternetConnection,
                        );
                      } else if (result.containsKey('error')) {
                        Navigator.of(context).pop();
                        showDialog(
                          context: context,
                          barrierDismissible: false,
                          builder: (_) {
                            return BaseErrorDialog(
                              title: StringConstants.oops,
                              error: StringConstants.somethingWentWrongTryAgain,
                            );
                          },
                        );
                      } else {
                        Map data = result['data'];
                        if (data.containsKey('error')) {
                          Navigator.of(context).pop();
                          CommonMethods.showSnackBar(
                            context,
                            StringConstants.somethingWentWrongTryAgain,
                          );
                        } else if (data['result'] == ResponsesKeys.SUCCESS) {
                          if (data['is_new_user'] == false) {
                            await requestForOTP(
                              number: _mobileController.text,
                              refercode: finalReferCode,
                              template: "LOGIN_MOBILE",
                              context: context,
                              hashString: hashString,
                            );
                          } else {
                            Navigator.of(context).pop();
                            showDialog(
                              context: context,
                              barrierDismissible: false,
                              builder: (_) {
                                return ImportantTermsOtp(
                                  number: _mobileController.text,
                                  refercode: finalReferCode,
                                  language: language,
                                  languageSelected: languageSelected,
                                  isNew: true,
                                  userId: userId,
                                  fToast: fToast,
                                  deepLinkAddress: widget.deepLinkAddress,
                                );
                              },
                            );
                          }
                        }
                      }
                    }
                  },
                ),
                SizedBox(
                  height: 18,
                ),
                //Or Widget
                OrWidget(),
                SizedBox(
                  height: 12,
                ),
                //Google Sign In Button
                GestureDetector(
                  onTap: () async {
                    var connectivityResult =
                        await Connectivity().checkConnectivity();
                    if (connectivityResult == ConnectivityResult.none) {
                      CommonMethods.showSnackBar(
                        context,
                        StringConstants.noInternetConnection,
                      );
                    } else if (FlavorInfo.isPS && !isAbove18) {
                      Fluttertoast.showToast(
                        msg: StringConstants.above18Msg,
                      );
                    } else {
                      await _handleSignIn(
                        context,
                        userId,
                        finalReferCode,
                        language,
                        languageSelected,
                      );
                    }
                  },
                  child: Image.asset(
                    AssetPaths.google,
                    scale: 3,
                  ),
                ),
                //Have A Referral Code Text Button
                TextButton(
                  onPressed: () {
                    setState(
                      () {
                        _referCodeController.text = finalReferCode;
                      },
                    );
                    showModalBottomSheet(
                      isScrollControlled: true,
                      context: context,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.only(
                          topLeft: Radius.circular(
                            10,
                          ),
                          topRight: Radius.circular(
                            10,
                          ),
                        ),
                      ),
                      builder: (context) {
                        return EnterReferralCodeWidget(
                          referCodeController: _referCodeController,
                          onApply: () {
                            FocusScope.of(context).unfocus();
                            if (_referCodeController.text.isEmpty) {
                              CommonMethods.showSnackBar(
                                context,
                                StringConstants.enterSomeCodeToApply,
                              );
                            } else {
                              setState(
                                () {
                                  codeAppliedRefer = true;

                                  finalReferCode = _referCodeController.text;
                                },
                              );
                              Navigator.pop(context);
                            }
                          },
                        );
                      },
                    ).whenComplete(
                      () {
                        if (!codeAppliedRefer) {
                          if (finalReferCode.length == 0) {
                            _referCodeController.clear();
                          }
                        }
                      },
                    );
                  },
                  child: Text(
                    (!codeAppliedRefer)
                        ? StringConstants.haveAReferralCode
                        : StringConstants.referralCodeApplied,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w700,
                      color: (!codeAppliedRefer)
                          ? ColorConstants.white
                          : ColorConstants.greenText,
                      decoration: TextDecoration.underline,
                    ),
                  ),
                ),
                SizedBox(
                  height: 4,
                ),
                //App Footer Icons and Text
                Footer(),
              ],
            ),
          ),
        ),
      ),
    );
  }

//METHODS
  void showLanguageBottomSheet(
    String language,
    List<String> languageType,
    BuildContext context,
  ) async {
    bool isLangSelected = await SharedPrefService.getBoolValuesFromSharedPref(
      SharedPrefKeys.languageSelected,
    );
    if (isLangSelected == null) {
      isLangChanged = false;
      CommonMethods.showCustomPickerBottomSheet(
        context: context,
        title: StringConstants.selectALanguage,
        initialItem: language,
        list: languageType,
        onTap: () {
          isLangChanged = true;
          Navigator.pop(context);
        },
        onSelectedItemChanged: (index) {
          setState(
            () {
              this.language = language = languageType[index];
            },
          );
        },
      ).whenComplete(
        () async {
          if (isLangChanged) {
            CommonMethods.changeLanguage(
              context: context,
              language: language,
            );
            languageSelected = true;
          }
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.languageSelected,
            true,
          );
          await SharedPrefService.addStringToSharedPref(
            SharedPrefKeys.preferredLanguage,
            language,
          );
        },
      );
    }
  }

  Future _handleSignIn(
    BuildContext context,
    String userId,
    String finalReferCode,
    String language,
    bool languageSelected,
  ) async {
    var user;

    GoogleSignIn googleSignIn = GoogleSignIn();
    if (await googleSignIn.isSignedIn()) {
      googleSignIn.signOut();
    }
    final GoogleSignInAccount googleUser = await googleSignIn.signIn();

    final GoogleSignInAuthentication googleAuth =
        await googleUser.authentication;
    AuthCredential authCredentials = GoogleAuthProvider.credential(
      accessToken: googleAuth.accessToken,
      idToken: googleAuth.idToken,
    );
    FirebaseAuth _auth = FirebaseAuth.instance;
    UserCredential authResult =
        await _auth.signInWithCredential(authCredentials);
    user = authResult.user;
    CommonMethods.printLog(
      "",
      "GOOGLE USER ----- >    ",
    );
    CommonMethods.printLog(
      "",
      user.toString(),
    );
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return Center(
          child: CircularProgressIndicator(
            backgroundColor: ColorConstants.blue,
          ),
        );
      },
    );
    Map<String, Object> result = await AuthService.verifyLogin(
      user.email,
      false,
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
        barrierDismissible: false,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        if (data['is_new_user'] == false) {
          Navigator.of(context).pop();
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return Center(
                child: CircularProgressIndicator(
                  backgroundColor: ColorConstants.blue,
                ),
              );
            },
          );
          handleSocialLogin(
            googleAuth.idToken,
            "GOOGLE",
            user,
            finalReferCode,
            language,
            languageSelected,
            userId,
            context,
          );
        } else {
          GoogleSignIn _googleSignIn = GoogleSignIn();
          Navigator.of(context).pop();
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return ImportantTermsGoogle(
                googleSignIn: _googleSignIn,
                socialToken: googleAuth.idToken,
                user: user,
                referralCode: finalReferCode,
                language: language,
                languageSelected: languageSelected,
                fToast: fToast,
              );
            },
          );
        }
      }
    }
    return user;
  }

  Future<void> requestForOTP({
    @required String number,
    @required String refercode,
    @required String template,
    @required String hashString,
    @required BuildContext context,
  }) async {
    CommonMethods.printLog(
      "",
      '-----------OTP REQUEST----------',
    );
    CommonMethods.printLog(
      "",
      number.toString(),
    );

    Map<String, Object> result = await AuthService.requestOTPMobile(
      number,
      template,
      hashString,
      userId,
    );

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
        barrierDismissible: false,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.errorInSendingOTP,
          );
        },
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        Navigator.of(context).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
      } else if (data['result'] == ResponsesKeys.TOKEN_EXPIRED) {
        await requestForOTP(
          number: number,
          refercode: refercode,
          template: template,
          context: context,
          hashString: hashString,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_MOBILE_NUMBER) {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.invalidMobileNumber,
            );
          },
        );
      } else if (data['result'] == ResponsesKeys.OTP_SENT) {
        Navigator.of(context).pop();
        CommonMethods.printLog(
          "",
          "data['request_id']  :  " + data['request_id'].toString(),
        );
        String requestID = data['request_id'];
        bool isStored = await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.requestId,
          data['request_id'],
        );

        if (!isStored) {
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (_) {
              return BaseErrorDialog(
                title: StringConstants.oops,
                error: StringConstants.couldNotSaveInSharedPref,
              );
            },
          );
          return;
        } else {
          navigateToOTPPage(
            number,
            requestID,
            refercode,
            language,
            languageSelected,
            context,
          );
        }
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.databaseError,
            );
          },
        );
        Navigator.of(context).pop();
      } else {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.errorInSendingOTP,
            );
          },
        );
      }
    }
  }

  void navigateToOTPPage(
    String number,
    String requestID,
    String refercode,
    String language,
    bool languageSelected,
    BuildContext context,
  ) {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (BuildContext context) => OtpScreen(
          mobileNumber: number,
          requestID: requestID,
          refercode: refercode,
          isNew: false,
          language: language,
          languageSelected: languageSelected,
          fToast: fToast,
          deepLinkAddress: widget.deepLinkAddress,
        ),
      ),
    );
  }

// this method will tell us if the user is already existing in our database
// (then show the terms and condition dialog box) or not.
  void handleSocialLogin(
    String socialLogin,
    socialLoginMethod,
    var user,
    String finalReferCode,
    String language,
    bool languageSelected,
    String userId,
    BuildContext context,
  ) async {
    CommonMethods.printLog(
      "LoginScreens  ",
      '-----------handle social login----------',
    );

    Map<String, Object> result = await AuthService.signInWithSocialLogin(
      socialLogin,
      socialLoginMethod,
      user,
      MediaQuery.of(context).size.width.toString(),
      MediaQuery.of(context).size.height.toString(),
      finalReferCode,
      language,
      languageSelected,
      userId,
    );
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
        barrierDismissible: false,
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
      };
      CleverTapPlugin.recordEvent(
        "Login",
        eventData,
      );

      var eventDataEmail = {
        'email_id': user.email,
        'email_verified': false,
      };
      CleverTapPlugin.recordEvent(
        "Email Status",
        eventDataEmail,
      );
      return;
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        Navigator.of(context).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        Navigator.of(context).pop();
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

        await getWalletUserInfo(
          data['userInfoResponse'],
        );

        Map<String, dynamic> profile = {
          'Name': userName,
          'Identity': userId,
          'Email': user.email,
          'Phone': ''
        };
        CommonMethods.printLog(
          "PROFILE cleverTap",
          profile.toString(),
        );
        await CleverTapPlugin.onUserLogin(profile);

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'SUCCESS',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': true,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );

        await FirebaseAnalyticsModel.analyticsSetUserId(
          userId: userId,
        );
        MaintenanceMethods.closeMaintenanceWarning(
          customToast: fToast,
        );
        if (widget.deepLinkAddress != null &&
            widget.deepLinkAddress.isNotEmpty) {
          DeepLinkHandler().navigateToLink(
            context: context,
            deepLinkAddress: widget.deepLinkAddress,
            userId: userId,
          );
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
        try {
          await sockets.send(
            json.encode(
              {
                "type": WebSocketTopics.initiate,
                "userId": userId,
              },
            ),
          );
        } catch (e) {
          log("line 504: $e");
        }
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.of(context).pop();
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
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
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      } else if (data['result'] == ResponsesKeys.UPS_NOT_REACHABLE) {
        Navigator.of(context).pop();
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.errorInLoadingProfile,
            );
          },
        );
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      } else if (data['result'] == ResponsesKeys.SOCIAL_AUTHENTICATION_FAILED) {
        Navigator.of(context).pop();
        CommonMethods.printLog(
          "",
          "Social Login Fail",
        );
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.socialLoginFailed,
            );
          },
        );
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      } else if (data['result'] == 'USER_LOCKED') {
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error:
                  '${StringConstants.yourAccountHasBeenLocked}${FlavorInfo.supportEmail}',
            );
          },
        );

        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      } else {
        Navigator.of(context).pop();
        Navigator.of(context).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.someTechnicalError,
            );
          },
        );
        var eventData = {
          'methodUsed': 'GOOGLE',
          'status': 'FAILED',
        };
        CleverTapPlugin.recordEvent(
          "Login",
          eventData,
        );
        var eventDataEmail = {
          'email_id': user.email,
          'email_verified': false,
        };
        CleverTapPlugin.recordEvent(
          "Email Status",
          eventDataEmail,
        );
      }
    }
  }

// Used To Store The User's Information In The Shared Preference
  Future<void> getWalletUserInfo(Map<String, dynamic> response) async {
    CommonMethods.printLog("", '-----------WALLET/USER INFO STORING----------');
    Map<String, dynamic> responseMap = response;
    if (responseMap['result'] == ResponsesKeys.SUCCESS) {
      SharedPrefMethods.storeUserInfo(
        responseMap: responseMap,
      );
      SharedPrefMethods.storeWalletInfo(
        cashAmount: responseMap["walletInfo"][0]["amount"].toString(),
        withdrawAmount: responseMap["walletInfo"][1]["amount"].toString(),
        chipsAmount: responseMap["Play Chips"].toString(),
        bonusAmount: responseMap["walletInfo"][3]["amount"].toString(),
        gameChipsAmount: responseMap["walletInfo"][2]["amount"].toString(),
        pokerChipsAmount: responseMap["walletInfo"][4]["amount"].toString(),
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
      SharedPrefMethods.storeWalletInfo(
        cashAmount: "0.0",
        withdrawAmount: "0.0",
        chipsAmount: "10000.0",
        bonusAmount: "0.0",
        gameChipsAmount: "0.0",
        pokerChipsAmount: "0.0",
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    } else if (responseMap['result'] == ResponsesKeys.USER_NOT_FOUND) {
      SharedPrefMethods.storeWalletInfo(
        cashAmount: "0.0",
        withdrawAmount: "0.0",
        chipsAmount: "10000.0",
        bonusAmount: "0.0",
        gameChipsAmount: "0.0",
        pokerChipsAmount: "0.0",
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    } else if (responseMap['result'] == ResponsesKeys.UPS_NOT_REACHABLE) {
      SharedPrefMethods.storeWalletInfo(
        cashAmount: "0.0",
        withdrawAmount: "0.0",
        chipsAmount: "10000.0",
        bonusAmount: "0.0",
        gameChipsAmount: "0.0",
        pokerChipsAmount: "0.0",
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    } else if (responseMap['result'] ==
        ResponsesKeys.WALLET_SERVICE_NOT_REACHABLE) {
      SharedPrefMethods.storeWalletInfo(
        cashAmount: "0.0",
        withdrawAmount: "0.0",
        chipsAmount: "10000.0",
        bonusAmount: "0.0",
        gameChipsAmount: "0.0",
        pokerChipsAmount: "0.0",
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    } else if (responseMap['result'] == ResponsesKeys.WALLET_DOES_NOT_EXIST) {
      SharedPrefMethods.storeUserInfo(
        responseMap: responseMap,
      );
      SharedPrefMethods.storeWalletInfo(
        cashAmount: "0.0",
        withdrawAmount: "0.0",
        chipsAmount: "10000.0",
        bonusAmount: "0.0",
        gameChipsAmount: "0.0",
        pokerChipsAmount: "0.0",
      );
      SharedPrefMethods.storeMandatoryInfo(
        responseMap: responseMap,
        languageSelected: languageSelected,
        language: language,
      );
    }
  }

  TextStyle textStyleCheckBox({
    Color color = ColorConstants.white,
    FontWeight fontWeight = FontWeight.w500,
    TextDecoration decoration = TextDecoration.none,
  }) {
    return TextStyle(
      fontSize: AppDimens.fontSize,
      color: color,
      fontWeight: fontWeight,
      decoration: decoration,
    );
  }

  void _onTapTnc() {
    CommonMethods.printLog('LoginScreen', 'OnTapTNC');
    Navigator.push(
      context,
      CupertinoDialogRoute(
        context: context,
        builder: (BuildContext context) => TermsAndConditionsScreen(
          userId: StringConstants.emptyString,
          isLoggedIn: false,
        ),
      ),
    );
  }
}
