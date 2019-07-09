package com.comeze.rangelti.hashisushiadmin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.model.Orders;


import java.util.List;

public class AdapterOrders  extends RecyclerView.Adapter<AdapterOrders.MyViewHolder> {

    private List<Orders> ordersList;
    private Context context;


    public AdapterOrders(List<Orders> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.pedidos_adp_list, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int i) {

        Orders orders = ordersList.get(i);

        holder.idOrders.setText("Cod Pedido :" + orders.getIdOrders());
        holder.idUser.setText("Cod Cliente :" + orders.getIdUser());
        holder.name.setText("Nome :" + orders.getName());
        holder.address.setText("End:" + orders.getAddress());
        holder.numberHome.setText("Numero :" + orders.getNumberHome());
        holder.neigthborhood.setText("Bairro :" + orders.getNeigthborhood());
        holder.cellphone.setText("Fone :" + orders.getCellphone());
        holder.dateOrder.setText("Data :" + orders.getDateOrder());
        holder.hour.setText("Hora :" + orders.getHour());
        holder.qrCode.setText("QrCode :" + orders.getQrCode());
        holder.quantProd.setText("Qt itens :" + orders.getQuantProd());
        holder.discont.setText("Desconto :" + orders.getDiscont());
        holder.status.setText("Status :" + orders.getStatus());
        holder.totalPrince.setText("Total do Pedido:" + orders.getTotalPrince());


    }




    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idOrders;
        TextView idUser;
        TextView name;
        TextView address;
        TextView numberHome;
        TextView neigthborhood;
        TextView cellphone;
        TextView dateOrder;
        TextView hour;
        TextView qrCode;
        TextView quantProd;
        TextView discont;
        TextView status;
        TextView txtordersItens;
        TextView totalPrince;
        TextView observation;

        public MyViewHolder(View itemView) {
            super(itemView);

            idOrders = itemView.findViewById(R.id.txtIdOrder);
            idUser = itemView.findViewById(R.id.txtidUser);
            name = itemView.findViewById(R.id.txtNameClient);
            address = itemView.findViewById(R.id.txtAddress);
            neigthborhood = itemView.findViewById(R.id.txtNeigtborhood);
            numberHome = itemView.findViewById(R.id.txtNumbeHome);
            cellphone = itemView.findViewById(R.id.txtCellPhone);
            dateOrder = itemView.findViewById(R.id.txtDateOrder);
            hour = itemView.findViewById(R.id.txtHour);
            qrCode = itemView.findViewById(R.id.txtQrCode);
            quantProd = itemView.findViewById(R.id.txtQuatProtod);
            discont = itemView.findViewById(R.id.txtDiscont);
            totalPrince = itemView.findViewById(R.id.txtTotalPrince);
            observation = itemView.findViewById(R.id.txtObservation);
            status = itemView.findViewById(R.id.txtStatus);

        }
    }

    public void updateListOrdes(int position){

        this.ordersList.remove(position);
        notifyDataSetChanged();

    }

}