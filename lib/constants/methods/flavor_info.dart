import 'package:flutter_flavor/flutter_flavor.dart';

import '../app_constants.dart';
import '../string_constants.dart';

class FlavorInfo{

  static bool get isDev =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeDev;

  static bool get isDevPS =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeDevPlayStore;

  static bool get isProdPS =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeProdPlayStore;

  static bool get isDevWithPS => isDev && isDevPS;

  static bool get isPS =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeDevPlayStore
          ||
          FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeProdPlayStore;

  static bool get isCom =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeProd;

  static bool get isCoIn =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey] == AppConstants.envTypeProdCoIn;

  static bool get isCoInWithPoker =>
      (isCoIn && isPoker);

  static bool get isCoInWithoutPoker =>
      (isCoIn && !isPoker);

  static bool get isPoker =>
      FlavorConfig.instance.variables[StringConstants.isPokerKey];

  static bool get showPokerChips =>
      isDev || isCoInWithPoker || isCom;

  static String get name =>
      isCom ? StringConstants.envProd :
      isCoIn ? StringConstants.envProdCoIn :
      isPS ? StringConstants.envProdPlayStore :
      isDevPS ? StringConstants.envDevPlayStore :
      StringConstants.envDev
  ;

  static int get source =>
      FlavorConfig.instance.variables[StringConstants.envTypeKey];

  static bool get isV8 =>
      FlavorConfig.instance.variables[StringConstants.envV8Key];

  static String get supportEmail => isPS
      ? StringConstants.supportEmailPS:
  StringConstants.supportEmail;
}