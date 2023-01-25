package in.glg.rummy.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RummyDepositsResposeData {
    @SerializedName("seam_less")
    @Expose
    private Boolean seamLess;
    @SerializedName("mobikwik_wallet")
    @Expose
    private Boolean mobikwikWallet;
    @SerializedName("lowerlimit")
    @Expose
    private Integer lowerlimit;
    @SerializedName("bonus_list")
    @Expose
    private List<RummyBonusList> bonusList = null;
    @SerializedName("mk_linked")
    @Expose
    private Object mkLinked;
    @SerializedName("ewallet_list")
    @Expose
    private List<RummyEwalletList> ewalletList = null;
    @SerializedName("saved_cards_list")
    @Expose
    private List<Object> savedCardsList = null;
    @SerializedName("banks_list")
    @Expose
    private List<RummyBanksList> banksList = null;
    @SerializedName("mobile_wallet_list")
    @Expose
    private List<RummyMobileWalletList> mobileWalletList = null;
    @SerializedName("cash_card_list")
    @Expose
    private List<RummyCashCardList> cashCardList = null;
    @SerializedName("upperlimit")
    @Expose
    private Integer upperlimit;

    public Boolean getSeamLess() {
        return seamLess;
    }

    public void setSeamLess(Boolean seamLess) {
        this.seamLess = seamLess;
    }

    public Boolean getMobikwikWallet() {
        return mobikwikWallet;
    }

    public void setMobikwikWallet(Boolean mobikwikWallet) {
        this.mobikwikWallet = mobikwikWallet;
    }

    public Integer getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(Integer lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public List<RummyBonusList> getBonusList() {
        return bonusList;
    }

    public void setBonusList(List<RummyBonusList> bonusList) {
        this.bonusList = bonusList;
    }

    public Object getMkLinked() {
        return mkLinked;
    }

    public void setMkLinked(Object mkLinked) {
        this.mkLinked = mkLinked;
    }

    public List<RummyEwalletList> getEwalletList() {
        return ewalletList;
    }

    public void setEwalletList(List<RummyEwalletList> ewalletList) {
        this.ewalletList = ewalletList;
    }

    public List<Object> getSavedCardsList() {
        return savedCardsList;
    }

    public void setSavedCardsList(List<Object> savedCardsList) {
        this.savedCardsList = savedCardsList;
    }

    public List<RummyBanksList> getBanksList() {
        return banksList;
    }

    public void setBanksList(List<RummyBanksList> banksList) {
        this.banksList = banksList;
    }

    public List<RummyMobileWalletList> getMobileWalletList() {
        return mobileWalletList;
    }

    public void setMobileWalletList(List<RummyMobileWalletList> mobileWalletList) {
        this.mobileWalletList = mobileWalletList;
    }

    public List<RummyCashCardList> getCashCardList() {
        return cashCardList;
    }

    public void setCashCardList(List<RummyCashCardList> cashCardList) {
        this.cashCardList = cashCardList;
    }

    public Integer getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(Integer upperlimit) {
        this.upperlimit = upperlimit;
    }
}
