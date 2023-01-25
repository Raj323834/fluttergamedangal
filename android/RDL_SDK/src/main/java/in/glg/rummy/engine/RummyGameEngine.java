package in.glg.rummy.engine;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.fragments.RummyTablesFragment;
import in.glg.rummy.models.RummyAuthReq;
import in.glg.rummy.models.RummyEngineRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.utils.RummyPrefManagerTracker;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public class RummyGameEngine {
    Context mContext;
    private static final String TAG = RummyGameEngine.class.getName();
    private static RummyOnResponseListener eventListener = new RummyOnResponseListener(RummyEvent.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyUtils.sendEvent((RummyEvent) response);
            }
        }
    };
    private static RummyGameEngine gameEngine;
    private static RummyOnResponseListener requestListner = new RummyOnResponseListener(RummyEngineRequest.class) {
        public void onResponse(Object var1) {
            if (var1 != null) {
                RummyUtils.sendRequest((RummyEngineRequest) var1);
            }
        }
    };
    private static RummyOnResponseListener responseListener;
    private RummyAuthReq authReq;
    private String msgUUID;
    private InputStream inputStream;
    private boolean isConnected = false;
    private boolean isDataDeliverReady = false;
    private boolean isHavingAuthRequest = false;
    private boolean isOtherLogin = false;
    private OutputStream outputStream;
    private String response = "";
    private AsyncSocket socket;
    private Socket tcpSocket = null;

    // $FF: synthetic method
    static InputStream access$200(RummyGameEngine var0) {
        return var0.inputStream;
    }

    public static RummyGameEngine getInstance() {
        if (gameEngine == null) {
            gameEngine = new RummyGameEngine();
        }
        return gameEngine;
    }

    private void handleConnectionResponses(Exception exp) {
        if (exp != null) {
            throw new RuntimeException(exp);
        } else {
            this.socket.setDataCallback(new DataCallback() {
                public void onDataAvailable(DataEmitter var1, ByteBufferList var2) {
                    int var3 = 0;
                    byte[] var7 = var2.getAllByteArray();
                    String var8 = new String(var7, 0, var7.length);

                    if (var8.endsWith("\u0000")) {
                        RummyGameEngine.this.isDataDeliverReady = true;
                    }

                    RummyGameEngine.this.response = RummyGameEngine.this.response + var8;
                    if (!response.contains("HEART_BEAT") && !response.contains("gamesetting_update")) {
//                        Log.e("HEART_BEAT",response);
                    }

                    if (RummyGameEngine.this.isDataDeliverReady) {
                        RummyGameEngine.this.isDataDeliverReady = false;
                        if (RummyGameEngine.this.authReq == null && RummyGameEngine.this.response.startsWith("<authreq")) {
                            RummyGameEngine.this.authReq = (RummyAuthReq) RummyUtils.getObject(RummyGameEngine.this.response, RummyAuthReq.class);
                            RummyGameEngine.this.isHavingAuthRequest = true;
                            RummyGameEngine.this.isConnected = true;
                            if (RummyGameEngine.this.authReq != null) {
                                RummyUtils.sendEvent(RummyGameEvent.SERVER_CONNECTED);

                                Context tableFragmentContext = RummyTablesFragment.getTableFragment();
                                if (null == tableFragmentContext)
                                    return;

                                Date currentTime = Calendar.getInstance().getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                                String dateFormat = sdf.format(currentTime);
                                RummyPrefManagerTracker.saveString(tableFragmentContext, "engineconnect", dateFormat + "");
                                RummyTablesFragment.alTrackList.add("engineconnect");
                                Log.e("engineconnect", dateFormat + "");

                                RummyPrefManagerTracker.saveString(tableFragmentContext, "internetconnect", dateFormat + "");
                                RummyTablesFragment.alTrackList.add("internetconnect");
                                Log.e("internetconnect", dateFormat + "");
                            }
                        } else {
                            String[] response = RummyGameEngine.this.response.split("\u0000");

                            for (int i = response.length; var3 < i; ++var3) {
                                String var6 = response[var3];
                                if (var6.startsWith("<reply")) {
                                    RummyTLog.d(RummyGameEngine.TAG, "Reply  : " + var6);
                                    if (RummyGameEngine.responseListener != null) {
                                        RummyGameEngine.responseListener.sendMessage(RummyGameEngine.responseListener.getResponseMessage(var6));
                                    }
                                   // GameEngine.responseListener = null;
                                } else if (var6.startsWith("<event")) {
                                    RummyEvent var5 = (RummyEvent) RummyUtils.getObject(var6, RummyEvent.class);

                                    // to log the event
                                    if (!var5.getEventName().equalsIgnoreCase("gamesetting_update") && !var5.getEventName().equalsIgnoreCase("HEART_BEAT"))
                                        RummyTLog.d(RummyGameEngine.TAG, "Event  : " + var6);

                                    if (var5.getEventName().equalsIgnoreCase("get_table_details")) {
                                        RummyUtils.tableDetailsList.add(var5);
                                    }

                                    if (var5.getEventName().equalsIgnoreCase("players_rank")) {
                                        RummyUtils.tableDetailsList.add(var5);
                                    }

                                    if (var5.getEventName().equalsIgnoreCase("TOURNEY_BALANCE")) {
                                        RummyUtils.tableDetailsList.add(var5);
                                    }

                                    if (var5.getEventName().equalsIgnoreCase("OTHER_LOGIN")) {
                                        RummyGameEngine.this.isOtherLogin = true;
                                    }

                                    if (var5.getEventName().equalsIgnoreCase("BALANCE_UPDATE")) {
                                        RummyUtils.tableDetailsList.add(var5);
                                        Log.e(TAG + "", "BALANCE_UPDATE");
                                       // CommonEventTracker.UpdateBalaceProperty(mContext,var5.getReaChips());
                                    }
                                    // catching SHOW request
                                    try {
                                        if (var5.getEventName().equalsIgnoreCase("show")) {
                                            RummyUtils.SHOW_EVENT = var5;
                                            Log.w(TAG, "SHOW EVENT CAUGHT");
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "EXP: catching rummy_show request-->> " + e.toString());
                                    }

                                    /*// catching SEND_DEAL request
                                    try {
                                        if (var5.getEventName().equalsIgnoreCase("SEND_DEAL")) {
                                            Log.w(TAG, "SEND_DEAL EVENT CAUGHT------");
                                            Utils.DEAL_SENT = true;
                                        }
                                    } catch (Exception e){
                                        Log.e(TAG, "EXP: catching SEND_DEAL request-->> "+e.toString());
                                    }*/

                                    RummyGameEngine.eventListener.sendMessage(RummyGameEngine.eventListener.getResponseMessage(var6));
                                } else if (var6.startsWith("<request")) {
                                    RummyTLog.d(RummyGameEngine.TAG, "Request from engine : " + var6);
                                    RummyUtils.getObject(RummyGameEngine.this.response, RummyEngineRequest.class);
                                    RummyGameEngine.requestListner.sendMessage(RummyGameEngine.requestListner.getResponseMessage(var6));

                                    try {
                                        RummyEngineRequest var5 = (RummyEngineRequest) RummyUtils.getObject(var6, RummyEngineRequest.class);
                                        if (var5.getCommand().equalsIgnoreCase("meld")) {
                                            RummyUtils.MELD_REQUEST = var5;
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "EXP: handling rummy_meld request-->> " + e.toString());
                                    }
                                }
                            }
                        }

                        RummyGameEngine.this.response = "";
                    }

                    var2.recycle();
                }
            });
            this.socket.setClosedCallback(new CompletedCallback() {
                public void onCompleted(Exception var1) {
                    RummyTLog.d(RummyGameEngine.TAG, "Socket closed");

                    RummyApplication.userNeedsAuthentication = true;

                    RummyGameEngine.this.isHavingAuthRequest = false;
                    RummyGameEngine.this.stop();
                    RummyGameEngine.this.stopEngine();
                    if (!RummyGameEngine.this.isOtherLogin) {
                        RummyUtils.sendEvent(RummyGameEvent.SERVER_DISCONNECTED);
                    } else {
                        RummyGameEngine.this.isOtherLogin = false;
                        RummyUtils.sendEvent(RummyGameEvent.OTHER_LOGIN);
                    }

                }
            });
            this.socket.setEndCallback(new CompletedCallback() {
                public void onCompleted(Exception var1) {
                    RummyTLog.d(RummyGameEngine.TAG, "Socket Ended");
                }
            });
        }
    }

    private void readResponse(RummyOnResponseListener var1) {
        (new Thread(new ReaderThread(var1))).start();
    }

    public static void sendRequestToEngine(Context ctx, String eventData, RummyOnResponseListener listener) throws RummyGameEngineNotRunning {
        RummyGameEngine ge = getInstance();
        if (ge == null) {
            throw new RummyGameEngineNotRunning("Game Engine not running");
        } else if (RummyUtils.isNetworkAvailable(ctx)) {
            ge.sendDataToEngine(ctx, eventData, listener);
        } else {
            Toast.makeText(ctx, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void tcpAsyncConn() {
       // InetSocketAddress address = new InetSocketAddress(RummyUtils.ENGINE_IP, 4271);
        InetSocketAddress address = new InetSocketAddress(RummyUtils.ENGINE_IP, 4270); //port change for keep alive
        AsyncServer.getDefault().connectSocket(address, new ConnectCallback() {
            public void onConnectCompleted(Exception var1, AsyncSocket var2) {
                RummyGameEngine.this.socket = var2;
                if (var1 != null) {
                    RummyGameEngine.this.isHavingAuthRequest = false;
                    RummyGameEngine.this.stop();
                    RummyGameEngine.this.stopEngine();
                    RummyUtils.sendEvent(RummyGameEvent.SERVER_DISCONNECTED);
                } else {
                    RummyGameEngine.this.handleConnectionResponses(var1);
                }

            }
        });
    }

    public void stopEngine() {
        this.isConnected = false;
        gameEngine = null;
        this.authReq = null;
    }

    public boolean haveAuthRequest() {
        return this.isHavingAuthRequest;
    }

    public boolean isSocketConnected() {
        return this.isConnected;
    }

    public void sendDataToEngine(Context context, String req, RummyOnResponseListener listener) {
        final String request = req + "\u0000";
        if (request.startsWith("<request") || request.startsWith("<authrep")) {
            responseListener = listener;
        }

        if (this.socket != null) {
            if (!request.contains("HEART_BEAT"))
                RummyTLog.w(TAG, "Req: " + request);

            if (request.contains("authrep")) {
                Log.e(TAG, "User Needs Authentication: " + RummyApplication.userNeedsAuthentication);

                if (RummyApplication.userNeedsAuthentication) {
                    Util.writeAll(this.socket, (byte[]) request.getBytes(), new CompletedCallback() {
                        public void onCompleted(Exception var1) {
                            if (request.contains("authrep")) {
                                Log.e(TAG, "AUTHREP onCompleted");
                                RummyApplication.userNeedsAuthentication = false;
                            }
                            if (var1 != null) {
                                Log.d(RummyGameEngine.TAG, "sendDataToEngine(): + " + var1.getLocalizedMessage());
                                throw new RuntimeException(var1);
                            }
                        }
                    });
                }
            } else {
                Util.writeAll(this.socket, (byte[]) request.getBytes(), new CompletedCallback() {
                    public void onCompleted(Exception var1) {
                        if (var1 != null) {
                            Log.d(RummyGameEngine.TAG, "sendDataToEngine(): + " + var1.getLocalizedMessage());
                            throw new RuntimeException(var1);
                        }
                    }
                });
            }
        } else {
            RummyTLog.e(TAG, "Socket is NULL: ");
            RummyUtils.sendEvent(RummyGameEvent.SERVER_DISCONNECTED);
        }

    }

    public void start() {
        (new Thread(new GameThread(null))).start();
    }

    public void stop() {
        this.isConnected = false;
        if (this.socket != null) {
            this.socket.getServer().stop();
            this.socket.close();
        }

    }

    private class GameThread implements Runnable {
        private GameThread() {
        }

        // $FF: synthetic method
        GameThread(Object var2) {
            this();
        }

        public void run() {
            RummyGameEngine.this.tcpAsyncConn();
        }
    }

    private class ReaderThread implements Runnable {
        private RummyOnResponseListener listener;

        public ReaderThread(RummyOnResponseListener var2) {
            this.listener = var2;
        }

        public void run() {
            // $FF: Couldn't be decompiled
        }
    }
}
