class TransactionsDM {
  String result;
  String userId;
  List<WalletInfo> walletInfo;

  TransactionsDM({this.result, this.userId, this.walletInfo});

  TransactionsDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    userId = json['user_id'];
    if (json['wallet_info'] != null) {
      walletInfo = <WalletInfo>[];
      json['wallet_info'].forEach((v) {
        walletInfo.add(new WalletInfo.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    data['user_id'] = this.userId;
    if (this.walletInfo != null) {
      data['wallet_info'] = this.walletInfo.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class WalletInfo {
  String id;
  String amount;
  String description;
  String transactionTime;
  String walletName;
  String transactionType;

  WalletInfo(
      {this.id,
      this.amount,
      this.description,
      this.transactionTime,
      this.walletName,
      this.transactionType});

  WalletInfo.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    amount = json['amount'];
    description = json['description'];
    transactionTime = json['transaction_time'];
    walletName = json['wallet_name'];
    transactionType = json['transaction_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['amount'] = this.amount;
    data['description'] = this.description;
    data['transaction_time'] = this.transactionTime;
    data['wallet_name'] = this.walletName;
    data['transaction_type'] = this.transactionType;
    return data;
  }
}
