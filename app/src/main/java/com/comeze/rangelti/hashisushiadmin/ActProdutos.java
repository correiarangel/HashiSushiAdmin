package com.comeze.rangelti.hashisushiadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.adapter.AdapterProduct;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.Product;
import com.comeze.rangelti.hashisushiadmin.model.User;
import com.google.android.gms.common.util.Strings;
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

public class ActProdutos extends AppCompatActivity {

    private TextView txtProdutos;

    private DatabaseReference reference;
    private List<Product> productsList = new ArrayList<Product>();
    private RecyclerView list_produsts;
    private AdapterProduct adapterProduct;
    private AlertDialog dialog;
    private String retornIdUser;
    private User user;
    private FirebaseAuth auth;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_produtos);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("");

        startComponet();
        initDB();
        retornIdUser = UserFirebase.getIdUser();

        recyclerViewConfig();
        recycleOnclick();


        this.auth = FirebaseAuth.getInstance();
        retornaProdutos();
    }


    private void recycleOnclick()
    {
        //Adiciona evento de clique no recyclerview
        list_produsts.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        list_produsts,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Product produtoSelecionado = productsList.get(position);

                                String idProduto = produtoSelecionado.getIdProd();

                                System.setProperty("ID_PRODUTO",idProduto );
                                startEditProd(produtoSelecionado);
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

    //Configura recyclerview
    private void recyclerViewConfig()
    {

        list_produsts.setLayoutManager(new LinearLayoutManager(this));
        list_produsts.setHasFixedSize(true);
        adapterProduct = new AdapterProduct(productsList, this);
        list_produsts.setAdapter(adapterProduct);
    }

    private void startComponet()
    {
        txtProdutos = findViewById(R.id.txtProdutos);
        //RecyclerView---
        list_produsts = findViewById(R.id.list_produsts);
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
        FirebaseApp.initializeApp(ActProdutos.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    public void retornaProdutos()
    {
        //retorna produto
        DatabaseReference productDB = reference.child("product");
        //retorna tipo setado

        //cria um ouvinte
        productDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    Product p = objSnapshot.getValue(Product.class);
                    productsList.add(p);
                }
                adapterProduct.notifyDataSetChanged();
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

    private void pesquisarProduto(String pesquisa){

        DatabaseReference empresasRef = reference
                .child("product");
        Query query = empresasRef.orderByChild("description")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff" );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productsList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    productsList.add( ds.getValue(Product.class) );
                }

                adapterProduct.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void startEditProd(Product product){
        Intent it = new Intent(this, ActRegProd.class);
        it.putExtra("PRODUTO_ENV", product);
        startActivity(it);
    }


}
