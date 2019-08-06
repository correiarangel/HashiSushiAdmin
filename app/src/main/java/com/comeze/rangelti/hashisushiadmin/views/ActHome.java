package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.model.Costs;
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
        listesnerEventPedidos();//escuta pedidos

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
        if (id == R.id.menu_ped_entregando)
        {

            Intent it = new Intent(this, ActEntregando.class);
            startActivity(it);
            return true;
        }
        if (id == R.id.menu_info)
        {
            Intent it = new Intent(this, ActInfo.class);
            startActivity(it);
            return true;
        }
        if (id == R.id.menu_custo)
        {
            configCost();
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

    // dialog para configura valor de entrega
    public void configCost ( ) {

        AlertDialog.Builder alert = new AlertDialog.Builder ( this );
        alert.setTitle ( "Custo de entrega" );
        alert.setMessage ( "\nConfigure o custo de sua entrega." );


        final EditText editCustoEntrega = new EditText ( this );

        LinearLayout layoutFilds = new LinearLayout ( this );
        layoutFilds.setOrientation ( LinearLayout.VERTICAL );

        layoutFilds.addView ( editCustoEntrega );
        editCustoEntrega.setHint ( "Digite somente valor monetario" );

        alert.setView ( layoutFilds );


        alert.setPositiveButton ( "Salvar Custo", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {

                String custoEntrega = editCustoEntrega.getText ( ).toString ( );

                if ( validaValor( custoEntrega ) == 1 ) {

                    msgShort ( "Atenção campo vazio ou valor incorreto !" );

                } else {
                    msgShort ( "Custo de entrega salvo com sucesso !" );
                    //instacia Costs e seta e salva valor
                    Costs costs = new Costs();
                    costs.editarCusto( custoEntrega );
                }
            }
        } );

        alert.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) { } } );
        AlertDialog dialog = alert.create ( );
        dialog.show ( );
    }


    private int validaValor ( String valor ) {//valida se o valor digitado é numérico
        String regexStr = "^(([1-9]\\d{0,2}(\\.\\d{3})*)|(([1-9]\\.\\d*)?\\d))(\\,\\d\\d)?";
        if ( !valor.trim ( ).matches ( regexStr ) ) {
            msgShort ( "Por favor, informe um valor monetario :=[" );
            return 1;
        } else return 0;
    }

    public void listesnerEventPedidos(){

        //retorna usuarios
        DatabaseReference pedidosDB = reference.child("orders");
        //retorna o no setado
        Query querySearch = pedidosDB.orderByChild("status").equalTo("confirmado");

        querySearch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                notificacao();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Orders orders = dataSnapshot.getValue(Orders.class);
                // System.out.println("PEDIDO MODOU Status-------  "+orders.getStatus());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //Orders orders = dataSnapshot.getValue(Orders.class);
                //System.out.println("PEDIDO REMOVIDO-------  "+orders.getStatus());

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void notificacao( ){


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this,0, new Intent(this,ActPedidosConfirm.class),0 );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Pedido Novo");
        builder.setContentTitle(" Chegou Pedido !");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources() ,R.mipmap.ic_launcher));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descs = new String[]{"Cheque a lista de pedido um novo pedido chegou !"};
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

}


