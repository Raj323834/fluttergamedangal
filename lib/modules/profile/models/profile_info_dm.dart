class ProfileInfoDM {
  String result;
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
  bool idVerified;
  bool addressVerified;
  bool kycVerified;
  bool emailOptIn;
  bool smsOptIn;
  String avatarUrl;
  String lrAvatarUrl;
  String preferredLanguage;
  String pokerAvatarUrl;

  ProfileInfoDM(
      {this.result,
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
      this.idVerified,
      this.addressVerified,
      this.kycVerified,
      this.emailOptIn,
      this.smsOptIn,
      this.avatarUrl,
      this.lrAvatarUrl,
      this.preferredLanguage,
      this.pokerAvatarUrl});

  ProfileInfoDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
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
    idVerified = json['idVerified'];
    addressVerified = json['addressVerified'];
    kycVerified = json['kycVerified'];
    emailOptIn = json['emailOptIn'];
    smsOptIn = json['smsOptIn'];
    avatarUrl = json['avatarUrl'];
    lrAvatarUrl = json['lrAvatarUrl'];
    preferredLanguage = json['preferredLanguage'];
    pokerAvatarUrl = json['pokerAvatarUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
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
    data['idVerified'] = this.idVerified;
    data['addressVerified'] = this.addressVerified;
    data['kycVerified'] = this.kycVerified;
    data['emailOptIn'] = this.emailOptIn;
    data['smsOptIn'] = this.smsOptIn;
    data['avatarUrl'] = this.avatarUrl;
    data['lrAvatarUrl'] = this.lrAvatarUrl;
    data['preferredLanguage'] = this.preferredLanguage;
    data['pokerAvatarUrl'] = this.pokerAvatarUrl;
    return data;
  }
}
