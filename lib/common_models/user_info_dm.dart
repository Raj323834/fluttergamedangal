class UserInfoDM {
  String result;
  List<WalletInfo> walletInfo;
  String userName;
  String avatarId;
  String firstName;
  String middleName;
  String lastName;
  String dob;
  String address;
  String address2;
  String city;
  String pin;
  String state;
  String gender;
  int usernameUpdateCount;
  String email;
  bool emailVerified;
  String mobile;
  bool mobileVerified;
  bool emailOptIn;
  bool smsOptIn;
  String avatarUrl;
  bool idVerified;
  bool addressVerified;
  bool kycVerified;
  String apkVersion;
  String apkUrl64Bit;
  String apkUrl32Bit;
  String apkName;
  String updateDetails;
  int callbreakBonusPercentage;
  int poolBonusPercentage;
  int quizBonusPercentage;
  int fantasyBonusPercentage;
  int worklooperBonusPercentage;
  String lrAvatarUrl;
  String preferredLanguage;
  String pokerAvatarUrl;
  String whitelistedApkWithPoker;
  List<GameCategories> gameCategories;
  int numberOfBanners;
  String playStoreApkVersion;
  String playStoreApkUrl;
  String playStoreApkName;
  String playStoreUpdateDetails;
  int playStoreNumberOfBanners;
  double playChips;

  UserInfoDM(
      {this.result,
      this.walletInfo,
      this.userName,
      this.avatarId,
      this.firstName,
      this.middleName,
      this.lastName,
      this.dob,
      this.address,
      this.address2,
      this.city,
      this.pin,
      this.state,
      this.gender,
      this.usernameUpdateCount,
      this.email,
      this.emailVerified,
      this.mobile,
      this.mobileVerified,
      this.emailOptIn,
      this.smsOptIn,
      this.avatarUrl,
      this.idVerified,
      this.addressVerified,
      this.kycVerified,
      this.apkVersion,
      this.apkUrl64Bit,
      this.apkUrl32Bit,
      this.apkName,
      this.updateDetails,
      this.callbreakBonusPercentage,
      this.poolBonusPercentage,
      this.quizBonusPercentage,
      this.fantasyBonusPercentage,
      this.worklooperBonusPercentage,
      this.lrAvatarUrl,
      this.preferredLanguage,
      this.pokerAvatarUrl,
      this.whitelistedApkWithPoker,
      this.gameCategories,
      this.numberOfBanners,
      this.playStoreApkVersion,
      this.playStoreApkUrl,
      this.playStoreApkName,
      this.playStoreUpdateDetails,
      this.playStoreNumberOfBanners,
      this.playChips});

  UserInfoDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['walletInfo'] != null) {
      walletInfo = <WalletInfo>[];
      json['walletInfo'].forEach((v) {
        walletInfo.add(new WalletInfo.fromJson(v));
      });
    }
    userName = json['userName'];
    avatarId = json['avatarId'];
    firstName = json['firstName'];
    middleName = json['middleName'];
    lastName = json['lastName'];
    dob = json['dob'];
    address = json['address'];
    address2 = json['address2'];
    city = json['city'];
    pin = json['pin'];
    state = json['state'];
    gender = json['gender'];
    usernameUpdateCount = json['usernameUpdateCount'];
    email = json['email'];
    emailVerified = json['emailVerified'];
    mobile = json['mobile'];
    mobileVerified = json['mobileVerified'];
    emailOptIn = json['emailOptIn'];
    smsOptIn = json['smsOptIn'];
    avatarUrl = json['avatarUrl'];
    idVerified = json['idVerified'];
    addressVerified = json['addressVerified'];
    kycVerified = json['kycVerified'];
    apkVersion = json['apkVersion'];
    apkUrl64Bit = json['apkUrl64Bit'];
    apkUrl32Bit = json['apkUrl32Bit'];
    apkName = json['apkName'];
    updateDetails = json['updateDetails'];
    callbreakBonusPercentage = json['callbreakBonusPercentage'];
    poolBonusPercentage = json['poolBonusPercentage'];
    quizBonusPercentage = json['quizBonusPercentage'];
    fantasyBonusPercentage = json['fantasyBonusPercentage'];
    worklooperBonusPercentage = json['worklooperBonusPercentage'];
    lrAvatarUrl = json['lrAvatarUrl'];
    preferredLanguage = json['preferredLanguage'];
    pokerAvatarUrl = json['pokerAvatarUrl'];
    whitelistedApkWithPoker = json['whitelistedApkWithPoker'];
    if (json['gameCategories'] != null) {
      gameCategories = <GameCategories>[];
      json['gameCategories'].forEach((v) {
        gameCategories.add(new GameCategories.fromJson(v));
      });
    }
    numberOfBanners = json['numberOfBanners'];
    playStoreApkVersion = json['playStoreApkVersion'];
    playStoreApkUrl = json['playStoreApkUrl'];
    playStoreApkName = json['playStoreApkName'];
    playStoreUpdateDetails = json['playStoreUpdateDetails'];
    playStoreNumberOfBanners = json['playStoreNumberOfBanners'];
    playChips = json['Play Chips'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.walletInfo != null) {
      data['walletInfo'] = this.walletInfo.map((v) => v.toJson()).toList();
    }
    data['userName'] = this.userName;
    data['avatarId'] = this.avatarId;
    data['firstName'] = this.firstName;
    data['middleName'] = this.middleName;
    data['lastName'] = this.lastName;
    data['dob'] = this.dob;
    data['address'] = this.address;
    data['address2'] = this.address2;
    data['city'] = this.city;
    data['pin'] = this.pin;
    data['state'] = this.state;
    data['gender'] = this.gender;
    data['usernameUpdateCount'] = this.usernameUpdateCount;
    data['email'] = this.email;
    data['emailVerified'] = this.emailVerified;
    data['mobile'] = this.mobile;
    data['mobileVerified'] = this.mobileVerified;
    data['emailOptIn'] = this.emailOptIn;
    data['smsOptIn'] = this.smsOptIn;
    data['avatarUrl'] = this.avatarUrl;
    data['idVerified'] = this.idVerified;
    data['addressVerified'] = this.addressVerified;
    data['kycVerified'] = this.kycVerified;
    data['apkVersion'] = this.apkVersion;
    data['apkUrl64Bit'] = this.apkUrl64Bit;
    data['apkUrl32Bit'] = this.apkUrl32Bit;
    data['apkName'] = this.apkName;
    data['updateDetails'] = this.updateDetails;
    data['callbreakBonusPercentage'] = this.callbreakBonusPercentage;
    data['poolBonusPercentage'] = this.poolBonusPercentage;
    data['quizBonusPercentage'] = this.quizBonusPercentage;
    data['fantasyBonusPercentage'] = this.fantasyBonusPercentage;
    data['worklooperBonusPercentage'] = this.worklooperBonusPercentage;
    data['lrAvatarUrl'] = this.lrAvatarUrl;
    data['preferredLanguage'] = this.preferredLanguage;
    data['pokerAvatarUrl'] = this.pokerAvatarUrl;
    data['whitelistedApkWithPoker'] = this.whitelistedApkWithPoker;
    if (this.gameCategories != null) {
      data['gameCategories'] =
          this.gameCategories.map((v) => v.toJson()).toList();
    }
    data['numberOfBanners'] = this.numberOfBanners;
    data['playStoreApkVersion'] = this.playStoreApkVersion;
    data['playStoreApkUrl'] = this.playStoreApkUrl;
    data['playStoreApkName'] = this.playStoreApkName;
    data['playStoreUpdateDetails'] = this.playStoreUpdateDetails;
    data['playStoreNumberOfBanners'] = this.playStoreNumberOfBanners;
    data['Play Chips'] = this.playChips;
    return data;
  }
}

class WalletInfo {
  String name;
  double amount;
  bool enable;

  WalletInfo({this.name, this.amount, this.enable});

  WalletInfo.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    amount = json['amount'];
    enable = json['enable'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['name'] = this.name;
    data['amount'] = this.amount;
    data['enable'] = this.enable;
    return data;
  }
}

class GameCategories {
  String name;
  List<String> games;

  GameCategories({this.name, this.games});

  GameCategories.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    games = json['games'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['name'] = this.name;
    data['games'] = this.games;
    return data;
  }
}
