package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import in.glg.rummy.R;
import in.glg.rummy.utils.RummyErrorCodes;

@Root(name = "reply", strict = false)
public class RummyLoginResponse extends RummyBaseResponse implements Serializable
{
    @Attribute(name = "bonuschips", required = false)
    private int bonousChips;
    @Attribute(name = "bonusinplay", required = false)
    private int bonusInPlay;
    @Attribute(name = "chatblock", required = false)
    private int chatBlock;
    @Attribute(name = "funchips", required = false)
    private String funChips;
    @Attribute(name = "funinplay", required = false)
    private String funInPlay;
    @Attribute(name = "gender", required = false)
    private String gender;
    @Attribute(name = "loyaltychips", required = false)
    private String loyalityChips;
    @Attribute(name = "loyaltyinplay", required = false)
    private String loyalityInPlay;
    @Attribute(name = "middleJoin", required = false)
    private boolean middleJoin;
    @Attribute(name = "minimum_to_reload", required = false)
    private String minToReload;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "playerlevel", required = false)
    private int playerLevel;
    @Attribute(name = "poollowbet", required = false)
    private String poolLowBet;
    @Attribute(name = "poolmediumbet", required = false)
    private String poolMediumBet;
    @Attribute(name = "prlowbet", required = false)
    private String prLowBet;
    @Attribute(name = "prmediumbet", required = false)
    private String prMediumBet;
    @Attribute(name = "realchips", required = false)
    private String realChips;
    @Attribute(name = "realinplay", required = false)
    private String realInPlay;
    @Attribute(name = "socialchips", required = false)
    private String socialChips;
    @Attribute(name = "socialinplay", required = false)
    private String socialInPlay;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "email", required = false)
    private String userEmail;
    @Attribute(name = "user_id", required = false)
    private String userId;

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isMiddleJoin() {
        return this.middleJoin;
    }

    public void setMiddleJoin(boolean middleJoin) {
        this.middleJoin = middleJoin;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getMinToReload() {
        return this.minToReload;
    }

    public void setMinToReload(String minToReload) {
        this.minToReload = minToReload;
    }

    public void setLoyalityChips(String loyalityChips) {
        this.loyalityChips = loyalityChips;
    }

    public int getBonousChips() {
        return this.bonousChips;
    }

    public void setBonousChips(int bonousChips) {
        this.bonousChips = bonousChips;
    }

    public int getBonusInPlay() {
        return this.bonusInPlay;
    }

    public void setBonusInPlay(int bonusInPlay) {
        this.bonusInPlay = bonusInPlay;
    }

    public int getChatBlock() {
        return this.chatBlock;
    }

    public void setChatBlock(int chatBlock) {
        this.chatBlock = chatBlock;
    }

    public String getFunChips() {
        return this.funChips;
    }

    public void setFunChips(String funChips) {
        this.funChips = funChips;
    }

    public String getFunInPlay() {
        return this.funInPlay;
    }

    public void setFunInPlay(String funInPlay) {
        this.funInPlay = funInPlay;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLoyalityChips() {
        return this.loyalityChips;
    }

    public String getLoyalityInPlay() {
        return this.loyalityInPlay;
    }

    public void setLoyalityInPlay(String loyalityInPlay) {
        this.loyalityInPlay = loyalityInPlay;
    }

    public int getPlayerLevel() {
        return this.playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getPoolLowBet() {
        return this.poolLowBet;
    }

    public void setPoolLowBet(String poolLowBet) {
        this.poolLowBet = poolLowBet;
    }

    public String getPoolMediumBet() {
        return this.poolMediumBet;
    }

    public void setPoolMediumBet(String poolMediumBet) {
        this.poolMediumBet = poolMediumBet;
    }

    public String getPrLowBet() {
        return this.prLowBet;
    }

    public void setPrLowBet(String prLowBet) {
        this.prLowBet = prLowBet;
    }

    public String getPrMediumBet() {
        return this.prMediumBet;
    }

    public void setPrMediumBet(String prMediumBet) {
        this.prMediumBet = prMediumBet;
    }

    public String getRealChips() {
        return this.realChips;
    }

    public void setRealChips(String realChips) {
        this.realChips = realChips;
    }

    public String getRealInPlay() {
        return this.realInPlay;
    }

    public void setRealInPlay(String realInPlay) {
        this.realInPlay = realInPlay;
    }

    public String getSocialChips() {
        return this.socialChips;
    }

    public void setSocialChips(String socialChips) {
        this.socialChips = socialChips;
    }

    public String getSocialInPlay() {
        return this.socialInPlay;
    }

    public void setSocialInPlay(String socialInPlay) {
        this.socialInPlay = socialInPlay;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getErrorMessage() {
        if (Integer.parseInt(getCode()) == RummyErrorCodes.INVALID_PASSWORD) {
            return R.string.error_invalid_password;
        }
        if (Integer.parseInt(getCode()) == RummyErrorCodes.INVALID_USER_NAME) {
            return R.string.error_invalid_email;
        }
        return R.string.unknown_server_error;
    }
}
