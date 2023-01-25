import 'package:easy_localization/easy_localization.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:overlay_support/overlay_support.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';
import 'package:toast/toast.dart';

import 'Model/navigation_service.dart';
import 'Model/push_notification.dart';
import 'Network/deep_link_bloc.dart';
import 'Network/singular_data_service.dart';
import 'constants/string_constants.dart';
import 'modules/login/login_screen.dart';
import 'modules/share_screen/share_screen.dart';
import 'modules/splash_screen/splash_screen.dart';

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    ToastContext().init(context);
    SingularDataService.singularStartup();
  }

  static final FirebaseMessaging _firebaseMessaging =
      FirebaseMessaging.instance;

  @override
  Widget build(BuildContext context) {
    DeepLinkBloc _bloc = DeepLinkBloc();
    final pushNotificationService = PushNotificationService(
      _firebaseMessaging,
    );
    pushNotificationService.initialise();
    return LayoutBuilder(
      builder: (context, constraints) {
        return OrientationBuilder(
          builder: (context, orientation) {
            return Sizer(
              builder: (
                context,
                orientation,
                deviceType,
              ) {
                return OverlaySupport.global(
                  child: MaterialApp(
                    title: StringConstants.dangalGames,
                    localizationsDelegates: context.localizationDelegates,
                    supportedLocales: context.supportedLocales,
                    locale: context.locale,
                    debugShowCheckedModeBanner: false,
                    builder: EasyLoading.init(),
                    navigatorKey: NavigationService.instance.navigationKey,
                    theme: ThemeData(
                      textSelectionTheme: TextSelectionThemeData(
                        selectionHandleColor: Colors.transparent,
                      ),
                      indicatorColor: Colors.black,
                      highlightColor: Colors.black,
                      bottomAppBarColor: Colors.black,
                      primaryColor: Colors.white,
                      primaryColorDark: Color(
                        0xffffff,
                      ),
                      colorScheme: ColorScheme.fromSwatch().copyWith(
                        secondary: Colors.black,
                      ),
                    ),
                    home: Provider<DeepLinkBloc>(
                      create: (context) => _bloc,
                      dispose: (context, bloc) => bloc.dispose(),
                      child: SplashScreen(),
                    ),
                    routes: <String, WidgetBuilder>{
                      '/LoginScreens': (
                        BuildContext context,
                      ) =>
                          LoginScreen(),
                      '/SplashScreen': (
                        BuildContext context,
                      ) =>
                          SplashScreen(),
                      '/PhotoContainerScreen': (
                        BuildContext context,
                      ) =>
                          ShareScreen(
                            userId: StringConstants.emptyString,
                          ),
                      '/ReferAndEarnScreen': (
                        BuildContext context,
                      ) =>
                          ShareScreen(
                            userId: StringConstants.emptyString,
                          ),
                      '/raf': (
                        BuildContext context,
                      ) =>
                          ShareScreen(
                            userId: StringConstants.emptyString,
                          )
                    },
                  ),
                );
              },
            );
          },
        );
      },
    );
  }
}