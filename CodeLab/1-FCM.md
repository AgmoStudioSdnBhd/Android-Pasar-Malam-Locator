# Firebase Cloud Messaging (FCM)
## Requirements
This section requires you to have a Google account.

## Android Studio Plugin: Firebase Assistant
> As of Android Studio 2.2, there is a Firebase Plugin integrated with the IDE. Most of the basic integration are done for you. If you have older version of Android Studio, read the "Accessing the Firebase Console" section instead.

1. At the top of the Android Studio, look for **Tools** > **Firebase**  
  ![Firebase Plugin](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/firebase_plugin.jpg)

2. This will open up a new side bar called *Assistant*  
  ![Firebase Assistant](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/assist_sidebar.jpg)

3. Click on *Cloud Messaging* and then *Set up Firebase Cloud Messaging*  
  ![FCM Sidebar](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/assist_fcm.jpg)

4. You will then be presented with Steps to perform. For this excerise, you will only need to do Step 1 and 2.
  - Connect your app to Firebase (just click the button)
  - Add FCM to your app (just click the button)

5. You maybe asked to login to your Google account in order to proceed.

6. In the Dialog, give it a Project Name and select Malaysia as your Country/Region and create your project. There will be a progress dialog at the bottom of Android Studio.  
  ![FCM Create](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/fcm_create_dialog.jpg)

7. Next click on "Add FCM to your app" button, accept the dialog changes and wait for it do complete.

## Accessing the Firebase Console
> This section only applies to Android Studio version older than 2.2 where there is no Firebase Plugin yet.  If you have newer version of Android Studio, read the "Android Studio Plugin: Firebase Assistant" section instead.

1. Go to https://console.firebase.google.com

2. Login or Create a new Google account.

3. If you already create your Firebase project, skip this step
  + Click on the "Create a new Project" button
  + In the pop up dialog. Give it a Project Name and select Malaysia as your Country/Region
  + Wait for your Project to be created by Google and you will be presented with your Project Dashboard

4. Click the **Add Firebase to your Android app**

5. To find your Package Name, look into your Android Studio project, `PasarMalamLocator/app/build.gradle`. Look for `applicationId` field. Copy the value (without quotes) e.g. *com.agmostudio.pasarmalamlocator* and paste it in the Firebase Dialog  
  ![Create Project](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/add_app_1.jpg)

6. Click **Next** and it will download a file `google-services.json`. Copy the file to your `PasarMalamLocator/app` directory and click **Continue** to the last step  
  ![Copy File](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/add_app_2.jpg)

7. In the last step, you are suppose to copy and paste the code snippets to your `PasarMalamLocator/build.gradle` and also `PasarMalamLocator/app/build.gradle` file  
  ![Update Gradle](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/add_app_3.jpg)

8. Click **Sync Now** in your Android Studio

9. To use Firebase Notifications in your app, you need to include the helper libraries in your `app/build.gradle`  
  ```groovy
  dependencies {
      // add this compile line in your dependencies section
      compile 'com.google.firebase:firebase-messaging:9.4.0'
  }
  ```

## Pushing Notification
1. Build and run your app. Close your app via the Back button or the Home button (important!)

2. Go back to Firebase Console Dashboard of your Project  https://console.firebase.google.com

3. At the side menu, look for **Notification** and click it.  
  ![Firebase Console Side Bar](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/fcm_noti.jpg)

4. Since we have never sent a push notification, we proceed to click on the "Send Your First Message"  
    ![Push Message](https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/art/fcm_msg.jpg)

5. Type in your message in the "Message Text" field

6. Under the "Target" section, select your app in the dropdown.

7. Click "Send Message" and confirm.
*Important: Make sure your app is in the background or you will not receive notifications*
