package com.cegep.sportify_admin.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.cegep.sportify_admin.R;
import java.util.ArrayList;
import java.util.List;

public class AddProductRequest {

    private String productName;

    private float price = -1f;

    private int sale = 0;

    private String category;

    private String subCategory;

    private String description;

    private int xSmallSize = 0;

    private int smallSize = 0;

    private int mediumSize = 0;

    private int largeSize = 0;

    private int xLargeSize = 0;

    private List<String> colors = new ArrayList<>();

    private List<String> images = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getxSmallSize() {
        return xSmallSize;
    }

    public void setxSmallSize(int xSmallSize) {
        this.xSmallSize = xSmallSize;
    }

    public int getSmallSize() {
        return smallSize;
    }

    public void setSmallSize(int smallSize) {
        this.smallSize = smallSize;
    }

    public int getMediumSize() {
        return mediumSize;
    }

    public void setMediumSize(int mediumSize) {
        this.mediumSize = mediumSize;
    }

    public int getLargeSize() {
        return largeSize;
    }

    public void setLargeSize(int largeSize) {
        this.largeSize = largeSize;
    }

    public int getxLargeSize() {
        return xLargeSize;
    }

    public void setxLargeSize(int xLargeSize) {
        this.xLargeSize = xLargeSize;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setProductPrice(String priceStr) {
        try {
            setPrice(Float.parseFloat(priceStr));
        } catch (NumberFormatException e) {
        }
    }

    public void addColor(String color) {
        colors.add(color);
    }

    public boolean hasColor(String color) {
        return colors.contains(color);
    }

    public boolean isValid(Context context) {
        if (TextUtils.isEmpty(getProductName())) {
            Toast.makeText(context, R.string.error_empty_product_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (getPrice() < 1) {
            Toast.makeText(context, R.string.error_empty_product_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(getCategory())) {
            Toast.makeText(context, R.string.error_empty_product_category, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(getSubCategory())) {
            Toast.makeText(context, R.string.error_empty_product_sub_category, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(getDescription())) {
            Toast.makeText(context, R.string.error_empty_product_description, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sale > 100) {
            Toast.makeText(context, R.string.error_invalid_sale, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
