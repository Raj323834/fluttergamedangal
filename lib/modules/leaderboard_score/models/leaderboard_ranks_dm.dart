class LeaderBoardRankDM {
  String result;
  CurrentUser currentUser;
  List<Ranks> ranks;

  LeaderBoardRankDM({this.result, this.currentUser, this.ranks});

  LeaderBoardRankDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    currentUser = json['currentUser'] != null
        ? new CurrentUser.fromJson(json['currentUser'])
        : null;
    if (json['ranks'] != null) {
      ranks = <Ranks>[];
      json['ranks'].forEach((v) {
        ranks.add(new Ranks.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.currentUser != null) {
      data['currentUser'] = this.currentUser.toJson();
    }
    if (this.ranks != null) {
      data['ranks'] = this.ranks.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CurrentUser {
  String result;
  PlayerDetails playerDetails;

  CurrentUser({this.result, this.playerDetails});

  CurrentUser.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    playerDetails = json['playerDetails'] != null
        ? new PlayerDetails.fromJson(json['playerDetails'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.playerDetails != null) {
      data['playerDetails'] = this.playerDetails.toJson();
    }
    return data;
  }
}

class PlayerDetails {
  String sId;
  String userName;
  int rank;
  String score;
  String userAvatar;
  double doubleScore;

  PlayerDetails(
      {this.sId,
      this.userName,
      this.rank,
      this.score,
      this.userAvatar,
      this.doubleScore});

  PlayerDetails.fromJson(Map<String, dynamic> json) {
    sId = json['_id'];
    userName = json['userName'];
    rank = json['rank'];
    score = json['score'];
    userAvatar = json['userAvatar'];
    doubleScore = json['doubleScore'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['_id'] = this.sId;
    data['userName'] = this.userName;
    data['rank'] = this.rank;
    data['score'] = this.score;
    data['userAvatar'] = this.userAvatar;
    data['doubleScore'] = this.doubleScore;
    return data;
  }
}

class Ranks {
  String sId;
  String userName;
  int rank;
  String score;
  String userAvatar;
  double doubleScore;

  Ranks(
      {this.sId,
      this.userName,
      this.rank,
      this.score,
      this.userAvatar,
      this.doubleScore});

  Ranks.fromJson(Map<String, dynamic> json) {
    sId = json['_id'];
    userName = json['userName'];
    rank = json['rank'];
    score = json['score'];
    userAvatar = json['userAvatar'];
    doubleScore = json['doubleScore'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['_id'] = this.sId;
    data['userName'] = this.userName;
    data['rank'] = this.rank;
    data['score'] = this.score;
    data['userAvatar'] = this.userAvatar;
    data['doubleScore'] = this.doubleScore;
    return data;
  }
}
