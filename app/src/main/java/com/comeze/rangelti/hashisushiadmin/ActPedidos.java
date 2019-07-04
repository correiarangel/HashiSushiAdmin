package com.comeze.rangelti.hashisushiadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.adapter.AdapterOrders;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterProduct;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.Orders;
import com.comeze.rangelti.hashisushiadmin.model.Product;
import com.comeze.rangelti.hashisushiadmin.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActPedidos extends AppCompatActivity implements View.OnClickListener {

    private TextView txtPedidos;
    private TextView txtStatus;


    private DatabaseReference reference;
    private List<Orders> ordersList = new ArrayList<>();
    private RecyclerView list_Orders;
    private AdapterOrders adapterOrders;
    private AdapterProduct adapterProduct;
    private List<Product>productList = new ArrayList<>();
    private AlertDialog dialog;
    private String retornIdUser;
    private User user;
    private Orders orders;
    private FirebaseAuth auth;
    private Orders ordersRecovery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pedidos);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("");
        //getSupportActionBar().hide();

        //Travæ rotaçãø da tela
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        startComponet();
        initDB();
        retornIdUser = UserFirebase.getIdUser();


        recyclerViewConfig();
        recycleOnclick();

        initSearch();
        recoveryDataUser();
        this.auth = FirebaseAuth.getInstance();


    }

    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        list_Orders.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        list_Orders,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                               Orders  pedidoSelecionado = ordersList.get(position);

                               confirmStatus(pedidoSelecionado);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Product produtoSelecionado = productsList.get(position);
                                // msgShort("Produto :"+produtoSelecionado);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }

                        }
                )
        );
    }

    //comfirmar item com dialog
    private void confirmStatus(final Orders pedidoSelecionado )
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Status do pedido : "+pedidoSelecionado.getStatus());
        alert.setMessage("\nConfirma statatus para ,produção. ");

        final EditText edtStatus = new EditText(this);
        edtStatus.setText("produção");

        alert.setView(edtStatus);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String status = edtStatus.getText().toString();
                    orders = new Orders();
                    orders.editStatus(status,pedidoSelecionado.getIdOrders());

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void recyclerViewConfig()
    {
        //Configura recyclerview
        list_Orders.setLayoutManager(new LinearLayoutManager(this));
        list_Orders.setHasFixedSize(true);
        adapterOrders = new AdapterOrders(ordersList, this);
        list_Orders.setAdapter(adapterOrders);
    }

    private void startComponet()
    {

        txtPedidos = findViewById(R.id.txtPedidos);
        //RecyclerView---
        list_Orders = findViewById(R.id.list_produsts);

    }



    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v)
    {

    }

    //Metudo que ativa vibração
    public void startVibrate(long time)
    {
        // cria um obj atvib que recebe seu valor de context
        Vibrator atvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        atvib.vibrate(time);
    }



    public void initDB()
    {
        FirebaseApp.initializeApp(ActPedidos.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }



    public void initSearch()
    {
        //retorna usuarios
        DatabaseReference productDB = reference.child("orders");
        //retorna o no setado
        Query querySearch = productDB.orderByChild("status").equalTo("confirmado");

        //cria um ouvinte
        querySearch.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    Orders o = objSnapshot.getValue(Orders.class);
                    //System.out.println("PEDIDO-------  "+o);
                    ordersList.add(o);
                }
                adapterOrders.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                msgShort("Houve algum erro:" + databaseError);
            }
        });
    }

    private void msgShort(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }



    private int validaQuantidade(String valor) {//valida se o valor digitado é numérico
        String regexStr = "^[0-9]*$";
        if (!valor.trim().matches(regexStr))
        {
            msgShort("Por favor, informe um valor numérico!");
            return 1;
        }
        else return 0;
    }

    //recupera dados do usuario esta com
    private void recoveryDataUser()
    {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados aguarde....")
                .setCancelable( true )
                .build();
        dialog.show();

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
                //typeUser = user.getIsAdmin();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }




}
