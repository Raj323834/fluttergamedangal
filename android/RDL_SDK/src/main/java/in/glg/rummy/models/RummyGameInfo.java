package in.glg.rummy.models;

public class RummyGameInfo {
    private String entryFee;
    private String extraTime;
    private String firstDrop;
    private String fullCount;
    private String gameType;
    private String maxExtraTime;
    private String middleDrop;
    private String moveTime;
    private String variant;

    public String getGameType() {
        return this.gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getVariant() {
        return this.variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getEntryFee() {
        return this.entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getFirstDrop() {
        return this.firstDrop;
    }

    public void setFirstDrop(String firstDrop) {
        this.firstDrop = firstDrop;
    }

    public String getMiddleDrop() {
        return this.middleDrop;
    }

    public void setMiddleDrop(String middleDrop) {
        this.middleDrop = middleDrop;
    }

    public String getFullCount() {
        return this.fullCount;
    }

    public void setFullCount(String fullCount) {
        this.fullCount = fullCount;
    }

    public String getMoveTime() {
        return this.moveTime;
    }

    public void setMoveTime(String moveTime) {
        this.moveTime = moveTime;
    }

    public String getExtraTime() {
        return this.extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public String getMaxExtraTime() {
        return this.maxExtraTime;
    }

    public void setMaxExtraTime(String maxExtraTime) {
        this.maxExtraTime = maxExtraTime;
    }
}
