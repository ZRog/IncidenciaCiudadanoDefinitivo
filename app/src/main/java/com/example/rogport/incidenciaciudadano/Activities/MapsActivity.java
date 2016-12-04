package com.example.rogport.incidenciaciudadano.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.Mail.Config;
import com.example.rogport.incidenciaciudadano.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApi;
    private Location ultimaLoc;
    private ArrayList<Incidencia> incidencias;
    private int ID;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mGoogleApi = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        incidencias = new ArrayList<>();
        DatabaseReference refID = FirebaseDatabase.getInstance().getReference("ID");
        refID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ID = Integer.parseInt(dataSnapshot.getValue().toString());
                        DatabaseReference refIncidencias = FirebaseDatabase.getInstance().getReference("Incidencias");
                        for (int i = 0; i < ID; i++) {
                            DatabaseReference refIncidencia = refIncidencias.child(String.valueOf(i));
                            refIncidencia.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue() != null) {
                                        Incidencia incTemp = dataSnapshot.getValue(Incidencia.class);
                                        incidencias.add(incTemp);
                                        añadirMarca(incTemp.getLatlon().get(0),incTemp.getLatlon().get(1),incTemp.getId());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        DatabaseReference refInc = FirebaseDatabase.getInstance().getReference("Incidencias").child(String.valueOf((int)marker.getZIndex()));
                        refInc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Incidencia inctemp=dataSnapshot.getValue(Incidencia.class);
                                Intent i = new Intent(MapsActivity.this,VerIncidencia.class);
                                i.putExtra("imagen",inctemp.getImagen());
                                i.putExtra("id",inctemp.getId());
                                i.putExtra("ubicacion",inctemp.getUbicacion());
                                i.putExtra("descripcion",inctemp.getDescripcion());
                                i.putExtra("likes",inctemp.getLikes());
                                startActivity(i);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(MapsActivity.this);
    }

    public void abrirDesplegable(View v){
        PopupMenu desplegable = new PopupMenu(this,v);
        desplegable.setGravity(3);

        desplegable.getMenuInflater().inflate(R.menu.menu_desplegable,desplegable.getMenu());
        desplegable.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addIncidencia:
                        Intent i2 = new Intent(MapsActivity.this, AddIncidencia.class);
                        startActivity(i2);
                        break;
                    case R.id.ListaIncidencia:
                        Intent i3 = new Intent(MapsActivity.this, ListaIncidencias.class);
                        startActivity(i3);
                        break;
                    case R.id.Contacta:
                        Intent i1 = new Intent(MapsActivity.this, Contacta.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        desplegable.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(41.390205, 2.154007);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in VMother"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       /* if (ContextCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
 */
        mMap.setMinZoomPreference(13.5f);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApi.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        mGoogleApi.disconnect();
        client.disconnect();
    }

    public void añadirMarca(Double la, Double lo, int id){
        mMap.addMarker(new MarkerOptions().position(new LatLng(la,lo)).zIndex((float)id));
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ultimaLoc = obtenerLocal();
            if(ultimaLoc != null) {
                double latitude = ultimaLoc.getLatitude();
                double longitude = ultimaLoc.getLongitude();
                LatLng posicion = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
                for(int i=0;i<incidencias.size();i++){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(incidencias.get(i).getLatlon().get(0),incidencias.get(i).getLatlon().get(1))).title("MARCA"));
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Imposible to connect",Toast.LENGTH_LONG).show();
            }

            return;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent refresco = getIntent();
        finish();
        startActivity(refresco);
    }

    public Location obtenerLocal(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ultimaLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApi);
        }
        return ultimaLoc;
    }

}
