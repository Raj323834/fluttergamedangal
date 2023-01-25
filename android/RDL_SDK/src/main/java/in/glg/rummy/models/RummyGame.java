package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class RummyGame {
    @Attribute(name = "game_id", required = false)
    private String gameId;
    @ElementList(inline = true, name = "user", required = false)
    private List<RummyUser> gamePlayer;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "points", required = false)
    private String points;
    @Attribute(name = "result", required = false)
    private String result;
    @Attribute(name = "score", required = false)
    private String score;

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<RummyUser> getGamePlayer() {
        return this.gamePlayer;
    }

    public void setGamePlayer(List<RummyUser> gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
