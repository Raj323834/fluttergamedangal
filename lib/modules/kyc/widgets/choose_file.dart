import 'dart:io';

import '../../../common_widgets/custom_button.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';
import '../../../constants/app_dimens.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class ChooseFile extends StatelessWidget {
  const ChooseFile({
    @required this.file,
    @required this.url,
    @required this.allowedDoc,
    @required this.fileSizeMax,
    @required this.onTap,
    Key key,
  }) : super(key: key);

  final File file;
  final String url;
  final String allowedDoc;
  final String fileSizeMax;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              file != null || url.length > 0
                  ? GestureDetector(
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) => Center(
                      child: Container(
                        child: Stack(
                          children: [
                            SizedBox(
                              height: 300,
                              child: file != null
                                  ? Image.file(
                                file,
                              )
                                  : Image.network(
                                url,
                                loadingBuilder: (
                                    BuildContext context,
                                    Widget child,
                                    ImageChunkEvent loadingProgress,
                                    ) {
                                  if (loadingProgress == null)
                                    return child;
                                  return Center(
                                    child: CircularProgressIndicator(
                                      backgroundColor:
                                      ColorConstants.blue,
                                      value: loadingProgress
                                          .expectedTotalBytes !=
                                          null
                                          ? loadingProgress
                                          .cumulativeBytesLoaded /
                                          loadingProgress
                                              .expectedTotalBytes
                                          : null,
                                    ),
                                  );
                                },
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
                child: Text(
                  "kyc_screen.view_file".tr(),
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 8.5.sp,
                    fontWeight: FontWeight.w700,
                    color: ColorConstants.viewFileCol,
                  ),
                ),
              )
                  : Text(
                StringConstants.noFileChosen,
                textAlign: TextAlign.start,
                style: TextStyle(
                  fontSize: 8.5.sp,
                  fontWeight: FontWeight.bold,
                  color: ColorConstants.white.withOpacity(0.5),
                ),
              ),
              SizedBox(
                height: 0.4.h,
              ),
              Text(
                "kyc_screen.file_must_be".tr(
                  args: [
                    allowedDoc,
                    (int.parse(fileSizeMax) ~/ 1024).toInt().toString()
                  ],
                ),
                style: TextStyle(
                  fontSize: 5.8.sp,
                  fontWeight: FontWeight.bold,
                  color: ColorConstants.white.withOpacity(0.5),
                ),
              ),
            ],
          ),
        ),
        SizedBox(
          width: 0.7.w,
        ),
        Expanded(
          child: CustomButton(
            height: AppDimens.textFieldH,
            width: 26.2.w,
            fontSize: 9.0.sp,
            fontWeight: FontWeight.w500,
            alignment: Alignment(-0.5, 0.0,),
            gradient: ColorConstants.goldGradientLight,
            buttonText: "kyc_screen.choose_file".tr(),
            onTap: onTap,),
        ),
      ],
    );
  }
}