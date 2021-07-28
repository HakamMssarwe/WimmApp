package com.example.wimm.Fragments;

public class CategoryModel {

    private String docID;
    private String nameCategory;
    private int noOfTests;

    public CategoryModel(String docID, String nameCategory, int noOfTests){
        this.docID = docID;
        this.nameCategory = nameCategory;
        this.noOfTests = noOfTests;
    }

    public String getDocID() {
        return docID;
    }

    public String getNameCategory(){
        return nameCategory;
    }

    public int getNoOfTests() {
        return noOfTests;
    }
}
