package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
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
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.Orders;
import com.comeze.rangelti.hashisushiadmin.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActPedPreparo extends AppCompatActivity {

    private DatabaseReference reference;
    private List<Orders> ordersList = new ArrayList<>();
    private RecyclerView recy_Orders;
    private AdapterOrders adapterOrders;

    private Orders orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ped_preparo);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Pedidos em Preparo");
        //getSupportActionBar().hide();

        //Travæ rotaçãø da tela
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        startComponet();
        initDB();

        recyclerViewConfig();
        recycleOnclick();

        listesnerEventPedidos();

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        recy_Orders.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        recy_Orders,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Orders  pedidoSelecionado = ordersList.get(position);
                                confirmStatus(pedidoSelecionado,position);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Orders  pedidoSelecionado = ordersList.get(position);

                                String idOrder =  pedidoSelecionado.getIdOrders();
                                //envia id order para actItensOrder
                                System.setProperty("ID_ORDER",idOrder );
                                //chama actItensOrder
                                startItem();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }

                        }
                )
        );
    }

    //comfirmar item com dialog
    private void confirmStatus(final Orders pedidoSelecionado , final int position)
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Status do pedido : "+pedidoSelecionado.getStatus());
        alert.setMessage("\nConfirme novo status. ");


        final EditText edtStatus = new EditText(this);
        edtStatus.setText("entregue");

        alert.setView(edtStatus);

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String status = edtStatus.getText().toString();

                orders = new Orders();
                orders.editStatus( status,pedidoSelecionado.getIdOrders());
                //remove item lista adpter
                adapterOrders.updateListOrdes(position);

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
        recy_Orders.setLayoutManager(new LinearLayoutManager(this));
        recy_Orders.setHasFixedSize(true);
        adapterOrders = new AdapterOrders(ordersList, this);
        recy_Orders.setAdapter(adapterOrders);

    }


    private void startComponet()
    {

        //RecyclerView---
        recy_Orders = findViewById(R.id.recy_Orders);

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
        FirebaseApp.initializeApp(ActPedPreparo.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }


    public void listesnerEventPedidos(){

        //retorna usuarios
        DatabaseReference pedidosDB = reference.child("orders");
        //retorna o no setado
        Query querySearch = pedidosDB.orderByChild("status").equalTo("em preparo");

        querySearch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);

                ordersList.add(orders);

                adapterOrders.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Orders orders = dataSnapshot.getValue(Orders.class);
               // System.out.println("PEDIDO MODOU Status-------  "+orders.getStatus());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //Orders orders = dataSnapshot.getValue(Orders.class);
                //System.out.println("PEDIDO REMOVIDO-------  "+orders.getStatus());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    public void retornaPedidos()
    {
        //retorna
        DatabaseReference pedidosDB = reference.child("orders");
        //retorna o no setado
        Query querySearch = pedidosDB.orderByChild("status").equalTo("em preparo");
        //cria um ouvinte
        querySearch.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    Orders orders = objSnapshot.getValue(Orders.class);
                    ordersList.add(orders);

                }
                adapterOrders.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                msgShort("Houve algum erro no retorno de pedidos :" + databaseError);
            }
        });
    }



    private void msgShort(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }





    private void startItem(){
        Intent it = new Intent(this, ActItensOrder.class);
        startActivity(it);
    }

    private void notificacao( ){


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this,0, new Intent(),0 );
        // PendingIntent p = PendingIntent.getActivity(this,0, new Intent(this,ActLivroRenovar.class),0 );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Status Pedido");
        builder.setContentTitle("Pedido em andamento !");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources() ,R.mipmap.ic_launcher));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descs = new String[]{"Novo pedido em andamento !"};
        for(int i = 0;i < descs.length; i++){
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification no = builder.build();
        no.vibrate = new long[]{150,300,150};
        no.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.ic_launcher,no);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,som);
            toque.play();
        }catch (Exception e){

            System.out.println("Erro ao gerar toque notificação : "+e);
        }
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
        if (id == R.id.menu_pedidos)
        {
            Intent it = new Intent(this, ActPedidos.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_pedidos_confirm)
        {
            startPedidosConfimados();
            return true;
        }
        if (id == R.id.menu_ped_andamento)
        {
            msgShort("Já estamos em Pedidos em andamento !");
            return true;
        }
        if (id == R.id.menu_home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startPedidosConfimados() {
        Intent it = new Intent(this, ActPedidosConfirm.class);
        startActivity(it);
        finish();
    }


}