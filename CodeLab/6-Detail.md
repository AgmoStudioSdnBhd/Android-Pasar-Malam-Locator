# Pasar Malam Detail

## Make PasarMalam a parcel

1. Update the `PasarMalam` class to implement `Parcelable` interface. This allows it to be pass between `Activity`.
  ```java
  public class PasarMalam implements Parcelable {

      public String name;
      public String imageUrl;
      public String operationHour;
      public String desc;
      public double latitude;
      public double longitude;

      private float[] result = new float[1];

      public float calculateDistance(Location location) {
          Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                  latitude, longitude, result);
          return result[0];
      }

      public PasarMalam() {
      }

      protected PasarMalam(Parcel in) {
          name = in.readString();
          imageUrl = in.readString();
          operationHour = in.readString();
          desc = in.readString();
          latitude = in.readDouble();
          longitude = in.readDouble();
      }

      @Override
      public int describeContents() {
          return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(name);
          dest.writeString(imageUrl);
          dest.writeString(operationHour);
          dest.writeString(desc);
          dest.writeDouble(latitude);
          dest.writeDouble(longitude);
      }

      @SuppressWarnings("unused")
      public static final Parcelable.Creator<PasarMalam> CREATOR = new Parcelable.Creator<PasarMalam>() {
          @Override
          public PasarMalam createFromParcel(Parcel in) {
              return new PasarMalam(in);
          }

          @Override
          public PasarMalam[] newArray(int size) {
              return new PasarMalam[size];
          }
      };
  }
  ```

## Create DetailActivity

1. Right click on the `java` folder in `/app/src/main`

2. Select **New** -> **Activity** -> **Empty Activity**

3. Change the Activity Name to `DetailActivity` and type *MainActivity* in the Hierarchical Parent and select from the dropdown to auto-complete the full name

4. Open the `activity_detail.xml` in the `layout` folder and modify according to the Design
  ```xml
  <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.agmostudio.pasarmalamlocator.DetailActivity">

      <android.support.design.widget.AppBarLayout
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:theme="@style/AppTheme.AppBarOverlay">

          <android.support.design.widget.CollapsingToolbarLayout
              android:layout_width="match_parent"
              android:layout_height="240dp"
              app:contentScrim="?colorPrimary"
              app:layout_scrollFlags="scroll|exitUntilCollapsed">

              <ImageView
                  android:id="@+id/image"
                  android:layout_width="match_parent"
                  android:layout_height="match_parent"
                  android:scaleType="centerCrop"
                  app:layout_collapseMode="parallax" />

              <android.support.v7.widget.Toolbar
                  android:id="@+id/toolbar"
                  android:layout_width="match_parent"
                  android:layout_height="?actionBarSize"
                  app:layout_collapseMode="pin"
                  app:popupTheme="@style/AppTheme.PopupOverlay" />

          </android.support.design.widget.CollapsingToolbarLayout>

      </android.support.design.widget.AppBarLayout>

      <android.support.v4.widget.NestedScrollView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          app:layout_behavior="@string/appbar_scrolling_view_behavior">

          <LinearLayout
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:orientation="vertical">

              <TextView
                  android:id="@+id/description"
                  android:layout_width="match_parent"
                  android:layout_height="wrap_content"
                  android:padding="16dp" />

              <fragment
                  android:id="@+id/map"
                  class="com.google.android.gms.maps.SupportMapFragment"
                  android:layout_width="match_parent"
                  android:layout_height="200dp"
                  app:liteMode="true"
                  app:useViewLifecycle="true" />
          </LinearLayout>
      </android.support.v4.widget.NestedScrollView>

  </android.support.design.widget.CoordinatorLayout>
  ```

5. Open `DetailActivity.java` and add the code into `onCreate` method
  ```java
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      ImageView imageView = (ImageView) findViewById(R.id.image);
      TextView description = (TextView) findViewById(R.id.description);

      final PasarMalam pasarMalam = getIntent().getParcelableExtra("pasar");

      getSupportActionBar().setTitle(pasarMalam.name);
      Glide.with(this).load(pasarMalam.imageUrl).into(imageView);
      description.setText(pasarMalam.desc);

      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map);
      mapFragment.getMapAsync(new OnMapReadyCallback() {
          @Override
          public void onMapReady(GoogleMap googleMap) {
              LatLng latLng = new LatLng(pasarMalam.latitude, pasarMalam.longitude);
              googleMap.addMarker(new MarkerOptions().position(latLng));
              googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
          }
      });
  }
  ```

6. Add a new method to conveniently start the  `DetailActivity`
  ```java
  public static void start(Context context, PasarMalam pasarMalam) {
      Intent intent = new Intent(context, DetailActivity.class);
      intent.putExtra("pasar", pasarMalam);
      context.startActivity(intent);
  }
  ```

## Linking Nearby to DetailActivity

1. Open `NearbyAdapter.java`

2. In the `onCreateViewHolder()` method, set a click listener on the `view` to launch the `DetailActivity`
  ```java
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.list_item_nearby, parent, false);
      final MyViewHolder viewHolder = new MyViewHolder(view);
      view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PasarMalam pasarMalam = pasarMalamList.get(viewHolder.getAdapterPosition());
              DetailActivity.start(v.getContext(), pasarMalam);
          }
      });
      return viewHolder;
  }
  ```

3. Build and run the app. Click on a Pasar Malam in the list

## Linking Maps to DetailActivity

1. Open `GoogleMapsFragment.java`

2. At the end of `onMapReady()` method after fetching data from Firebase. Add a listener to the `Marker`'s `InfoWindow` to launch DetailActivity when clicked
  ```java
  @Override
  public void onMapReady(GoogleMap googleMap) {
      // ...

      map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
          @Override
          public void onInfoWindowClick(Marker marker) {
              PasarMalam pasarMalam = (PasarMalam) marker.getTag();
              DetailActivity.start(getContext(), pasarMalam);
          }
      });
  }
  ```

3. Build and run the app. Click on a `Marker` and then click on the popup title.
