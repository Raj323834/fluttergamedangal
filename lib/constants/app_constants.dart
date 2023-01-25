import 'package:sizer/sizer.dart';

class AppConstants {
  //Sizes
  static const double padding = 30;
  static const double avatarRadius = 45;
  static const double headerWidth = 350;
  static final double walletRowHeight = 5.5.h;
  static final double upsRowHeight = 3.8.h;
  static final double headerHeight = 5.0.h;
  static final double distAppbarHeader = 5.0.h;
  static final double distHeaderWidget = 2.0.h;
  static final double circularBorder = 2.0.w;

  //Maintenance
  static const String maintenanceStartTime = "maintenanceStartTime";
  static const String maintenanceEndTime = "maintenanceEndTime";
  static const String showWarning = "showWarning";
  static const String isMaintenanceUpdated = "isMaintenanceUpdated";
  static const String maintenance = "Scheduled maintenance starting at ";
  static const String underMaintenance =
      "App is under maintenance.\nPlease check back at ";
  static const String space = " ";
  static const String colon = ":";
  static const String zero = "0";
  static const String imagePath = "assets/images/background.webp";
  static const String warningShowed = "WARNING_SHOWED";
  static const String websocketMaintenanceReceived =
      "WEBSOCKET_MAINTENANCE_RECEIVED";
  static const String onMaintenanceScreen = "ON_MAINTENANCE_SCREEN";
  static const int maxSingleDigitNumber = 9;

//HomePage Screen Numbers for Prod
  static const int home = 0;
  static const int allGames = 1;
  static const int addCash = 2;
  static const int referAndEarn = 3;
  static const int leaderBoard = 4;

  //HomePage Screen Numbers for Prod PlayStore
  static const int homePS = 0;
  static const int addCashPS = 1;
  static const int referAndEarnPS = 2;
  static const int leaderBoardPS = 3;
  static const int supportPS = 4;

  //kyc screen
  static const int addressDocumentAddress = 1;
  static const int panDocumentIndex = 0;
  static const int configId = 0;
  static const int maxLengthAadhaar = 12;
  static const int maxLengthVoterId = 10;
  static const int maxLengthPassport = 20;
  static const int maxLengthDrivingLicense = 20;
  static const int minLengthDrivingLicense = 9;

  //flavors
  static const int envTypeDev = 1;
  static const int envTypeDevPlayStore = 2;
  static const int envTypeProd = 3;
  static const int envTypeProdCoIn = 4;
  static const int envTypeProdPlayStore = 5;
}
