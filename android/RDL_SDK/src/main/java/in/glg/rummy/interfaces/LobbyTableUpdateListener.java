package in.glg.rummy.interfaces;

import in.glg.rummy.models.RummyGamePlayer;

public interface LobbyTableUpdateListener {
    void setTableUi(String tableId, RummyGamePlayer player, int maxPlayerCount, boolean isLeft);
    void reSetTableUi(String tableId);
}
