package com.comeze.rangelti.hashisushiadmin.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//app center

public class ActHome extends AppCompatActivity implements View.OnClickListener {
	
	private DatabaseReference reference;
	private Button btnCadProd;
	private Button btnPedidos;
	private Button btnProdutos;
	private String retornIdUser;
	private ConstraintLayout ActHome;
	private boolean typeUser;
	private User user;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState ) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.act_home );
		
		ActionBar bar = getSupportActionBar ( );
		bar.setBackgroundDrawable ( new ColorDrawable ( Color.parseColor ( "#000000" ) ) );
		bar.setTitle ( "Hashi Sushi ADM" );
		
		initComponents ( );
		initDB ( );
		retornIdUser = UserFirebase.getIdUser ( );
		recoveryDataUser ( );
		listesnerEventPedidos ( );//escuta pedidos
		
		AppCenter.start ( getApplication ( ), "932cfb85-f2d8-4a22-a32c-516f23b9ceb8",
				Analytics.class, Crashes.class );
	}
	
	@Override
	protected void attachBaseContext ( Context newBase ) {
		super.attachBaseContext ( CalligraphyContextWrapper.wrap ( newBase ) );
	}
	
	private void initComponents ( ) {
		btnCadProd = findViewById ( R.id.btnCdaProd );
		btnPedidos = findViewById ( R.id.btntPedido );
		btnProdutos = findViewById ( R.id.btnProdotos );
		ActHome= findViewById ( R.id.ActHome );
		
		btnPedidos.setOnClickListener ( this );
		btnCadProd.setOnClickListener ( this );
		btnProdutos.setOnClickListener ( this );
	}
	
	@Override
	public void onClick ( View v ) {
		switch ( v.getId ( ) ) {
			case R.id.btnCdaProd: {
				startVibrate ( 90 );
				Intent it = new Intent ( this, ActRegProd.class );
				startActivity ( it );
				break;
			}
			case R.id.btntPedido: {
				startVibrate ( 90 );
				Intent it = new Intent ( this, ActPedidosConfirm.class );
				startActivity ( it );
				
				break;
			}
			case R.id.btnProdotos: {
				startVibrate ( 90 );
				Intent it = new Intent ( this, ActProdutos.class );
				startActivity ( it );
				break;
			}
		}
	}
	
	public void startVibrate ( long time ) {
		// cria um obj atvib que recebe seu valor de context
		Vibrator atvib = ( Vibrator ) getSystemService ( Context.VIBRATOR_SERVICE );
		atvib.vibrate ( time );
	}
	
	//==> MENUS
	@Override
	public boolean onCreateOptionsMenu ( Menu menu ) {
		getMenuInflater ( ).inflate ( R.menu.menu_promotion, menu );
		return true;
	}
	
	@TargetApi ( Build.VERSION_CODES.LOLLIPOP )
	@RequiresApi ( api = Build.VERSION_CODES.LOLLIPOP )
	@Override
	public boolean onOptionsItemSelected ( MenuItem item ) {
		int id = item.getItemId ( );
		
		switch ( id ) {
			case R.id.menu_produtos: {
				Intent it = new Intent ( this, ActProdutos.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_usuarios: {
				Intent it = new Intent ( this, ActUsuarios.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_cadastrar_prod: {
				Intent it = new Intent ( this, ActRegProd.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_pedidos: {
				Intent it = new Intent ( this, ActPedidos.class );
				startActivity ( it );
				finish ( );
				return true;
			}
			case R.id.menu_ped_andamento: {
				Intent it = new Intent ( this, ActPedPreparo.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_pedidos_confirm: {
				Intent it = new Intent ( this, ActPedidosConfirm.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_home:
				msgShort ( "Já estamos em Home !" );
				return true;
			case R.id.menu_ped_entregando: {
				
				Intent it = new Intent ( this, ActEntregando.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_info: {
				Intent it = new Intent ( this, ActInfo.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_custo:
				configCost ( );
				return true;
		}
		return super.onOptionsItemSelected ( item );
	}
	
	private void msgShort( String msg ) {
		Toast.makeText ( getApplicationContext ( ), msg, Toast.LENGTH_SHORT ).show ( );
	}
	
	public void initDB ( ) {
		FirebaseApp.initializeApp ( ActHome.this );
		this.reference = FirebaseDatabase.getInstance ( ).getReference ( );
	}
	
	//recupera dados do usuario esta com
	private void recoveryDataUser ( ) {
		DatabaseReference usuariosDB = reference.child ( "users" ).child ( retornIdUser );
		
		usuariosDB.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
			@Override
			public void onDataChange ( DataSnapshot dataSnapshot ) {
				if ( dataSnapshot.getValue ( ) != null ) {
					user = dataSnapshot.getValue ( User.class );
				}
				
				typeUser = user.getIsAdmin ( );
				
				if ( typeUser != true ) {
					String msg = "Vocẽ não é Admin,o App será finalizado !";
					Toast.makeText ( getApplicationContext ( ), msg, Toast.LENGTH_LONG ).show ( );
					finish ( );
				}
			}
			
			@Override
			public void onCancelled ( DatabaseError databaseError ) {
			}
		} );
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
				
				if ( validaValor ( custoEntrega ) == 1 ) {
					
					msgShort ( "Atenção campo vazio ou valor incorreto !" );
					
				} else {
					msgShort ( "Custo de entrega salvo com sucesso !" );
					//instacia Costs e seta e salva valor
					Costs costs = new Costs ( );
					costs.editarCusto ( custoEntrega );
				}
			}
		} );
		
		alert.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
			@Override
			public void onClick ( DialogInterface dialog, int which ) {
			}
		} );
		AlertDialog dialog = alert.create ( );
		dialog.show ( );
	}
	
	
	private int validaValor ( String valor ) {//valida se o valor digitado é numérico
		String regexStr = "^(([1-9]\\d{0,2}(\\.\\d{3})*)|(([1-9]\\.\\d*)?\\d))(\\,\\d\\d)?";
		if ( !valor.trim ( ).matches ( regexStr ) ) {
			msgShort ( "Por favor, informe um valor monetário" );
			return 1;
		} else return 0;
	}
	
	public void listesnerEventPedidos ( ) {
		//retorna usuarios
		DatabaseReference pedidosDB = reference.child ( "orders" );
		//retorna o no setado
		Query querySearch = pedidosDB.orderByChild ( "status" ).equalTo ( "confirmado" );
		
		querySearch.addChildEventListener ( new ChildEventListener ( ) {
			@Override
			public void onChildAdded ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {
				Orders orders = dataSnapshot.getValue ( Orders.class );
				notificationTypeOne ( );
			}
			
			@Override
			public void onChildChanged ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {}
			
			@Override
			public void onChildRemoved ( @NonNull DataSnapshot dataSnapshot ) {}
			
			@Override
			public void onChildMoved ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {}
			
			@Override
			public void onCancelled ( @NonNull DatabaseError databaseError ) {}
		} );
	}


	private void notificationTypeOne( ){
		int notification_id = (int) System.currentTimeMillis();
		NotificationManager notificationManager = null;
		NotificationCompat.Builder mBuilder;
        String  ticker = "Chegou Pedido" ;
        String title = "Novo Pedido";
		String CHANNEL_DESCRIPTION = "Cheque a lista de pedido... " ; //ms
		String body = CHANNEL_DESCRIPTION  ; //title
		String CHANNEL_NAME = title; //title
		//Set pending intent to builder
		Intent intent = new Intent(getApplicationContext(), ActPedidosConfirm.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

		//Notification builder
		if (notificationManager == null){
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}


		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = notificationManager.getNotificationChannel(ticker);
			if (mChannel == null){
				mChannel = new NotificationChannel(ticker, CHANNEL_NAME, importance);
				mChannel.setDescription(CHANNEL_DESCRIPTION);
				mChannel.enableVibration(true);
				mChannel.getSound();
				mChannel.setLightColor(Color.GREEN);
				mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
				notificationManager.createNotificationChannel(mChannel);
			}

			mBuilder = new NotificationCompat.Builder(this, ticker);
			mBuilder.setContentTitle(title)
					.setSmallIcon(R.drawable.iconstrave)
					.setContentText(body) //show icon on status bar
					.setContentIntent(pendingIntent)
					.setAutoCancel(true)
					.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
					.setDefaults(Notification.DEFAULT_ALL);
			notificationTypeTwo();
		}else {
			mBuilder = new NotificationCompat.Builder(this);
			mBuilder.setContentTitle(title)
					.setSmallIcon(R.drawable.iconstrave)
					.setContentText(body)
					.setPriority(Notification.PRIORITY_HIGH)
					.setContentIntent(pendingIntent)
					.setAutoCancel(true)
					.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
					.setDefaults(Notification.DEFAULT_VIBRATE);
			notificationTypeTwo();
		}

		notificationManager.notify(1002, mBuilder.build());
	}


	private void notificationTypeTwo ( ) {
		//Snackbar.make ( ActHome, "Novo pedido recebido!", Snackbar.LENGTH_LONG ).show ( );
		
		Snackbar mySnackbar = Snackbar.make(ActHome,
				"Novo pedido recebido!", Snackbar.LENGTH_LONG);
		mySnackbar.setAction("Ver Pedidos", new MyUndoListener());
		mySnackbar.show();
	}
	
	public class MyUndoListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent it = new Intent ( getApplicationContext (), ActPedidosConfirm.class );
			startActivity ( it );
		}
	}
}