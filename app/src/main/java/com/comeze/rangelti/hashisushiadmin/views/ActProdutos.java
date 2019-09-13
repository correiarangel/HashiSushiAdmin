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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterProduct;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.Costs;
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

public class ActProdutos extends AppCompatActivity {

    private FloatingActionButton floatBtnPesquisa;
    private EditText edtPesquisa;

    private DatabaseReference reference;
    private List<Product> productsList = new ArrayList<Product>();
    private RecyclerView list_produsts;
    private AdapterProduct adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_produtos);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Produtos");


        startComponet();
        initDB();

        recyclerViewConfig();
        recycleOnclick();

        retornaProdutos();
    }


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

                                Product produtoSelecionado = productsList.get(position);

                                confirmExclusao(produtoSelecionado);

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
        //RecyclerView---
        list_produsts = findViewById(R.id.list_Orders);
        edtPesquisa = findViewById(R.id.edtPesquisar);
        floatBtnPesquisa = findViewById(R.id.floatBtnPesquisar);

        floatBtnPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVibrate(90);
                String textPesquisa = edtPesquisa.getText().toString();

                if( textPesquisa.equals("")){
                    msgShort("Digite nome de produto para pesquisa !");
                    retornaProdutos();
                }else {
                    pesquisarProduto(textPesquisa);
                }
            }
        });
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

        DatabaseReference produtosRef = reference
                .child("product");
        Query query = produtosRef.orderByChild("description")
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

    //comfirmar item com dialog
    private void confirmExclusao(final Product product )
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Excluir Produto"+product.getName());
        alert.setMessage("Confirma exclusão ? ");


        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                product.remover(product.getIdInterno());
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
            //   Intent it = new Intent(this, ActUsuarios.class);
            //   startActivity(it);
            //   finish();
            // return true;
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
            Intent it = new Intent(this, ActPedidos.class);
            startActivity(it);
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
            finish();
            return true;
        }
        if (id == R.id.menu_info)
        {
            Intent it = new Intent(this, ActInfo.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_ped_entregando)
        {

            Intent it = new Intent(this, ActEntregando.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.menu_custo)
        {
            configCost();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
