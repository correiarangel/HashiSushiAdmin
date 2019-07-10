package com.comeze.rangelti.hashisushiadmin.model;


import com.comeze.rangelti.hashisushiadmin.dao.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Orders implements Serializable {

    private String idOrders;
    private String idUser;
    private String name;
    private String address;
    private String neigthborhood;
    private String numberHome;
    private String cellphone;
	private String dateOrder;
    private String hour;
    private String qrCode;
    private int quantProd;
    private int discont;
    private double totalPrince;
    private List<OrderItens> orderItens;
    private String observation;
    private String status;

    public Orders() {
    }

    public void editStatus(String status , String iDOrders) {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders")
                .child(iDOrders);
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("status", status );

        pedidoRef.updateChildren(hopperUpdates);
    }

    //cria pedito temp
    public Orders(String idUser) {

        setIdUser(idUser);
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders_user")
                .child(idUser);
        UUID uuid = UUID.randomUUID();
        String strUuid = uuid.toString();
        setIdOrders(strUuid);
    }

    public void salvar() {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders_user")
                .child(getIdUser());
        pedidoRef.setValue(this);

    }

    public void remover() {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders_user")
                .child(getIdUser());
        pedidoRef.removeValue();

    }

    public void removerOrderItens(String id_user) {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders_user")
                .child(id_user);
        pedidoRef.removeValue();
    }

    public void confimar() {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("orders")
                .child(getIdOrders());
        pedidoRef.setValue(this);

    }

    public String getIdOrders() {
        return idOrders;
    }

    public void setIdOrders(String idOrders) {
        this.idOrders = idOrders;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeigthborhood() {
        return neigthborhood;
    }

    public void setNeigthborhood(String neigthborhood) {
        this.neigthborhood = neigthborhood;
    }

    public String getNumberHome() {
        return numberHome;
    }

    public void setNumberHome(String numberHome) {
        this.numberHome = numberHome;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getQuantProd() {
        return quantProd;
    }

    public void setQuantProd(int quantProd) {
        this.quantProd = quantProd;
    }

    public int getDiscont() {
        return discont;
    }

    public void setDiscont(int discont) {
        this.discont = discont;
    }

    public double getTotalPrince() {
        return totalPrince;
    }

    public void setTotalPrince(double totalPrince) {
        this.totalPrince = totalPrince;
    }

    public List<OrderItens> getOrderItens() {
        return orderItens;
    }

    public void setOrderItens(List<OrderItens> orderItens) {
        this.orderItens = orderItens;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "idOrders='" + idOrders + '\'' +
                ", idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", neigthborhood='" + neigthborhood + '\'' +
                ", numberHome='" + numberHome + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", dateOrder=" + dateOrder +
                ", hour=" + hour +
                ", qrCode='" + qrCode + '\'' +
                ", quantProd=" + quantProd +
                ", discont=" + discont +
                ", totalPrince='" + totalPrince + '\'' +
                ", observation='" + observation + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
