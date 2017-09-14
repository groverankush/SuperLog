# SuperLog

[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/groverankush/SuperLog) 
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/groverankush/SuperLog/blob/master/LICENSE)


SuperLog is a logger for Android Apps. It has always been a trouble to write logs in a file and console seperately. After Marshmallow apps need to ask for explicit permission for accessing file storage. Some apps demand file access but some doesn't. Creating an extra file to maintain logs forces developers to ask permission everytime. SuperLogs allows users to create console logs and persistent logs just with one statement. The logs generated by SuperLog can also be viewed in the app itself. It allows developers focussing on IoT or Android things apps to directly view the logs in the device itself. The logs genertaed by SuperLog can be mailed with developer's ease.   

<img src="https://github.com/groverankush/SuperLog/blob/master/pictures/SuperLog.gif" width="250"/>


## Getting Started

These are the all the instructions you need to use SuperLog in your apps.

### Compile

Just a simple debug `debugCompile` will get you up and running with SuperLog.

```
debugCompile 'com.ankushgrover:SuperLog:1.0.9'
```
`debugCompile` will make sure that the library is used in debug build only.

### Initialize

It's preferable to initialize SuperLog in your application's `onCreate()` method.

```
SuperLog.init(new SuperLog.Builder(getApplication())
        .setSuperLogViewVisibility(true) \\For globally changing SuperLogView's visibility
        .setCredentials("YOUR_GMAIL_ADDRESS", "YOUR_PASSWORD")); \\For sending email in background
```
For sending mails in the background, you need to specify email address and password of the account. As the library uses explicit SMTP server for sending mails in background, so for that account credentials are required.
IF YOU ARE USING THIS FEATURE, IT IS HIGHLY RECOMMENDED TO USE A TEST ACCOUNT IN PLACE OF YOUR PRIMARY ACCOUNT. YOU ALSO NEED TO ALLOW ACCESS TO LESS SECURE APPS FOR YOUR GMAIL ACCOUNT. More about this [here](https://support.google.com/accounts/answer/6010255?hl=en) and [here](https://myaccount.google.com/lesssecureapps). 

### Add View

After initialization you can simply add `SuperLogView` in any of your layout files. On clicking this view, you will be presented with all the logs created by SuperLog in a color coded format along with some extra functionality.

```
    <com.ankushgrover.superlog.views.SuperLogView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:textStyle="bold" />
```

### Examples

You can use the SuperLog fuctions as specified here.

```
SuperLog.d("TAG", "message"); // debug log
SuperLog.v("TAG", "message"); // verbose log
SuperLog.w("TAG", "message"); // warning log
SuperLog.e("TAG", "message"); // error log
SuperLog.log("No tag required") 

```
These statements will create logs in android monitor as well as save in persistent storage. Logs generated by these can be easily accessed by tapping SuperLogView.

End with an example of getting some data out of the system or using it for a little demo

## Built With

* Android SDK
* [Maven](https://bintray.com/ankushgrover) - Dependency Management


## Author

[Ankush Grover](https://ankushgrover.com/)


## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Renil Babu
