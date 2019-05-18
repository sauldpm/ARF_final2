package com.saul.arf;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import com.saul.arf.Escenarios.Escenario;
import com.saul.arf.Escenarios.Load;
import com.saul.arf.Video.VideoRecorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArFragment fragment;
    private Escenario escenario;
    private Context context;
    private Activity activity;
    private int id_usuario;
    private int id_proyecto;
    private VideoRecorder videoRecorder;
    private FloatingActionButton grabar;
    private String videoPath;

    private ModelRenderable model3D;
    private TransformableNode seleccionActual;
    private FootprintSelectionVisualizer footPrintModel;
    private AnchorNode anchorBotonDelete;
    private ModelRenderable footModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        videoPath=null;
        context=this;
        activity=this;
        escenario=(Escenario) getIntent().getExtras().getSerializable("Escenario");
        id_usuario=getIntent().getExtras().getInt("id");
        id_proyecto=getIntent().getExtras().getInt("id_proyecto");


        fragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> takePhoto());

        FloatingActionButton guardarFotos=(FloatingActionButton)findViewById(R.id.guardarFotos);
        guardarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(videoPath!=null)
                   // escenario.agregarRutaFotos(videoPath);
                Intent intent=new Intent(context, Load.class);
                intent.putExtra("id",id_usuario);
                intent.putExtra("id_proyecto",id_proyecto);
                intent.putExtra("escenario",escenario);
                intent.putExtra("accion",3);
                activity.startActivity(intent);
            }
        });
    //----------------------video initialize--------------------------------------------
        videoRecorder = new VideoRecorder();
        int orientation = getResources().getConfiguration().orientation;
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
        videoRecorder.setSceneView(fragment.getArSceneView());
        //Boton recording
        grabar = findViewById(R.id.grabar);
        grabar.setOnClickListener(this::toggleRecording);
           grabar.setEnabled(true);
        grabar.setImageResource(R.drawable.ic_play_arrow_black_30dp);
    //----------------------end video initialize--------------------------------------------

        footPrintModel=new FootprintSelectionVisualizer();
    //-------------------------Fragment-------------------------------------------------------
        fragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

            if(model3D==null) {
                Toast.makeText(this,"No a seleccionado un mueble",Toast.LENGTH_LONG);
                return;
            }
            if(footModel==null)
                createFootModel();

            addModel(hitResult);
        });
     //---------------------------End Fragment----------------------------------------------
    }


//---------------------------------Make Model--------------------------------------------------------

    //Crea un nodo anchor(Agrega objeto a la escena)
    private void addModel(HitResult hitResult){
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(fragment.getArSceneView().getScene());
        createTranformableNode(anchorNode);
    }

    //Crea un nodo Transformable
    private void createTranformableNode(AnchorNode anchorNode){
        TransformableNode objeto3D;
        objeto3D = new TransformableNode(fragment.getTransformationSystem());
        objeto3D.setParent(anchorNode);
        objeto3D.setRenderable(model3D);
        objeto3D.setOnTapListener((HitTestResult hitTestResult,MotionEvent motionEventNode) ->{
            if(seleccionActual!=null)
                footPrintModel.removeSelectionVisual(seleccionActual);
            if(anchorBotonDelete!=null)
                fragment.getArSceneView().getScene().removeChild(anchorBotonDelete);
            seleccionActual=objeto3D;
            footPrintModel.applySelectionVisual(objeto3D);
            addPrecioBotonEliminar(anchorNode,objeto3D);
        });
    }

    //Agregar boton eliminar y precio a los muebles
    public void addPrecioBotonEliminar(AnchorNode anchorNode,TransformableNode transformableNode){
        ViewRenderable.builder()
                .setView(this,R.layout.boton_eliminar_objeto)
                .build()
                .thenAccept(viewRenderable->{
                    addPrecioBotonEliminarToObject(anchorNode,viewRenderable,transformableNode);
                });
    }

    //Especifica la posicion del precio y del boton eliminar
    public void addPrecioBotonEliminarToObject(AnchorNode anchorNodeObject,ViewRenderable viewRenderable,TransformableNode transformableNode){
        float x=transformableNode.getWorldPosition().x;
        float y=(transformableNode.getUp().y+transformableNode.getWorldPosition().y)/2;
        float z= transformableNode.getWorldPosition().z;
        Vector3 botonVector=new Vector3(x,y,z);
        Quaternion quaternion=fragment.getArSceneView().getScene().getCamera().getWorldRotation();

        anchorBotonDelete=new AnchorNode();
        anchorBotonDelete.setWorldPosition(botonVector);
        anchorBotonDelete.setWorldRotation(quaternion);
        anchorBotonDelete.setRenderable(viewRenderable);
        anchorBotonDelete.setParent(fragment.getArSceneView().getScene());
        View view=viewRenderable.getView();
        FloatingActionButton eliminar=view.findViewById(R.id.eliminarmuebles);
        TextView textView=view.findViewById(R.id.textmuebles);
        textView.setText("$255654");
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarObjeto(anchorNodeObject);
            }
        });
    }

    //Eliminar objeto en pantalla
    public void eliminarObjeto(AnchorNode anchorNodeObject){
        fragment.getArSceneView().getScene().removeChild(anchorNodeObject);
        fragment.getArSceneView().getScene().removeChild(anchorNodeObject);
    }

    //Crea el modelo para la seleccion de objetos
    public void createFootModel(){
        MaterialFactory.makeTransparentWithColor(this, new Color(1f,0f,1f,.5f))
                .thenAccept(
                        material -> {
                            footModel =
                                    ShapeFactory.makeCylinder(.5f,.01f,new Vector3(0.0f, 0.0f, 0.0f),material);});
    }

    //Crea el modelo seleccionado
    public void createModel(String modelo){
        if(model3D!=null)
            model3D=null;
        ModelRenderable.builder()
                .setSource(this, RenderableSource.builder().setSource(
                        this,
                        Uri.parse(modelo),
                        RenderableSource.SourceType.GLTF2)
                        .setScale(0.5f)  // Scale the original model to 50%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(modelo)
                .build()
                .thenAccept(renderable -> model3D = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "No se pudo cargar el modelo" +
                                            modelo, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }
//---------------------------------End Make Model----------------------------------------------------


//----------------------------take Video-------------------------------------------------------------
    private void toggleRecording(View unusedView) {
        boolean recording = videoRecorder.onToggleRecord();
        if (recording) {
            grabar.setImageResource(R.drawable.ic_stop_black_30dp);
        } else {
            grabar.setImageResource(R.drawable.ic_play_arrow_black_30dp);
            videoPath = videoRecorder.getVideoPath().getAbsolutePath();

            // Send  notification of updated content.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    @Override
    protected void onPause() {
        if (videoRecorder.isRecording()) {
            toggleRecording(null);
        }
        super.onPause();
    }

//----------------------------End Take Video-------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.quit_arf) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//-------------------------------------Take Photo--------------------------------------------------------
    private String generateFilename() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        //viewDialog.showDialog();
        final String filename = generateFilename();
        escenario.agregarRutaFotos(filename);
        ArSceneView view = fragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(SessionActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                //viewDialog.hideDialog();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Se guardo la foto",Snackbar.LENGTH_INDEFINITE);//Snackbar.LENGTH_LONG
                snackbar.setAction("Abrir en fotos", v -> {
                    File photoFile = new File(filename);
                    Uri photoURI = FileProvider.getUriForFile(SessionActivity.this,
                            SessionActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.setAction("Continuar", v -> {
                    snackbar.dismiss();
                });
                snackbar.show();

            } else {
                Toast toast = Toast.makeText(SessionActivity.this,
                        "Error en copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }
    //-----------------------------------------------end "Take Photo------------------------------------------------------
}

