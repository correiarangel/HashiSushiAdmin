package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActEditarUser extends AppCompatActivity  implements View.OnClickListener {

    private EditText userName, userCPF, userBornDate ,edtPontos;
    private EditText userAddressStreet, userAddressNeighborhood, userAddressNumber;
    private EditText userAddressCity, userAddressCEP, userAddressState;
    private EditText userEmail, userPhone, userPassword, userPasswordRetype, userReferencePoint;
    private Button btnSignUp;
    private ScrollView ActSignUp;
    private DatabaseReference reference;
    private String retornIdUser ;
    private User user;

    private String[] isAdmin = { "Não", "Sim" };
    private Spinner spnIsAdmin;

    private Bundle extrasUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_user);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        bar.setTitle("Editar Usuario");

        //reference db and recover value
        startDB();

        //Travæ rotaçãø da tela
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViewById();
        isAdmSpn();
        btnSignUp.setOnClickListener(this);
        setFilds();

        extrasUser = getIntent ( ).getExtras ( );
        if ( extrasUser != null ) {
            setFilds ( );
        }
    }

    //finaliza se voltar
    @Override
    public void onBackPressed()
    {
        finish();
    }


    private void startDB()
    {
        FirebaseApp.initializeApp(ActEditarUser.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void startVibrate(long time)
    {
        // cria um obj atvib que recebe seu valor de context
        Vibrator atvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        atvib.vibrate( time );
    }

    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.button_user_signup)
        {
            startVibrate(90);

            if (userName.getText().toString().equals(""))
            {
                ShowMSG();
                userName.setError(getString(R.string.your_name));
            }
            else if (userCPF.getText().toString().equals(""))
            {
                ShowMSG();
                userCPF.setError(getString(R.string.your_cpf));
            }
            else if (userEmail.getText().toString().equals(""))
            {
                ShowMSG();
                userEmail.setError(getString(R.string.your_email2));
            }
            else if (userPhone.getText().toString().equals(""))
            {
                ShowMSG();
                userPhone.setError(getString(R.string.your_phone));
            }
            else if (userPassword.getText().toString().equals(""))
            {
                ShowMSG();
                userPassword.setError(getString(R.string.your_password));
            }
            else if (userPassword.getText().length() < 6)
            {
                Snackbar.make(ActSignUp, R.string.wrong_pass, Snackbar.LENGTH_LONG).show();
                userPassword.setError(getString(R.string.try_another_pass));
            }
            else if (!userPasswordRetype.getText().toString().equals(userPassword.getText().toString()))
            {
                Snackbar.make(ActSignUp, R.string.pass_not_equals2, Snackbar.LENGTH_LONG).show();
                userPasswordRetype.setError(getString(R.string.pass_not_equals));
            }
            else
            {
                editarUser( retornIdUser );
            }
        }
    }

    private void editarUser( String idUser)
    {
        try
        {
            user = new User();

            user.setIdUser( retornIdUser );
            user.setName(userName.getText().toString());
            user.setBornDate(userBornDate.getText().toString());

            //captura resposta
            String strIsAdm = spnIsAdmin.getSelectedItem ( ).toString ( );
            boolean boolPromo = false;
            // se sim retorn true
            boolPromo = strIsAdm.equals ( "Sim" );
            user.setIsAdmin( boolPromo );

            if(userReferencePoint.getText().toString().equals(""))
            {
                user.setAddress(userAddressStreet.getText().toString());
            }
            else
            {
                user.setAddress(userAddressStreet.getText().toString() + " - Ponto de referência: " + userReferencePoint.getText().toString());
            }
            user.setNeigthborhood(userAddressNeighborhood.getText().toString());
            user.setNumberHome(userAddressNumber.getText().toString());
            user.setCity(userAddressCity.getText().toString());
            user.setCep(userAddressCEP.getText().toString());
            user.setState(userAddressState.getText().toString());
            user.setPhone(userPhone.getText().toString());
            user.setEmail(userEmail.getText().toString());
            user.setPassword(userPassword.getText().toString());
            user.setCpf(userCPF.getText().toString());

            //captura ponto
            String strPontos = edtPontos.getText().toString();
            //conveter para int
            int intPontos = Integer.parseInt( strPontos );
            user.setPonts( intPontos );

            //novo metudo para salvar user
            user.atualisarUser( idUser );

            clearFilds();
            finish();

            msgShort("Cadastro Editado com sucesso!");

        }
        catch (Exception erro)
        {
            msgShort("Erro na edição de cadastro !");
        }
    }


    private void ShowMSG()
    {
        Snackbar.make(ActSignUp, R.string.preencha_os_campos, Snackbar.LENGTH_LONG).show();
    }

    private void findViewById()
    {
        userName = findViewById(R.id.user_name);
        edtPontos = findViewById(R.id.edtPontos);
        spnIsAdmin = findViewById(R.id.spnIsAdmin);
        userCPF = findViewById(R.id.user_cpf);
        userBornDate = findViewById(R.id.user_born_date);
        userAddressStreet = findViewById(R.id.user_address_street);
        userAddressNumber = findViewById(R.id.user_address_number);
        userAddressNeighborhood = findViewById(R.id.user_address_neighborhood);
        userAddressCity = findViewById(R.id.user_address_city);
        userAddressCEP = findViewById(R.id.user_cep);
        userAddressState = findViewById(R.id.user_adress_state);
        userPassword = findViewById(R.id.user_password);
        userPasswordRetype = findViewById(R.id.user_password_RETYPE);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        btnSignUp = findViewById(R.id.button_user_signup);
        ActSignUp = findViewById(R.id.ActSignUp);
        userReferencePoint = findViewById(R.id.user_reference_point);
    }

    private void msgShort(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //recebe user selecionado para edição
    private void setFilds ( ) {


        if ( extrasUser != null ) {
            User u = (User) extrasUser.getSerializable ( "USER_ENV" );

            retornIdUser = u.getIdUser();

            userName.setText(u.getName());

            String p = String.valueOf(u.getPonts());

            edtPontos.setText( p );
            userCPF.setText(u.getCpf());
            userBornDate.setText(u.getBornDate());
            userAddressStreet.setText(u.getAddress());
            userAddressNumber.setText(u.getNumberHome());
            userAddressNeighborhood.setText(u.getNeigthborhood());
            userAddressCity.setText(u.getCity());
            userAddressCEP.setText(u.getCep());
            userAddressState.setText(u.getState());
            userPassword.setText(u.getPassword());
            userEmail.setText(u.getEmail());
            userPhone.setText(u.getPhone());
            userReferencePoint.setText(u.getReferencePoint());

        }
    }

    private void clearFilds(){

        userName.setText("");
        edtPontos.setText("");
        userCPF.setText("");
        userBornDate.setText("");
        userAddressStreet.setText("");
        userAddressNumber.setText("");
        userAddressNeighborhood.setText("");
        userAddressCity.setText("");
        userAddressCEP.setText("");
        userAddressState.setText("");
        userPassword.setText("");
        userEmail.setText("");
        userPhone.setText("");
        userReferencePoint.setText("");

    }

    private void isAdmSpn ( ) {
        try {
            ArrayAdapter< String > adapter = new ArrayAdapter< String > ( this,
                    android.R.layout.simple_list_item_1, isAdmin );
            adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
            spnIsAdmin.setAdapter ( adapter );
        } catch ( Exception ex ) {
            msgShort ( "Erro:-->" + ex.getMessage ( ) );
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
            Intent it = new Intent(this, ActProdutos.class);
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
