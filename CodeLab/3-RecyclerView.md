# Nearby List

## Creating the List
1. Start by adding the RecyclerView library in your `app/build.gradle` dependencies.

  ```java
  dependencies {
      // ...
      compile 'com.android.support:recyclerview-v7:24.2.1'
  }
  ```
1. Right click on the `java` folder in `/app/src/main`
2. Select **New** -> **Fragment** -> **Fragment (Blank)**
3. In the popup dialog, change the *Fragment Name* field to "NearbyFragment". Uncheck "*Include fragment factory methods?*" and also "*Include interface callbacks?*"  
  **Note**: If you accidentally chose the wrong Fragment or did not uncheck, just delete all the code in the class that was generated except `onCreateView()` method.
4. Open `fragment_nearby.xml` in the layout folder
5. Replace the existing `TextView` with a `RecyclerView`
  ```xml
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.agmostudio.pasarmalamlocator.NearbyFragment">

      <android.support.v7.widget.RecyclerView
          android:id="@+id/recycler_view"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:clipToPadding="false"
          android:padding="8dp"
          app:layoutManager="LinearLayoutManager"
          tools:listitem="@layout/list_item_nearby" />

  </FrameLayout>
  ```
6. Right click on the `layout` folder, select **New** -> **Layout resource file**. Give it the file name `list_item_nearby`
7. Replace the content  
  ```xml
  <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="?selectableItemBackground"
      android:padding="8dp">

      <ImageView
          android:id="@+id/image"
          android:layout_width="45dp"
          android:layout_height="45dp"
          android:layout_marginEnd="16dp"
          android:layout_marginRight="16dp"
          android:scaleType="centerCrop"
          android:src="@mipmap/ic_launcher" />

      <TextView
          android:id="@+id/name"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_alignTop="@+id/image"
          android:layout_toEndOf="@+id/image"
          android:layout_toRightOf="@+id/image"
          android:text="SS17"
          android:textAppearance="@style/TextAppearance.AppCompat.Medium" />

      <TextView
          android:id="@+id/distance"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_alignLeft="@+id/name"
          android:layout_alignStart="@+id/name"
          android:layout_below="@+id/name"
          android:text="1.5km" />

      <TextView
          android:id="@+id/hours"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_alignLeft="@+id/distance"
          android:layout_alignStart="@+id/distance"
          android:layout_below="@+id/distance"
          android:text="6:00pm - 11:00pm" />

  </RelativeLayout>
  ```
8. In the `java` folder, create a **New** -> **Java Class**, and name it "NearbyAdapter"  
  ```java
  public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {

      @Override
      public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.list_item_nearby, parent, false);
          return new MyViewHolder(view);
      }

      @Override
      public void onBindViewHolder(MyViewHolder holder, int position) {

      }

      @Override
      public int getItemCount() {
          return 20;
      }

      static class MyViewHolder extends RecyclerView.ViewHolder {

          ImageView image;
          TextView name;
          TextView distance;
          TextView hours;

          public MyViewHolder(View itemView) {
              super(itemView);
              image = (ImageView) itemView.findViewById(R.id.image);
              name = (TextView) itemView.findViewById(R.id.name);
              distance = (TextView) itemView.findViewById(R.id.distance);
              hours = (TextView) itemView.findViewById(R.id.hours);
          }

      }
  }
  ```
9. Back in your `NearbyFragment.java`, override `onViewCreated()` method  
  ```java
  NearbyAdapter adapter

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

      adapter = new NearbyAdapter();
      recyclerView.setAdapter(adapter);
  }
  ```
10. In your `MainActivity.java`, in the `MyPagerAdapter`, change the `getItem()`
  ```java
  @Override
  public Fragment getItem(int position) {
      if (position == 0) return new NearbyFragment();
      else return new ListFragment();
  }
  ```

11. Build and run the app.
