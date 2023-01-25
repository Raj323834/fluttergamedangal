import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../constants/shared_pref_keys.dart';
import '../utils/shared_pref_service.dart';

WalletInfoModel wallet = new WalletInfoModel();

class WalletInfoModel with ChangeNotifier {
  dynamic _cashValue = 0;
  dynamic _withdrawalValue = 0;
  dynamic _chipsValue = 0;
  dynamic _bonusValue = 0;
  dynamic _gameChipsValue = 0;
  dynamic _pokerChipsValue = 0;
  dynamic _combinedValue = 0.0;

  dynamic get cashValue => _cashValue;

  dynamic get withdrawalValue => _withdrawalValue;

  dynamic get chipsValue => _chipsValue;

  dynamic get bonusValue => _bonusValue;

  dynamic get gameChipsValue => _gameChipsValue;

  dynamic get pokerChipsValue => _pokerChipsValue;

  dynamic get combinedValue => _combinedValue;

  WalletInfoModel() {
    initialValues();
  }

  void initialValues() async {
    var cashAmount = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.cashAmount,
        ) ??
        "0.0";
    var chipsAmount = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.chipsAmount,
        ) ??
        "0.0";
    var withdrawAmount = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.withdrawAmount,
        ) ??
        "0.0";
    var bonusAmount = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.bonusAmount,
        ) ??
        "0.0";
    var gameChipsAmount = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.gameChipsAmount,
        ) ??
        "0.0";
    var pokerChipsAmount =
        await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.pokerChipsAmount,
    );
    pokerChipsAmount = pokerChipsAmount == "null" ? "0.0" : pokerChipsAmount;
    var combinedAmount = double.parse(
          cashAmount,
        ) +
        double.parse(
          withdrawAmount,
        ) +
        double.parse(
          gameChipsAmount,
        );

    _cashValue = double.parse(
      cashAmount,
    ).toStringAsFixed(2);
    _withdrawalValue = double.parse(
      withdrawAmount,
    ).toStringAsFixed(2);
    _chipsValue = double.parse(
      chipsAmount,
    ).toInt().toString();
    _bonusValue = double.parse(
      bonusAmount,
    ).toStringAsFixed(2);
    _gameChipsValue = double.parse(
      gameChipsAmount,
    ).toStringAsFixed(2);
    _pokerChipsValue = double.tryParse(
      pokerChipsAmount ?? 0.0.toString(),
    ).toStringAsFixed(2);
    _combinedValue =
        combinedAmount; //combinedAmount.toInt().toString(); (CHANGED FROM)

    notifyListeners();
  }

  set cashValue(dynamic newValue) {
    _cashValue = newValue;
    notifyListeners();
  }

  set withdrawalValue(dynamic newValue) {
    _withdrawalValue = newValue;
    notifyListeners();
  }

  set chipsValue(dynamic newValue) {
    _chipsValue = newValue;
    notifyListeners();
  }

  set bonusValue(dynamic newValue) {
    _bonusValue = newValue;
    notifyListeners();
  }

  set gameChipsValue(dynamic newValue) {
    _gameChipsValue = newValue;
    notifyListeners();
  }

  set pokerChipsValue(dynamic newValue) {
    _pokerChipsValue = newValue;
    notifyListeners();
  }

  set combinedValue(dynamic newValue) {
    _combinedValue = newValue;
    notifyListeners();
  }

  changeGamechips(
    double newValue,
  ) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.gameChipsAmount,
      newValue.toString(),
    );
    _gameChipsValue = newValue.toStringAsFixed(2);
    notifyListeners();
  }

  changePokerchips(double newValue) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.pokerChipsAmount,
      newValue.toString(),
    );
    _pokerChipsValue = newValue.toStringAsFixed(2);
    notifyListeners();
  }

  changeCash(double newValue) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.cashAmount,
      newValue.toString(),
    );
    _cashValue = newValue.toStringAsFixed(2);
    notifyListeners();
  }

  changeWithdraw(double newValue) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.withdrawAmount,
      newValue.toString(),
    );
    _withdrawalValue = newValue.toStringAsFixed(2);
    notifyListeners();
  }

  changeChips(int newValue) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.chipsAmount,
      newValue.toString(),
    );
    _chipsValue = newValue;
    notifyListeners();
  }

  changeBonus(double newValue) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.bonusAmount,
      newValue.toString(),
    );
    _bonusValue = newValue.toStringAsFixed(2);
    notifyListeners();
  }

  changeCombined(
    double newCashValue,
    double newWithdrawalValue,
    double newGameChipsValue,
  ) async {
    await Future.delayed(
      Duration(
        seconds: 2,
      ),
    );
    double temp = newCashValue + newWithdrawalValue + newGameChipsValue;
    _combinedValue = temp;
    notifyListeners();
  }
}
