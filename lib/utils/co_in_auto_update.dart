import 'package:dangal_games_demo/constants/methods/common_methods.dart';
import 'package:dangal_games_demo/constants/methods/flavor_info.dart';
import 'package:dangal_games_demo/constants/shared_pref_keys.dart';
import 'package:dangal_games_demo/utils/shared_pref_service.dart';

import 'datetime_util.dart';

class CoInAutoUpdate{
  static Future setAppLaunchAt() async {
    CommonMethods.printLog('main_prod_co_in', 'setAppLaunchAt');
    try{
      if(FlavorInfo.isCoInWithoutPoker){
        String appLaunchedAt = await SharedPrefService.getStringValuesFromSharedPref(SharedPrefKeys.appLaunchedAt);
        if(appLaunchedAt==null || appLaunchedAt.isEmpty){
          appLaunchedAt = DatetimeUtil.currentDateTime;
          await SharedPrefService.addStringToSharedPref(SharedPrefKeys.appLaunchedAt,appLaunchedAt);
          CommonMethods.printLog('main_prod_co_in', 'setAppLaunchAt saved');
        }
        CommonMethods.printLog('main_prod_co_in', 'setAppLaunchAt appLaunchedAt=>$appLaunchedAt');
      }
    }catch(e){
      CommonMethods.printLog('main_prod_co_in', 'setAppLaunchAt e=>$e');
    }
  }

 static Future<bool> isVersionCheck() async {
    bool isCheck = true;
    try{
      if(FlavorInfo.isCoInWithoutPoker){
        String appLaunchedAt = await SharedPrefService.getStringValuesFromSharedPref(SharedPrefKeys.appLaunchedAt);
        CommonMethods.printLog('main_prod_co_in', 'isVersionCheck');
        if(appLaunchedAt!=null && appLaunchedAt.isNotEmpty){
          int mins = DatetimeUtil.diffFromCurrentInMins(appLaunchedAt);
          isCheck = mins >= 60;
          CommonMethods.printLog('main_prod_co_in', 'isVersionCheck mins=>$mins | isCheck=>$isCheck');
          return isCheck;
        }
      }
    }catch(e){
      isCheck = true;
      CommonMethods.printLog('main_prod_co_in', 'isVersionCheck e=>$e');
    }
    CommonMethods.printLog('main_prod_co_in', 'isVersionCheck isCheck=>$isCheck');
    return isCheck;
  }
}

