package in.glg.rummy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RummySavedCardsList {
    @SerializedName("name_on_card")
    @Expose
    private String nameOnCard;
    @SerializedName("card_name")
    @Expose
    private String cardName;
    @SerializedName("card_brand")
    @Expose
    private String cardBrand;
    @SerializedName("card_bin")
    @Expose
    private String cardBin;
    @SerializedName("card_cvv")
    @Expose
    private Integer cardCvv;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("expiry_year")
    @Expose
    private String expiryYear;
    @SerializedName("card_token")
    @Expose
    private String cardToken;
    @SerializedName("expiry_month")
    @Expose
    private String expiryMonth;
    @SerializedName("is_expired")
    @Expose
    private Integer isExpired;
    @SerializedName("card_mode")
    @Expose
    private String cardMode;
    @SerializedName("isDomestic")
    @Expose
    private String isDomestic;
    @SerializedName("card_no")
    @Expose
    private String cardNo;

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public Integer getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(Integer cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public String getCardMode() {
        return cardMode;
    }

    public void setCardMode(String cardMode) {
        this.cardMode = cardMode;
    }

    public String getIsDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

}
