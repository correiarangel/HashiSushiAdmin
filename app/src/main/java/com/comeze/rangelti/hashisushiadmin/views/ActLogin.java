package com.comeze.rangelti.hashisushiadmin.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActLogin extends AppCompatActivity implements View.OnClickListener  {

    private Button btnEntrar;
    private TextView txtLogo;
    private EditText edtEmail,edtSenha;
    private String senha,email;
    private int cont;
    private char controlBtn;
    private FirebaseAuth userAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        getSupportActionBar().hide();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //Trava a rotaçãø da tela

        findViewByIds();

        if (!edtEmail.getText().toString().equals(""))
        {
            edtSenha.requestFocus();
        }

        startDB();
        testUserCurrent();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void startDB()
    {
        FirebaseApp.initializeApp(ActLogin.this);
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.userAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnEntrar)
        {

            controlBtn = 'E';
            startVibrate(90);
            validateFields();
        }

    }


    //login user in firebase
    public void login(String email, String senha)
    {
        userAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(ActLogin.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            msgShort(getString(R.string.welcome));
                            startHome();
                        }
                        else
                        {
                            msgShort(getString(R.string.error_to_access));
                            //desloga
                            userAuth.signOut();
                        }
                    }
                });
    }

    //create user in firebase
    public void addUserLogin(String email, String senha)
    {
        userAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(ActLogin.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            msgShort("Pré cadastro concluido.");
                            msgShort("Só mais um momento e terminamos tudo!");
                        }
                        else
                        {
                            msgShort("Infelizmente não foi possível concluir o cadastro :-(");
                            Log.i("Erro", "Infelizmente não foi possível concluir o cadastro :(");
                            //desloga
                            userAuth.signOut();
                        }
                    }
                });
    }

    private void startHome()
    {
        Intent it = new Intent(this, ActHome.class);
        startActivity(it);
    }

    //Método que ativa vibração
    public void startVibrate(long time)
    {
        // cria um obj atvib que recebe seu valor de context
        Vibrator atvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        atvib.vibrate(time);
    }

    public void validateFields()
    {
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        if (cont <= 3)
        {
            if (email.trim().isEmpty() || senha.trim().isEmpty())
            {
                cont++;
               msgShort("Digite email e senha");
            }
            else
            {
                if (controlBtn == 'E')
                {
                    login(email, senha);


                }if(controlBtn == 'C')
            {
                addUserLogin(email,senha);
            }

                //System.setProperty("STATUS_ENV", STATUS);
                clearFields();
                cont = 0;
            }
        }
        else
        {
            finaliza();
        }
    }

    private void finaliza()
    {
        msgShort(getString(R.string.app_finished));
        finish();
    }

    private void msgShort(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void clearFields()
    {
        edtEmail.setText("");
        edtSenha.setText("");
    }


    private void findViewByIds()
    {
        btnEntrar = findViewById(R.id.btnEntrar);
        txtLogo = findViewById(R.id.txtLogoC);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar.setOnClickListener(this);

    }

    //case user login  ok  actpromotion
    public void testUserCurrent(){

        if (userAuth.getCurrentUser() != null)
        {
            Intent it = new Intent(this, ActHome.class);
            startActivity(it);
            finish();
        }

    }
}
