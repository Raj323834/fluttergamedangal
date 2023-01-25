import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';
import '../constants/shared_pref_keys.dart';
import '../modules/maintenance/maintenance_screen.dart';
import '../utils/shared_pref_service.dart';

String calculateMinutes(
  DateTime maintenanceStartTime,
) {
  if (maintenanceStartTime.minute <= 9) {
    return "0" + maintenanceStartTime.minute.toString();
  } else {
    return maintenanceStartTime.minute.toString();
  }
}

void showMaintenanceWarning(
  FToast fToast,
  BuildContext context,
  int startTime,
) async {
  DateTime currentTime = DateTime.now();
  if (currentTime.isAfter(
    DateTime.fromMillisecondsSinceEpoch(
      startTime,
    ),
  )) {
    return;
  }
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.warningShowed,
    true,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.maintenanceStartTime,
    startTime,
  );
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.onMaintenanceScreen,
    false,
  );
  DateTime maintenanceStartTime = DateTime.fromMillisecondsSinceEpoch(
    startTime,
  );
  currentTime = DateTime.now();
  if (await SharedPrefService.getBoolValuesFromSharedPref(
    SharedPrefKeys.warningShowed,
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
        seconds: maintenanceStartTime
            .difference(
              currentTime,
            )
            .inSeconds,
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
            stops: [0.0, 0.8],
            tileMode: TileMode.clamp,
          ),
        ),
        child: Center(
          child: Text(
            "Scheduled maintenance starting at " +
                maintenanceStartTime.hour.toString() +
                ":" +
                calculateMinutes(maintenanceStartTime) +
                " " +
                maintenanceStartTime.timeZoneName,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: Colors.black,
              fontSize: 8.0.sp,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }
}

void closeMaintenanceWarning(
  FToast customToast,
) async {
  try {
    customToast.removeCustomToast();
  } catch (e) {
    // log("$e");
  }
}

void startMaintenance(
  BuildContext context,
  int maintenanceEndTime,
  FToast fToast,
) async {
  DateTime currentTime = DateTime.now();
  if (currentTime.isAfter(
    DateTime.fromMillisecondsSinceEpoch(
      maintenanceEndTime,
    ),
  )) {
    return;
  }
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.warningShowed,
    false,
  );
  closeMaintenanceWarning(
    fToast,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.maintenanceEndTime,
    maintenanceEndTime,
  );
  if (!(await SharedPrefService.getBoolValuesFromSharedPref(
        SharedPrefKeys.onMaintenanceScreen,
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

void endMaintenance(
  BuildContext context,
  FToast fToast,
) async {
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.warningShowed,
    false,
  );
  closeMaintenanceWarning(
    fToast,
  );
  if (await SharedPrefService.getBoolValuesFromSharedPref(
    SharedPrefKeys.onMaintenanceScreen,
  )) {
    Navigator.pop(context);
  }
}

void updateMaintenance(
  int maintenanceEndTime,
) async {
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.maintenanceEndTime,
    maintenanceEndTime,
  );
}
