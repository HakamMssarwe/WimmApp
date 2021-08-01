package com.example.wimm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.Helper.DataAccess;
import com.example.wimm.Modules.Category;
import com.example.wimm.Modules.Item;
import com.example.wimm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsFragment extends Fragment implements View.OnClickListener {

    private GridView catView;
    private List<Item> itemList = new ArrayList<>();
    Button refreshItemsBtn;
    Button addItemBtn;
    Button updateItemBtn;
    EditText itemNameInput;
    EditText itemPriceInput;
    TextView categoryNameView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_items, container, false);

        catView = view.findViewById(R.id.cat_Grid);
        LoadItems();

        ItemAdapter adapter = new ItemAdapter(itemList);
        catView.setAdapter(adapter);

        catView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemName = itemList.get(position).GetName();
                DataAccess.DeleteItem(itemName,DataAccess.openedCategory);
                Toast.makeText(getActivity(), "Item was successfully deleted!", Toast.LENGTH_SHORT).show();
                itemList.remove(position);

                for(int i = 0; i < DataAccess.itemsInCurrentCategory.size();i++)
                    if (DataAccess.itemsInCurrentCategory.get(i).GetName().equals(itemName))
                        DataAccess.itemsInCurrentCategory.remove(DataAccess.itemsInCurrentCategory.get(i));


                catView.setAdapter(new ItemAdapter(itemList));

            }
        });

        categoryNameView = view.findViewById(R.id.categoryNameView);
        categoryNameView.setText(DataAccess.openedCategory);

        addItemBtn = view.findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(this);

        updateItemBtn = view.findViewById(R.id.updateItemBtn);
        updateItemBtn.setOnClickListener(this);


        refreshItemsBtn = view.findViewById(R.id.refreshItemsBtn);
        refreshItemsBtn.setOnClickListener(this);


        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemPriceInput = view.findViewById(R.id.itemPriceInput);

        return view;
    }


    void LoadItems() {
        itemList.clear();

        if (DataAccess.itemsInCurrentCategory.size() != 0) {
            for (int i = 0; i < DataAccess.itemsInCurrentCategory.size(); i++) {
                itemList.add(new Item(DataAccess.itemsInCurrentCategory.get(i).GetName(), DataAccess.itemsInCurrentCategory.get(i).GetPrice() + "$"));
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshItemsBtn:
                LoadItems();
                ItemsFragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                Toast.makeText(getActivity(), "Page Successfully refreshed!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addItemBtn:
                if (itemNameInput.getText().toString().equals("") || itemPriceInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please do not leave any empty fields!", Toast.LENGTH_SHORT).show();
                    break;
                }

                for (int i = 0; i < DataAccess.itemsInCurrentCategory.size(); i++) {
                    if (itemNameInput.getText().toString().toLowerCase().equals(DataAccess.itemsInCurrentCategory.get(i).GetName().toLowerCase())) {
                        Toast.makeText(getActivity(), "Item already exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                DataAccess.AddItem(itemNameInput.getText().toString(),itemPriceInput.getText().toString(),DataAccess.openedCategory);
                DataAccess.itemsInCurrentCategory.add(new Item(itemNameInput.getText().toString(),itemPriceInput.getText().toString()));
                itemNameInput.setText("");
                itemPriceInput.setText("");
                break;


            case R.id.updateItemBtn:
                if (itemNameInput.getText().toString().equals("") || itemPriceInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please do not leave any empty fields!", Toast.LENGTH_SHORT).show();
                    break;
                }


                for (int i = 0; i < DataAccess.itemsInCurrentCategory.size(); i++) {
                    if (itemNameInput.getText().toString().toLowerCase().equals(DataAccess.itemsInCurrentCategory.get(i).GetName().toLowerCase())) {
                        DataAccess.UpdateItemPrice(itemNameInput.getText().toString(),itemPriceInput.getText().toString(),DataAccess.openedCategory);
                        DataAccess.itemsInCurrentCategory.get(i).SetPrice(itemPriceInput.getText().toString());

                        for (int j = 0; j < itemList.size();j++)
                            if (itemList.get(j).GetName().equals(itemNameInput.getText().toString()))
                                itemList.get(j).SetPrice(itemPriceInput.getText().toString());


                        Toast.makeText(getActivity(), "Item Price was successfully updated!", Toast.LENGTH_SHORT).show();
                        itemNameInput.setText("");
                        itemPriceInput.setText("");
                        return;
                }

                Toast.makeText(getActivity(), "Item is not found!", Toast.LENGTH_SHORT).show();
                break;

        }
    }
    }
}


