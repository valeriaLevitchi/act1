package com.example.actividad3_m08;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;


import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.view.View;

import android.widget.ImageView;
import android.widget.SeekBar;


import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    boolean VALERIAL;



    private  boolean play = false;
    int posicion ;



    File[] ficheros;

    int i;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable updateSeekBar;

    MediaPlayer reporductor_VL =  new MediaPlayer();


    private ArrayList<String> mp3List;

    String path;

    Intent inton;

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;

    private ImageView albumArtImageView;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1 );
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        }


        mp3List = new ArrayList<>();
        inton = getIntent();
        path = inton.getStringExtra("path");






        File directorio;
//listar todos los archivos
        directorio = new File(Environment.getExternalStorageDirectory().getPath()
                +"/Download");
        //loistamos
        ficheros = directorio.listFiles();
         i = 0;

        for(File filo:  ficheros){
            if(filo.getName().endsWith(".mp3")) {
                mp3List.add(filo.getAbsolutePath());
                i = i +1 ;

            }


        }

        posicion = mp3List.indexOf(path);




        seekBar = findViewById(R.id.seekBar);
        seekBar.setEnabled(false);  // Deshabilitar la interacción del usuario con la barra de progreso
        handler = new Handler();

        // Configurar la actualización automática de la barra de progreso
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (reporductor_VL != null) {
                    int currentPosition = reporductor_VL.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
                // Actualizar cada 1000 milisegundos (1 segundo)
                handler.postDelayed(this, 1000);
            }
        };



        try {
                reporductor_VL.setDataSource(mp3List.get(posicion));
                reporductor_VL.prepare();
                reporductor_VL.start();
                VALERIAL = true;
                play = true;


            } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cambioimagen();



    }

    private void cambioimagen() {
        ImageView imageView = findViewById(R.id.imageView);

        // Cambiar la imagen utilizando setImageResource
        imageView.setImageResource(R.drawable.portada2);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);  // Reemplaza con la ruta real del archivo de música
        byte[] albumArt = retriever.getEmbeddedPicture();

        if(albumArt !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);

            imageView.setImageBitmap(bitmap);
        }
    }


    public void PlayPause  (View View){

        try {


                if (!VALERIAL){
                reporductor_VL.setDataSource(mp3List.get(posicion));
                reporductor_VL.prepare();
                reporductor_VL.start();
                VALERIAL = true;

            }
                 if(!play){
                     reporductor_VL.start();
                     play =true;
                     updateSeekBar.run();
                 }else {
                     reporductor_VL.pause();
                     play = false;

                 }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }






    public void Stop (View view){

        reporductor_VL.seekTo(0);
        reporductor_VL.pause();

        play = false;

    }



    public void Siguiente (View view) {

        if(posicion == 0 ){
            posicion = i-1;
        }else{
            posicion = posicion -1;}

        path = mp3List.get(posicion);
        cambioimagen();


        reporductor_VL.reset();
        play = false;


        VALERIAL =  false;


        PlayPause(view);


        String posicio = String.valueOf(posicion);




    }



       public void anterior (View view ) {

           if(posicion == i-1 ){
               posicion = 0;
           }else{
           posicion = posicion +1;}

           path = mp3List.get(posicion);

           cambioimagen();
               reporductor_VL.reset();
                play = false;


           VALERIAL =  false;



           PlayPause(view);


           String posicio = String.valueOf(posicion);






       }



        public void palante (View view){
        if(reporductor_VL.isPlaying()) {


            int newPosition = reporductor_VL.getCurrentPosition() + 10000; // 10 segundos en milisegundos
            if(newPosition <= reporductor_VL.getDuration()){
            reporductor_VL.seekTo(newPosition);}
            else{
                posicion = posicion +1;
                PlayPause(view);
            }

        }

        }

    public void patras (View view){
        if(reporductor_VL.isPlaying()) {


            int newPosition = reporductor_VL.getCurrentPosition() - 10000; // 10 segundos en milisegundos
            if(newPosition >= 0) {
                reporductor_VL.seekTo(newPosition);
            }else{
                reporductor_VL.seekTo(0);
            }
        }

    }

    @Override
    public void onBackPressed() {
        // Detén la reproducción al presionar el botón "atrás"
        super.onBackPressed();
        if (reporductor_VL != null) {
            reporductor_VL.stop();
            reporductor_VL.release();
        }

        // Llama a la implementación predeterminada para realizar el cierre normal de la actividad
        Intent into = new Intent(MainActivity.this, ListActivity.class);
        startActivity(into);
        finish();
    }


}





