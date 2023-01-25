import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:sizer/sizer.dart';

import '../../modules/maintenance/maintenance_screen.dart';
import '../../utils/shared_pref_service.dart';
import '../app_constants.dart';
import '../color_constants.dart';

class MaintenanceMethods {
  static String calculateMinutes({
    @required DateTime maintenanceStartTime,
  }) {
    if (maintenanceStartTime.minute <= AppConstants.maxSingleDigitNumber) {
      return AppConstants.zero + maintenanceStartTime.minute.toString();
    } else {
      return maintenanceStartTime.minute.toString();
    }
  }

  static void showMaintenanceWarning({
    @required FToast fToast,
    @required BuildContext context,
    @required int startTime,
  }) async {
    DateTime currentTime = DateTime.now();
    if (currentTime.isAfter(
      DateTime.fromMillisecondsSinceEpoch(
        startTime,
      ),
    )) {
      return;
    }
    await SharedPrefService.addBoolToSharedPref(
      AppConstants.warningShowed,
      true,
    );
    await SharedPrefService.addIntToSharedPref(
      AppConstants.maintenanceStartTime,
      startTime,
    );
    await SharedPrefService.addBoolToSharedPref(
      AppConstants.onMaintenanceScreen,
      false,
    );
    DateTime maintenanceStartTime = DateTime.fromMillisecondsSinceEpoch(
      startTime,
    );
    currentTime = DateTime.now();
    if (await SharedPrefService.getBoolValuesFromSharedPref(
      AppConstants.warningShowed,
    )) {
      fToast.showToast(
        positionedToastBuilder: (
          context,
          child,
        ) =>
            Positioned(
          top: 10.0.h,
          left: 24.0.sp,
          right: 24.0.sp,
          child: child,
        ),
        toastDuration: Duration(
          seconds: maintenanceStartTime.difference(currentTime).inSeconds,
        ),
        child: Container(
          width: 70.0.w,
          height: 5.0.h,
          decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(
                2.0.w,
              ),
              gradient: LinearGradient(
                colors: [
                  ColorConstants.lightYellow,
                  ColorConstants.brownishYellow,
                ],
                begin: Alignment.topLeft,
                end: Alignment.bottomLeft,
                stops: [
                  0.0,
                  0.8,
                ],
                tileMode: TileMode.clamp,
              )),
          child: Center(
            child: Text(
              AppConstants.maintenance +
                  maintenanceStartTime.hour.toString() +
                  AppConstants.colon +
                  calculateMinutes(
                    maintenanceStartTime: maintenanceStartTime,
                  ) +
                  AppConstants.space +
                  maintenanceStartTime.timeZoneName,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: ColorConstants.black,
                fontSize: 8.0.sp,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        ),
      );
    }
  }

  static void closeMaintenanceWarning({
    @required FToast customToast,
  }) async {
    try {
      customToast.removeCustomToast();
    } catch (e) {
      // log("$e");
    }
  }

  static void startMaintenance({
    @required BuildContext context,
    @required int maintenanceEndTime,
    @required FToast fToast,
  }) async {
    DateTime currentTime = DateTime.now();
    if (currentTime.isAfter(
      DateTime.fromMillisecondsSinceEpoch(
        maintenanceEndTime,
      ),
    )) {
      return;
    }
    await SharedPrefService.addBoolToSharedPref(
      AppConstants.warningShowed,
      false,
    );
    closeMaintenanceWarning(
      customToast: fToast,
    );
    await SharedPrefService.addIntToSharedPref(
      AppConstants.maintenanceEndTime,
      maintenanceEndTime,
    );
    if (!(await SharedPrefService.getBoolValuesFromSharedPref(
          AppConstants.onMaintenanceScreen,
        ) ??
        false)) {
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => MaintenanceScreen(
            maintenanceEndTime: DateTime.fromMillisecondsSinceEpoch(
              maintenanceEndTime,
            ),
          ),
        ),
      );
    }
  }

  static void endMaintenance({
    @required BuildContext context,
    @required FToast fToast,
  }) async {
    await SharedPrefService.addBoolToSharedPref(
      AppConstants.warningShowed,
      false,
    );
    closeMaintenanceWarning(
      customToast: fToast,
    );
    if (await SharedPrefService.getBoolValuesFromSharedPref(
      AppConstants.onMaintenanceScreen,
    )) {
      Navigator.pop(context);
    }
  }

  static void updateMaintenance({
    @required int maintenanceEndTime,
  }) async {
    await SharedPrefService.addIntToSharedPref(
      AppConstants.maintenanceEndTime,
      maintenanceEndTime,
    );
  }
}
