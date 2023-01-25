import 'dart:convert' as convert;

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';

class WalletService {
  static Future<Map<String, dynamic>> reloadCoins(
    BuildContext context,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );

    try {
      Map requestData = {
        "chips": 10000,
      };
      var body = convert.jsonEncode(
        requestData,
      );
      CommonMethods.printLog(
        "Reload coins",
        "Request Body---> " + body.toString(),
      );
      final headers = {
        "content-type": "application/json",
        "authorization": "Bearer $accessToken",
        "identifier": userId != null ? userId : StringConstants.emptyString
      };

      CommonMethods.printLog(
        "Reload coins url: ",
        UrlConstants.reloadCoinUrl.toString(),
      );
      final response = await http.put(
        Uri.parse(
          UrlConstants.reloadCoinUrl,
        ),
        headers: headers,
        body: body,
      );

      Map<String, dynamic> responseMap = convert.jsonDecode(
        response.body,
      );

      result['data'] = responseMap;
      CommonMethods.printLog(
        "Reload coins",
        'Response: ' + result.toString(),
      );
      return result;
    } catch (e) {
      result['error'] = 'Exception  : ' + e.toString();
      CommonMethods.printLog(
        "Reload coins",
        'Response: ' + result.toString(),
      );
      return result;
    }
  }
}
