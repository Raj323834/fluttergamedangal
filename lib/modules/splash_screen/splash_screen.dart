import 'dart:async';

import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../Model/logging_model.dart';
import '../../Network/deep_link_bloc.dart';
import '../../Network/deep_link_handler.dart';
import '../../Network/singular_data_service.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_widgets/base_retry_dialog.dart';
import '../../constants/shared_pref_keys.dart';
import '../../utils/co_in_auto_update.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../login/login_screen.dart';
import 'widgets/splash_screen_widget.dart';

class SplashScreen extends StatefulWidget {
  @override
  SplashScreenState createState() => new SplashScreenState();
}

class SplashScreenState extends State<SplashScreen> {
  //Determines the inital page with deep link
  String deepLinkAddress = "";

  @override
  void initState() {
    super.initState();
    checkForExistingUser();
    SingularDataService.appLaunch();
    CoInAutoUpdate.setAppLaunchAt();
  }

  @override
  Widget build(BuildContext context) {
    DeepLinkBloc _bloc = Provider.of<DeepLinkBloc>(context);
    //Storing Device Size In App Singleton
    Singleton().deviceSize = MediaQuery.of(context).size;

    return StreamBuilder<String>(
      stream: _bloc.state,
      builder: (
        context,
        snapshot,
      ) {
        if (snapshot.hasData) {
          deepLinkAddress =
              DeepLinkHandler().getDeepLinkAddress(data: snapshot.data);
          return SplashScreenWidget();
        } else {
          return SplashScreenWidget();
        }
      },
    );
  }

  //Checks If User Is Already Loggedin Or Not
  checkForExistingUser() async {
    var _duration = new Duration(
      seconds: 3,
    );
    //fetching userid and token from shared pref
    var userId = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userID,
    );
    var accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.onMaintenanceScreen,
      false,
    );

    if (userId == null && accessToken == null) {
      return Timer(
        _duration,
        navigateToLoginPage,
      );
    } else if (userId == null || accessToken == null) {
      LoggingModel.logging(
        "Not fresh login",
        "Either user id or access token is null",
        DateTime.now().toString(),
        userId,
      );
      return new Timer(
        _duration,
        navigateToLoginPage,
      );
    } else {
      return new Timer(
        _duration,
        navigateToHomePage,
      );
    }
  }

  void navigateToLoginPage() async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) {
          return BaseRetryDialog(
            onTap: () {
              Navigator.of(
                context,
                rootNavigator: true,
              ).pop();
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(
                  builder: (
                    BuildContext context,
                  ) =>
                      SplashScreen(),
                ),
                (route) => false,
              );
            },
          );
        },
      );
    } else {
      await sockets.reconnect();
      await SharedPrefService.addBoolToSharedPref(
        SharedPrefKeys.deepLink,
        false,
      );
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (
            BuildContext context,
          ) =>
              LoginScreen(
            deepLinkAddress: deepLinkAddress,
          ),
        ),
      );
    }
  }

  //TO BE CHANGED
  Future<void> navigateToHomePage() async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) {
          return BaseRetryDialog(
            onTap: () {
              Navigator.of(
                context,
                rootNavigator: true,
              ).pop();
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(
                  builder: (
                    BuildContext context,
                  ) =>
                      SplashScreen(),
                ),
                (route) => false,
              );
            },
          );
        },
      );
    } else {
      await sockets.reconnect();
      var userId = await SharedPrefService.getStringValuesFromSharedPref(
        SharedPrefKeys.userID,
      );
      var accessToken = await SharedPrefService.getStringValuesFromSharedPref(
        SharedPrefKeys.accessToken,
      );

      if (userId == null && accessToken == null) {
        await SharedPrefService.addBoolToSharedPref(
          SharedPrefKeys.deepLink,
          false,
        );
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (
              BuildContext context,
            ) =>
                LoginScreen(
              deepLinkAddress: deepLinkAddress,
            ),
          ),
        );
      } else if (userId == null || accessToken == null) {
        LoggingModel.logging(
          "Not fresh login",
          "Either user id or access token is null",
          DateTime.now().toString(),
          userId,
        );
        await SharedPrefService.addBoolToSharedPref(
          SharedPrefKeys.deepLink,
          false,
        );
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (
              BuildContext context,
            ) =>
                LoginScreen(
              deepLinkAddress: deepLinkAddress,
            ),
          ),
        );
      } else {
        DeepLinkHandler().navigateToLink(
            context: context, deepLinkAddress: deepLinkAddress, userId: userId);
      }
    }
  }
}
