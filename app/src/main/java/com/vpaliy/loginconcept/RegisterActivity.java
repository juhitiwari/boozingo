package com.vpaliy.loginconcept;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.vpaliy.loginconcept.Interfaces.ConfirmOTP;
import com.vpaliy.loginconcept.Interfaces.RegisterUSer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final String ROOT_URL = "http://34.199.49.88:3000/";
    String phoneNo="9461155105";
    @BindView(R.id.dob) EditText dob_edit;
    String uname,fname,lname,email,password,confirm_password,gender;
    Date dob;
    @BindViews(value = {R.id.fname_input_edit,
            R.id.lname_input_edit,
    R.id.uname_input_edit,R.id.password_input_edit,R.id.confirm_password_edit,R.id.email_input_edit})

    protected List<TextInputEditText> views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }
    @OnClick({R.id.male,R.id.female})
    public void onRadioButtonClicked(RadioButton radioButton){
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.male:
                if (checked) {
                    gender="male";
                }
                break;
            case R.id.female:
                if (checked) {
                    gender="female";
                }
                break;
        }
    }
    @OnClick(R.id.register)
    public void register(){
        for(TextInputEditText editText:views){
            fname=views.get(0).getText().toString();
            lname=views.get(1).getText().toString();
            uname=views.get(2).getText().toString();
           password=views.get(3).getText().toString();
            confirm_password=views.get(4).getText().toString();
            email=views.get(5).getText().toString();

        }
        dob= (Date) dob_edit.getText();

registerUser();
    }

    private void registerUser() {

        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        RegisterUSer api=adapter.create(RegisterUSer.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(RegisterActivity.this);
        progressDoalog.setTitle("Registering...");
        progressDoalog.show();
        api.RegisterUser(
                fname,
                lname,
                uname,
                password,
                confirm_password,
                gender,
                email,
                phoneNo,dob,



                new retrofit.Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try{
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(RegisterActivity.this, output, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
}

