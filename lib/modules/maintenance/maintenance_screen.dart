import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../Network/web_socket_helper_service.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/web_socket_topics.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';

class MaintenanceScreen extends StatefulWidget {
  final DateTime maintenanceEndTime;

  const MaintenanceScreen({
    Key key,
    @required this.maintenanceEndTime,
  }) : super(key: key);

  @override
  _MaintenanceScreenState createState() => _MaintenanceScreenState();
}

class _MaintenanceScreenState extends State<MaintenanceScreen> {
  DateTime currentTime;
  DateTime maintenanceEndTime;

  @override
  void initState() {
    setState(
      () {
        maintenanceEndTime = widget.maintenanceEndTime;
      },
    );
    savingCacheOfThisScreen();
    super.initState();
  }

  @override
  void dispose() {
    disposingCacheOfThisScreen();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () => null,
      child: SafeArea(
        child: Scaffold(
          extendBodyBehindAppBar: true,
          appBar: AppBar(
            leading: GestureDetector(
              onTap: () {
                if (DateTime.now().isAfter(
                  maintenanceEndTime,
                )) {
                  Navigator.pop(context);
                } else {
                  CommonMethods.showSnackBar(
                    context,
                    "App is under Maintenence",
                  );
                }
              },
              child: Icon(
                Icons.arrow_back_ios,
                color: ColorConstants.black,
              ),
            ),
            backgroundColor: ColorConstants.transparent,
            elevation: 0,
          ),
          body: Container(
            height: Singleton().deviceSize.height,
            width: Singleton().deviceSize.width,
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage(
                  "assets/images/background.webp",
                ),
                fit: BoxFit.cover,
              ),
            ),
            child: StreamBuilder<Object>(
              stream: sockets.streamController.stream,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  try {
                    var snapBody = jsonDecode(snapshot.data);
                    if (snapBody['type'] == WebSocketTopics.updateMaintenance) {
                      updatedEndTimeReceived(
                        snapBody['maintenanceEndTime'],
                      );
                    }
                  } catch (e) {}
                }
                return Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      Icons.warning,
                      size: 10.0.h,
                      color: ColorConstants.red900,
                    ),
                    Padding(
                      padding: EdgeInsets.only(
                        left: 3.0.w,
                        right: 3.0.w,
                        top: 2.0.h,
                      ),
                      child: Text(
                        fetchWarningMessage(),
                        style: TextStyle(
                          fontSize: 14.0.sp,
                          fontWeight: FontWeight.bold,
                          color: ColorConstants.black,
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ),
                  ],
                );
              },
            ),
          ),
        ),
      ),
    );
  }

  //Methods
  void savingCacheOfThisScreen() async {
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.onMaintenanceScreen,
      true,
    );
  }

  void disposingCacheOfThisScreen() async {
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.onMaintenanceScreen,
      false,
    );
  }

  String fetchWarningMessage() {
    return "App is under maintenance.\nPlease check back at " +
        maintenanceEndTime.hour.toString() +
        ':' +
        calculateMinutes(maintenanceEndTime);
  }

  String calculateMinutes(DateTime maintenanceEndTime) {
    if (maintenanceEndTime.minute <= 9) {
      return '0' + maintenanceEndTime.minute.toString();
    } else {
      return maintenanceEndTime.minute.toString();
    }
  }

  void updatedEndTimeReceived(int updatedEndtime) {
    setState(
      () {
        maintenanceEndTime =
            DateTime.fromMillisecondsSinceEpoch(updatedEndtime);
      },
    );
  }
}
