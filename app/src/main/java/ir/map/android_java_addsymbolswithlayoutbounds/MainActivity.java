package ir.map.android_java_addsymbolswithlayoutbounds;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;

public class MainActivity extends AppCompatActivity {

    MapboxMap map;
    Style mapStyle;
    MapView mapView;

    List<LatLng> sampleLatLngs = new ArrayList<>();

    Feature selectedFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;

                        // TODO; جهت انجام هرکاری با شیٔ نقشه، از اینجا به بعد می توانید اقدام کنید
                        addSymbolsSourceAndLayerToMap();
                        boundMapToPoints();
                    }
                });
            }
        });
    }

    private void getAnnotation(LatLng point) {
        List<Feature> features = map.queryRenderedFeatures(map.getProjection().toScreenLocation(point));

        if (features.size() > 0) {
            selectedFeature = features.get(0);
            Toast.makeText(this, "انتخاب شد", Toast.LENGTH_SHORT).show();
            map.getUiSettings().setAllGesturesEnabled(false);
        }
    }

    private void addSymbolsSourceAndLayerToMap() {
        sampleLatLngs.add(new LatLng(35.757578, 51.409932));
        sampleLatLngs.add(new LatLng(35.757500, 51.409900));

        //region add feature
        List<Feature> samplePointsFeatures = new ArrayList<>();

        for (int i = 0; i < sampleLatLngs.size(); i++) {
            Feature sampleFeature = Feature.fromGeometry(
                    Point.fromLngLat(
                            sampleLatLngs.get(i).getLongitude(),
                            sampleLatLngs.get(i).getLatitude()
                    )
            );
            samplePointsFeatures.add(sampleFeature);
        }

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(samplePointsFeatures);
        //endregion add feature

        // Add source to map
        GeoJsonSource geoJsonSource = new GeoJsonSource("my_source_id", featureCollection);
        mapStyle.addSource(geoJsonSource);

        // Add image to map
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.mapbox_marker_icon_default);
        mapStyle.addImage("my_image_id", icon);

        // Add layer to map
        SymbolLayer symbolLayer = new SymbolLayer("my_symbol_layer_id_1", "my_source_id");
        symbolLayer.setProperties(
                PropertyFactory.iconImage("my_image_id"),
                PropertyFactory.iconSize(1.5f),
                PropertyFactory.iconOpacity(1f),
                PropertyFactory.textColor("#ff5252"),
                PropertyFactory.textFont(new String[]{"IranSans-Noto"})
        );
        mapStyle.addLayer(symbolLayer);
    }


    private void boundMapToPoints() {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(sampleLatLngs)
                .build();

        map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}