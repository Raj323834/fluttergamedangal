class RegExpMethods {
  static final RegExp alphabets = RegExp(
    "[a-zA-Z]",
  );

  static final RegExp digits = RegExp(
    "[0-9]",
  );

  static final RegExp alphaNumeric = RegExp(
    "[0-9a-zA-Z]",
  );

  static final RegExp panNo = RegExp(
    r"^([a-zA-Z]){5}[0-9]{4}[A-Z]{1}$",
  );

  static final RegExp aadhaarNo = RegExp(
    r'^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$',
  );

  static final RegExp voterNo = RegExp(
    r'^([a-zA-Z]){3}([0-9]){7}$',
  );

  static final RegExp dl = RegExp(
    "[0-9a-zA-Z-]",
  );
}
