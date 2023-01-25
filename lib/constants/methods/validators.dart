import '../string_constants.dart';

class Validators {
  static String mobileNumberValidator(String value) {
    if (value.isEmpty) {
      return StringConstants.required;
    }
    if (value.contains(" ")) {
      return StringConstants.pleaseDoNotUseSpaceInMobileNumber;
    }
    if (value.length < 10 || value.length > 10) {
      return StringConstants.pleaseEnterA10DigitMobileNumber;
    } else {
      return null;
    }
  }

}
