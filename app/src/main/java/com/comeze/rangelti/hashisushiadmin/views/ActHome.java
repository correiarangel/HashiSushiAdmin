package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.comeze.rangelti.hashisushiadmin.R;

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
            Intent it = new Intent(this, ActPedidosConfirm.class);
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

    //==> MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_promotion, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menu_produtos)
        {
            Intent it = new Intent(this, ActPedidosConfirm.class);
            startActivity(it);
            finish();
            return true;
        }

        if (id == R.id.menu_usuarios)
        {
            Intent it = new Intent(this, ActUsuarios.class);
            startActivity(it);

            finish();
            return true;
        }

        if (id == R.id.menu_cadastrar_prod)
        {
            Intent it = new Intent(this, ActRegProd.class);
            startActivity(it);
            finish();
            return true;
        }

        if (id == R.id.menu_cadastrar_user)
        {
            finish();
            return true;
        }
        if (id == R.id.menu_pedidos)
        {
            finish();
            return true;
        }
        if (id == R.id.menu_pedidos_confirm)
        {
            Intent it = new Intent(this, ActPedidosConfirm.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_home)
        {
            Intent it = new Intent(this, ActHome.class);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
