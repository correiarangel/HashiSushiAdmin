package com.comeze.rangelti.hashisushiadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.model.OrderItens;

import java.util.List;

public class AdapterItensPedido extends
		RecyclerView.Adapter< AdapterItensPedido.MyViewHolder > {
	
	private List< OrderItens > orderItens;
	
	public AdapterItensPedido ( List< OrderItens > orderItens ) {
		this.orderItens = orderItens;
	}
	
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int i ) {
		View itemLista = LayoutInflater.from ( parent.getContext ( ) ).
				inflate ( R.layout.itens_pedido_adp_list, parent, false );
		return new MyViewHolder ( itemLista );
	}
	
	@Override
	public void onBindViewHolder ( @NonNull MyViewHolder holder, int i ) {
		
		OrderItens itens = orderItens.get ( i );
		
		holder.idProd.setText ( String.format ( "Cód. produto: %s", itens.getIdProduct ( ) ) );
		holder.nameProduction.setText ( String.format ( "Nome produto: %s", itens.getNameProduct ( ) ) );
		holder.sales_price.setText ( String.format ( "R$: %s", itens.getItenSalePrice ( ) ) );
		holder.quantity.setText ( String.format ( "Itens: %d", itens.getQuantity ( ) ) );
	}
	
	@Override
	public int getItemCount ( ) {
		return orderItens.size ( );
	}
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		
		TextView nameProduction;
		TextView quantity;
		TextView sales_price;
		TextView idProd;
		
		public MyViewHolder ( View itemView ) {
			super ( itemView );
			
			nameProduction = itemView.findViewById ( R.id.txtName );
			quantity = itemView.findViewById ( R.id.txtQuant );
			sales_price = itemView.findViewById ( R.id.txtSalesPrice );
			idProd = itemView.findViewById ( R.id.txtIdProd );
			
		}
	}
}