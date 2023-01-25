import 'app_constants.dart';
import 'string_constants.dart';

class Environment {
  //DEVELOP Environment
  static const String devURL = 'https://apidev.dangalgames.com';
  static const Map<String, dynamic> devEnvVariables = {
    StringConstants.envKey: StringConstants.envDev,
    StringConstants.envTypeKey: AppConstants.envTypeDev,
    'paytm_mid': 'XETAGA23602160214059',
    'razorpay_key': 'rzp_test_wh9tajsjEJQU2x',
    'paytm_callback_url':
        'https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage?ORDER_ID=',
    'base_url': devURL,
    'paytm_is_staging': true,
    'callbreakToken':
        '2b\$10\$niJ3HwwC1o9cYs0CwQRBU.Tp0SecQipZWmKRGfutsDCMMKWi7MqUO',
    'callbreakUrl': devURL + '/v1/artoon/game/lobby/callbreak',
    'ball8Url': devURL + '/v1/artoon/game/lobby/pool',
    'streetRacerUrl': devURL + "/v1/workloopers/games/lobby",
    'webSocketUrl':
        'ws://k8s-default-websocke-9a05da360e-5593631d1b59a70d.elb.ap-south-1.amazonaws.com:9090/ws',
    'packageName': 'com.example.dangal_games_demo',
    'apkPathName': 'dangal_games_stage_update.apk',
    'cashfree_stage': 'TEST',
    'showlogs': true,
    'callbreakDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/CallBreak+Staging/CallBreak_staging(V3.2).apk',
    'ballPoolDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/8+Ball+Pool+Staging/8BallPool(09-08-2021)_Dev1.5.apk',
    'callbreakApk': 'CallBreak_staging(V3.2).apk',
    'ballPoolApk': '8BallPool(09-08-2021)_Dev1.5.apk',
    'streetRacerApk': 'street_racer_stage.apk',
    'fruitSplitUrl': devURL + "/v1/workloopers/games/fruit-split/lobby",
    'fruitSplitApk': 'fruit_split_stage.apk',
    'sumoRunnerUrl': devURL + "/v1/workloopers/games/runner-number1/lobby",
    'sumoRunnerApk': 'runner_number_one_stage.apk',
    'bubbleShooterUrl': devURL + "/v1/workloopers/games/bubble-shooter/lobby",
    'bubbleShooterApk': 'bubble_shooter_stage.apk',
    'knifeCutUrl': devURL + "/v1/workloopers/games/knife-cut/lobby",
    'knifeCutApk': 'knife_hit_stage.apk',
    'archeryUrl': devURL + "/v1/workloopers/games/archery/lobby",
    'archeryApk': 'archery_stage.apk',
    'carromUrl': devURL + "/v1/workloopers/games/carrom/lobby",
    'carromApk': 'carrom_stage.apk',
    'candyCrushUrl': devURL + "/v1/workloopers/games/candy-crush/lobby",
    'candyCrushApk': 'candy_clash_stage.apk',
    'ludoUrl': devURL + "/v1/workloopers/games/ludo/lobby",
    'ludoApk': "ludo_stage.apk",
    StringConstants.envV8Key: false,
  };

  //PRODUCTION ENVIRONMENT (.COM)
  static const String prodURL = "https://api.dangalgames.com";
  static const Map<String, dynamic> prodEnvVariables = {
    StringConstants.envKey: StringConstants.envProd,
    StringConstants.envTypeKey: AppConstants.envTypeProd,
    'paytm_mid': 'XETAGA07436606741805',
    'razorpay_key': 'rzp_live_u0CNnCNmTOYzgX',
    'paytm_callback_url':
        'https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=',
    'base_url': prodURL,
    'paytm_is_staging': false,
    'callbreakToken':
        '2b\$10\$Jcmont3Ch3lHgoe3jyV.Devx8Mz/vUXm2E.8gzIcTRRrSZ9kd1Ic6',
    'callbreakUrl': prodURL + '/v1/artoon/game/lobby/callbreak',
    'ball8Url': prodURL + '/v1/artoon/game/lobby/pool',
    'streetRacerUrl': prodURL + "/v1/workloopers/games/lobby",
    'webSocketUrl': 'ws://websocket.dangalgames.com:9091/ws',
    'packageName': 'com.app.dangalgames',
    'apkPathName': 'dangal_games_update_app.apk',
    'cashfree_stage': "PROD",
    'showlogs': false,
    'callbreakDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/callbreak/CallBreak_live(16-7-21).apk',
    'ballPoolDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/8-ballpool-game/pool_live(16-07-2021).apk',
    'callbreakApk': 'CallBreak_live(16-7-21).apk',
    'ballPoolApk': 'pool_live(16-07-2021).apk',
    'streetRacerApk': 'street_racer.apk',
    'fruitSplitUrl': prodURL + "/v1/workloopers/games/fruit-split/lobby",
    'fruitSplitApk': 'fruit_split.apk',
    'sumoRunnerUrl': prodURL + "/v1/workloopers/games/runner-number1/lobby",
    'sumoRunnerApk': 'runner_number_one.apk',
    'bubbleShooterUrl': prodURL + "/v1/workloopers/games/bubble-shooter/lobby",
    'bubbleShooterApk': 'bubble_shooter.apk',
    'knifeCutUrl': prodURL + "/v1/workloopers/games/knife-cut/lobby",
    'knifeCutApk': 'knife_hit.apk',
    'archeryUrl': prodURL + "/v1/workloopers/games/archery/lobby",
    'archeryApk': 'archery.apk',
    'carromUrl': prodURL + "/v1/workloopers/games/carrom/lobby",
    'carromApk': 'carrom.apk',
    'candyCrushUrl': prodURL + "/v1/workloopers/games/candy-crush/lobby",
    'candyCrushApk': 'candy_clash_stage.apk',
    'ludoUrl': prodURL + "/v1/workloopers/games/ludo/lobby",
    'ludoApk': 'ludo.apk',
    StringConstants.envV8Key: false,
  };

  //PRODUCTION ENVIRONMENT (CO.IN)
  static const String prodCoDotInURL = "https://api.dangalgames.com";
  static const Map<String, dynamic> prodCoDotInEnvVariables = {
    StringConstants.envKey: StringConstants.envProdCoIn,
    StringConstants.envTypeKey: AppConstants.envTypeProdCoIn,
    StringConstants.isPokerKey: false,
    'paytm_mid': 'XETAGA07436606741805',
    'razorpay_key': 'rzp_live_u0CNnCNmTOYzgX',
    'paytm_callback_url':
        'https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=',
    'base_url': prodCoDotInURL,
    'paytm_is_staging': false,
    'callbreakToken':
        '2b\$10\$Jcmont3Ch3lHgoe3jyV.Devx8Mz/vUXm2E.8gzIcTRRrSZ9kd1Ic6',
    'callbreakUrl': prodCoDotInURL + '/v1/artoon/game/lobby/callbreak',
    'ball8Url': prodCoDotInURL + '/v1/artoon/game/lobby/pool',
    'streetRacerUrl': prodCoDotInURL + "/v1/workloopers/games/lobby",
    'webSocketUrl': 'ws://websocket.dangalgames.com:9091/ws',
    'packageName': 'com.app.dangalgames.co.in',
    'apkPathName': 'dangal_games_update_app.apk',
    'cashfree_stage': "PROD",
    'showlogs': false,
    'callbreakDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/callbreak/CallBreak_live(16-7-21).apk',
    'ballPoolDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/8-ballpool-game/pool_live(16-07-2021).apk',
    'callbreakApk': 'CallBreak_live(16-7-21).apk',
    'ballPoolApk': 'pool_live(16-07-2021).apk',
    'streetRacerApk': 'street_racer.apk',
    'fruitSplitUrl': prodCoDotInURL + "/v1/workloopers/games/fruit-split/lobby",
    'fruitSplitApk': 'fruit_split.apk',
    'sumoRunnerUrl':
        prodCoDotInURL + "/v1/workloopers/games/runner-number1/lobby",
    'sumoRunnerApk': 'runner_number_one.apk',
    'bubbleShooterUrl':
        prodCoDotInURL + "/v1/workloopers/games/bubble-shooter/lobby",
    'bubbleShooterApk': 'bubble_shooter.apk',
    'knifeCutUrl': prodCoDotInURL + "/v1/workloopers/games/knife-cut/lobby",
    'knifeCutApk': 'knife_hit.apk',
    'archeryUrl': prodCoDotInURL + "/v1/workloopers/games/archery/lobby",
    'archeryApk': 'archery.apk',
    'carromUrl': prodCoDotInURL + "/v1/workloopers/games/carrom/lobby",
    'carromApk': 'carrom.apk',
    'candyCrushUrl': prodCoDotInURL + "/v1/workloopers/games/candy-crush/lobby",
    'candyCrushApk': 'candy_clash_stage.apk',
    'ludoUrl': prodCoDotInURL + "/v1/workloopers/games/ludo/lobby",
    'ludoApk': 'ludo.apk',
    StringConstants.envV8Key: false,
  };

  //NON-PROD PLAY-STORE Environment
  static const String devPlayStoreURL = 'https://apidev.dangalgames.com';
  static const Map<String, dynamic> devPlayStoreEnvVariables = {
    StringConstants.envKey: StringConstants.envDevPlayStore,
    StringConstants.envTypeKey: AppConstants.envTypeDevPlayStore,
    'paytm_mid': 'XETAGA23602160214059',
    'razorpay_key': 'rzp_test_wh9tajsjEJQU2x',
    'paytm_callback_url':
        'https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage?ORDER_ID=',
    'base_url': devPlayStoreURL,
    'paytm_is_staging': true,
    'callbreakToken':
        '2b\$10\$niJ3HwwC1o9cYs0CwQRBU.Tp0SecQipZWmKRGfutsDCMMKWi7MqUO',
    'callbreakUrl': devPlayStoreURL + '/v1/artoon/game/lobby/callbreak',
    'ball8Url': devPlayStoreURL + '/v1/artoon/game/lobby/pool',
    'streetRacerUrl': devPlayStoreURL + "/v1/workloopers/games/lobby",
    'webSocketUrl':
        'ws://a6ca79d62067f44b9877b3ba8ff300a0-26613580.ap-south-1.elb.amazonaws.com:9090/ws',
    'packageName': 'com.devapp.play.dangalgames',
    'apkPathName': 'dangal_games_stage_update.apk',
    'cashfree_stage': 'TEST',
    'showlogs': true,
    'callbreakDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/CallBreak+Staging/CallBreak_staging(V3.2).apk',
    'ballPoolDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/8+Ball+Pool+Staging/8BallPool(09-08-2021)_Dev1.5.apk',
    'callbreakApk': 'CallBreak_staging(V3.2).apk',
    'ballPoolApk': '8BallPool(09-08-2021)_Dev1.5.apk',
    'streetRacerApk': 'street_racer_stage.apk',
    'fruitSplitUrl':
        devPlayStoreURL + "/v1/workloopers/games/fruit-split/lobby",
    'fruitSplitApk': 'fruit_split_stage.apk',
    'sumoRunnerUrl':
        devPlayStoreURL + "/v1/workloopers/games/runner-number1/lobby",
    'sumoRunnerApk': 'runner_number_one_stage.apk',
    'bubbleShooterUrl':
        devPlayStoreURL + "/v1/workloopers/games/bubble-shooter/lobby",
    'bubbleShooterApk': 'bubble_shooter_stage.apk',
    'knifeCutUrl': devPlayStoreURL + "/v1/workloopers/games/knife-cut/lobby",
    'knifeCutApk': 'knife_hit_stage.apk',
    'archeryUrl': devPlayStoreURL + "/v1/workloopers/games/archery/lobby",
    'archeryApk': 'archery_stage.apk',
    'carromUrl': devPlayStoreURL + "/v1/workloopers/games/carrom/lobby",
    'carromApk': 'carrom_stage.apk',
    'candyCrushUrl':
        devPlayStoreURL + "/v1/workloopers/games/candy-crush/lobby",
    'candyCrushApk': 'candy_clash_stage.apk',
    'ludoUrl': devPlayStoreURL + "/v1/workloopers/games/ludo/lobby",
    'ludoApk': "ludo_stage.apk",
    StringConstants.envV8Key: false,
  };

  //PROD PLAY-STORE Environment
  static const String prodPlayStoreURL = "https://api.dangalgames.com";
  static const Map<String, dynamic> prodPlayStoreEnvVariables = {
    StringConstants.envKey: StringConstants.envProdPlayStore,
    StringConstants.envTypeKey: AppConstants.envTypeProdPlayStore,
    'paytm_mid': 'XETAGA07436606741805',
    'razorpay_key': 'rzp_live_u0CNnCNmTOYzgX',
    'paytm_callback_url':
        'https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=',
    'base_url': prodPlayStoreURL,
    'paytm_is_staging': false,
    'callbreakToken':
        '2b\$10\$Jcmont3Ch3lHgoe3jyV.Devx8Mz/vUXm2E.8gzIcTRRrSZ9kd1Ic6',
    'callbreakUrl': prodPlayStoreURL + '/v1/artoon/game/lobby/callbreak',
    'ball8Url': prodPlayStoreURL + '/v1/artoon/game/lobby/pool',
    'streetRacerUrl': prodPlayStoreURL + "/v1/workloopers/games/lobby",
    'webSocketUrl': 'ws://websocket.dangalgames.com:9091/ws',
    'packageName': 'com.app.play.dangalgames',
    'apkPathName': 'dangal_games_update_app.apk',
    'cashfree_stage': "PROD",
    'showlogs': false,
    'callbreakDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/callbreak/CallBreak_live(16-7-21).apk',
    'ballPoolDownloadUrl':
        'https://artoon-apps.s3.ap-south-1.amazonaws.com/8-ballpool-game/pool_live(16-07-2021).apk',
    'callbreakApk': 'CallBreak_live(16-7-21).apk',
    'ballPoolApk': 'pool_live(16-07-2021).apk',
    'streetRacerApk': 'street_racer.apk',
    'fruitSplitUrl':
        prodPlayStoreURL + "/v1/workloopers/games/fruit-split/lobby",
    'fruitSplitApk': 'fruit_split.apk',
    'sumoRunnerUrl':
        prodPlayStoreURL + "/v1/workloopers/games/runner-number1/lobby",
    'sumoRunnerApk': 'runner_number_one.apk',
    'bubbleShooterUrl':
        prodPlayStoreURL + "/v1/workloopers/games/bubble-shooter/lobby",
    'bubbleShooterApk': 'bubble_shooter.apk',
    'knifeCutUrl': prodPlayStoreURL + "/v1/workloopers/games/knife-cut/lobby",
    'knifeCutApk': 'knife_hit.apk',
    'archeryUrl': prodPlayStoreURL + "/v1/workloopers/games/archery/lobby",
    'archeryApk': 'archery.apk',
    'carromUrl': prodPlayStoreURL + "/v1/workloopers/games/carrom/lobby",
    'carromApk': 'carrom.apk',
    'candyCrushUrl':
        prodPlayStoreURL + "/v1/workloopers/games/candy-crush/lobby",
    'candyCrushApk': 'candy_clash_stage.apk',
    'ludoUrl': prodPlayStoreURL + "/v1/workloopers/games/ludo/lobby",
    'ludoApk': 'ludo.apk',
    StringConstants.envV8Key: false,
  };
}
