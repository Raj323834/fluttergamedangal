package in.glg.rummy.models;

public class RummyScore {
    public String gameId;
    public String nickName;
    public String points;
    public String scoreResult;
    public String totalScore;
    public String userId;

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getScoreResult() {
        return this.scoreResult;
    }

    public void setScoreResult(String scoreResult) {
        this.scoreResult = scoreResult;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
