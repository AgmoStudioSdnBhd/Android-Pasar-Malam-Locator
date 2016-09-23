# Firebase Database

## Sample Data

1. Go to https://console.firebase.google.com

2. On the side menu, click on **Database**

3. Click on the menu (3 dots) on the right side.

4. Select *Import JSON* and import this file. https://github.com/AgmoStudioSdnBhd/Firebase-Pasar-Malam-Locator/raw/master/Database/pasar-malam-locator-export.json

5. Click on the **Rules** tab and change the rules. This allow anyone to read your data, but requires user login to change your data.
  ```json
  {
    "rules": {
      ".read": true,
      ".write": "auth != null"
    }
  }
  ```

6. Click on **Publish** to apply your new rules.

## Adding Firebase Database

> If you do not have Android Studio v2.2 and above, just
- add `compile 'com.google.firebase:firebase-database:9.4.0'` to your dependencies in your app/build.gradle
- (Done during FCM) ensure you have `apply plugin: 'com.google.gms.google-services'` at the end of your app/build.gradle file
- (Done during FCM) ensure you have added `classpath 'com.google.gms:google-services:3.0.0'` in the project level build.gradle

1. Open the Firebase Assistant side menu or via **Tools** > **Firebase**

2. Select **Realtime Database** > **Save and retrieve data**

3. Click on Step 1 (Connect to Firebase) and Step 2 button (Add the realtime database to your app)

## PasarMalam model

1. Create a new Java class named `PasarMalam.java` which corresponds to the data that we imported into the Firebase
  ```java
  public class PasarMalam {

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
  }
  ```

## Loading Image

We will be using a 3rd party library to load images in our app. https://github.com/bumptech/glide

1. In your `app/build.gradle` add the Glide dependency  

  ```groovy
  dependencies {
    // ...
    compile 'com.github.bumptech.glide:glide:3.7.0'
  }
  ```

## Modify NearbyAdapter

1. Open `NearbyAdapter.java`

2. We need to allow the adapter to receive our List of PasarMalam. Modify to add replaceAll method.
  ```java
  public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {

      List<PasarMalam> pasarMalamList = new ArrayList<>();

      public void replaceAll(List<PasarMalam> list) {
          pasarMalamList.clear();
          pasarMalamList.addAll(list);
          notifyDataSetChanged();
      }

      // ...
  }
  ```

3. Modify the `onBindViewHolder()` and `getItemCount()` method
  ```java
  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
      PasarMalam pasarMalam = pasarMalamList.get(position);

      Glide.with(holder.image.getContext())
                .load(pasarMalam.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.image);
      holder.name.setText(pasarMalam.name);
      holder.hours.setText(pasarMalam.operationHour);
  }

  @Override
  public int getItemCount() {
    return pasarMalamList.size();
  }
  ```

## Displaying Data from Firebase

1. You can make Firebase to support offline viewing by `setPersistenceEnabled`. This must be called before you use Firebase. For our case, the best place is in `MainActivity.java`
  ```java
  public class MainActivity extends AppCompatActivity {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    // ...
  }
  ```

2. Open `NearbyFragment.java`

3. In the `onViewCreated()` method, after you set the `NearbyAdapter`. Add the code the listen to Firebase
  ```java
  // add this code after recyclerView.setAdapter(adapter);
  FirebaseDatabase database = FirebaseDatabase.getInstance();
  DatabaseReference table = database.getReference("pasarmalam");
  table.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
          ArrayList<PasarMalam> list = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              list.add(snapshot.getValue(PasarMalam.class));
          }
          adapter.replaceAll(list);
      }

      @Override
      public void onCancelled(DatabaseError error) {
          error.toException().printStackTrace();
      }
  });
  ```

4. Build and run your app.

## Getting User Location

We will be using 3rd party library to help us simplify getting User location. https://github.com/mrmans0n/smart-location-lib

1. In your `app/build.gradle` add the Smart Location library dependency
  ```groovy
  dependencies {
    // ...
    compile 'io.nlopez.smartlocation:library:3.2.7'
  }
  ```

2. In your `AndroidManifest.xml`
  - Add Location permission within the `<manifest>` `</manifest>` tag
    ```xml
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    ```
  - within the `<application>` `</application>` tag, add
    ```xml
    <meta-data xmlns:tools="http://schemas.android.com/tools"
      android:name="com.google.android.gms.version"
      android:value="@integer/google_play_services_version"
      tools:replace="android:value" />
    ```

3. Open `NearbyFragment.java`, modify `replaceAll()` method and add `setCurrentLocation()` and `sortList()`
  ```java
  public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {

      List<PasarMalam> pasarMalamList = new ArrayList<>();
      Location currentLocation;

      public void replaceAll(List<PasarMalam> list) {
          pasarMalamList.clear();
          pasarMalamList.addAll(list);
          sortList();
          notifyDataSetChanged();
      }

      public void setCurrentLocation(Location location) {
          currentLocation = location;
          sortList();
          notifyDataSetChanged();
      }

      private void sortList() {
          if (currentLocation == null || pasarMalamList.isEmpty()) return;

          Collections.sort(pasarMalamList, new Comparator<PasarMalam>() {

              @Override
              public int compare(PasarMalam o1, PasarMalam o2) {
                  return Float.compare(
                          o1.calculateDistance(currentLocation),
                          o2.calculateDistance(currentLocation));
              }
          });
      }

      // ...
  }
  ```

4.  Modify `onBindViewHolder` to add the code
  ```java
  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
      // ... add this after set image, name, and hours

      if (currentLocation == null) {
          holder.distance.setText(null);
      } else {
          float distanceMeter = pasarMalam.calculateDistance(currentLocation);
          String text;
          if (distanceMeter < 1000f) {
              text = String.format(Locale.ENGLISH, "%.2f m", distanceMeter);
          } else {
              text = String.format(Locale.ENGLISH, "%.2f km", distanceMeter / 1000f);
          }
          holder.distance.setText(text);
      }
  }
  ```

5. Open `NearbyFragment.java` and add 3 methods: `onResume()`, `onRequestPermissionsResult()`, and `startLocationUpdate()`
   ```java
  public class NearbyFragment extends Fragment {
    // ...

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdate();
        }
    }

    private void startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SmartLocation.LocationControl smartLocation = SmartLocation.with(getContext()).location();
            smartLocation.oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    adapter.setCurrentLocation(location);
                }
            });
            adapter.setCurrentLocation(smartLocation.getLastLocation());
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }
  }
  ```
