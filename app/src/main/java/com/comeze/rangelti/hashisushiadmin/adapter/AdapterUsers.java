package com.comeze.rangelti.hashisushiadmin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeze.rangelti.hashisushiadmin.R;
import com.comeze.rangelti.hashisushiadmin.model.User;

import java.util.List;

public class AdapterUsers extends
        RecyclerView.Adapter<AdapterUsers.MyViewHolder> {

    private List<User>users ;
    private Context context;

    public AdapterUsers(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterUsers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista =  LayoutInflater.from(parent.getContext()).
                inflate(R.layout.users_adp_list,parent, false);
        return new AdapterUsers.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUsers.MyViewHolder holder, int i) {

        User user = users.get(i);

        holder.idUser.setText( String.format ( "Cód.: %s", user.getIdUser ( ) ) );
        holder.name.setText( String.format ( "Cliente: %s", user.getName ( ) ) );
        holder.address.setText( String.format ( "Rua: %s", user.getAddress ( ) ) );
        holder.numberHome.setText( String.format ( "Nº:%s", user.getNumberHome ( ) ) );
        holder.neigthborhood.setText( String.format ( "Bairro: %s", user.getNeigthborhood ( ) ) );
        holder.cep.setText( String.format ( "CEP: %s", user.getCep ( ) ) );
        holder.city.setText( String.format ( "Cidade: %s", user.getCity ( ) ) );
        holder.referencePoint.setText( String.format ( "Referencia: %s", user.getReferencePoint ( ) ) );
        holder.state.setText( String.format ( "Estado: %s", user.getState ( ) ) );
        holder.phone.setText( String.format ( "Fone: %s", user.getPhone ( ) ) );
        holder.email.setText( String.format ( "E-mail: %s", user.getEmail ( ) ) );
        holder.bornDate.setText( String.format ( "Nascimento: %s", user.getBornDate ( ) ) );
        holder.cpf.setText( String.format ( "CPF: %s", user.getCpf ( ) ) );
        holder.isAdmin.setText( String.format ( "Administardor: %s", user.getIsAdmin ( ) ) );
        holder.ponts.setText( String.format ( "Pontos: %d", user.getPonts ( ) ) );
        holder.password.setText( String.format ( "Senha: %s", user.getPassword ( ) ) );

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idUser;
        TextView name;
        TextView bornDate;
        TextView address;
        TextView neigthborhood;
        TextView numberHome;
        TextView city;
        TextView cep;
        TextView state;
        TextView phone;
        TextView email;
        TextView password;
        TextView isAdmin;
        TextView ponts;
        TextView referencePoint;
        TextView cpf;

        public MyViewHolder(View itemView) {
            super(itemView);

            idUser = itemView.findViewById(R.id.txtidUser);
            name = itemView.findViewById(R.id.txtName);
            bornDate = itemView.findViewById(R.id.txtDateNasc);
            address = itemView.findViewById(R.id.txtAddress);
            neigthborhood =  itemView.findViewById(R.id.txtNeigtborhood);
            numberHome  =  itemView.findViewById(R.id.txtNumbeHome);
            city =  itemView.findViewById(R.id.txtCidade);
            cep  =  itemView.findViewById(R.id.txtCep);
            state  =  itemView.findViewById(R.id.txtEstado);
            phone  =  itemView.findViewById(R.id.txtCellPhone);
            email  =  itemView.findViewById(R.id.txtEmail);
            password  =  itemView.findViewById(R.id.txtPassword);
            isAdmin  =  itemView.findViewById(R.id.txtIsAdmin);
            ponts  =  itemView.findViewById(R.id.txtPontos);
            referencePoint  =  itemView.findViewById(R.id.txtPontoReferencia);
            cpf  =  itemView.findViewById(R.id.txtCpf);
        }
    }
}
