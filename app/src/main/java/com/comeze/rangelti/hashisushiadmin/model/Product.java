package com.comeze.rangelti.hashisushiadmin.model;

import com.comeze.rangelti.hashisushiadmin.dao.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Product implements Serializable {
	
	private String description;
	private String idProd;
	private String idInterno;
	private Boolean isPromotion;
	private String name;
	private String salePrice;
	private String type;
	private String imgUrl;
	
	
	public Product ( ) {
	
	}
	
	public void salvar ( ) {
		
		DatabaseReference firebaseRef = FirebaseConfig.getFirebase ( );
		DatabaseReference produtoRef = firebaseRef
				.child ( "product" )
				.push ( );
		//pega retorno de id gerado com push
		String internoId = produtoRef.getKey ( );
		setIdInterno ( internoId );
		
		produtoRef.setValue ( this );
		
	}
	
	public void atualisar ( String id ) {
		
		DatabaseReference firebaseRef = FirebaseConfig.getFirebase ( );
		DatabaseReference produtoRef = firebaseRef
				.child ( "product" )
				.child ( id );
		produtoRef.setValue ( this );
	}
	
	public void remover ( String id ) {
		DatabaseReference firebaseRef = FirebaseConfig.getFirebase ( );
		DatabaseReference produtoRef = firebaseRef
				.child ( "product" )
				.child ( id );
		produtoRef.removeValue ( );
	}
	
	public String getIdInterno ( ) {
		return idInterno;
	}
	
	public void setIdInterno ( String idInterno ) {
		this.idInterno = idInterno;
	}
	
	public String getDescription ( ) {
		return description;
	}
	
	public void setDescription ( String description ) {
		this.description = description;
	}
	
	public String getIdProd ( ) {
		return idProd;
	}
	
	public void setIdProd ( String idProd ) {
		this.idProd = idProd;
	}
	
	public Boolean getPromotion ( ) {
		return isPromotion;
	}
	
	public void setPromotion ( Boolean promotion ) {
		isPromotion = promotion;
	}
	
	public String getName ( ) {
		return name;
	}
	
	public void setName ( String name ) {
		this.name = name;
	}
	
	public String getSalePrice ( ) {
		return salePrice;
	}
	
	public void setSalePrice ( String salePrice ) {
		this.salePrice = salePrice;
	}
	
	public String getType ( ) {
		return type;
	}
	
	public void setType ( String type ) {
		this.type = type;
	}
	
	public String getImgUrl ( ) {
		return imgUrl;
	}
	
	public void setImgUrl ( String imgUrl ) {
		this.imgUrl = imgUrl;
	}
	
	@Override
	public String toString ( ) {
		return "Product{" +
				"description='" + description + '\'' +
				", idProd='" + idProd + '\'' +
				", isPromotion=" + isPromotion +
				", name='" + name + '\'' +
				", salePrice='" + salePrice + '\'' +
				", type='" + type + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				'}';
	}
}
