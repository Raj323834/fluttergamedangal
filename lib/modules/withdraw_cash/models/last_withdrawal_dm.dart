class LastWithdrawalDM {
  String result;
  String vpa;
  bool emailVerified;
  bool mobileVerified;
  bool idVerified;
  bool addressVerified;
  double maxWithdrawalAmount;
  double minWithdrawalAmount;
  int dailyMaxUserWithdrawalCount;
  double dailyMaxUserWithdrawalAmount;
  int dailyMaxInstantCount;
  double dailyMaxInstantAmountPerWithdrawal;
  double dailyTotalMaxInstantAmount;
  bool kycVerified;
  String lastPaymentMode;
  String accountNumber;
  String ifscCode;
  String accountHolderName;

  LastWithdrawalDM(
      {this.result,
      this.vpa,
      this.emailVerified,
      this.mobileVerified,
      this.idVerified,
      this.addressVerified,
      this.maxWithdrawalAmount,
      this.minWithdrawalAmount,
      this.dailyMaxUserWithdrawalCount,
      this.dailyMaxUserWithdrawalAmount,
      this.dailyMaxInstantCount,
      this.dailyMaxInstantAmountPerWithdrawal,
      this.dailyTotalMaxInstantAmount,
      this.kycVerified,
      this.lastPaymentMode,
      this.accountNumber,
      this.ifscCode,
      this.accountHolderName});

  LastWithdrawalDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    vpa = json['vpa'];
    emailVerified = json['emailVerified'];
    mobileVerified = json['mobileVerified'];
    idVerified = json['idVerified'];
    addressVerified = json['addressVerified'];
    maxWithdrawalAmount = json['maxWithdrawalAmount'];
    minWithdrawalAmount = json['minWithdrawalAmount'];
    dailyMaxUserWithdrawalCount = json['dailyMaxUserWithdrawalCount'];
    dailyMaxUserWithdrawalAmount = json['dailyMaxUserWithdrawalAmount'];
    dailyMaxInstantCount = json['dailyMaxInstantCount'];
    dailyMaxInstantAmountPerWithdrawal =
        json['dailyMaxInstantAmountPerWithdrawal'];
    dailyTotalMaxInstantAmount = json['dailyTotalMaxInstantAmount'];
    kycVerified = json['kycVerified'];
    lastPaymentMode = json['last_payment_mode'];
    accountNumber = json['account_number'];
    ifscCode = json['ifsc_code'];
    accountHolderName = json['account_holder_name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    data['vpa'] = this.vpa;
    data['emailVerified'] = this.emailVerified;
    data['mobileVerified'] = this.mobileVerified;
    data['idVerified'] = this.idVerified;
    data['addressVerified'] = this.addressVerified;
    data['maxWithdrawalAmount'] = this.maxWithdrawalAmount;
    data['minWithdrawalAmount'] = this.minWithdrawalAmount;
    data['dailyMaxUserWithdrawalCount'] = this.dailyMaxUserWithdrawalCount;
    data['dailyMaxUserWithdrawalAmount'] = this.dailyMaxUserWithdrawalAmount;
    data['dailyMaxInstantCount'] = this.dailyMaxInstantCount;
    data['dailyMaxInstantAmountPerWithdrawal'] =
        this.dailyMaxInstantAmountPerWithdrawal;
    data['dailyTotalMaxInstantAmount'] = this.dailyTotalMaxInstantAmount;
    data['kycVerified'] = this.kycVerified;
    data['last_payment_mode'] = this.lastPaymentMode;
    data['account_number'] = this.accountNumber;
    data['ifsc_code'] = this.ifscCode;
    data['account_holder_name'] = this.accountHolderName;
    return data;
  }
}
