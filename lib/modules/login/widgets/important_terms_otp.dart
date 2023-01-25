import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:provider/provider.dart';
import 'package:sms_autofill/sms_autofill.dart';

import '../../../Model/wallet_info_model.dart';
import '../../../Network/auth_service.dart';
import '../../../common_widgets/base_error_dialog.dart';
import '../../../constants/app_constants.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/methods/common_methods.dart';
import '../../../constants/shared_pref_keys.dart';
import '../../../constants/string_constants.dart';
import '../../../network_new/constants/responses_keys.dart';
import '../../../utils/shared_pref_service.dart';
import '../../home/home.dart';
import '../../otp/otp_screen.dart';
import 'important_terms_ui.dart';

class ImportantTermsOtp extends StatefulWidget {
  ImportantTermsOtp({
    @required this.number,
    @required this.refercode,
    @required this.language,
    @required this.languageSelected,
    @required this.isNew,
    @required this.userId,
    @required this.deepLinkAddress,
    @required this.fToast,
  });
  final String number, refercode, language;
  final bool languageSelected;
  final bool isNew;
  final String userId;
  final FToast fToast;
  final String deepLinkAddress;

  @override
  _ImportantTermsOtpState createState() => _ImportantTermsOtpState(userId);
}

class _ImportantTermsOtpState extends State<ImportantTermsOtp> {
  String userId;
  GoogleSignIn _googleSignIn = GoogleSignIn();
  FirebaseAuth _auth;
  bool isUserSignedIn = false;

  String hashString = '';

  _ImportantTermsOtpState(String userId) {
    this.userId = userId;
  }

  void initApp() async {
    _auth = FirebaseAuth.instance;
    checkIfUserIsSignedIn();
  }

  @override
  void initState() {
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

    initApp();
  }

  @override
  Widget build(BuildContext context) {
    return ImportantTermsUI(
      iAgree: () async {
        FocusScope.of(context).requestFocus(
          FocusNode(),
        );
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return Center(
              child: CircularProgressIndicator(
                backgroundColor: ColorConstants.blue,
              ),
            );
          },
        );
        await requestForOTP(
          widget.number,
          widget.refercode,
          "LOGIN_MOBILE",
        );
      },
    );
  }

  //METHODS
  void checkIfUserIsSignedIn() async {
    var userSignedIn = await _googleSignIn.isSignedIn();

    setState(
      () {
        isUserSignedIn = userSignedIn;
      },
    );
  }

  void navigateToOTPPage(
    String number,
    String requestId,
    String referCode,
  ) {
    Navigator.of(context).push(
      CupertinoPageRoute(
        builder: (context) => MultiProvider(
          providers: [
            ChangeNotifierProvider(
              create: (context) => WalletInfoModel(),
            )
          ],
          child: OtpScreen(
            mobileNumber: number,
            requestID: requestId,
            refercode: referCode,
            isNew: widget.isNew,
            language: widget.language,
            languageSelected: widget.languageSelected,
            fToast: widget.fToast,
            deepLinkAddress: widget.deepLinkAddress,
          ),
        ),
      ),
    );
  }

  Future<void> requestForOTP(
    String number,
    String refercode,
    String template,
  ) async {
    CommonMethods.printLog(
      "",
      '-----------OTP REQUEST----------',
    );
    CommonMethods.printLog(
      "",
      number.toString(),
    );

    Map<String, Object> result = await AuthService.requestOTPMobile(
      widget.number,
      template,
      hashString,
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
      } else if (data['result'] == ResponsesKeys.TOKEN_EXPIRED) {
        // await GenerateAccessToken.reloadSF();
        await requestForOTP(number, refercode, template);
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
        String requestId = data['request_id'];
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
            widget.number,
            requestId,
            refercode,
          );
        }
      } else if (data['result'] == 'DB_ERROR') {
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

  Future _handleSignIn() async {
    var user;
    bool userSignedIn = await _googleSignIn.isSignedIn();

    setState(
      () {
        isUserSignedIn = userSignedIn;
      },
    );

    if (isUserSignedIn) {
      user = _auth.currentUser;
    } else {
      final GoogleSignInAccount googleUser = await _googleSignIn.signIn();
      final GoogleSignInAuthentication googleAuth =
          await googleUser.authentication;

      final AuthCredential credential = GoogleAuthProvider.credential(
        accessToken: googleAuth.accessToken,
        idToken: googleAuth.idToken,
      );

      user = (await _auth.signInWithCredential(credential)).user;
      userSignedIn = await _googleSignIn.isSignedIn();
      setState(
        () {
          isUserSignedIn = userSignedIn;
        },
      );
    }

    return user;
  }

  void onGoogleSignIn(BuildContext context) async {
    await _handleSignIn();
    _googleSignIn.signOut();
    var userSignedIn = await Navigator.pushAndRemoveUntil(
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

    setState(
      () {
        isUserSignedIn = userSignedIn == null ? true : false;
      },
    );
  }
}
