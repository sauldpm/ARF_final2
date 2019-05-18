/*
 * @Autor: Martin Alejandro Carrillo Mendoza
 * @Fecha: Enero 2019
 * @Proyecto : TT 2018-A002
 */
package com.saul.arf;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.saul.arf.Escenarios.Escenario;
import com.saul.arf.Login.LoginActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.Registro.RegistroActivity;
import com.saul.arf.ScenarioCamara.LoadSessionActivity;

import android.widget.ViewFlipper;
public class MainActivity extends AppCompatActivity {
    private Button btnAcceder;
    private Button btnRegistro;
    private Button btnPrueba;
    private View activity_main;
    private ViewFlipper viewFlipper;
    private static final String DEFAULT="Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        activity_main = (View) findViewById(R.id.activity_arf);
        isSesion();
        btnAcceder = findViewById(R.id.btnAcceso);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnPrueba = findViewById(R.id.btnTrialArf);
        int images[] = {R.drawable.w_03,R.drawable.w_04,R.drawable.w_05};
        viewFlipper = (ViewFlipper) findViewById(R.id.arf_flipper);

        for(int i =0; i < images.length; i++){
            fliṕperImages(images[i]);
        }

        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcceso();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegistro();
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrueba();
            }
        });


        imprimirMensaje("¡Bienvenido a Augmented Reallity Furniture!");

    }

    public void getAcceso(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void getRegistro(){
        Intent intent = new Intent( this, RegistroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void getPrueba(){
        Intent intent = new Intent ( this, LoadSessionActivity.class);
        intent.putExtra("id",40);
        intent.putExtra("id_proyecto",-1);
        intent.putExtra("escenarios","");
        intent.putExtra("Escenario",new Escenario(-1,DEFAULT,"9999999",DEFAULT,DEFAULT,DEFAULT));
        intent.putExtra("proyecto",new Proyecto(-1,DEFAULT,DEFAULT,DEFAULT,DEFAULT));
        startActivity(intent);
        finish();
    }

    private void isSesion(){
        SharedPreferences tutorial = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
        SharedPreferences.Editor tutorialEditor=tutorial.edit();
        tutorialEditor.remove("mostrar2");
        tutorialEditor.commit();
        SharedPreferences preferences = getSharedPreferences("arf_session__variables", Context.MODE_PRIVATE);
        int id_usuario=preferences.getInt("id_arf",-1);
        if(id_usuario!=-1){
            Intent intent = new Intent(this, MisProyectosActivity.class);
            intent.putExtra("id",id_usuario);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void fliṕperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }

}

