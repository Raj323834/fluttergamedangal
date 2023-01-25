package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.utils.RummyUtils;

@Root(name = "authrep",strict = false)
public class RummyLoginRequest extends RummyBaserequest
{
    @Attribute(name = "build_version")
    private String buildVersion = "1.0";

    @Attribute(name = "client_type")
    private String clientType = RummyUtils.CLIENT_TYPE;

    @Attribute(name = "device_type")
    private String deviceType;

    @Attribute(name = "user_id")
    private String email;

    @Attribute(name = "flash")
    public static String flash = "14"; // 14 - Prod (DG)

    @Attribute(name = "os")
    private String os = "Android";

    @Attribute(name = "password")
    private String password;

    @Attribute(name = "player_in")
    private String playerIn = "new_lobby";

    @Attribute(name = "session_id")
    private String sessionId;

    @Attribute(name = "version_api")
    private String versionApi = "v2";

    public String getPlayerIn() {
        return this.playerIn;
    }

    public void setPlayerIn(String playerIn) {
        this.playerIn = playerIn;
    }

    public String getBuildVersion() {
        return this.buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }


    public String getOs() {
        return this.os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getFlash() {
        return this.flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getClientType() {
        return this.clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getVersionApi() {
        return this.versionApi;
    }

    public void setVersionApi(String versionApi) {
        this.versionApi = versionApi;
    }
}
