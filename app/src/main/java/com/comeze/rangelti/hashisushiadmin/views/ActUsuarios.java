package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterProduct;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterUsers;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
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

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActUsuarios extends AppCompatActivity {
    private TextView txtProdutos;

    private DatabaseReference reference;
    private List<User> users = new ArrayList<User>();
    private RecyclerView list_Users;
    private AdapterUsers adapterUser;
    private String retornIdUser;
    private User user;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_usuarios);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("");

        startComponet();
        initDB();
        retornIdUser = UserFirebase.getIdUser();

        recyclerViewConfig();
        recycleOnclick();


        this.auth = FirebaseAuth.getInstance();
        retornaUsers();
    }

    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        list_Users.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        list_Users,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                User userSelecionado = users.get(position);

                                String idUser = userSelecionado.getIdUser();

                                System.setProperty("ID_PRODUTO",idUser );
                                startEditUser(userSelecionado);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                User userSelecionado = users.get(position);

                                confirmExclusao(userSelecionado);

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

        list_Users.setLayoutManager(new LinearLayoutManager(this));
        list_Users.setHasFixedSize(true);
        adapterUser = new AdapterUsers(users, this);
        list_Users.setAdapter(adapterUser);
    }

    private void startComponet()
    {
        txtProdutos = findViewById(R.id.txtProdutos);
        //RecyclerView---
        list_Users = findViewById(R.id.list_Users);
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
        FirebaseApp.initializeApp(ActUsuarios.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    public void  retornaUsers()
    {
        DatabaseReference userDB = reference.child("users");
        //retorna tipo setado

        //cria um ouvinte
        userDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    User u = objSnapshot.getValue(User.class);
                    users.add(u);
                }
                adapterUser.notifyDataSetChanged();
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

    private void pesquisarUsuario(String pesquisa){

        DatabaseReference userRef = reference
                .child("users");
        Query query = userRef.orderByChild("name")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff" );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    users.add( ds.getValue(User.class) );
                }

                adapterUser.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void startEditUser( User user ){
        //Intent it = new Intent(this, ActCadastraUser.class);
        //it.putExtra("USER_ENV", user);
        //startActivity(it);
    }

    //comfirmar item com dialog
    private void confirmExclusao(final User user )
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Excluir Usuario"+user.getName());
        alert.setMessage("Confirma exclusão ? ");

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //user.remover(user.getIdUser());
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
            Intent it = new Intent(this, ActPedidosConfirm.class);
            startActivity(it);
            finish();
            return true;
        }

        if (id == R.id.menu_usuarios)
        {
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
