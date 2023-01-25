
import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:http/http.dart' as http;

import '../../constants/string_constants.dart';
import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../utils/shared_pref_service.dart';
import 'constants/app_exceptions.dart';
import 'constants/parameter_constants.dart';

class ApiBaseHelper {
  Future get({
    final queryParameters,
    @required String url,
    @required String userId,
  }) async {
    var connectivityResult = await Connectivity().checkConnectivity();
    //Fetch AccessToken
    final accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.devLog('NO INTERNET');
    }

    try {
      //Create URI
      final uri = Uri.https(
        FlavorConfig.instance.variables["base_url"].split("https://")[1],
        url,
        queryParameters,
      );
      CommonMethods.devLog("URL: ${uri.toString()}");
      //Headers
      final headers = {
        ParameterConstants.contentTypeHeader: "application/json",
        ParameterConstants.authorizationHeader: "Bearer $accessToken",
        ParameterConstants.identifier:
            userId != null ? userId : StringConstants.emptyString,
        ParameterConstants.source: FlavorInfo.source.toString()
      };
      CommonMethods.printLog(
        "Headers :  ",
        headers.toString(),
      );
      //API CALL
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
      dynamic responseBodyJson = _returnResponse(response);
      return responseBodyJson;
    } catch (e) {
      //TO BE CHANGED ACCORDING TO ARCHITECTURE
    }
  }

  dynamic _returnResponse(http.Response response) {
    // ErrorDm errorDm;
    // String errorMessage;
    CommonMethods.devLog('STATUS CODE: ${response.statusCode}');
    CommonMethods.devLog('RESPONSE: ${response.body}');
    switch (response.statusCode) {
      case 200:
        return response;
      case 400:
        return response;
      // errorDm = ErrorDm.fromJson(
      //   jsonDecode(
      //     response.body,
      //   ),
      // );
      // errorMessage = (errorDm.message != null)
      //     ? errorDm.message
      //     : StringConstants.somethingWentWrong;
      // throw BadRequestException(errorMessage);
      case 401:
        return response;
      // errorDm = ErrorDm.fromJson(
      //   jsonDecode(
      //     response.body,
      //   ),
      // );
      // errorMessage = (errorDm.message != null)
      //     ? errorDm.message
      //     : StringConstants.somethingWentWrong;
      // throw UnauthorisedException(errorMessage);
      case 403:
        return response;
      // errorDm = ErrorDm.fromJson(
      //   jsonDecode(
      //     response.body,
      //   ),
      // );
      // errorMessage = (errorDm.message != null)
      //     ? errorDm.message
      //     : StringConstants.somethingWentWrong;
      // throw UnauthorisedException(errorMessage);
      case 404:
        return response;
      // errorDm = ErrorDm.fromJson(
      //   jsonDecode(
      //     response.body,
      //   ),
      // );
      // errorMessage = (errorDm.message != null)
      //     ? errorDm.message
      //     : StringConstants.somethingWentWrong;
      // throw UnauthorisedException(errorMessage);
      case 500:

      default:
        throw FetchDataException(
            'Error occured while Communication with Server with StatusCode : ${response.statusCode}');
    }
  }
}
