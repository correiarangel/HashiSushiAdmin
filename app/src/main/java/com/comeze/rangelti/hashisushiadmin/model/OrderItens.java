package com.comeze.rangelti.hashisushiadmin.model;
import java.io.Serializable;

public class OrderItens  implements Serializable {


    private String idProduct;
    private String nameProduct;
    private int quantity;
    private String itenSalePrice;

    public OrderItens() {
    }

    public String getIdProduct() {
	    return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantidade) {
        this.quantity = quantidade;
    }

    public String getItenSalePrice() {
        return itenSalePrice;
    }

    public void setItenSalePrice(String itenSalePrice) {
        this.itenSalePrice = itenSalePrice;
    }

    @Override
    public String toString() {
        return "OrderItens{" +
                "idProduct='" + idProduct + '\'' +
                ", nameProduct='" + nameProduct + '\'' +
                ", quantity=" + quantity +
                ", itenSalePrice='" + itenSalePrice + '\'' +
                '}';
    }
}
