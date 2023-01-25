import 'package:flutter/material.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:sizer/sizer.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';
import '../../../constants/app_dimens.dart';

class DocumentStatus extends StatelessWidget {
  const DocumentStatus({
    @required this.status,
    @required this.statusText,
    @required this.reason,
    @required this.isInfo,
    @required this.onTap,
    Key key}) : super(key: key);
  final String status;
  final String statusText;
  final String reason;
  final bool isInfo;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(
          "kyc_screen.document_status".tr(),
          style: TextStyle(
            color: ColorConstants.white,
            fontSize: 8.5.sp,
            fontWeight: FontWeight.bold,
          ),
        ),
        Flexible(
          child: Text(
            statusText,
            style: TextStyle(
              color: (status ==
                  StringConstants.approved)
                  ? ColorConstants.green
                  : ColorConstants.red,
              fontWeight: FontWeight.bold,
              fontSize: 8.5.sp,
            ),
          ),
        ),
        Visibility(
          visible: isInfo,
          child: GestureDetector(
            onTap: () {
              onTap();
              showDialog(
                context: context,
                builder:
                    (BuildContext context) =>
                    Dialog(
                      elevation: 40,
                      backgroundColor:
                      ColorConstants
                          .transparent,
                      child: Container(
                        height: 30.0.h,
                        decoration: BoxDecoration(
                          borderRadius:
                          BorderRadius.circular(
                            AppDimens
                                .circularBorder,
                          ),
                          color:
                          ColorConstants.white,
                        ),
                        child: Center(
                          child: Padding(
                            padding:
                            const EdgeInsets
                                .all(
                              10,
                            ),
                            child: Column(
                              mainAxisAlignment:
                              MainAxisAlignment
                                  .center,
                              crossAxisAlignment:
                              CrossAxisAlignment
                                  .center,
                              children: [
                                Text(
                                  reason,
                                  textAlign:
                                  TextAlign
                                      .center,
                                  style: TextStyle(
                                    fontWeight:
                                    FontWeight
                                        .bold,
                                    fontSize:
                                    14.0.sp,
                                  ),
                                ),
                                SizedBox(
                                  height: 1.0.h,
                                ),
                                GestureDetector(
                                  onTap: () {
                                    Navigator.pop(
                                        context);
                                  },
                                  child: Container(
                                    height: 4.5.h,
                                    width: 23.0.w,
                                    decoration:
                                    BoxDecoration(
                                      gradient:
                                      ColorConstants
                                          .blueDarkGradient,
                                      borderRadius:
                                      BorderRadius
                                          .circular(
                                        AppDimens
                                            .circularBorder,
                                      ),
                                    ),
                                    child: Center(
                                      child: Text(
                                        StringConstants
                                            .ok,
                                        style:
                                        TextStyle(
                                          color: ColorConstants
                                              .white,
                                          fontWeight:
                                          FontWeight
                                              .bold,
                                          fontSize:
                                          9.0.sp,
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
              );
            },
            child: Icon(
              Icons.info_outlined,
            ),
          ),
        ),
      ],
    );
  }
}
