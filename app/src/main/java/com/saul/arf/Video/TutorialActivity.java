package com.saul.arf.Video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.saul.arf.MisProyectosAWS;
import com.saul.arf.MisProyectosActivity;
import com.saul.arf.R;

public class TutorialActivity extends AppCompatActivity {
    private int duracionVideo;
    private int id_user;
    private VideoView videoTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        id_user=getIntent().getExtras().getInt("id");
        videoTutorial=findViewById(R.id.video_tutorial);
        ImageButton salirTutorial=findViewById(R.id.video_tutorial_salir);
        MediaController mediaController=new MediaController(this);
        Uri path = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.tutorial);
        videoTutorial.setVideoURI(path);
        videoTutorial.setMediaController(mediaController);
        videoTutorial.start();
        duracionVideo=videoTutorial.getDuration();
        videoTutorial.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {@Override public void onCompletion(MediaPlayer mp) { endVideo(); }});
        salirTutorial.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { back(); }});
    }

    private void endVideo(){
        final Activity activity=this;
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Confirmación");
        dialogo1.setMessage("El tutorial termino.¿Desea verlo de nuevo?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Ver de nuevo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                videoTutorial.seekTo(0);
                videoTutorial.start();
            }
        });
        dialogo1.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                back();
            }
        });
        dialogo1.show();
    }

    private void back(){
        Intent intent=new Intent(this, MisProyectosActivity.class);
        intent.putExtra("id",id_user);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
