import '../constants/methods/common_methods.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';
import 'network_get_request.dart';
import 'network_post_request.dart';

class UserProfileService {
  static const String TAG = "UserProfileService ";

  static Future<Map<String, Object>> verifyMobile(
    String mobileNumber,
    String otp,
    String requestId,
    String oldMobileNumber,
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "---------verifyMobile-------",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      // "user_id": user_id,
      "mobile_number": mobileNumber,
      "otp": otp,
      "request_id": requestId,
      "old_mobile_number": oldMobileNumber
    };

    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyMobileupsUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> updateUsername(
    String userId,
    String username,
  ) async {
    CommonMethods.printLog(
      TAG,
      "------updateUsername--------",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      // "user_id": user_id,
      "username": username,
    };
    result = await NetworkPostRequest.putRequestWithAccess(
      requestData,
      UrlConstants.updateUsernameUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> updateUserProfileInfo(
    String firstName,
    String middleName,
    String lastName,
    int dateOfBirth,
    String mobile,
    String gender,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    CommonMethods.printLog(
      TAG,
      "------updateUserProfileInfo--------",
    );
    Map requestData = {
      "first_name": firstName,
      "last_name": lastName,
      "middle_name": middleName,
      "date_of_birth": dateOfBirth,
      "mobile": mobile,
      "gender": gender,
    };
    result = await NetworkPostRequest.putRequestWithAccess(
      requestData,
      UrlConstants.updateUserProfileInfoUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> getUserProfileInfo(
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "------getUserProfileInfo--------",
    );
    Map<String, Object> result = Map();

    result = await NetworkGetRequest.getRequestWithoutQueryParameter(
      "USER INFO",
      UrlConstants.getUserProfileUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> verifyEmail(
    String email,
    String otp,
    String requestId,
    String emailUpdate,
    String oldEmail,
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "------verifyEmail--------",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      "email": email,
      "otp": otp,
      "request_id": requestId,
      "email_update": emailUpdate,
      "old_email": oldEmail
    };

    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyEmailupsUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> verifyKyc(
      String amount,
      String accNumber,
      String ifsc,
      String holder,
      String mode,
      String vpa,
      String userId) async {
    CommonMethods.printLog(
      TAG,
      "------verifyKyc--------",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      "amount": amount,
      "account_number": accNumber,
      "ifsc_code": ifsc,
      "account_holder_name": holder,
      "payment_mode": mode,
      "vpa": vpa
    };
    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyEmailupsUrl,
      userId,
    );

    return result;
  }
}

class UsernameServiceValidator {
  static String usernameValidator(
    String value,
  ) {
    Pattern pattern = r'^\d{9,18}$';
    RegExp regex = RegExp(
      pattern,
    );
    if (value.isEmpty) return StringConstants.emptyString;
    if (!regex.hasMatch(
      value,
    ))
      return StringConstants.emptyString;
    else
      return null;
  }

  static String nameValidator(
    String value,
  ) {
    Pattern pattern = r'^[A-Za-z]{4}0[A-Z0-9a-z]{6}$';
    RegExp regex = RegExp(pattern);
    if (value.isEmpty) return StringConstants.emptyString;
    if (!regex.hasMatch(
      value,
    ))
      return StringConstants.emptyString;
    else
      return null;
  }

  static String mobileValidator(
    String value,
  ) {
    Pattern pattern = r'^\d{9,18}$';
    RegExp regex = RegExp(
      pattern,
    );
    if (value.isEmpty) return StringConstants.emptyString;
    if ((value.length < 10) || (value.length > 10))
      return StringConstants.emptyString;
    if (!regex.hasMatch(
      value,
    ))
      return StringConstants.emptyString;
    else
      return null;
  }

  static String emailValidator(String value) {
    Pattern pattern = r'^\d{9,18}$';
    RegExp regex = RegExp(
      pattern,
    );
    if (value.isEmpty) return StringConstants.emptyString;
    if (!regex.hasMatch(
      value,
    ))
      return StringConstants.emptyString;
    else
      return null;
  }

  static String holderNameValidator(
    String value,
  ) {
    if (value.isEmpty)
      return StringConstants.emptyString;
    else
      return null;
  }

  static String allowEmptyValidator(
    String value,
  ) {
    return null;
  }
}
