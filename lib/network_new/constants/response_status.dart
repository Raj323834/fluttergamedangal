class ResponseStatus {
  static const String success = 'SUCCESS';
  static const String supportContactNotAvailable =
      'SUPPORT_CONTACT_NUMBER_NOT_AVAILABLE';
  static const String dbError = 'DB_ERROR';
  static const String dbOperationFailed = 'DB_OPERATION_FAILED';
  static const String tokenExpired = 'TOKEN_EXPIRED';
  static const String tokenParsingFailed = "TOKEN_PARSING_FAILED";
  static const String noBonusExist = 'NO_BONUS_EXIST';
  static const String notFound = 'NOT_FOUND';
  static const String leaderboardNotFound = 'LEADERBOARD_NOT_FOUND';
  static const String userNotQualified = "USER_NOT_QUALIFIED";
  static const String invalidDateRange = 'INVALID_DATE_RANGE';
  static const String userNotExist = 'USER_NOT_EXIST';
  static const String noDepositsExist = 'NO_DEPOSITS_EXIST';
  static const String noWithdrawalsExist = 'NO_WITHDRAWALS_EXIST';
  static const String userDoesNotExist = 'USER_DOES_NOT_EXIST';
  static const String restrictedActivity = 'RESTRICTED_ACTIVITY';
  static const String kycNotVerified = 'KYC_NOT_VERIFIED';
  static const String noSuccessfulWithdrawalExist =
      'NO_SUCCESSFUL_WITHDRAWAL_EXIST';
  static const String upsNotReachable = 'UPS_NOT_REACHABLE';
  static const String walletServiceNotReachable =
      'WALLET_SERVICE_NOT_REACHABLE';
  static const String walletDoesNotExist = 'WALLET_DOES_NOT_EXIST';
  static const String userNotFound = 'USER_NOT_FOUND';

  //
  static const String account = 'ACCOUNT';
  static const String upi = 'UPI';
}
