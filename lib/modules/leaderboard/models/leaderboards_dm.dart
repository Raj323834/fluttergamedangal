class LeaderBoardsDM {
  String result;
  List<Leaderboard> leaderboards;
  List<Leaderboard> upcomingLeaderboards;
  List<Leaderboard> completedLeaderboards;

  LeaderBoardsDM(
      {this.result,
      this.leaderboards,
      this.upcomingLeaderboards,
      this.completedLeaderboards});

  LeaderBoardsDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['leaderboards'] != null) {
      leaderboards = <Leaderboard>[];
      json['leaderboards'].forEach((v) {
        leaderboards.add(new Leaderboard.fromJson(v));
      });
    }
    if (json['upcomingLeaderboards'] != null) {
      upcomingLeaderboards = <Leaderboard>[];
      json['upcomingLeaderboards'].forEach((v) {
        upcomingLeaderboards.add(new Leaderboard.fromJson(v));
      });
    }
    if (json['completedLeaderboards'] != null) {
      completedLeaderboards = <Leaderboard>[];
      json['completedLeaderboards'].forEach((v) {
        completedLeaderboards.add(new Leaderboard.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.leaderboards != null) {
      data['leaderboards'] = this.leaderboards.map((v) => v.toJson()).toList();
    }
    if (this.upcomingLeaderboards != null) {
      data['upcomingLeaderboards'] =
          this.upcomingLeaderboards.map((v) => v.toJson()).toList();
    }
    if (this.completedLeaderboards != null) {
      data['completedLeaderboards'] =
          this.completedLeaderboards.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Leaderboard {
  String leaderboardId;
  String name;
  String ruleType;
  String type;
  int minValue;
  int noOfPrizes;
  List<String> games;
  double promoPrizePool;
  double lockedPrizePool;
  double depositPrizePool;
  double withdrawalPrizePool;
  double minEntryfee;
  String startTime;
  String endTime;
  List<PrizeDetails> prizeDetails;
  String termsAndConditions;
  String aboutLeaderboard;
  String createdAt;
  List<Users> users;
  bool enabled;
  bool completed;
  bool stakeWise;

  Leaderboard(
      {this.leaderboardId,
      this.name,
      this.ruleType,
      this.type,
      this.minValue,
      this.noOfPrizes,
      this.games,
      this.promoPrizePool,
      this.lockedPrizePool,
      this.depositPrizePool,
      this.withdrawalPrizePool,
      this.minEntryfee,
      this.startTime,
      this.endTime,
      this.prizeDetails,
      this.termsAndConditions,
      this.aboutLeaderboard,
      this.createdAt,
      this.users,
      this.enabled,
      this.completed,
      this.stakeWise});

  Leaderboard.fromJson(Map<String, dynamic> json) {
    leaderboardId = json['leaderboardId'];
    name = json['name'];
    ruleType = json['ruleType'];
    type = json['type'];
    minValue = json['minValue'];
    noOfPrizes = json['noOfPrizes'];
    games = json['games'].cast<String>();
    promoPrizePool = json['promoPrizePool'];
    lockedPrizePool = json['lockedPrizePool'];
    depositPrizePool = json['depositPrizePool'];
    withdrawalPrizePool = json['withdrawalPrizePool'];
    minEntryfee = json['minEntryfee'];
    startTime = json['startTime'];
    endTime = json['endTime'];
    if (json['prizeDetails'] != null) {
      prizeDetails = <PrizeDetails>[];
      json['prizeDetails'].forEach((v) {
        prizeDetails.add(new PrizeDetails.fromJson(v));
      });
    }
    termsAndConditions = json['termsAndConditions'];
    aboutLeaderboard = json['aboutLeaderboard'];
    createdAt = json['createdAt'];
    if (json['users'] != null) {
      users = <Users>[];
      json['users'].forEach((v) {
        users.add(new Users.fromJson(v));
      });
    }
    enabled = json['enabled'];
    completed = json['completed'];
    stakeWise = json['stakeWise'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['leaderboardId'] = this.leaderboardId;
    data['name'] = this.name;
    data['ruleType'] = this.ruleType;
    data['type'] = this.type;
    data['minValue'] = this.minValue;
    data['noOfPrizes'] = this.noOfPrizes;
    data['games'] = this.games;
    data['promoPrizePool'] = this.promoPrizePool;
    data['lockedPrizePool'] = this.lockedPrizePool;
    data['depositPrizePool'] = this.depositPrizePool;
    data['withdrawalPrizePool'] = this.withdrawalPrizePool;
    data['minEntryfee'] = this.minEntryfee;
    data['startTime'] = this.startTime;
    data['endTime'] = this.endTime;
    if (this.prizeDetails != null) {
      data['prizeDetails'] = this.prizeDetails.map((v) => v.toJson()).toList();
    }
    data['termsAndConditions'] = this.termsAndConditions;
    data['aboutLeaderboard'] = this.aboutLeaderboard;
    data['createdAt'] = this.createdAt;
    if (this.users != null) {
      data['users'] = this.users.map((v) => v.toJson()).toList();
    }
    data['enabled'] = this.enabled;
    data['completed'] = this.completed;
    data['stakeWise'] = this.stakeWise;
    return data;
  }
}

class PrizeDetails {
  String rank;
  double promoPercent;
  double lockedPercent;
  double depositPercent;
  double withdrawalPercent;

  PrizeDetails(
      {this.rank,
      this.promoPercent,
      this.lockedPercent,
      this.depositPercent,
      this.withdrawalPercent});

  PrizeDetails.fromJson(Map<String, dynamic> json) {
    rank = json['rank'];
    promoPercent = json['promoPercent'];
    lockedPercent = json['lockedPercent'];
    depositPercent = json['depositPercent'];
    withdrawalPercent = json['withdrawalPercent'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['rank'] = this.rank;
    data['promoPercent'] = this.promoPercent;
    data['lockedPercent'] = this.lockedPercent;
    data['depositPercent'] = this.depositPercent;
    data['withdrawalPercent'] = this.withdrawalPercent;
    return data;
  }
}

class Users {
  String userId;
  double score;
  double scheduledPoints;
  String username;
  String avatar;

  Users(
      {this.userId,
      this.score,
      this.scheduledPoints,
      this.username,
      this.avatar});

  Users.fromJson(Map<String, dynamic> json) {
    userId = json['userId'];
    score = json['score'];
    scheduledPoints = json['scheduledPoints'];
    username = json['username'];
    avatar = json['avatar'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userId'] = this.userId;
    data['score'] = this.score;
    data['scheduledPoints'] = this.scheduledPoints;
    data['username'] = this.username;
    data['avatar'] = this.avatar;
    return data;
  }
}
