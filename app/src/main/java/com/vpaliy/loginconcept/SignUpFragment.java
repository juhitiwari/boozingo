package com.vpaliy.loginconcept;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.vpaliy.loginconcept.Interfaces.ConfirmOTP;
import com.vpaliy.loginconcept.Interfaces.OTP;

import android.util.TypedValue;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import android.support.annotation.Nullable;
import android.annotation.TargetApi;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SignUpFragment extends AuthFragment{
    public static final String ROOT_URL = "http://34.199.49.88:3000/";


String phoneNo;
String otp;
    @BindViews(value = {R.id.phone_input_edit,
            R.id.otp_input_edit})

    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_sign_up));
            caption.setText(getString(R.string.sign_up_label));
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.otp_input_edit){
                    final TextInputLayout inputLayout= ButterKnife.findById(view,R.id.otp_input);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);

                }
                editText.setOnFocusChangeListener((temp,hasFocus)->{
                    if(!hasFocus){
                        boolean isEnabled=editText.getText().length()>0;
                        editText.setSelected(isEnabled);
                    }
                });
            }
            caption.setVerticalText(true);
            foldStuff();
            caption.setTranslationX(getTextPadding());
        }




    }
@OnClick(R.id.generate_otp)
public void submit(){
    for(TextInputEditText editText:views){
        phoneNo=views.get(0).getText().toString();
    }
    generateOTP();

    }
    @OnClick(R.id.confirm_otp)
    public void submit_otp(){
        for(TextInputEditText editText:views){
            otp=views.get(1).getText().toString();
        }
        confirmOTP();
    }

    private void confirmOTP() {
        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        ConfirmOTP api=adapter.create(ConfirmOTP.class);
        api.confirmOTP(
                otp,
                phoneNo,



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
if(output.equals("OK")){
    Intent intent=new Intent(getActivity(),RegisterActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("phone",phoneNo);
intent.putExtras(bundle);
    startActivity(intent);}
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    public void generateOTP() {
        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        OTP api=adapter.create(OTP.class);
        api.generateOTP(
                "true",
                phoneNo,



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
                        Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public int authLayout() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    @Override
    public void fold() {
        lock=false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(getTextPadding());
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        foldStuff();
        caption.setTranslationX(-caption.getWidth()/8+getTextPadding());
    }

    private void foldStuff(){
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }

    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
    }
}
