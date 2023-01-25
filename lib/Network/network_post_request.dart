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

class NetworkPostRequest {
  static const String TAG = "NetworkPostRequest  : ";

  static Future<Map<String, Object>> postRequestWithAccess(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    CommonMethods.printLog(
      TAG + " Request data",
      requestData.toString(),
    );

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var response;

        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : StringConstants.emptyString,
          StringConstants.source: FlavorInfo.source.toString()
        };
        response = await http
            .post(
              Uri.parse(
                url,
              ),
              headers: headers,
              body: body,
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
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postRequestWithAccessNoTimeout(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    CommonMethods.printLog(
      TAG + " Request data",
      requestData.toString(),
    );

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var response;

        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : StringConstants.emptyString,
          StringConstants.source: FlavorInfo.source.toString()
        };
        response = await http.post(
          Uri.parse(
            url,
          ),
          headers: headers,
          body: body,
        );

        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postRequestHttpClientWithAccessNoTimeout(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    CommonMethods.printLog(
      TAG + " Request data",
      requestData.toString(),
    );

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var stringData;

        var client = HttpClient();
        try {
          HttpClientRequest request = await client.postUrl(
            Uri.parse(
              url,
            ),
          );
          request.headers.set(
            HttpHeaders.contentTypeHeader,
            "application/json",
          );
          request.headers.set(
            HttpHeaders.authorizationHeader,
            "Bearer $accessToken",
          );
          request.headers.set(
            "identifier",
            userId,
          );
          request.headers.set(
            StringConstants.source,
            FlavorInfo.source.toString(),
          );
          request.write(
            body,
          );

          HttpClientResponse response = await request.close().timeout(
                Duration(
                  seconds: 30,
                ),
              );

          stringData = await response
              .transform(
                utf8.decoder,
              )
              .join();
        } finally {
          client.close();
        }

        Map<String, dynamic> responseMap = convert.jsonDecode(
          stringData,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postRequestWithAccessGameName(
    Map requestData,
    String url,
    String gameName,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    CommonMethods.printLog(
      TAG + " Request data",
      requestData.toString(),
    );

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var response;

        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : StringConstants.emptyString,
          "game": gameName,
          StringConstants.source: FlavorInfo.source.toString()
        };
        response = await http
            .post(
              Uri.parse(url),
              headers: headers,
              body: body,
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
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postRequestWithoutAccess(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();

    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        Map<String, String> headers = {
          'Content-Type': 'application/json',
          "identifier": userId != null ? userId : StringConstants.emptyString,
          StringConstants.source: FlavorInfo.source.toString()
        };
        var response;
        response = await http
            .post(
              Uri.parse(url),
              headers: headers,
              body: body,
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
          TAG,
          "Header----->  " + headers.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception handled : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postNewHttpRequestWithoutAccess(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();

    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var stringData = StringConstants.emptyString;

        var client = HttpClient();
        try {
          HttpClientRequest request = await client.postUrl(
            Uri.parse(
              url,
            ),
          );
          request.headers.set(
            HttpHeaders.contentTypeHeader,
            "application/json",
          );
          request.headers.set(
            "identifier",
            userId,
          );
          request.headers.set(
            StringConstants.source,
            FlavorInfo.source.toString(),
          );
          request.write(body);
          HttpClientResponse response = await request.close();

          stringData = await response
              .transform(
                utf8.decoder,
              )
              .join();
        } finally {
          client.close();
        }

        Map<String, dynamic> responseMap = convert.jsonDecode(
          stringData,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception handled : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> putRequestWithAccess(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();
    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );

    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var response;
        response = await http
            .put(
                Uri.parse(
                  url,
                ),
                headers: {
                  HttpHeaders.contentTypeHeader: "application/json",
                  HttpHeaders.authorizationHeader: "Bearer $accessToken",
                  "identifier":
                      userId != null ? userId : StringConstants.emptyString,
                  StringConstants.source: FlavorInfo.source.toString()
                },
                body: body)
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
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> putRequestWithAccessAndQueryParam(
    Map queryParameters,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();
    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );

    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        final uri = Uri.https(
          FlavorConfig.instance.variables["base_url"].split("https://")[1],
          url,
          queryParameters,
        );
        var response = await http.put(
          uri,
          headers: {
            HttpHeaders.contentTypeHeader: "application/json",
            HttpHeaders.authorizationHeader: "Bearer $accessToken",
            "identifier": userId != null ? userId : StringConstants.emptyString,
            StringConstants.source: FlavorInfo.source.toString()
          },
        ).timeout(
          Duration(
            seconds: 20,
          ),
        );
        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> putRequestWithoutAccess(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    try {
      CommonMethods.printLog(
        TAG,
        "URL---> " + url.toString(),
      );
      var body = convert.jsonEncode(
        requestData,
      );
      CommonMethods.printLog(
        TAG,
        "Request Body---> " + body.toString(),
      );
      var response;
      response = await http
          .put(
            Uri.parse(url),
            headers: {
              HttpHeaders.contentTypeHeader: "application/json",
              "identifier":
                  userId != null ? userId : StringConstants.emptyString,
              StringConstants.source: FlavorInfo.source.toString()
            },
            body: body,
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
        TAG,
        "Header----->  " + userId.toString(),
      );
      CommonMethods.printLog(
        TAG,
        'Response: ' + result.toString(),
      );
      return result;
    } catch (e) {
      result['error'] = 'Exception  : ' + e.toString();
      CommonMethods.printLog(
        TAG,
        'Response: ' + result.toString(),
      );
      return result;
    }
  }

  static Future<Map<String, Object>> postRequestWithAccessKyc(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );
        var response;

        final headers = {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : StringConstants.emptyString,
          StringConstants.source: FlavorInfo.source.toString()
        };
        response = await http
            .post(
              Uri.parse(
                url,
              ),
              headers: headers,
              body: body,
            )
            .timeout(
              Duration(
                seconds: 50,
              ),
            );

        Map<String, dynamic> responseMap = convert.jsonDecode(
          response.body,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }

  static Future<Map<String, Object>> postRequestHttpClientWithAccessKyc(
    Map requestData,
    String url,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    var stringData = StringConstants.emptyString;

    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.none) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    } else {
      try {
        CommonMethods.printLog(
          TAG,
          "URL---> " + url.toString(),
        );
        var body = convert.jsonEncode(
          requestData,
        );
        CommonMethods.printLog(
          TAG,
          "Request Body---> " + body.toString(),
        );

        var client = HttpClient();
        try {
          HttpClientRequest request = await client.postUrl(
            Uri.parse(
              url,
            ),
          );
          request.headers.set(
            HttpHeaders.contentTypeHeader,
            "application/json",
          );
          request.headers.set(
            HttpHeaders.authorizationHeader,
            "Bearer $accessToken",
          );
          request.headers.set(
            "identifier",
            userId,
          );
          request.headers.set(
            StringConstants.source,
            FlavorInfo.source.toString(),
          );
          request.write(
            body,
          );

          HttpClientResponse response = await request.close().timeout(
                Duration(
                  seconds: 90,
                ),
              );

          stringData = await response
              .transform(
                utf8.decoder,
              )
              .join();
        } finally {
          client.close();
        }

        Map<String, dynamic> responseMap = convert.jsonDecode(
          stringData,
        );

        result['data'] = responseMap;
        CommonMethods.printLog(
          TAG,
          "Header----->  " + userId.toString(),
        );
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      } catch (e) {
        result['error'] = 'Exception  : ' + e.toString();
        CommonMethods.printLog(
          TAG,
          'Response: ' + result.toString(),
        );
        return result;
      }
    }
  }
}
