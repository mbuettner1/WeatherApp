package com.example.fullweatherapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fullweatherapp.MainActivity;
import com.example.fullweatherapp.R;
import com.example.fullweatherapp.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }

    private void onMapReady(GoogleMap googleMap) {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {            /* Define the URL pattern for the tile images */
                String s = String.format("https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=Your-API-Key (Links to an external site.)", zoom, x, y);
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }            try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }        /*
             * Check that the tile server supports the requested x, y and zoom.
             * Complete this stub according to the tile range you support.
             * If you support a limited range of tiles at different zoom levels, then you
             * need to define the supported x, y range at each zoom level.
             */
            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 12;
                int maxZoom = 16;            return (zoom >= minZoom && zoom <= maxZoom);
            }
        };


        LatLng loc = new LatLng(151.211,-33.852 );
        googleMap.addMarker(new MarkerOptions().position(loc).title("You are here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider));
    }
}