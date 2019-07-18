package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActHome extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private Button btnCadProd;
    private Button btnPedidos;
    private Button btnProdutos;
    private String retornIdUser;
    private boolean typeUser;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Hashi Sushi Admin");

        initComponet();
        initDB();
        retornIdUser = UserFirebase.getIdUser();
        recoveryDataUser();

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        atvib.vibrate( time );
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
            Intent it = new Intent(this, ActProdutos.class);
            startActivity(it);
            return true;
        }

        if (id == R.id.menu_usuarios)
        {
            Intent it = new Intent(this, ActUsuarios.class);
            startActivity(it);
            return true;
        }

        if (id == R.id.menu_cadastrar_prod)
        {
            Intent it = new Intent(this, ActRegProd.class);
            startActivity(it);
            return true;
        }
        if (id == R.id.menu_pedidos)
        {
            Intent it = new Intent(this, ActPedidos.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_ped_andamento)
        {
            Intent it = new Intent(this, ActPedPreparo.class);
            startActivity(it);
            return true;
        }

        if (id == R.id.menu_pedidos_confirm)
        {
            Intent it = new Intent(this, ActPedidosConfirm.class);
            startActivity(it);

            return true;
        }
        if (id == R.id.menu_home)
        {

            msgShort("Já estamos em Home !");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void msgShort(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void initDB()
    {
        FirebaseApp.initializeApp(ActHome.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    //recupera dados do usuario esta com
    private void recoveryDataUser()
    {

        DatabaseReference usuariosDB = reference.child("users").child(retornIdUser);

        usuariosDB.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getValue() != null)
                {
                    user = dataSnapshot.getValue(User.class);
                }

                typeUser = user.getIsAdmin();

                if(typeUser != true){
                    String msg = "Vocẽ não é Admin,o App será finalizado !";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}
