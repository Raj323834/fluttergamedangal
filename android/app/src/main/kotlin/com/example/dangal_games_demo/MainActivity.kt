package com.example.dangal_games_demo

import `in`.glg.rummy.activities.RummyInstance
import `in`.glg.rummy.interfaces.RummyListener
import `in`.glg.rummy.utils.RummyTLog
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import com.facebook.appevents.AppEventsLogger
import com.singular.flutter_sdk.SingularBridge
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.coroutines.*
import java.io.File
import android.R
import android.util.Log;

import android.view.WindowManager

import android.view.Window

import android.R.attr.data
import androidx.core.content.ContextCompat


class MainActivity: FlutterActivity() {
  private val CHANNEL = "com.flutter.rummyDangal"
  private val CHANNEL_LINK = "https.dangalgames.sng.link/channel"
  private val CHANNEL_UNINSTALL = "com.flutter.uninstall"
  private val CHANNEL_INSTALL = "com.flutter.install"
  private val CHANNEL_DOWNLOAD = "com.flutter.download"
  private val CHANNEL_FB_EVENT = "com.flutter.fbevents"
  private val CHANNEL_DIR = "com.flutter.downloadDir"
  private var startString: String? = null

  private val EVENTS = "https.dangalgames.sng.link/events"
  private val DOWNLOAD_EVENTS = "com.app.dangalgames/downloadEvents"
  private var linksReceiver: BroadcastReceiver? = null

  private lateinit var _result: MethodChannel.Result
  private lateinit var _event: EventChannel.EventSink
  private var downloadManager: DownloadManager? = null
  private var downloadId: Long? = null

  private var downloadJob: Job? = null

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_OK) {
        _result.success("Uninstalled")
      } else if (resultCode == Activity.RESULT_CANCELED) {
        _result.success("Cancelled")
      } else if (resultCode == Activity.RESULT_FIRST_USER) {
        _result.success("Cancelled")
      }
    }else if (requestCode == 0) {
    }
  }

  override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
      if (call.method == "startRummyDangal") {
        val uid = call.argument<String>("user_id")
        val uName = call.argument<String>("user_name")
        val assetImg = call.argument<String>("asset_image")
        val rummyListener: RummyListener = object : RummyListener {
          override fun lowBalance(lowAmount: Double) {
            RummyTLog.e("vikas", "Low balance listener calling amount =$lowAmount")
            RummyInstance.getInstance().close()
            result.success("Low Balance")
          }

          override fun onSdkCrash() {
            RummyTLog.e("vikas", "calling sdk crashed")
            result.success("SDK Crash")
          }

          override fun onResourceNotFound() {} 
        }
        if(BuildConfig.FLAVOR == "dev" || BuildConfig.FLAVOR == "dev_playstore") {
          RummyTLog.e("Received This UserId From Client:", uid);
          RummyInstance.getInstance().init(
            this@MainActivity,
            "aparna@dangalgames.com",
            "9953504756",
            uid,
            uName,
            "14",
            "https://dangalgames.com/",
            "staging",
            assetImg,
            rummyListener
          );
//          RummyInstance.getInstance().init(this@MainActivity, "aparna@dangalgames.com", "9953504756", uid, uName, "14", "https://dangalgames.com/", "test", assetImg, rummyListener,);
        }else {
          RummyInstance.getInstance().init(this@MainActivity, "support@dangalgames.com", "9999999999", uid, uName, "14", "https://dangalgames.com/", "prod", assetImg, rummyListener);
        }
        RummyInstance.getInstance().startSDK()
      } else if (call.method == "closeRummyDangal") {
        RummyInstance.getInstance().close()
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_LINK).setMethodCallHandler { call, result ->
      if (call.method == "initialLink") {
          if (startString != null) {
              result.success(startString)
          }
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_UNINSTALL).setMethodCallHandler { call, result ->
      if (call.method == "Uninstall") {
        _result = result

        val packageName = call.argument<String>("package")
        Log.e("Package Name", "$packageName")

        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = Uri.parse("package:$packageName")
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(intent, 1)
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_DIR).setMethodCallHandler { call, result ->
      if (call.method == "downloadDir") {
        val dataDir = context.applicationInfo.dataDir.toString() + "/files/dg_app"
        result.success(dataDir)
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_INSTALL).setMethodCallHandler { call, result ->
      if (call.method == "install") {
        _result = result

        val fileName = call.argument<String>("fileName")
        val packageName = call.argument<String>("packageName")

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
          Uri.fromFile(File("$path/$fileName")),packageName
        )
        val apkURI: Uri = FileProvider.getUriForFile(
          context, context.applicationContext
            .packageName.toString() + ".provider", File("$path/$fileName")
        )
        intent.data = apkURI
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(intent, 0)
      } else if (call.method == "installDG") {
        try{
          val fileName = call.argument<String>("fileName")
          val packageName = call.argument<String>("packageName")
          val path = call.argument<String>("path")

          Log.d("InstallDG", "$fileName $packageName $path")

          val file: File = File("$path/$fileName")
          if(file.exists()){
            Log.d("InstallDG", "File exists")
          }

          val intent = Intent(Intent.ACTION_VIEW)
          intent.setDataAndType(
            Uri.fromFile(file),packageName
          )

          Log.d("InstallDG", "Line 151")

          var androidAuthorityProvider = ""

          val component = ComponentName(context, FileProvider::class.java)
          val info = context.packageManager.getProviderInfo(component, android.content.pm.PackageManager.GET_META_DATA)
          val authority = info.authority

          Log.d("InstallDG", "$authority")

          androidAuthorityProvider = authority?.toString() ?: context.packageName.toString() + "." + "fileprovider"

          val apkURI: Uri = FileProvider.getUriForFile(
            context, androidAuthorityProvider, file
          )
          intent.data = apkURI
          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
          intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
          intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
          startActivity(intent)
          Log.d("InstallDG", "Finish")
        }catch (ex: Exception){
          Log.d("InstallDG", "Ex=$ex")
        }
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_DOWNLOAD).setMethodCallHandler { call, result ->
      if (call.method == "apkDownload") {
        val downloadUrl = call.argument<String>("downloadUrl")
        val gameName = call.argument<String>("gameName")
        val fileName = call.argument<String>("fileName")

        _result = result

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file: File = File("$path/$fileName")
        if(file.exists()){
          file.delete()
        }

        val downloadUri = Uri.parse(downloadUrl)
        val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("$gameName")
        request.setDescription("Downloading Resources")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        downloadId = downloadManager?.enqueue(request)
        result.success("downloadInitiated")
      } else if (call.method == "apkDownloadDg") {
        val downloadUrl = call.argument<String>("downloadUrl")
        val gameName = call.argument<String>("gameName")
        val fileName = call.argument<String>("fileName")
        val customFileName = call.argument<String>("customFileName")

        _result = result

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file: File = File("$path/$fileName")
        val customFile: File = File("$path/$customFileName")
        var filePermissionApplicable: Boolean = true
        if(file.exists()){
          file.delete()
          if (file.exists()){
            filePermissionApplicable = false
          }
        }
        if(customFile.exists()){
          customFile.delete()
        }

        val downloadUri = Uri.parse(downloadUrl)
        val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("$gameName")
        request.setDescription("Downloading Resources")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, if (filePermissionApplicable) fileName else customFileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        downloadId = downloadManager?.enqueue(request)
        result.success("$filePermissionApplicable")
      }
    }

    MethodChannel(flutterEngine.dartExecutor, CHANNEL_FB_EVENT).setMethodCallHandler { call, result ->
      if (call.method == "customEvents") {
        val eventName = call.argument<String>("eventName")
        AppEventsLogger.newLogger(context).logEvent(eventName)
      }

    }



    EventChannel(flutterEngine.dartExecutor, EVENTS).setStreamHandler(
      object : EventChannel.StreamHandler {
        override fun onListen(args: Any?, events: EventChannel.EventSink) {
          linksReceiver = createChangeReceiver(events)
        }

        override fun onCancel(args: Any?) {
          linksReceiver = null
        }
      }
    )

    EventChannel(flutterEngine.dartExecutor, DOWNLOAD_EVENTS).setStreamHandler(
      object : EventChannel.StreamHandler {
        override fun onListen(args: Any?, events: EventChannel.EventSink) {
          downloadJob = GlobalScope.launch {
            withContext(Dispatchers.Main){
              while (true) {
                val downloadQuery = DownloadManager.Query()
                downloadQuery.setFilterById(downloadId!!);
                val cursor: Cursor = downloadManager!!.query(downloadQuery)

                if (cursor!=null && cursor.moveToFirst()){
                  val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                  val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                  Log.d("DOWNLOAD","Downloaded: $bytesDownloaded")
                  Log.d("DOWNLOAD","Total: $bytesTotal")

                  events.success("$bytesDownloaded $bytesTotal")

                  val downloadStatus = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))

                  if (downloadStatus === DownloadManager.STATUS_SUCCESSFUL || downloadStatus === DownloadManager.STATUS_FAILED) {
                    cursor.close()
                    break
                  }
                }

                delay(2000)
              }
            }
          }
        }

        override fun onCancel(args: Any) {
          if (downloadJob!=null) {
            downloadJob?.cancel()
          }
        }
      }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val window: Window = activity.getWindow()
// clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
// finally change the color
    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.black))

    val intent = getIntent()
    startString = intent.data?.toString()
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    SingularBridge.onNewIntent(intent)
    if (intent.action === Intent.ACTION_VIEW) {
      linksReceiver?.onReceive(this.applicationContext, intent)
    }
  }

  fun createChangeReceiver(events: EventChannel.EventSink): BroadcastReceiver? {
    return object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) { // NOTE: assuming intent.getAction() is Intent.ACTION_VIEW
        val dataString = intent.dataString ?:
        events.error("UNAVAILABLE", "Link unavailable", null)
        events.success(dataString)
      }
    }
  }
}
