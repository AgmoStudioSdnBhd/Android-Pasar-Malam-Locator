# Material Design
We will be using the Design Support Library to provide Material Design to older Android versions.

## Design Support Library
1. Start by adding the library in your `app/build.gradle` dependencies.  
*Note: At the time of writing, the latest support library is r24.2.1 (September 2016). Refer* https://developer.android.com/topic/libraries/support-library/revisions.html

  ```java
  dependencies {
      // ...
      compile 'com.android.support:design:24.2.1'
  }
  ```

2. Open the `styles.xml` in `/res/values`

3. Change the Theme and add 2 new Themes
  ```xml
  <resources>

      <!-- Base application theme. -->
      <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
          <!-- Customize your theme here. -->
          <item name="colorPrimary">@color/colorPrimary</item>
          <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
          <item name="colorAccent">@color/colorAccent</item>
      </style>

      <style name="AppTheme.AppBarOverlay" parent="ThemeOverlay.AppCompat.Dark.ActionBar" />

      <style name="AppTheme.PopupOverlay" parent="ThemeOverlay.AppCompat.Light" />

  </resources>
  ```

4. Open `colors.xml` in `/res/values`

5. Change the color to our own app branding color
  - colorPrimary = #D41805
  - colorPrimaryDark = #961104
  - colorAccent = #FFEA00

6. Open the `activity_main.xml` located in `app/src/main/res/layout`  
**Pro-Tip** *Press SHIFT key twice to open up Search and start typing*

7. Convert the existing layout to use `CoordinatorLayout`, `AppBarLayout` and `TabLayout` from the Design Support Library; `ViewPager` from the v4 Support Library to enable left and right swiping
  ```xml
  <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.agmostudio.pasarmalamlocator.MainActivity">

      <android.support.design.widget.AppBarLayout
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:theme="@style/AppTheme.AppBarOverlay">

          <android.support.v7.widget.Toolbar
              android:id="@+id/toolbar"
              android:layout_width="match_parent"
              android:layout_height="?actionBarSize"
              app:popupTheme="@style/AppTheme.PopupOverlay" />

          <android.support.design.widget.TabLayout
              android:id="@+id/tab_layout"
              android:layout_width="match_parent"
              android:layout_height="wrap_content" />

      </android.support.design.widget.AppBarLayout>

      <android.support.v4.view.ViewPager
          android:id="@+id/view_pager"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          app:layout_behavior="@string/appbar_scrolling_view_behavior" />

  </android.support.design.widget.CoordinatorLayout>
  ```

8. Find the `View`s in the XML layout from your Java code by using `findViewById()`
  ```java
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
      ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
  }
  ```

9. Next, set the `Toolbar` to become our `ActionBar` using `setSupportActionBar(toolbar);`

10. `ViewPager` is an adapter-based `View`. The adapter will provide UI for every page the `ViewPager` ask for. Write the Adapter code after the existing `onCreate` method.
  ```java
  private static class MyPagerAdapter extends FragmentPagerAdapter {

      public MyPagerAdapter(FragmentManager fm) {
          super(fm);
      }

      @Override
      public Fragment getItem(int position) {
          return new ListFragment();
      }

      @Override
      public int getCount() {
          return 2;
      }

      @Override
      public CharSequence getPageTitle(int position) {
          if (position == 0) return "Nearby";
          else return "Maps";
      }
  }
  ```

11. Create `MyPagerAdapter` and set it to `ViewPager`

12. Setup the `TabLayout` with the `ViewPager` using `tabLayout.setupWithViewPager(viewPager);`
  ```java
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      // ...<omitted>

      MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
      viewPager.setAdapter(adapter);
      tabLayout.setupWithViewPager(viewPager);
  }
  ```
13. Build and run the app. You now have 2 pages and can swipe left and right.

## App Icon

1. Download the prepared App Icon from https://github.com/AgmoStudioSdnBhd/Android-Pasar-Malam-Locator/raw/master/asset/ic_launcher.zip

2. Extract and replace the `ic_launcher` in your `res/mipmap-*`

### References
[1] https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html  
[2] https://developer.android.com/reference/android/support/design/widget/AppBarLayout.html  
[3] https://developer.android.com/reference/android/support/design/widget/TabLayout.html  
[4] https://developer.android.com/reference/android/support/v4/view/ViewPager.html  
