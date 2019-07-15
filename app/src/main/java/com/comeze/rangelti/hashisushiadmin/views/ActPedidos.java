package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterOrders;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterProduct;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.OrderItens;
import com.comeze.rangelti.hashisushiadmin.model.Orders;
import com.comeze.rangelti.hashisushiadmin.model.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActPedidos extends AppCompatActivity {

    private FloatingActionButton floatBtnPesqPed;
    private EditText edtPesqPed;

    private DatabaseReference reference;
    private List<Orders> ordersList = new ArrayList<Orders>();
    private RecyclerView recycre_Orders;
    private AdapterOrders adapterOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pedidos);


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Pedidos");


        startComponet();
        initDB();

        recyclerViewConfig();
        recycleOnclick();

        retornaPedidos();
    }

    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        recycre_Orders.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        recycre_Orders,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Orders pedidoSecionado = ordersList.get(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Orders pedidoSecionado = ordersList.get(position);


                                // msgShort("Produto :"+produtoSelecionado);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }

                        }
                )
        );
    }

    //Configura recyclerview
    private void recyclerViewConfig()
    {

        recycre_Orders.setLayoutManager(new LinearLayoutManager(this));
        recycre_Orders.setHasFixedSize(true);
        adapterOrders = new AdapterOrders( ordersList, this);
        recycre_Orders.setAdapter(adapterOrders);
    }

    private void startComponet()
    {
        //RecyclerView---
        recycre_Orders = findViewById(R.id.recycre_Orders);
        edtPesqPed = findViewById(R.id.edtPesqPed);
        floatBtnPesqPed = findViewById(R.id.floatBtnPesqPed);

        floatBtnPesqPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVibrate(90);
                String textPesquisa = edtPesqPed.getText().toString();

                if( textPesquisa.equals("")){
                    msgShort("Digite uma data para pesquisa !");
                    retornaPedidos();
                }else {
                    pesquisarPedido(textPesquisa);
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    public void retornaPedidos()
    {
        //retorna
        DatabaseReference pedidosDB = reference.child("orders");

        //cria um ouvinte
        pedidosDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    Orders o  = objSnapshot.getValue(Orders.class);
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

    private void pesquisarPedido(String pesquisa){

        DatabaseReference produtosRef = reference
                .child("orders");
        Query query = produtosRef.orderByChild("dateOrder")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff" );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ordersList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ordersList.add( ds.getValue(Orders.class) );
                }

                adapterOrders.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                msgShort("Busca cancelada !");
            }
        });
    }

    //comfirmar item com dialog
    private void confirmExclusao(final Orders  orders )
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Excluir Pedido :"+orders.getIdOrders());
        alert.setMessage("Confirma exclusão ? ");


        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                orders.removerOrder(orders.getIdOrders());
                msgShort("Excluido !");

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                msgShort("Exclusão cancelada");
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
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

            msgShort("Já estamos em Produtos");
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
        if (id == R.id.menu_ped_andamento)
        {
            Intent it = new Intent(this, ActPedPreparo.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_pedidos)
        {
            msgShort("Já estamos em pedidos !");
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
