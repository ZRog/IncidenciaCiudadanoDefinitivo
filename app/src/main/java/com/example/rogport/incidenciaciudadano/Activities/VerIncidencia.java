package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.R;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class VerIncidencia extends AppCompatActivity {
    TextView TVIncidencia,TVubicacion,TVdescripcion;
    ImageView imagen;
    int ID,likes,size;

    public static Uri idImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Slide(Gravity.RIGHT));
        }


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.activity_ver_incidencia);

        TVdescripcion = (TextView) findViewById(R.id.textoDescripcion);
        TVubicacion = (TextView) findViewById(R.id.textoLocalizacion);
        imagen = (ImageView) findViewById(R.id.fotoIncidencia);
        Bundle parametros = getIntent().getExtras();
        String url = parametros.getString("imagen");
        ID = parametros.getInt("id");
        final String ubicacion = parametros.getString("ubicacion");
        String descripcion = parametros.getString("descripcion");
        TVdescripcion.setText(descripcion);
        TVubicacion.setText(ubicacion);
        likes = parametros.getInt("likes");

        Picasso.with(VerIncidencia.this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(imagen);

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //imagen.setImageURI(idImagen);*/

        TVIncidencia=(TextView)findViewById(R.id.TextIncidencia);
        TVIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(VerIncidencia.this, Reporte.class);
                i.putExtra("ID",ID);
                i.putExtra("Ubicacion",ubicacion);
                startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(VerIncidencia.this,view, "").toBundle());
            }
        });
    }

    public void darLike(View v){
        DatabaseReference refLikes = FirebaseDatabase.getInstance().getReference("Incidencias");
        final DatabaseReference refSize = FirebaseDatabase.getInstance().getReference("size");
        final DatabaseReference refID = refLikes.child(String.valueOf(ID));
        refSize.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Incidencia temp = dataSnapshot.getValue(Incidencia.class);
                List<String> listaTemp = temp.getTokens();
                for(int i=0;i<listaTemp.size();i++){
                    if(listaTemp.get(i).matches("")){
                        listaTemp.set(i,token);
                        DatabaseReference refTokens = refID.child("tokens");
                        refTokens.setValue(listaTemp);
                        DatabaseReference refL = refID.child("likes");
                        refL.setValue(likes + 1);
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.gracias),Toast.LENGTH_SHORT).show();
                        if(likes >= 2) {
                            refID.removeValue();
                            refSize.setValue(size-1);
                            finish();
                        }else {
                            finish();
                        }
                        break;
                    }
                    else
                    {
                        if(listaTemp.get(i).matches(token)){
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.disteLike),Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
