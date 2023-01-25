package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */


import java.util.Comparator;

import in.glg.rummy.models.RummyEvent;

public class RummyEventComparator implements Comparator<RummyEvent> {
    public int compare(RummyEvent p1, RummyEvent p2) {
        int face1;
        int face2;
        String[] gameIdArry1 = p1.getGameId().split("-");
        String[] gameIdArry2 = p1.getGameId().split("-");
        if (gameIdArry1.length <= 1 || gameIdArry2.length <= 1) {
            face1 = Integer.valueOf(gameIdArry1[0]).intValue();
            face2 = Integer.valueOf(gameIdArry2[0]).intValue();
        } else {
            face1 = Integer.valueOf(gameIdArry1[1]).intValue();
            face2 = Integer.valueOf(gameIdArry2[1]).intValue();
        }
        if (face1 == face2) {
            return 0;
        }
        if (face1 > face2) {
            return 1;
        }
        return -1;
    }
}