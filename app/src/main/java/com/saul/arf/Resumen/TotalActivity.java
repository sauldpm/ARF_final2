package com.saul.arf.Resumen;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saul.arf.Proyectos.Proyecto;
import com.saul.arf.R;
import com.saul.arf.ScenarioCamara.Cotizacion;
import com.saul.arf.ScenarioCamara.Mueble;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class TotalActivity extends AppCompatActivity {
    private Proyecto proyecto;
    private Cotizacion cotizacion;
    private ImageButton  botonRegresar;
    private ListView listView;
    private TextView subtotalPre;
    private TextView ivaPre;
    private TextView totalPre;
    private String escenarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        cotizacion=(Cotizacion) getIntent().getExtras().getSerializable("cotizacion");
        proyecto=(Proyecto)getIntent().getExtras().getSerializable("proyecto");
        escenarios=getIntent().getExtras().getString("escenarios");
        View footerView = ((LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.foot_listview_total, null, false);
        listView=findViewById(R.id.list_view_total);
        listView.setAdapter(new AdaptadorTotal(cotizacion,this));
        subtotalPre=footerView.findViewById(R.id.total_subtotal_num);
        ivaPre=footerView.findViewById(R.id.total_iva_num);
        totalPre=footerView.findViewById(R.id.total_total_num);
        botonRegresar=findViewById(R.id.btn_total_regresar);
        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
        agregarTotal();


        listView.addFooterView(footerView);
    }

    public void agregarTotal(){
        BigDecimal bd = new BigDecimal(cotizacion.total());
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        subtotalPre.setText(Double.toString(bd.doubleValue()));
        subtotalPre.setSelected(true);

        BigDecimal b = new BigDecimal(.16*cotizacion.total());
        b = b.setScale(2, RoundingMode.HALF_UP);
        ivaPre.setText(Double.toString(b.doubleValue()));
        ivaPre.setSelected(true);

        BigDecimal a = new BigDecimal(1.16*cotizacion.total());
        a = a.setScale(2, RoundingMode.HALF_UP);
        totalPre.setText(Double.toString(a.doubleValue()));
        totalPre.setSelected(true);
    }

    public void regresar(){
        Intent intent=new Intent(this,ResumenActivity.class);
        intent.putExtra("cotizacion",cotizacion);
        intent.putExtra("escenarios",escenarios);
        intent.putExtra("proyecto",proyecto);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        regresar();
    }
}
