package in.glg.rummy.interfaces;

import java.util.List;

import in.glg.rummy.models.RummyTable;

public interface RummyTableRefreshListener {
    void refreshTable(List<RummyTable> rummyTables);
}
