package com.cegep.sportify_admin.model;

public class ProductFilter {

    private String categoryFilter = "All";

    private String subCategoryFilter = "All";

    public String getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(String categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public String getSubCategoryFilter() {
        return subCategoryFilter;
    }

    public void setSubCategoryFilter(String subCategoryFilter) {
        this.subCategoryFilter = subCategoryFilter;
    }
}
