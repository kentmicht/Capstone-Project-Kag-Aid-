package com.example.kagaid.kagaid.Maps;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kagaid.kagaid.Patient.ViewPatientInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.kagaid.kagaid.Maps.MapsActivity;

import maes.tech.intentanim.CustomIntent;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by navneet on 23/7/16.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    Context mContext;
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    String[] hospitalList = new String[100];
    String markerTitle;



    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            final String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            hospitalList[i] = markerOptions.getTitle();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
//                    Intent intent = new Intent(context,ShowNearbyDoctorsList.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                    Intent intent = new Intent(RequestJsonString.this,ShowNearbyDoctorsList.class);
//                    Intent i = new Intent(mContext, ShowNearbyDoctorsList.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(i);
                    System.out.println(marker.getTitle());
//                    doctorInterface.getResponse(marker.getTitle());
//                    mContext.startActivity(new Intent(mContext, ShowNearbyDoctorsList.class));
//                    Intent intent = new Intent(GetNearbyPlacesData.context, ShowNearbyDoctorsList.class);
//                    context.startActivity(intent);
//                    nearbyDoctors.showNearbyDoctors(marker.getTitle());
//                    GetNearbyPlacesData.this.context.startActivity(new Intent(GetNearbyPlacesData.this.context,ShowNearbyDoctorsList.class));
//                    System.out.println(marker.getTitle());
                    return false;
                }
            });

            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

//    public GetNearbyPlacesData(NearbyDoctorInterface nearbyDoctors) {
//        this.nearbyDoctors = nearbyDoctors;
//    }

    public GetNearbyPlacesData() {
    }

    public GetNearbyPlacesData(Context mContext) {
        this.mContext = mContext;
    }
}
