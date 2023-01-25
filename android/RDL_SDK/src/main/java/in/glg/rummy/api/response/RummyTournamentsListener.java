package in.glg.rummy.api.response;

import org.simpleframework.xml.Root;

/**
 * Created by GridLogic on 14/12/17.
 */

@Root(strict = false)
public class RummyTournamentsListener extends RummyBaseResponse
{

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
