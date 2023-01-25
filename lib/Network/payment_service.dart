
import 'package:connectivity/connectivity.dart';

import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import 'network_post_request.dart';

class PaymentService {
  static const String TAG = "PaymentService  ";

  static Future<Map<String, Object>> initiateTransactioncashfree(
    String cash,
    String bonusId,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    try {
      var connectivityResult = await Connectivity().checkConnectivity();

      if (connectivityResult == ConnectivityResult.none) {
        result['noInternet'] = StringConstants.noInternetConnection;
        return result;
      } else {
        String state = await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.state,
            ) ??
            StringConstants.emptyString;
        Map requestData = {
          "amount": cash,
          "bonus_id": bonusId,
          "state": state,
        };

        result = await NetworkPostRequest.postRequestWithAccess(
          requestData,
          UrlConstants.initiateCashfreeTransactionUrl,
          userId,
        );

        CommonMethods.printLog(
          TAG,
          "Initiate Transaction -> " + result.toString(),
        );
        return result;
      }
    } catch (e) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    }
  }

  static Future<Map<String, Object>> initiateTransactionRazorpay(
    String cash,
    String bonusId,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    try {
      var connectivityResult = await Connectivity().checkConnectivity();

      if (connectivityResult == ConnectivityResult.none) {
        result['noInternet'] = StringConstants.noInternetConnection;
        return result;
      } else {
        String state = await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.state,
            ) ??
            StringConstants.emptyString;
        Map requestData = {
          "amount": cash,
          "bonus_id": bonusId,
          "profileState": state
        };

        result = await NetworkPostRequest.postRequestWithAccess(
          requestData,
          UrlConstants.initiateRazorpayOrderUrl,
          userId,
        );

        CommonMethods.printLog(
          TAG,
          "Initiate Transaction Razorpay: " + result.toString(),
        );
        return result;
      }
    } catch (e) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    }
  }

  static Future<Map<String, Object>> initiateTransactionPaytm(
    String cash,
    String bonusId,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    try {
      var connectivityResult = await Connectivity().checkConnectivity();
      if (connectivityResult == ConnectivityResult.none) {
        result['noInternet'] = StringConstants.noInternetConnection;
        return result;
      } else {
        String state = await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.state,
            ) ??
            StringConstants.emptyString;
        Map requestData = {
          "amount": cash,
          "bonus_id": bonusId,
          "state": state,
        };

        result = await NetworkPostRequest.postRequestWithAccess(
          requestData,
          UrlConstants.initiateTransactionPaytmUrl,
          userId,
        );

        CommonMethods.printLog(
          TAG,
          "Initiate Transaction -> " + result.toString(),
        );
        return result;
      }
    } catch (e) {
      result['noInternet'] = StringConstants.noInternetConnection;
      return result;
    }
  }

  static Future<Map<String, Object>> verifyTransactioncashfree(
    String orderAmount,
    String orderId,
    String referenceId,
    String txStatus,
    String txTime,
    String paymentMode,
    String txMsg,
    String signature,
    String bonusId,
    String userId,
  ) async {
    Map requestData = {
      "order_id": orderId,
      "order_amount": orderAmount,
      "reference_id": referenceId,
      "tx_status": txStatus,
      "tx_time": txTime,
      "payment_mode": paymentMode,
      "tx_msg": txMsg,
      "signature": signature,
      "type": "CashFreeResponse",
      "bonus_id": bonusId
    };

    Map<String, Object> result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyCashfreeTransactionUrl,
      userId,
    );

    CommonMethods.printLog(
      TAG,
      "VerifyTransaction ->" + result.toString(),
    );
    return result;
  }

  static Future<Map<String, Object>> verifyTransactionpaytm(
    String cash,
    String orderId,
    String bonusId,
    String status,
    String userId,
  ) async {
    Map requestData = {
      "ORDERID": orderId,
      "TXNAMOUNT": cash,
      "bonus_id": bonusId,
      "status": status
    };

    Map<String, Object> result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyTransactionPaytmUrl,
      userId,
    );

    CommonMethods.printLog(
      TAG,
      "VerifyTransaction ->" + result.toString(),
    );
    return result;
  }

  static Future<Map<String, Object>> verifyTransactionRazorpay(
    String transactionId,
    String orderId,
    String paymentId,
    String signature,
    String status,
    String message,
    String bonusId,
    String userId,
  ) async {
    Map requestData = {
      "transactionId": transactionId,
      "orderId": orderId,
      "paymentId": paymentId,
      "signature": signature,
      "status": status,
      "message": message,
      "bonusId": bonusId
    };

    CommonMethods.devLog(
      "Verify Razorpay requestData: $requestData",
    );

    Map<String, Object> result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyRazorpayTransactionUrl,
      userId,
    );

    CommonMethods.printLog(
      TAG,
      "Verify Transaction Razorpay: " + result.toString(),
    );
    return result;
  }
}
