package com.example.wimm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragement extends Fragment {


    private GridView catView;
    private List<CategoryModel> catList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_categories,container,false);

        catView = view.findViewById(R.id.cat_Grid);


        loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(catList);
        catView.setAdapter(adapter);

        return view;
    }

    private void loadCategories() {

        catList.clear();

        catList.add(new CategoryModel("1","HEBREUX",20));
        catList.add(new CategoryModel("2","FOOD",30));
        catList.add(new CategoryModel("3","ENGLISH",10));
        catList.add(new CategoryModel("4","MATH",20));
        catList.add(new CategoryModel("5","SCIENCE",15));
        catList.add(new CategoryModel("6","FRENCH",10));
    }
}
