class WithdrawalsDM {
  String result;
  List<WithdrawalDetails> details;

  WithdrawalsDM({this.result, this.details});

  WithdrawalsDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['details'] != null) {
      details = <WithdrawalDetails>[];
      json['details'].forEach((v) {
        details.add(new WithdrawalDetails.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.details != null) {
      data['details'] = this.details.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class WithdrawalDetails {
  String id;
  String amount;
  String status;
  String reason;
  int withdrawalTime;
  int fulfilledTime;
  bool isCancellable;

  WithdrawalDetails(
      {this.id,
      this.amount,
      this.status,
      this.reason,
      this.withdrawalTime,
      this.fulfilledTime,
      this.isCancellable});

  WithdrawalDetails.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    amount = json['amount'];
    status = json['status'];
    reason = json['reason'];
    withdrawalTime = json['withdrawal_time'];
    fulfilledTime = json['fulfilled_time'];
    isCancellable = json['isCancellable'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['amount'] = this.amount;
    data['status'] = this.status;
    data['reason'] = this.reason;
    data['withdrawal_time'] = this.withdrawalTime;
    data['fulfilled_time'] = this.fulfilledTime;
    data['isCancellable'] = this.isCancellable;
    return data;
  }
}
