import '../constants/methods/common_methods.dart';
import 'package:in_app_update/in_app_update.dart';

class PSInAppUpdate{
  static const String TAG='PSInAppUpdate';
  static bool _isFlexibleUpdateAvailable = false;

  static bool get isFlexibleUpdateAvailable =>
      _isFlexibleUpdateAvailable;

  static Future<AppUpdateInfo> get checkForUpdate async {
    return await InAppUpdate.checkForUpdate()
        .catchError((e) {
          CommonMethods.printLog(TAG, 'checkForUpdate e=>$e');
      return null;
    });
  }

  static Future<void> get performImmediate async{
    try{
      AppUpdateInfo _updateInfo = await checkForUpdate;
      if(_updateInfo?.updateAvailability ==
          UpdateAvailability.updateAvailable){
        AppUpdateResult result = await InAppUpdate.performImmediateUpdate();
        CommonMethods.printLog(TAG, 'performImmediate result=>$result');
      }
    }catch(e){
      CommonMethods.printLog(TAG, 'performImmediate e=>$e');
    }
  }

  static Future<void> get startFlexible async{
    try{
      // if(_isFlexibleUpdateAvailable){
      //   completeFlexible;
      // }else{
        AppUpdateInfo _updateInfo = await checkForUpdate;
        if(_updateInfo?.updateAvailability ==
            UpdateAvailability.updateAvailable){
          AppUpdateResult result = await InAppUpdate.startFlexibleUpdate();
          if(result==AppUpdateResult.success){
            _isFlexibleUpdateAvailable = true;
            completeFlexible;
          }
        }
      // }
    }catch(e){
      CommonMethods.printLog(TAG, 'startFlexible e=>$e');
    }
  }

  static Future<void> get completeFlexible async{
    try{
      await InAppUpdate.completeFlexibleUpdate();
    }catch(e){
      CommonMethods.printLog(TAG, 'completeFlexible e=>$e');
    }
  }

}