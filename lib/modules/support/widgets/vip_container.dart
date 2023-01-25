import 'package:dangal_games_demo/constants/methods/common_methods.dart';
import 'package:flutter/material.dart';

import '../../../constants/color_constants.dart';
import 'package:url_launcher/url_launcher.dart';

class VipContainer extends StatelessWidget {
  const VipContainer({
    Key key,
    @required this.supportContactNumber,
  }) : super(key: key);
  final String supportContactNumber;
  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.topCenter,
      children: [
        Container(
          padding: EdgeInsets.only(
            top: 22,
          ),
          child: Container(
            width: double.infinity,
            margin: EdgeInsets.symmetric(
              horizontal: 34,
            ),
            decoration: BoxDecoration(
              border: Border.all(
                color: Color(
                  0xffC1AE00,
                ),
              ),
              borderRadius: BorderRadius.all(
                Radius.circular(
                  10.0,
                ),
              ),
            ),
            child: Padding(
              padding: const EdgeInsets.symmetric(
                vertical: 36.0,
                horizontal: 20,
              ),
              child: (supportContactNumber != null)
                  ? Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'You have got our attention, you now have a dedicated Key Account Manager!',
                          style: TextStyle(
                            color: ColorConstants.white,
                            fontWeight: FontWeight.w500,
                            fontSize: 14,
                            fontStyle: FontStyle.italic,
                          ),
                        ),
                        SizedBox(
                          height: 12,
                        ),
                        GestureDetector(
                          onTap: () async {
                            await _launchCaller(
                              supportContactNumber: supportContactNumber,
                              context: context,
                            );
                          },
                          child: Text(
                            'Phone: $supportContactNumber',
                            style: TextStyle(
                              color: ColorConstants.white,
                              fontWeight: FontWeight.w500,
                              fontSize: 16,
                              fontStyle: FontStyle.italic,
                            ),
                          ),
                        )
                      ],
                    )
                  : Center(
                      child: Text(
                        'Play more to unlock VIP Support',
                        style: TextStyle(
                          color: ColorConstants.white,
                          fontWeight: FontWeight.w500,
                          fontSize: 14,
                          fontStyle: FontStyle.italic,
                        ),
                      ),
                    ),
            ),
          ),
        ),
        Container(
          decoration: BoxDecoration(
            color: ColorConstants.kBackgroundColor,
            gradient: ColorConstants.greyGradient,
            border: Border.all(
              color: ColorConstants.golderBorder,
            ),
            borderRadius: BorderRadius.all(
              Radius.circular(
                10.0,
              ),
            ),
          ),
          child: Container(
            padding: const EdgeInsets.all(12.0),
            child: Text(
              'VIP Support',
              style: TextStyle(
                color: ColorConstants.white,
                fontWeight: FontWeight.w700,
                fontSize: 16,
              ),
            ),
          ),
        ),
      ],
    );
  }

  //METHODS
  _launchCaller({
    @required String supportContactNumber,
    @required BuildContext context,
  }) async {
    final url = Uri.encodeFull(
      'tel:$supportContactNumber',
    );

    try {
      if (await canLaunchUrl(
        Uri.parse(
          url,
        ),
      )) {
        await launchUrl(
          Uri.parse(
            url,
          ),
        );
      } else {
        throw 'Could not launch $url';
      }
    } catch (e) {
      CommonMethods.showSnackBar(
        context,
        'Something Went Wrong',
      );
    }
  }
}
