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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
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
import com.comeze.rangelti.hashisushiadmin.adapter.AdapterOrders;
import com.comeze.rangelti.hashisushiadmin.dao.UserFirebase;
import com.comeze.rangelti.hashisushiadmin.listener.RecyclerItemClickListener;
import com.comeze.rangelti.hashisushiadmin.model.Costs;
import com.comeze.rangelti.hashisushiadmin.model.Orders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActPedidosConfirm extends AppCompatActivity {
	
	private DatabaseReference reference;
	private List< Orders > ordersList = new ArrayList<> ( );
	private RecyclerView list_Orders;
	private AdapterOrders adapterOrders;
	private String retornIdUser;
	private Orders orders;
	private AlertDialog alertaDialog;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState ) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.act_pedidos_confirm );
		
		ActionBar bar = getSupportActionBar ( );
		bar.setBackgroundDrawable ( new ColorDrawable ( Color.parseColor ( "#000000" ) ) );
		bar.setTitle ( "Pedidos Confirmados" );
		
		startComponent ( );
		initDB ( );
		retornIdUser = UserFirebase.getIdUser ( );
		
		recyclerViewConfig ( );
		recycleOnclick ( );
		ordersEventListener ( );
	}
	
	@Override
	protected void attachBaseContext ( Context newBase ) {
		super.attachBaseContext ( CalligraphyContextWrapper.wrap ( newBase ) );
	}
	
	private void recycleOnclick ( ) {
		//Adiciona evento de clique no recyclerview
		list_Orders.addOnItemTouchListener (
				
				new RecyclerItemClickListener (
						this,
						list_Orders,
						new RecyclerItemClickListener.OnItemClickListener ( ) {
							@Override
							public void onItemClick ( View view, int position ) {
								
								Orders selectedOrder = ordersList.get ( position );
								confirmStatus ( selectedOrder, position );
								
							}
							
							@Override
							public void onLongItemClick ( View view, int position ) {
								
								Orders selectedOrder = ordersList.get ( position );
								
								String idOrder = selectedOrder.getIdOrders ( );
								//envia id order para actItensOrder
								System.setProperty ( "ID_ORDER", idOrder );
								//chama actItensOrder
								startItem ( );
								
							}
							
							@Override
							public void onItemClick ( AdapterView< ? > parent, View view, int position, long id ) {
							
							}
							
						}
				)
		);
	}
	
	//comfirmar item com dialog
	private void confirmStatus ( final Orders selectedOrder, final int position ) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder ( this );
		alert.setTitle ( "Status do pedido : " + selectedOrder.getStatus ( ) );
		alert.setMessage ( "\nConfirme novo status. " );
		
		final EditText edtStatus = new EditText ( this );
		edtStatus.setText ( "em preparo" );
		
		alert.setView ( edtStatus );
		
		alert.setPositiveButton ( "Confirmar", new DialogInterface.OnClickListener ( ) {
			@Override
			public void onClick ( DialogInterface dialog, int which ) {
				
				String status = edtStatus.getText ( ).toString ( );
				
				orders = new Orders ( );
				orders.editStatus ( status, selectedOrder.getIdOrders ( ) );
				//remove item lista adpter
				adapterOrders.updateListOrdes ( position );
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
	
	private void recyclerViewConfig ( ) {
		//Configura recyclerview
		list_Orders.setLayoutManager ( new LinearLayoutManager ( this ) );
		list_Orders.setHasFixedSize ( true );
		adapterOrders = new AdapterOrders ( ordersList, this );
		list_Orders.setAdapter ( adapterOrders );
	}
	
	private void startComponent ( ) {
		//RecyclerView---
		list_Orders = findViewById ( R.id.list_Orders );
	}
	
	public void initDB ( ) {
		FirebaseApp.initializeApp ( ActPedidosConfirm.this );
		this.reference = FirebaseDatabase.getInstance ( ).getReference ( );
	}
	
	public void ordersEventListener ( ) {
		
		//retorna usuarios
		DatabaseReference pedidosDB = reference.child ( "orders" );
		//retorna o no setado
		Query querySearch = pedidosDB.orderByChild ( "status" ).equalTo ( "confirmado" );
		
		querySearch.addChildEventListener ( new ChildEventListener ( ) {
			@Override
			public void onChildAdded ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {
				Orders orders = dataSnapshot.getValue ( Orders.class );
				
				notificationTypeOne ( );
				ordersList.add ( orders );
				adapterOrders.notifyDataSetChanged ( );
			}
			
			@Override
			public void onChildChanged ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {
			 System.out.println("PEDIDO MODOU Status-------  "+orders.getStatus()); }
			
			@Override
			public void onChildRemoved ( @NonNull DataSnapshot dataSnapshot ) { }
			
			@Override
			public void onChildMoved ( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) { }
			
			@Override
			public void onCancelled ( @NonNull DatabaseError databaseError ) { }
		} );
	}
	
	private void msgShort ( String msg ) {
		//totalVenda = o.getTotalPrince();
		//totalDevendas = totalVenda * quantVendas;
		Toast.makeText ( getApplicationContext ( ), msg, Toast.LENGTH_SHORT ).show ( );
	}
	
	private void startItem ( ) {
		Intent it = new Intent ( this, ActItensOrder.class );
		startActivity ( it );
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
				//notificationTypeTwo();
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
				//notificationTypeTwo();
			}

			notificationManager.notify(1002, mBuilder.build());
		}
	
/*	private void notificationTypeTwo ( ) {
		//Cria o gerador do AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder ( this );
		//define o titulo
		builder.setTitle ( "Novo Pedido!" );
		//define a mensagem
		builder.setMessage ( "Temos mais um pedido confirmado...." );
		//define um botão como positivo
		builder.setPositiveButton ( "Continuar", new DialogInterface.OnClickListener ( ) {
			public void onClick ( DialogInterface arg0, int arg1 ) {
			}
		} );
		
		builder.setNegativeButton ( "Voltar", new DialogInterface.OnClickListener ( ) {
			public void onClick ( DialogInterface arg0, int arg1 ) {
				Intent it = new Intent ( getApplicationContext ( ), ActHome.class );
				startActivity ( it );
				finish ( );
			}
		} );
		//cria o AlertDialog
		alertaDialog = builder.create ( );
		//Exibe
		alertaDialog.show ( );
	}*/
	
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
				Intent it = new Intent ( this, ActPedidosConfirm.class );
				startActivity ( it );
				finish ( );
				return true;
			}
			case R.id.menu_usuarios: {
				Intent it = new Intent ( this, ActUsuarios.class );
				startActivity ( it );
				finish ( );
				return true;
			}
			case R.id.menu_cadastrar_prod: {
				Intent it = new Intent ( this, ActRegProd.class );
				startActivity ( it );
				finish ( );
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
				finish ( );
				return true;
			}
			case R.id.menu_info: {
				Intent it = new Intent ( this, ActInfo.class );
				startActivity ( it );
				finish ( );
				return true;
			}
			case R.id.menu_pedidos_confirm:
				msgShort ( "Você já está em Pedidos confirmados!" );
				return true;
			case R.id.menu_home:
				finish ( );
				return true;
			case R.id.menu_ped_entregando: {
				
				Intent it = new Intent ( this, ActEntregando.class );
				startActivity ( it );
				return true;
			}
			case R.id.menu_custo:
				configCost ( );
				return true;
		}
		
		return super.onOptionsItemSelected ( item );
	}
	
	// dialog para configurar valor de entrega
	public void configCost ( ) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder ( this );
		alert.setTitle ( "Custo de entrega" );
		alert.setMessage ( "\nConfigure o custo de sua entrega." );
		
		final EditText editCustoEntrega = new EditText ( this );
		
		LinearLayout layoutFilds = new LinearLayout ( this );
		layoutFilds.setOrientation ( LinearLayout.VERTICAL );
		
		layoutFilds.addView ( editCustoEntrega );
		editCustoEntrega.setHint ( "Digite somente valores monetários!" );
		
		alert.setView ( layoutFilds );
		
		alert.setPositiveButton ( "Salvar Custo", new DialogInterface.OnClickListener ( ) {
			@Override
			public void onClick ( DialogInterface dialog, int which ) {
				
				String custoEntrega = editCustoEntrega.getText ( ).toString ( );
				
				if ( validaValor ( custoEntrega ) == 1 ) {
					msgShort ( "Atenção! Campo vazio ou valor incorreto!" );
				} else {
					msgShort ( "Custo de entrega salvo com sucesso!" );
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
			msgShort ( "Por favor, informe um valor monetario :=[" );
			return 1;
		} else return 0;
	}
}
