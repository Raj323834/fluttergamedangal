class LoginStreakModel {
  String _result;
  int _userDailyLoginStreak;
  int _applicableDailyLoginStreak;
  double _totalIncentiveAmount;
  double _maxIncentiveAmount;

  LoginStreakModel(
      {String result,
        int userDailyLoginStreak,
        int applicableDailyLoginStreak,
        double totalIncentiveAmount,
        double maxIncentiveAmount}) {
    this._result = result;
    this._userDailyLoginStreak = userDailyLoginStreak;
    this._applicableDailyLoginStreak = applicableDailyLoginStreak;
    this._totalIncentiveAmount = totalIncentiveAmount;
    this._maxIncentiveAmount = maxIncentiveAmount;
  }

  String get result => _result;
  int get userDailyLoginStreak => _userDailyLoginStreak;
  int get applicableDailyLoginStreak => _applicableDailyLoginStreak;
  double get totalIncentiveAmount => _totalIncentiveAmount;
  double get maxIncentiveAmount => _maxIncentiveAmount;

  LoginStreakModel.fromJson(Map<String, dynamic> json) {
    _result = json['result'];
    _userDailyLoginStreak = json['userDailyLoginStreak'];
    _applicableDailyLoginStreak = json['applicableDailyLoginStreak'];
    _totalIncentiveAmount = json['totalIncentiveAmount'];
    _maxIncentiveAmount = json['maxIncentiveAmount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this._result;
    data['userDailyLoginStreak'] = this._userDailyLoginStreak;
    data['applicableDailyLoginStreak'] = this._applicableDailyLoginStreak;
    data['totalIncentiveAmount'] = this._totalIncentiveAmount;
    data['maxIncentiveAmount'] = this._maxIncentiveAmount;
    return data;
  }
}