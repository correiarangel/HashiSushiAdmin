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

        holder.idUser.setText("Cod.:"+user.getIdUser());
        holder.name.setText("Nome :"+user.getName());
        holder.address.setText("End :"+user.getAddress());
        holder.numberHome.setText("Nº :"+ user.getNumberHome());
        holder.neigthborhood.setText("Bairro :"+ user.getNeigthborhood());
        holder.cep.setText("CEP :"+ user.getCep());
        holder.city.setText("Cidade :"+ user.getCity());
        holder.state.setText("Estado :"+ user.getState());
        holder.phone.setText("Fone :"+ user.getPhone());
        holder.email.setText("E-mail :"+ user.getEmail());
        holder.bornDate.setText("Data Nasc. :"+ user.getBornDate());
        holder.cpf.setText("CPF :"+ user.getCpf());
        holder.isAdmin.setText("Administardor :"+ user.getIsAdmin());
        holder.ponts.setText("Pontos :"+ user.getPonts());
        holder.password.setText("Senha :"+ user.getPassword());
        holder.referencePoint.setText("Ponto de referencia :"+ user.getReferencePoint());
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
            referencePoint  =  itemView.findViewById(R.id.txtPontos);
            cpf  =  itemView.findViewById(R.id.txtCpf);
        }
    }
}
