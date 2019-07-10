package com.comeze.rangelti.hashisushiadmin.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterItensPedido;

import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.OrderItens;
import com.comeze.rangelti.hashisushiadmin.model.Orders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActItensOrder extends AppCompatActivity {

    private DatabaseReference reference;
    private List<OrderItens> itensList = new ArrayList<>();
    private RecyclerView list_Itens_Orders;
    private AdapterItensPedido adapterItensPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_itens_order);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Itens Pedido");
        //getSupportActionBar().hide();

        //Travæ rotaçãø da tela
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        startComponet();
        initDB();

        recyclerViewConfig();
        recycleOnclick();

        String idOrder =  System.getProperty("ID_ORDER");
        initSearch(idOrder);

    }

    public void initDB()
    {
        FirebaseApp.initializeApp(ActItensOrder.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        list_Itens_Orders.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        list_Itens_Orders,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                voltarPedidos();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                finish();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }

                        }
                )
        );
    }

    private void recyclerViewConfig()
    {
        //Configura recyclerview
        list_Itens_Orders.setLayoutManager(new LinearLayoutManager(this));
        list_Itens_Orders.setHasFixedSize(true);
        adapterItensPedido = new AdapterItensPedido(itensList);
        list_Itens_Orders.setAdapter(adapterItensPedido);

    }

    private void startComponet()
    {

        //RecyclerView---
        list_Itens_Orders = findViewById(R.id.list_Itens_Orders);

    }

    public void initSearch(final String idOrder)
    {
        //retorna pedido
        DatabaseReference pedidosDB = reference.child("orders");
        //retorna o no setado
        final Query querySearch = pedidosDB.orderByChild("idOrders").equalTo(idOrder);

        //cria um ouvinte
        querySearch.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {

                    Orders orders = objSnapshot.getValue(Orders.class);
                    List<OrderItens> list = orders.getOrderItens();
                    for (int i = 0;i < list.size();i ++) {

                        OrderItens itens = list.get(i);
                        itensList.add(itens);
                    }
                }
                adapterItensPedido.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void startItem(){
        Intent it = new Intent(this, ActItensOrder.class);
        startActivity(it);
    }

    private void voltarPedidos()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Itens de Pedido");
        alert.setMessage("Voltar para pedidos ? ");


        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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
}
