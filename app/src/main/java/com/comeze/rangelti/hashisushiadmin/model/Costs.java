package com.comeze.rangelti.hashisushiadmin.model;

import com.comeze.rangelti.hashisushiadmin.dao.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Costs implements Serializable {

    private  String idCustoEntrega;
    private String custoEntrega;

    public Costs() {
    }

    public void salvar(){

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("costs")
                .push();
        //pega retorno de id gerado com push
        String internoId = produtoRef.getKey();
        setIdCustoEntrega( internoId );

        produtoRef.setValue(this);

    }

    public void editarCusto(String custoEntrega ) {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference hopperRef = firebaseRef.child("costs").child( "-LlDgMNFRyCWC1bjkL55" );
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("custoEntrega", custoEntrega  );

        hopperRef.updateChildren(hopperUpdates);
    }

    public String getIdCustoEntrega() {
        return idCustoEntrega;
    }

    public void setIdCustoEntrega(String idCustoEntrega) {
        this.idCustoEntrega = idCustoEntrega;
    }

    public String getCustoEntrega() {
        return custoEntrega;
    }

    public void setCustoEntrega(String custoEntrega) {
        this.custoEntrega = custoEntrega;
    }
}
