import '../../../constants/color_constants.dart';
import '../../../constants/methods/common_methods.dart';
import '../../../constants/string_constants.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CopyReferralCodeWidget extends StatelessWidget {
  const CopyReferralCodeWidget({
    Key key,
    @required this.referralCode,
  }) : super(key: key);

  final String referralCode;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: 42,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //Your Referral Code Text
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8.0),
              child: Text(
                StringConstants.yourReferralCode,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontSize: 12.0,
                  fontWeight: FontWeight.w300,
                  color: ColorConstants.white,
                ),
              ),
            ),
            SizedBox(
              height: 8,
            ),
            //Refarral Code Copy Widget
            Container(
              height: 48,
              width: 240,
              padding: EdgeInsets.symmetric(
                horizontal: 25,
              ),
              decoration: BoxDecoration(
                border: Border.all(
                  color: ColorConstants.greyBorderColor,
                ),
                borderRadius: BorderRadius.circular(
                  10,
                ),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    referralCode.toUpperCase(),
                    style: TextStyle(
                      fontWeight: FontWeight.w600,
                      fontSize: 15,
                      color: ColorConstants.white,
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      if (referralCode == '-') {
                        CommonMethods.showSnackBar(
                          context,
                          StringConstants.serverErrorCannotCopy,
                        );
                      } else {
                        Clipboard.setData(
                          ClipboardData(
                            text: referralCode,
                          ),
                        );
                        CommonMethods.showSnackBar(
                          context,
                          StringConstants.copiedToClipboard,
                        );
                      }
                    },
                    child: Icon(
                      Icons.copy,
                      color: Colors.white,
                    ),
                  )
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
