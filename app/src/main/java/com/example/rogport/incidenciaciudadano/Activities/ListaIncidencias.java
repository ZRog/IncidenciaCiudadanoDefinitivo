package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.Adaptadores.AdaptadorListaIncidencia;
import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaIncidencias extends AppCompatActivity {

    public static ArrayList<Incidencia> incidencias;
    private RecyclerView listaIncidencias;
    private int ID,likes,size;
    private String ubicacion,descripcion,id,imagen;
    private String[] valores;

    public ListaIncidencias() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_lista_incidencias);

        incidencias = new ArrayList<>();

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaIncidencias = (RecyclerView) findViewById(R.id.rvIncidencias);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaIncidencias.setLayoutManager(llm);
        inicializarListaIncidencias();


    }
    public void inicializarAdaptador(){
        AdaptadorListaIncidencia adaptador = new AdaptadorListaIncidencia(incidencias,this);
        listaIncidencias.setAdapter(adaptador);
    }
    public void inicializarListaIncidencias(){
        final DatabaseReference refID = FirebaseDatabase.getInstance().getReference("ID");
        DatabaseReference refSize = FirebaseDatabase.getInstance().getReference("size");
        refSize.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = Integer.parseInt(dataSnapshot.getValue().toString());
                refID.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ID = Integer.parseInt(dataSnapshot.getValue().toString());
                        if(size==0) {
                            ID = 0;
                            refID.setValue(0);
                        }
                        if(ID!=0) {
                            if (size > 0) {
                                DatabaseReference refIncidencias = FirebaseDatabase.getInstance().getReference("Incidencias");
                                for (int i = 0; i < ID; i++) {
                                    DatabaseReference refIncidencia = refIncidencias.child(String.valueOf(i));
                                    refIncidencia.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            Incidencia incTemp = dataSnapshot.getValue(Incidencia.class);
                                            incidencias.add(incTemp);
                                            inicializarAdaptador();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });


                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent refresco = getIntent();
        finish();
        startActivity(refresco);
    }
}




