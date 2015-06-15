
package com.deveyes.italist.model;

import com.google.gson.annotations.Expose;

public class Product {

    @Expose
    private Integer error;
    @Expose
    private String error_description;
    @Expose
    private String model;
    @Expose
    private String model_number;
    @Expose
    private String store;
    @Expose
    private String marca;
    @Expose
    private String color;
    @Expose
    private String product_image;
    @Expose
    private String size;
    @Expose
    private String unit_system;
    @Expose
    private String acronym;
    @Expose
    private String nr_available;
    @Expose
    private String nr_available_store;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_number() {
        return model_number;
    }

    public void setModel_number(String model_number) {
        this.model_number = model_number;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnit_system() {
        return unit_system;
    }

    public void setUnit_system(String unit_system) {
        this.unit_system = unit_system;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getNr_available() {
        return nr_available;
    }

    public void setNr_available(String nr_available) {
        this.nr_available = nr_available;
    }

    public String getNr_available_store() {
        return nr_available_store;
    }

    public void setNr_available_store(String nr_available_store) {
        this.nr_available_store = nr_available_store;
    }

}
