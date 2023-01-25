package in.glg.rummy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RummyBonusList {

    @SerializedName("valid_from")
    @Expose
    private String validFrom;
    @SerializedName("bonus_code")
    @Expose
    private String bonusCode;
    @SerializedName("max_amount")
    @Expose
    private Integer maxAmount;
    @SerializedName("min_amount")
    @Expose
    private Integer minAmount;
    @SerializedName("valid_to")
    @Expose
    private String validTo;
    @SerializedName("percentage")
    @Expose
    private Integer percentage;

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getBonusCode() {
        return bonusCode;
    }

    public void setBonusCode(String bonusCode) {
        this.bonusCode = bonusCode;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

}