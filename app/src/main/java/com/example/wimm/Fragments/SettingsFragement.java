package com.example.wimm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.ForgotPasswordActivity;
import com.example.wimm.Helper.DataAccess;
import com.example.wimm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class SettingsFragement extends Fragment implements View.OnClickListener {

    private FirebaseAuth auth;
    TextView userEmailTextView;
    EditText inputUpdateSalary;
    Button saveChangesBtn;
    Button changePasswordBtn;
    String salaryInDB;
    Map<String, Object> fields;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        auth = FirebaseAuth.getInstance();
        userEmailTextView =  view.findViewById(R.id.userEmailTextView);
        inputUpdateSalary = view.findViewById(R.id.inputUpdateSalary);
        saveChangesBtn = view.findViewById(R.id.saveChangesBtn);
        saveChangesBtn.setOnClickListener(this);
        changePasswordBtn = view.findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(this);


        fields = DataAccess.GetUserFields();
        //Setup fields
        if (fields != null)
        {

            for (Map.Entry<String,Object> field : fields.entrySet())
            {
                if (field.getKey().equals("email"))
                    userEmailTextView.setText(field.getValue() + "");
                else if (field.getKey().equals("salary")) {
                    inputUpdateSalary.setText(field.getValue() + "");
                    salaryInDB = field.getValue() + "";
                }

            }
        }
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.saveChangesBtn:

                if ((inputUpdateSalary.getText().toString()).equals(salaryInDB))
                {
                    Toast.makeText(getActivity(), "You already have this salary!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DataAccess.UpdateSalary(Integer.parseInt(inputUpdateSalary.getText().toString()));
                salaryInDB = inputUpdateSalary.getText().toString();
                Toast.makeText(getActivity(), "Salary was successfully updated!", Toast.LENGTH_SHORT).show();

                break;

            case R.id.changePasswordBtn:
                auth.sendPasswordResetEmail(DataAccess.userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Check your email to reset your password !",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Try again ! Something wrong happened",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;
        }



    }
}
