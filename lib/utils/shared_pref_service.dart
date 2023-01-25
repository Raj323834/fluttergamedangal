import 'package:dangal_games_demo/constants/methods/flavor_info.dart';
import 'package:dangal_games_demo/constants/shared_pref_keys.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/methods/common_methods.dart';

class SharedPrefService {
  static Future<Map> storeInSharedPref(
    String key,
    String value,
  ) async {
    Map<String, Object> result = Map();
    result['isStored'] = false;
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      result['isStored'] = await prefs.setString(
        key,
        value,
      );
    } catch (e) {
      result['error'] =
          'Error occured while storing ' + key + ': ' + e.toString();
      CommonMethods.printLog(
        "",
        result['error'].toString(),
      );
    }
    return result;
  }

  static Future<Map> fetchFromSharedPref(
    String key,
  ) async {
    Map<String, Object> result = Map();
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      result[key] = prefs.getString(key);
    } catch (e) {
      result['error'] =
          'Error occured while getting ' + key + ': ' + e.toString();
    }
    return result;
  }

  static Future<Map> clear() async {
    Map<String, Object> result = Map();
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      if (FlavorInfo.isCoInWithoutPoker) {
        String appLaunchedAt = prefs.getString(SharedPrefKeys.appLaunchedAt);
        result['cleared'] = await prefs.clear();
        prefs.setString(SharedPrefKeys.appLaunchedAt, appLaunchedAt);
      } else {
        result['cleared'] = await prefs.clear();
      }
      CommonMethods.printLog(
        "",
        'Persistence storage cleared.',
      );
    } catch (e) {
      result['error'] =
          'Error occured while clearing shared prefernces : ' + e.toString();
    }
    return result;
  }

  static Future<bool> addStringToSharedPref(
    String key,
    String value,
  ) async {
    bool isStored = false;
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString(
        key,
        value,
      );
      isStored = true;
    } catch (e) {
      isStored = false;
      CommonMethods.printLog(
        "",
        'Error occured while storing ' + key + ': ' + e.toString(),
      );
    }
    return isStored;
  }

  static Future<bool> addIntToSharedPref(
    String key,
    int value,
  ) async {
    bool isStored = false;
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setInt(
        key,
        value,
      );
      isStored = true;
    } catch (e) {
      isStored = false;
      CommonMethods.printLog(
        "",
        'Error occured while storing ' + key + ': ' + e.toString(),
      );
    }
    return isStored;
  }

  static Future<bool> addBoolToSharedPref(
    String key,
    bool value,
  ) async {
    bool isStored = false;
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setBool(
        key,
        value,
      );
      isStored = true;
    } catch (e) {
      isStored = false;
      CommonMethods.printLog(
        "",
        'Error occured while storing ' + key + ': ' + e.toString(),
      );
    }
    return isStored;
  }

  static Future<bool> addListToSharedPref(
    String key,
    List value,
  ) async {
    bool isStored = false;
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setStringList(
        key,
        value.toList(),
      );
      isStored = true;
    } catch (e) {
      isStored = false;
      CommonMethods.printLog(
        "",
        'Error occured while storing ' + key + ': ' + e.toString(),
      );
    }
    return isStored;
  }

  static Future<String> getStringValuesFromSharedPref(
    String key,
  ) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return String
    return prefs.getString(key);
  }

  static Future<bool> getBoolValuesFromSharedPref(
    String key,
  ) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return bool
    return prefs.getBool(key);
  }

  static Future<int> getIntValuesFromSharedPref(
    String key,
  ) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return int
    return prefs.getInt(key);
  }

  static Future<double> getDoubleValuesFromSharedPref(
    String key,
  ) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return double
    return prefs.getDouble(key);
  }

  static Future<List> getListValuesFromSharedPref(
    String key,
  ) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return List
    return prefs.get(key);
  }
}
