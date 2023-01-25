
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sizer/sizer.dart';

import '../../constants/string_constants.dart';
import '../constants/app_dimens.dart';
import '../constants/color_constants.dart';

class CustomTextFormField extends StatelessWidget {
  const CustomTextFormField({
    @required this.controller,
    this.isForm=false,
    this.isAutoValidate=false,
    this.validator,
    this.formKey,
    this.regExp,
    this.regExpAllow = true,
    this.maxLength,
    this.keyboardType,
    this.readOnly = false,
    this.autofocus = false,
    this.hintText,
    this.hintTextColor=ColorConstants.textFieldTextCol,
    this.inputTextColor=ColorConstants.textFieldTextCol,
    this.fillColor = false,
    this.suffix=const SizedBox(),
    this.counterText = StringConstants.emptyString,
    this.textCapitalization = TextCapitalization.characters,
    this.onChanged,
    Key key,
  }) : super(key: key);

  const CustomTextFormField.withForm({
    @required this.controller,
    this.isForm=true,
    this.isAutoValidate=true,
    @required this.formKey,
    @required this.validator,
    this.regExp,
    this.regExpAllow = true,
    this.maxLength,
    this.keyboardType,
    this.readOnly = false,
    this.fillColor = false,
    this.autofocus = false,
    this.hintText,
    this.hintTextColor=ColorConstants.textFieldTextCol,
    this.inputTextColor=ColorConstants.textFieldTextCol,
    this.suffix=const SizedBox(),
    this.counterText = StringConstants.emptyString,
    this.textCapitalization = TextCapitalization.characters,
    this.onChanged,
    Key key,
  }) : super(key: key);

  final bool isForm;
  final bool isAutoValidate;
  final Key formKey;
  final FormFieldValidator<String> validator;
  final TextEditingController controller;
  final int maxLength;
  final TextInputType keyboardType;
  final bool readOnly;
  final bool fillColor;
  final bool autofocus;
  final String hintText;
  final Color inputTextColor;
  final Color hintTextColor;
  final String counterText;
  final RegExp regExp;
  final bool regExpAllow;
  final Widget suffix;
  final TextCapitalization textCapitalization;
  final ValueChanged onChanged;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: boxDecoration(),
      child: Row(
        children: [
          Expanded(
              child:
              isForm?
              Form(
                autovalidateMode: isAutoValidate
                    ? AutovalidateMode.always
                    : AutovalidateMode.disabled,
                key: formKey,
                child: textFormField(),
              )
            : textFormField()
            ,),
          suffix
        ],
      ),
    );
  }

  TextFormField textFormField() => TextFormField(
    inputFormatters: regExp == null
        ? null
        : [
      FilteringTextInputFormatter(regExp, allow: regExpAllow),
    ],
    textCapitalization: textCapitalization,
    autofocus: autofocus,
    readOnly: readOnly,
    onChanged: onChanged,
    maxLength: maxLength,
    controller: controller,
    keyboardType: keyboardType,
    style: textStyle(inputTextColor),
    validator:validator,
    decoration: buildInputDecoration(),
  );

  InputDecoration buildInputDecoration() =>  InputDecoration(
      counterText: counterText,
      // fillColor: Colors.white,
      // filled: true,
      hintText: hintText,
      hintStyle:  textStyle(hintTextColor),
      contentPadding: EdgeInsets.all(1.5.h),
      border: outlineInputBorder(),
      focusedBorder: outlineInputBorder(),
      enabledBorder: outlineInputBorder()
  );

  OutlineInputBorder outlineInputBorder() => OutlineInputBorder(
    borderSide: BorderSide.none,
    borderRadius: BorderRadius.all(Radius.circular(AppDimens.circularBorder)),
  );

  BoxDecoration boxDecoration() => BoxDecoration(
        borderRadius: BorderRadius.circular(AppDimens.circularBorder),
        border:
            Border.all(color: ColorConstants.textFieldBorderCol, width: 0.15.h),
        color: readOnly && fillColor ? Colors.grey[300] : ColorConstants.white
      );

  TextStyle textStyle(Color color) {
    return TextStyle(
        fontSize: AppDimens.fontSize,
        color: color,
        fontWeight: FontWeight.w500);
  }
}
