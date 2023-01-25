import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import 'network_get_request.dart';
import 'network_post_request.dart';

class WithdrawalService {
  static const String TAG = "WithdrawService  ";
  static Future<Map<String, Object>> withdrawTransaction(
    String amount,
    String accNumber,
    String ifsc,
    String holder,
    String mode,
    String vpa,
    bool isNew,
    String userId,
  ) async {
    String state = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.state,
    );

    Map requestData = {
      "amount": amount,
      "account_number": accNumber,
      "ifsc_code": ifsc,
      "account_holder_name": holder,
      "payment_mode": mode,
      "vpa": vpa,
      "isNew": isNew,
      "state": state
    };
    Map<String, Object> result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.withdrawTransactionUrl,
      userId,
    );

    CommonMethods.printLog(
      TAG,
      'withdrawTransaction:--> ' + result.toString(),
    );
    return result;
  }

  static Future<Map<String, Object>> validateBankAccount(
    String ifscCode,
    String name,
    String userId,
  ) async {
    Map requestData = {
      "ifsc_code": ifscCode,
      "account_holder_name": name,
    };
    Map<String, Object> result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.validateBankAccountUrl,
      userId,
    );

    CommonMethods.printLog(
      TAG,
      'ValidateBankAccount:--> ' + result.toString(),
    );
    return result;
  }

  static Future<Map<String, dynamic>> cancelWithdrawal(
    String userId,
    int withdrawalId,
  ) async {
    final queryParameters = {
      "withdrawalId": withdrawalId.toString(),
    };

    Map<String, dynamic> result =
        await NetworkPostRequest.putRequestWithAccessAndQueryParam(
      queryParameters,
      UrlConstants.cancelWithdrawalUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, dynamic>> getWithdrawalList(
    dynamic fromTime,
    int endTime,
    String status,
    String userId,
  ) async {
    final queryParameters = {
      "fromTime": fromTime.toString(),
      "toTime": endTime.toString(),
      "status": status,
    };

    Map<String, dynamic> result =
        await NetworkGetRequest.getRequestWithQueryParameter(
      "Withdrawal List ",
      queryParameters,
      UrlConstants.getWithdrawalListUrl,
      userId,
    );
    return result;
  }
}

class WithdrawalServiceValidator {
  static bool accountNumberValidator(
    String value,
  ) {
    Pattern pattern = r'^\d{9,18}$';
    RegExp regex = RegExp(
      pattern,
    );
    if (value.isEmpty) return true;
    if (!regex.hasMatch(
      value,
    ))
      return true;
    else
      return false;
  }

  static bool ifscCodeValidator(
    String value,
  ) {
    Pattern pattern = r'^[A-Za-z]{4}0[A-Z0-9a-z]{6}$';
    RegExp regex = RegExp(
      pattern,
    );
    if (value.isEmpty) return true;
    if (!regex.hasMatch(
      value,
    ))
      return true;
    else
      return false;
  }
}
