import 'package:intl/intl.dart';

class DatetimeUtil {
  static const String defaultDFPattern = 'yyyy-MM-dd HH:mm:ss';

  static DateFormat get dateFormat => DateFormat(defaultDFPattern);

  static String get currentDateTime => dateFormat.format(DateTime.now());

  static DateTime getDate(String sDateTime) => dateFormat.parse(sDateTime);

  static int diffFromCurrentInMins(String savedDT) =>
      getDate(currentDateTime).difference(getDate(savedDT)).inMinutes;
}
