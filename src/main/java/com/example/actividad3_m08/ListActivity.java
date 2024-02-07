package com.example.actividad3_m08;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    File [] ficheros;
    int i;

    ArrayList<String> mp3 =  new ArrayList<>();

    Context VALERIA = this;


    private ArrayList<String> mp3List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        // Obtener la lista de canciones (puedes cargarla de alguna manera)
        // Aquí, estoy usando una lista de ejemplo
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_AUDIO}, 1 );
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        }
        mp3List = new ArrayList<>();

        File directorio;
        //directorio = new File("/document/raw:/storage/emulated/0/Download/");
        directorio = new File(Environment.getExternalStorageDirectory().getPath()
                +"/Download");
        ficheros = directorio.listFiles();
        i = 0;

        for(File filo:  ficheros){
            if(filo.getName().endsWith(".mp3")) {
                mp3List.add(filo.getAbsolutePath());

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(filo.getAbsolutePath()); // Reemplaza con la ruta real del archivo de música

                // Obtener el álbum, el artista y el nombre de la canción
                String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                String albom = "Titulo: " + title+ " Album: " +album + " Autor:  " + artist;

                mp3.add(albom);
                i = i +1 ;

            }


        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter adapter = new SongAdapter(mp3);
        recyclerView.setAdapter(adapter);// MUY IMPORTANTE AÑADO ADAPTADOR

        adapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String path = mp3List.get(position);
               Intent inten = new Intent(ListActivity.this, MainActivity.class);
               inten.putExtra("path", path ); // COMPPARTIR INFOPASAR EL DATO AL MAIN ACVTIVITY
                inten.putExtra("POSITION", position);
                startActivity(inten); //PARA CUANDO LE DE A ESTA ABRA OTRA


            }
        });
    }
}
