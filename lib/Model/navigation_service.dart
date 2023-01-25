import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class NavigationService {
  GlobalKey<NavigatorState> navigationKey;

  static NavigationService instance = NavigationService();

  NavigationService() {
    navigationKey = GlobalKey<NavigatorState>();
  }

  Future<dynamic> navigateToReplacement(
    String _rn,
  ) {
    return navigationKey.currentState.pushReplacementNamed(
      _rn,
    );
  }

  Future<dynamic> navigateTo(
    String routeName, {
    Object args,
  }) {
    return navigationKey.currentState.pushNamed(
      routeName,
      arguments: args,
    );
  }

  Future<dynamic> navigateToRoute(
    CupertinoPageRoute _rn,
  ) {
    return navigationKey.currentState.push(
      _rn,
    );
  }

  goback() {
    return navigationKey.currentState.pop();
  }
}
