import 'package:flutter/material.dart';
import 'package:social_share/social_share.dart';

import '../../../constants/asset_paths.dart';
import '../../../constants/enum.dart';
import '../../../constants/methods/common_methods.dart';
import '../../../constants/string_constants.dart';
import 'grid_view_icon.dart';

class ShareIcon extends StatelessWidget {
  const ShareIcon({
    Key key,
    @required this.inviteMessage,
    @required this.shareIconType,
  }) : super(key: key);

  final String inviteMessage;
  final ShareIconType shareIconType;

  @override
  Widget build(BuildContext context) {
    return GridViewIcon(
      onTap: () async {
        var installedApps = await SocialShare.checkInstalledAppsForShare();
        if (shareIconType == ShareIconType.whatsapp) {
          if (installedApps['whatsapp'] == false) {
            CommonMethods.showSnackBar(
              context,
              StringConstants.whatsappNotInstalled,
            );
          } else {
            await SocialShare.shareWhatsapp(
              inviteMessage,
            );
          }
        } else if (shareIconType == ShareIconType.telegram) {
          if (installedApps['telegram'] == false) {
            CommonMethods.showSnackBar(
              context,
              StringConstants.telegramNotInstalled,
            );
          } else {
            await SocialShare.shareTelegram(
              inviteMessage,
            );
          }
        } else if (shareIconType == ShareIconType.messages) {
          if (installedApps['sms'] == false) {
            CommonMethods.showSnackBar(
              context,
              StringConstants.contactsAppNotInstalled,
            );
          } else {
            await SocialShare.shareSms(
              inviteMessage,
            );
          }
        } else {
          await SocialShare.shareOptions(
            inviteMessage,
          );
        }
      },
      assetPath: (shareIconType == ShareIconType.whatsapp)
          ? AssetPaths.whatsapp
          : (shareIconType == ShareIconType.telegram)
              ? AssetPaths.telegram
              : (shareIconType == ShareIconType.messages)
                  ? AssetPaths.messages
                  : AssetPaths.share,
    );
  }
}
