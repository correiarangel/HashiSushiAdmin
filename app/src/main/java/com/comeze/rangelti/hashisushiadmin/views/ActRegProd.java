package com.comeze.rangelti.hashisushiadmin.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.dao.FirebaseConfig;
import com.comeze.rangelti.hashisushiadmin.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActRegProd extends AppCompatActivity implements View.OnClickListener {
	
	private static final int SELECAO_GALERIA = 200;
	private String[] type = { "Entrada", "Pratos", "Monte_Prato",
			"Temakis", "Combo", "Porções", "Bebidas", "Adicionais" };
	private String[] isPromotion = { "Não", "Sim" };
	private Spinner spnType;
	private Spinner spnIsPrmotion;
	private EditText edtDiscriptionProd;
	private EditText edtNumberPro;
	private EditText edtNameProd;
	private EditText edtValProd;
	private EditText edtUrl;
	private ImageView imgVwProduction;
	private FloatingActionButton flotBntSaveReg;
	private FloatingActionButton flotBntNewReg;
	private FloatingActionButton flotBntExitReg;
	private StorageReference storageReference;
	private DatabaseReference firebaseReference;
	private String idIntenoProduto;
	private String urlImagemSelecionada = "";
	//private String ID_PRODUTO_RECEBIDO;
	private Product product;
	
	private Bundle extrasProd;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState ) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.act_reg_prod );
		
		ActionBar bar = getSupportActionBar ( );
		bar.setBackgroundDrawable ( new ColorDrawable ( Color.parseColor ( "#000000" ) ) );
		bar.setTitle ( "Cadastro de Produtos" );
		
		startCompnent ( );
		
		isPromotionSpn ( );
		typeSpn ( );
		startDB ( );
		
		flotBntSaveReg.setOnClickListener ( this );
		flotBntNewReg.setOnClickListener ( this );
		flotBntExitReg.setOnClickListener ( this );
		
		imgVwProduction.setOnClickListener ( new View.OnClickListener ( ) {
			@Override
			public void onClick ( View v ) {
				startVibrate ( 90 );
				getImage ( );
			}
		} );
		
		extrasProd = getIntent ( ).getExtras ( );
		if ( extrasProd != null ) {
			recebeProduto ( );
		}
	}
	
	private void getImage ( ) {
		
		String idProd = edtNumberPro.getText ( ).toString ( );
		if ( idProd.equals ( "" ) ) {
			
			msgShort ( "Defina um valor para Nº do produto." );
			
		} else {
			Intent intent = new Intent (
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI
			);
			if ( intent.resolveActivity ( getPackageManager ( ) ) != null ) {
				startActivityForResult ( intent, SELECAO_GALERIA );
			}
		}
	}
	
	private void startDB ( ) {
		storageReference = FirebaseConfig.getFirebaseStorage ( );
		firebaseReference = FirebaseConfig.getFirebase ( );
	}
	
	@Override
	protected void attachBaseContext ( Context newBase ) {
		super.attachBaseContext ( CalligraphyContextWrapper.wrap ( newBase ) );
	}
	
	private void startCompnent ( ) {
		spnType = findViewById ( R.id.spnType );
		spnIsPrmotion = findViewById ( R.id.spnIsPrmotion );
		
		edtDiscriptionProd = findViewById ( R.id.edtDiscriptionProd );
		edtNumberPro = findViewById ( R.id.edtNumberPro );
		edtNameProd = findViewById ( R.id.edtNameProd );
		edtValProd = findViewById ( R.id.edtValProd );
		edtUrl = findViewById ( R.id.edtUrl );
		
		imgVwProduction = findViewById ( R.id.imgVwProduction );
		flotBntSaveReg = findViewById ( R.id.flotBntSaveReg );
		flotBntNewReg = findViewById ( R.id.flotBntNewReg );
		flotBntExitReg = findViewById ( R.id.flotBntExitReg );
	}
	
	private void clearFilds ( ) {
		edtDiscriptionProd.setText ( "" );
		edtNumberPro.setText ( "" );
		edtNameProd.setText ( "" );
		edtValProd.setText ( "" );
		edtUrl.setText ( "" );
		
		imgVwProduction.setImageResource ( R.drawable.lghashi );
	}
	
	private void isPromotionSpn ( ) {
		try {
			ArrayAdapter< String > adapter = new ArrayAdapter< String > ( this,
					android.R.layout.simple_list_item_1, isPromotion );
			adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
			spnIsPrmotion.setAdapter ( adapter );
		} catch ( Exception ex ) {
			msgShort ( "Erro:-->" + ex.getMessage ( ) );
		}
	}
	
	private void typeSpn ( ) {
		try {
			ArrayAdapter< String > adapter = new ArrayAdapter< String > ( this,
					android.R.layout.simple_list_item_1, type );
			adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
			spnType.setAdapter ( adapter );
		} catch ( Exception ex ) {
			msgShort ( "Erro:-->" + ex.getMessage ( ) );
		}
	}
	
	private void msgShort ( String msg ) {
		Toast.makeText ( getApplicationContext ( ), msg, Toast.LENGTH_SHORT ).show ( );
	}
	
	//Metudo que ativa vibração
	public void startVibrate ( long time ) {
		// cria um obj atvib que recebe seu valor de context
		Vibrator atvib = ( Vibrator ) getSystemService ( Context.VIBRATOR_SERVICE );
		atvib.vibrate ( time );
	}
	
	@Override
	public void onClick ( View v ) {
		switch ( v.getId ( ) ) {
			case R.id.flotBntNewReg:
				startVibrate ( 90 );
				clearFilds ( );
				break;
				
			case R.id.flotBntSaveReg:
				startVibrate ( 90 );
				addProd ( );
				clearFilds ( );
				break;
				
			case R.id.flotBntExitReg:
				startVibrate ( 90 );
				finish ( );
				break;
		}
	}
	
	private void addProd ( ) {
		
		String descricao = edtDiscriptionProd.getText ( ).toString ( );
		String numberProd = edtNumberPro.getText ( ).toString ( );
		String name = edtNameProd.getText ( ).toString ( );
		String valProd = edtValProd.getText ( ).toString ( );
		String url = edtUrl.getText ( ).toString ( );
		
		if ( descricao.equals ( "" ) && numberProd.equals ( "" ) && name.equals ( "" ) &&
				url.equals ( "" ) && valProd.equals ( "" ) ) {
			msgShort ( "Há campos sem valor! Por favor, preencha todos os campos." );
		} else {
			try {
				Product p = new Product ( );
				
				p.setName ( edtNameProd.getText ( ).toString ( ) );
				p.setDescription ( edtDiscriptionProd.getText ( ).toString ( ) );
				p.setSalePrice ( edtValProd.getText ( ).toString ( ) );
				p.setIdProd ( edtNumberPro.getText ( ).toString ( ) );
				p.setIdInterno ( idIntenoProduto );
				//captura resposta
				String strProm = spnIsPrmotion.getSelectedItem ( ).toString ( );
				
				boolean boolPromo = false;
				// se sim retor true
				boolPromo = strProm.equals ( "Sim" );
				p.setPromotion ( boolPromo );
				
				String strType = spnType.getSelectedItem ( ).toString ( );
				p.setType ( strType );
				
				p.setImgUrl ( edtUrl.getText ( ).toString ( ) );
				
				//caso receba um produto edita
				if ( extrasProd != null ) {
					p.atualisar ( idIntenoProduto );
				} else {
					p.salvar ( );
				}
				
				msgShort ( "Cadastro realizado com sucesso!" );
				
				clearFilds ( );
				
			} catch ( Exception erro ) {
				msgShort ( "Erro na gravação de produto! Erro: " + erro );
			}
		}
	}
	
	@Override
	protected void onActivityResult ( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult ( requestCode, resultCode, data );
		
		if ( resultCode == RESULT_OK ) {
			Bitmap imagem = null;
			
			try {
				
				switch ( requestCode ) {
					case SELECAO_GALERIA:
						Uri localImagem = data.getData ( );
						imagem = MediaStore.Images
								.Media
								.getBitmap (
										getContentResolver ( ),
										localImagem
								);
						break;
				}
				
				if ( imagem != null ) {
					
					imgVwProduction.setImageBitmap ( imagem );
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream ( );
					imagem.compress ( Bitmap.CompressFormat.JPEG, 70, baos );
					byte[] dadosImagem = baos.toByteArray ( );
					
					String nameImagem = UUID.randomUUID ( ).toString ( );
					StorageReference imagemRef = storageReference
							.child ( "produtos" )
							.child ( "img" )
							.child ( nameImagem + "jpeg" );
					
					final UploadTask uploadTask = imagemRef.putBytes ( dadosImagem );
					uploadTask.addOnFailureListener ( new OnFailureListener ( ) {
						@Override
						public void onFailure ( @NonNull Exception e ) {
							msgShort ( "Erro ao fazer upload da imagem !" );
						}
					} ).addOnSuccessListener ( new OnSuccessListener< UploadTask.TaskSnapshot > ( ) {
						@Override
						public void onSuccess ( UploadTask.TaskSnapshot taskSnapshot ) {
							
							urlImagemSelecionada = taskSnapshot.getDownloadUrl ( ).toString ( );
							
							edtUrl.setText ( urlImagemSelecionada );
							msgShort ( "Sucesso ao carregar a imagem!" );
						}
					} );
				}
				
			} catch ( Exception e ) {
				e.printStackTrace ( );
			}
		}
	}
	
	//recebe produto selecionado para edição
	private void recebeProduto ( ) {
		
		if ( extrasProd != null ) {
			product = ( Product ) extrasProd.getSerializable ( "PRODUTO_ENV" );
			
			idIntenoProduto = product.getIdInterno ( );
			
			edtNameProd.setText ( product.getName ( ) );
			edtDiscriptionProd.setText ( product.getDescription ( ) );
			String srtSalePrice = String.valueOf ( product.getSalePrice ( ) );
			edtValProd.setText ( srtSalePrice );
			edtNumberPro.setText ( product.getIdProd ( ) );
			//  spnIsPrmotion.setSelected(product.getPromotion());
			// spnType.setSelection();
			edtUrl.setText ( product.getImgUrl ( ) );
			
			urlImagemSelecionada = product.getImgUrl ( );
			if ( urlImagemSelecionada != "" ) {
				Picasso.get ( )
						.load ( urlImagemSelecionada )
						.into ( imgVwProduction );
			}
		}
	}
	
}

