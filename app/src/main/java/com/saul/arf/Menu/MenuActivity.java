package com.saul.arf.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import com.saul.arf.Login.LoginActivity;
import com.saul.arf.MainActivity;
import com.saul.arf.MisEscenariosActivity;
import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.Resumen.ResumenActivity;
import com.saul.arf.ScenarioCamara.Categoria;
import com.saul.arf.ScenarioCamara.Cotizacion;
import com.saul.arf.ScenarioCamara.Mueble;
import com.saul.arf.Video.VideoRecorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private List<String> listTitle;
    private Map<String, List<String>> listChild;
    private ArrayList<Categoria> arrayListCategorias;
    private int id_user;
    private int id_proyecto;
    private Proyecto proyecto;
    private Escenario escenario;
    private ArrayList<Mueble> muebles;
    private static Context context;
    private Activity activity;
    private Cotizacion cotizacion;
    private EditText nombre_proyecto;
    private EditText email_user;
    private EditText name_user_arf;
    private EditText name_project;
    private EditText tipo_interior;
    private EditText escenary_size;
    private EditText tipo_muebles;
    private EditText presupuesto;
    private Button presupuestoEnCamara;
    private FloatingActionButton fab;
    private FloatingActionButton guardarFotos;


    //----------------Modelos--------------------
    private ArFragment fragment;
    private static ModelRenderable model3D;
    private TransformableNode seleccionActual;
    private FootprintSelectionVisualizer footPrintModel;
    private AnchorNode anchorBotonDelete;
    private ModelRenderable footModel;

    //---------------video---------------------
    private VideoRecorder videoRecorder;
    private FloatingActionButton grabar;
    private String videoPath;

    private String escenarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        videoPath=null;
        context=this;
        activity=this;
        drawerLayout = findViewById(R.id.drawer_layout);
        expandableListView = findViewById(R.id.navList);
        setVariablesSession((int)getIntent().getExtras().getInt("id"),(int)getIntent().getExtras().getInt("id_proyecto"),(Proyecto)getIntent().getExtras().getSerializable("proyecto"), (Escenario) getIntent().getExtras().getSerializable("Escenario"));
        escenarios=getIntent().getExtras().getString("escenarios");
        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header,null,false);
        expandableListView.addHeaderView(listHeaderView);
        presupuestoEnCamara=findViewById(R.id.presupuesto_en_camara);
        this.name_user_arf = findViewById(R.id.name_user_arf);
        this.name_user_arf.setText(this.proyecto.getClient_firstname()+" "+this.proyecto.getClient_lastname());
        this.email_user = findViewById(R.id.email_user);
        this.email_user.setText(this.proyecto.getClient_email());
        this.name_project = findViewById(R.id.name_project);
        this.name_project.setText("Proyecto: "+this.escenario.getNombreProyecto());
        this.tipo_interior = findViewById(R.id.tipo_interior);
        this.tipo_interior.setText("Habitación: "+this.escenario.getTipoHabitacion());
        this.escenary_size = findViewById(R.id.escenary_size);
        this.escenary_size.setText("Tamaño: "+this.escenario.getTamanio());
        this.tipo_muebles = findViewById(R.id.tipo_muebles);
        this.tipo_muebles.setText("Tipo muebles: "+this.escenario.getTipoMuebles());
        this.presupuesto = findViewById(R.id.presupuesto);
        this.presupuesto.setText("Presupesto: "+this.escenario.getPresupuesto());

        cotizacion=new Cotizacion(id_user,id_proyecto,Double.parseDouble(escenario.getPresupuesto()));
        genData();
        System.out.println("VARIABLES_SESSION"+this.getId_user()+" "+this.getId_proyecto()+" "+this.getEscenario().getNombreProyecto());
        addDrawersItem();
        setupDrawer();
       /* if(savedInstanceState == null){
            selectFirstItemAsDefault();
        }*/
        presupuestoEnCamara.setText(Double.toString(cotizacion.getPresupuesto()));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Escenario aux = (Escenario)getIntent().getExtras().getSerializable("Escenario");
        getSupportActionBar().setTitle(aux.getNombreProyecto());

        //-----------------------------Fragment  sesion start-------------------------------------------------------------
        fragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> takePhoto());

        guardarFotos=(FloatingActionButton)findViewById(R.id.guardarFotos);
        guardarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(videoPath!=null)
                // escenario.agregarRutaFotos(videoPath);
                saveWork();
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
        //-----------------------------------End fragment sesion------------------------------------------------------------
        verificarPrueba();
    }

    private void verificarPrueba(){
        if(id_user==40){
            //presupuestoEnCamara.setVisibility(View.INVISIBLE);
            fab.hide();
            guardarFotos.hide();
            grabar.hide();
        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cotizacion.eliminarMuebleActual();
    }

    /*private void selectFirstItemAsDefault(){
        if(navigationManager != null){
            String firstItem = new String("¡Seleccione un mueble!");
            navigationManager.showFragment(firstItem);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.quit_arf:
                finish();
                Intent intent_quit = new Intent(this, LoginActivity.class);
                startActivity(intent_quit);
                break;
            //case R.id.return_scenarios_arf:
                /*Intent intent_scenario = new Intent(this, MisEscenariosActivity.class);
                startActivity(intent_scenario);*/
            //break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (videoRecorder.isRecording()) {
            toggleRecording(null);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(id_user==40)
            showAlertDialogPrueba();
        else
            showAlertDialog();
    }

    private void showAlertDialogPrueba(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación")
                .setCancelable(false)
                .setMessage("¿Desea salir de la version de prueba?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        Intent intent=new Intent(context, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        dialogo.cancel();
                    }
                });
        alert.create();
        alert.show();
    }

    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación")
                .setMessage("¿Seguro que desea regresar?. Se perderán los datos guardados")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        back();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        dialogo.cancel();
                    }
                });
        alert.create();
        alert.show();
    }

    private void mensaje(){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("Advertencia")
                .setMessage("Para guardar un escenario debes guardar al menos una foto")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    private void saveWork(){
        if(escenario.getRutasFotos().size()<=0){
            mensaje();
         return;
        }
        cotizacion.setEscenario(escenario);
        cotizacion.borrarMuebles();
        Intent intent=new Intent(context, ResumenActivity.class);
        intent.putExtra("cotizacion",cotizacion);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("proyecto",proyecto);
        activity.startActivity(intent);
        activity.finish();
    }
    private void back(){
        Intent intent=new Intent(context, MisEscenariosActivity.class);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("id",id_user);
        intent.putExtra("id_proyecto",id_proyecto);
        intent.putExtra("proyecto",proyecto);
        startActivity(intent);
        finish();
    }

    private void genData(){

        this.listChild = new TreeMap<>();
        this.setArrayListCategorias((ArrayList<Categoria>) getIntent().getExtras().getSerializable("c"));


        for(int i = 0; i< getArrayListCategorias().size(); i++){
            getArrayListCategorias().get(i).setListNombres();
            if(!getArrayListCategorias().isEmpty()){
                this.listChild.put(getArrayListCategorias().get(i).getNombre(), getArrayListCategorias().get(i).getArrayListNombresSubCategorias());
            }else{
                this.listChild.put("Sin Categorias",new ArrayList<String>());
            }
        }
        this.listTitle = new ArrayList<>(this.listChild.keySet());
    }

    private void addDrawersItem(){

        adapter = new CustomExpandableListAdapter(this,listTitle,this.listChild);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener( new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String selectedItem =((List)(listChild.get(listTitle.get(groupPosition)))).get(childPosition).toString();
                String categoria = new String(listTitle.get(groupPosition).toString());
                FragmentManager manager = getSupportFragmentManager();
                System.out.println("CATEGORIA "+categoria);
                System.out.println("SUBCATEGORIA "+selectedItem);
                for(int i=0; i < arrayListCategorias.size(); i++){
                    for(int j=0; j < arrayListCategorias.get(i).getArrayListNombresSubCategorias().size(); j++){
                        if(categoria.equals(arrayListCategorias.get(i).getNombre())&&selectedItem.equals(arrayListCategorias.get(i).getSubCategorias().get(j).getNombre())){
                            if(arrayListCategorias.get(i).getSubCategorias().get(j).getMuebles().isEmpty()){
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                builder.setInverseBackgroundForced(true);
                                builder.setTitle("Mensaje");
                                builder.setMessage("¡Esta subcategoría aún no tiene muebles!");
                                builder.setPositiveButton("Cancelar", null);
                                AlertDialog messagedialog = builder.create();
                                messagedialog.show();*/
                                imprimirMensaje("¡Esta subcategoría aún no tiene muebles!");
                            }else {
                                MessageFragment dialog = new MessageFragment();
                                obtenerMuebles(categoria,selectedItem);
                                dialog.show(manager,"MessageDialog");
                            }
                        }
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupDrawer(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

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
        Mueble muebleAux=cotizacion.getMuebleActual();
        objeto3D = new TransformableNode(fragment.getTransformationSystem());
        objeto3D.setParent(anchorNode);
        objeto3D.setRenderable(model3D);
        cotizacion.agregarMueble(objeto3D);
        objeto3D.setOnTapListener((HitTestResult hitTestResult, MotionEvent motionEventNode) ->{
            if(footModel!=null)
                footPrintModel.setFootprintRenderable(footModel);
            if(seleccionActual!=null)
                footPrintModel.removeSelectionVisual(seleccionActual);
            if(anchorBotonDelete!=null)
                fragment.getArSceneView().getScene().removeChild(anchorBotonDelete);
            seleccionActual=objeto3D;
            footPrintModel.applySelectionVisual(objeto3D);
            addPrecioBotonEliminar(anchorNode,objeto3D,muebleAux);
        });
        presupuestoEnCamara.setText(Double.toString(cotizacion.getPresupuesto()));
        if(cotizacion.getPresupuesto()<0){
            presupuestoEnCamara.setTextColor(getResources().getColor(R.color.rojo));
            Toast.makeText(this,"Se sobrepaso el presupuesto ",Toast.LENGTH_SHORT).show();
        }
        else
            presupuestoEnCamara.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    //Agregar boton eliminar y precio a los muebles
    public void addPrecioBotonEliminar(AnchorNode anchorNode,TransformableNode transformableNode,Mueble muebleAux){
        ViewRenderable.builder()
                .setView(this,R.layout.boton_eliminar_objeto)
                .build()
                .thenAccept(viewRenderable->{
                    addPrecioBotonEliminarToObject(anchorNode,viewRenderable,transformableNode,muebleAux);
                });
    }

    //Especifica la posicion del precio y del boton eliminar
    public void addPrecioBotonEliminarToObject(AnchorNode anchorNodeObject,ViewRenderable viewRenderable,TransformableNode transformableNode,Mueble muebleAux){
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
        textView.setText(Double.toString(muebleAux.getPrice()));
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarObjeto(anchorNodeObject,transformableNode,muebleAux);
            }
        });
    }

    //Eliminar objeto en pantalla
    public void eliminarObjeto(AnchorNode anchorNodeObject,TransformableNode transformableNode,Mueble muebleAux){
        cotizacion.eliminarMueble(transformableNode);
        cotizacion.sumarPrecioMueble(muebleAux.getPrice());
        presupuestoEnCamara.setText(Double.toString(cotizacion.getPresupuesto()));
        if(cotizacion.getPresupuesto()>=0)
            presupuestoEnCamara.setTextColor(getResources().getColor(R.color.colorWhite));
        fragment.getArSceneView().getScene().removeChild(anchorNodeObject);
        fragment.getArSceneView().getScene().removeChild(anchorBotonDelete);
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
    public static void createModel(String modelo){
        if(model3D!=null)
            model3D=null;
        ModelRenderable.builder()
                .setSource(context, RenderableSource.builder().setSource(
                        context,
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
                                    Toast.makeText(context, "No se pudo cargar el modelo" +
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

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Se guardó el video",Snackbar.LENGTH_INDEFINITE);//Snackbar.LENGTH_LONG
            snackbar.setAction("Abrir en fotos", v -> {
                File photoFile = new File(videoPath);
                Uri photoURI = FileProvider.getUriForFile(MenuActivity.this,
                        MenuActivity.this.getPackageName() + ".ar.codelab.name.provider",
                        photoFile);
                Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                intent.setDataAndType(photoURI, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

            });

            snackbar.show();


            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }
//----------------------------End Take Video-------------------------------------------------------------

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
                    Toast toast = Toast.makeText(MenuActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                //viewDialog.hideDialog();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Se guardó la foto",Snackbar.LENGTH_INDEFINITE);//Snackbar.LENGTH_LONG
                snackbar.setAction("Abrir en fotos", v -> {
                    File photoFile = new File(filename);
                    Uri photoURI = FileProvider.getUriForFile(MenuActivity.this,
                            MenuActivity.this.getPackageName() + ".ar.codelab.name.provider",
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
                Toast toast = Toast.makeText(MenuActivity.this,
                        "Error en copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }
//-----------------------------------------------end "Take Photo------------------------------------------------------


    public ArrayList<Categoria> getArrayListCategorias() {
        return arrayListCategorias;
    }

    public void setArrayListCategorias(ArrayList<Categoria> arrayListCategorias) {
        this.arrayListCategorias = arrayListCategorias;
    }

    public int getId_user() {
        return this.id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_proyecto() {
        return this.id_proyecto;
    }

    public void setId_proyecto(int id_proyecto) {
        this.id_proyecto = id_proyecto;
    }

    public void setVariablesSession(int id_user,int id_project,Proyecto proyecto,Escenario escenario){
        this.setId_user(id_user);
        this.setId_proyecto(id_project);
        this.setEscenario(escenario);
        this.setProyecto(proyecto);
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario=escenario;
    }

    public void obtenerMuebles(String categoria,String subcategorira){

        for(int i=0; i < arrayListCategorias.size(); i++) {
            for (int j = 0; j < arrayListCategorias.get(i).getArrayListNombresSubCategorias().size(); j++) {
                if(categoria.equals(arrayListCategorias.get(i).getNombre())&&subcategorira.equals(arrayListCategorias.get(i).getSubCategorias().get(j).getNombre())){
                    this.setMuebles(arrayListCategorias.get(i).getSubCategorias().get(j).getMuebles());
                }
            }
        }
        this.getIntent().putExtra("objetos",muebles);
    }

    public ArrayList<Mueble> getMuebles() {
        return muebles;
    }

    public void setMuebles(ArrayList<Mueble> muebles) {
        this.muebles = muebles;
    }

    public void imprimirMuebles(String subcategorira){

        for(int i=0; i < arrayListCategorias.size(); i++) {
            for (int j = 0; j < arrayListCategorias.get(i).getArrayListNombresSubCategorias().size(); j++) {
                if (subcategorira.equals(arrayListCategorias.get(i).getSubCategorias().get(j).getNombre())) {
                    for (int k = 0; k < arrayListCategorias.get(i).getSubCategorias().get(j).getMuebles().size(); k++) {
                        System.out.println("MUEBLES: " + arrayListCategorias.get(i).getSubCategorias().get(j).getMuebles().get(k).getImage() + " get " + k);
                    }
                }
            }
        }
    }

    public void imprimirMensaje(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("variables_sesion",Context.MODE_PRIVATE);
        String usuarioArf = preferences.getString("user_arf","EMPTY_USER");
        int idArf = preferences.getInt("id_arf",0);
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

}
