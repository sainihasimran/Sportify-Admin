package com.cegep.sportify_admin.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.cegep.sportify_admin.R;
import java.util.ArrayList;
import java.util.List;

public class AddEquipmentRequest {

    private String equipmentName;

    private float price = -1f;

    private int sale;

    private int stock;

    private long createdAt;

    private String sport;

    private String description;

    private List<String> images = new ArrayList<>();

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getSport() {
        return sport;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setEquipmentPrice(String priceStr) {
        try {
            setPrice(Float.parseFloat(priceStr));
        } catch (NumberFormatException e) {

        }
    }

    public boolean isValid(Context context) {
        if (TextUtils.isEmpty(getEquipmentName())) {
            Toast.makeText(context, R.string.error_empty_equipment_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (getPrice() < 1) {
            Toast.makeText(context, R.string.error_empty_equipment_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(getSport())) {
            Toast.makeText(context, R.string.error_empty_equipment_sport, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (getSale() > 100) {
            Toast.makeText(context, R.string.error_invalid_sale, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
