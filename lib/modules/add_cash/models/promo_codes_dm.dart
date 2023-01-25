class PromoCodesDM {
  String result;
  List<Bonuses> bonuses;
  String gateway;
  double minimumDepositAmount;
  double defaultDepositAmount;

  PromoCodesDM({
    this.result,
    this.bonuses,
    this.gateway,
    this.minimumDepositAmount,
    this.defaultDepositAmount,
  });

  PromoCodesDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['bonuses'] != null) {
      bonuses = <Bonuses>[];
      json['bonuses'].forEach((v) {
        bonuses.add(new Bonuses.fromJson(v));
      });
    }
    gateway = json['gateway'];
    minimumDepositAmount = json['minimumDepositAmount'];
    defaultDepositAmount = json['defaultDepositAmount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.bonuses != null) {
      data['bonuses'] = this.bonuses.map((v) => v.toJson()).toList();
    }
    data['gateway'] = this.gateway;
    data['minimumDepositAmount'] = this.minimumDepositAmount;
    data['defaultDepositAmount'] = this.defaultDepositAmount;
    return data;
  }
}

class Bonuses {
  String id;
  String code;
  double minAmount;
  String isVisibleInUI;
  String description;
  int priority;
  String error;
  bool applicable;

  Bonuses(
      {this.id,
      this.code,
      this.minAmount,
      this.isVisibleInUI,
      this.description,
      this.priority,
      this.error = '',
      this.applicable});

  Bonuses.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    code = json['code'];
    minAmount = json['minAmount'];
    isVisibleInUI = json['isVisibleInUI'];
    description = json['description'];
    priority = json['priority'];
    error = json['error'];
    applicable = json['applicable'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['code'] = this.code;
    data['minAmount'] = this.minAmount;
    data['isVisibleInUI'] = this.isVisibleInUI;
    data['description'] = this.description;
    data['priority'] = this.priority;
    data['error'] = this.error;
    data['applicable'] = this.applicable;
    return data;
  }
}
