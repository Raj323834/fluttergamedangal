import 'dart:convert';
import 'dart:convert' as convert;
import 'dart:io';

import 'package:connectivity/connectivity.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:http/http.dart' as http;

import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../utils/shared_pref_service.dart';

class NetworkGetRequest {
  static const String TAG = "NetworkGetRequest  : ";

  static Future<Map<String, Object>> getRequestWithQueryParameter(
    String tag,
    final queryParameters,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();

    final accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        final uri = Uri.https(
          FlavorConfig.instance.variables["base_url"].split("https://")[1],
          url,
          queryParameters,
        );
        CommonMethods.printLog(
          tag,
          "URL : " + uri.toString(),
        );
        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : "",
          StringConstants.source: FlavorInfo.source.toString()
        };
        final response = await http
            .get(
              uri,
              headers: headers,
            )
            .timeout(
              Duration(
                seconds: 20,
              ),
            );
        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );
        result['data'] = responseMap;
        CommonMethods.printLog(
          tag,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          tag,
          "response " + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          tag,
          "response ->" + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> getRequestWithoutQueryParameter(
    String tag,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();

    final accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );

    final headers = {
      HttpHeaders.contentTypeHeader: "application/json",
      HttpHeaders.authorizationHeader: "Bearer $accessToken",
      "identifier": userId != null ? userId : "",
      StringConstants.source: FlavorInfo.source.toString()
    };
    CommonMethods.printLog(
      tag,
      "URL----->  " + url,
    );
    CommonMethods.printLog(
      tag,
      "Header----->  " + headers.toString(),
    );

    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        final response = await http
            .get(
              Uri.parse(url),
              headers: headers,
            )
            .timeout(
              Duration(
                seconds: 20,
              ),
            );
        Map<String, dynamic> responseMap = json.decode(
          response.body,
        );
        result['data'] = responseMap;
        CommonMethods.printLog(
          tag,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          tag,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          tag,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> georesGetRequestWithoutAccessToken(
    String tag,
    final queryParameters,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();

    CommonMethods.printLog(
      "",
      queryParameters.toString(),
    );
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog("", DateTime.now().toString());
        final uri = Uri.https(
          FlavorConfig.instance.variables["base_url"].split("https://")[1],
          url,
          queryParameters,
        );
        CommonMethods.printLog(
          tag,
          "URL : " + uri.toString(),
        );
        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          "identifier": userId != null ? userId : "",
          StringConstants.source: FlavorInfo.source.toString()
        };
        final response = await http
            .get(
              uri,
              headers: headers,
            )
            .timeout(
              Duration(
                seconds: 3,
              ),
            );
        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );
        result['data'] = responseMap;
        CommonMethods.printLog(
          tag,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          tag,
          "response " + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] =
            'Exception  : ' + DateTime.now().toString() + e.toString();

        CommonMethods.printLog(
          tag,
          "response ->" + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> georesGetRequestWithQueryParameter(
    String tag,
    final queryParameters,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = new Map();
    CommonMethods.printLog(
      "",
      queryParameters.toString(),
    );
    var connectivityResult = await Connectivity().checkConnectivity();

    final accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        final uri = Uri.https(
            FlavorConfig.instance.variables["base_url"].split("https://")[1],
            url,
            queryParameters);
        CommonMethods.printLog(
          tag,
          "URL : " + uri.toString(),
        );
        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : "",
          StringConstants.source: FlavorInfo.source.toString()
        };
        final response = await http
            .get(
              uri,
              headers: headers,
            )
            .timeout(
              Duration(
                seconds: 3,
              ),
            );
        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );
        result['data'] = responseMap;
        CommonMethods.printLog(
          tag,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          tag,
          "response " + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          tag,
          "response ->" + result.toString(),
        );
        return result;
      }
    }
  }
}
