package com.comeze.rangelti.hashisushiadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActHome extends AppCompatActivity implements View.OnClickListener {


    private Button btnCadProd;
    private Button btnPedidos;
    private Button btnProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initComponet();
    }
    private void initComponet()
    {
        btnCadProd = findViewById(R.id.btnCdaProd);
        btnPedidos = findViewById(R.id.btntPedido);
        btnProdutos = findViewById(R.id.btnProdotos);


        btnPedidos.setOnClickListener(this);
        btnCadProd.setOnClickListener(this);
        btnProdutos.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnCdaProd)
        {
            startVibrate(90);
            Intent it = new Intent(this, ActRegProd.class);
            startActivity(it);

        }
        else if (v.getId() == R.id.btntPedido)
        {
            //-----------------
            startVibrate(90);
            Intent it = new Intent(this, ActPedidos.class);
            startActivity(it);

        } else if (v.getId() == R.id.btnProdotos)
        {
            //-----------------
            startVibrate(90);
            Intent it = new Intent(this, ActProdutos.class);
            startActivity(it);

        }
    }

    public void startVibrate(long time)
    {
        // cria um obj atvib que recebe seu valor de context
        Vibrator atvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        atvib.vibrate(time);
    }

}
