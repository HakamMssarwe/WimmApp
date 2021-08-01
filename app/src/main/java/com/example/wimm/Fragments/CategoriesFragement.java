package com.example.wimm.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.Helper.DataAccess;
import com.example.wimm.Modules.Category;
import com.example.wimm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesFragement extends Fragment implements View.OnClickListener {


    private GridView catView;
    private List<Category> catList = new ArrayList<>();
    Button refreshCategoriesBtn;
    Button addCategoryBtn;
    Button deleteCategoryBtn;
    EditText categoryNameInput;
    TextView userSalaryView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_categories,container,false);
        catView = view.findViewById(R.id.cat_Grid);

        CategoryAdapter adapter = new CategoryAdapter(catList);
        catView.setAdapter(adapter);
        catView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String categoryName = catList.get(position).GetName();
                DataAccess.UpdateItemsList(categoryName);
                DataAccess.openedCategory = categoryName;
                ItemsFragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


            }
        });


        refreshCategoriesBtn = view.findViewById(R.id.refreshCategoriesBtn);
        refreshCategoriesBtn.setOnClickListener(this);

        addCategoryBtn = view.findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setOnClickListener(this);

        deleteCategoryBtn = view.findViewById(R.id.deleteCategory);
        deleteCategoryBtn.setOnClickListener(this);

        categoryNameInput = view.findViewById(R.id.categoryNameInput);


        LoadCategories();


        return view;
    }




    void LoadCategories()
    {
        //Update the list
        DataAccess.UpdateCategoriesList();
  

        catList.clear();
        for (int i = 0; i < DataAccess.categories.size();i++)
        {
            catList.add(DataAccess.categories.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshCategoriesBtn:
                LoadCategories();
                CategoriesFragement fragment = new CategoriesFragement();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                Toast.makeText(getActivity(), "Page Successfully refreshed!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addCategoryBtn:

                if (categoryNameInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Input cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < DataAccess.categories.size(); i++) {
                    if (categoryNameInput.getText().toString().toLowerCase().equals(DataAccess.categories.get(i).GetName().toLowerCase())) {
                        Toast.makeText(getActivity(), "Category already exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DataAccess.AddCategory(categoryNameInput.getText().toString());
                DataAccess.categories.add(new Category(categoryNameInput.getText().toString()));

                categoryNameInput.setText("");
                Toast.makeText(getActivity(), "Category was successfully added!", Toast.LENGTH_SHORT).show();

                break;

            case R.id.deleteCategory:
                if (categoryNameInput.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter the name of the category you want to delete", Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < DataAccess.categories.size(); i++) {
                    if (categoryNameInput.getText().toString().toLowerCase().equals(DataAccess.categories.get(i).GetName().toLowerCase())) {
                        DataAccess.DeleteCategory(categoryNameInput.getText().toString());
                        DataAccess.categories.remove(DataAccess.categories.get(i));

                        for(int j = 0; j < catList.size();j++)
                            if (catList.get(j).GetName().equals(categoryNameInput.getText().toString())) {
                                catList.remove(catList.get(j));
                                CategoryAdapter adapter = new CategoryAdapter(catList);
                                catView.setAdapter(adapter);
                                categoryNameInput.setText("");
                                Toast.makeText(getActivity(), "Category was successfully removed!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                    }
                }

                Toast.makeText(getActivity(), "No such category exist!", Toast.LENGTH_SHORT).show();
                break;
        }

    }



}
