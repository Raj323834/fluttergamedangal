class ReferralCodeDM {
  String result;
  String referralCode;
  String apkLink;
  String userId;

  ReferralCodeDM({
    this.result,
    this.referralCode,
    this.apkLink,
    this.userId,
  });

  ReferralCodeDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    referralCode = json['referralCode'];
    apkLink = json['apkLink'];
    userId = json['user_id'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    data['referralCode'] = this.referralCode;
    data['apkLink'] = this.apkLink;
    data['user_id'] = this.userId;
    return data;
  }
}
