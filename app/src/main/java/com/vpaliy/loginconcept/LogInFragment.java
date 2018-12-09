package com.vpaliy.loginconcept;

import android.app.ProgressDialog;
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
import android.text.TextWatcher;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.vpaliy.loginconcept.Interfaces.Login;
import com.vpaliy.loginconcept.Interfaces.RegisterUSer;
import com.vpaliy.loginconcept.helper.SQLiteHandler;
import com.vpaliy.loginconcept.helper.SessionManager;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInFragment extends AuthFragment{
    String uname,password;
    private SessionManager session;
    private SQLiteHandler db;

    public static final String ROOT_URL = "http://34.199.49.88:3000/";

    @BindViews(value = {R.id.email_input_edit,R.id.password_input_edit})
    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new SQLiteHandler(getContext());

        // Session manager
        session = new SessionManager(getContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
getActivity().finish();
        }
        if(view!=null){
            caption.setText(getString(R.string.log_in_label));
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_log_in));
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout=ButterKnife.findById(view,R.id.password_input);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter(){
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length()>0);
                        }
                    });
                }
                editText.setOnFocusChangeListener((temp,hasFocus)->{
                    if(!hasFocus){
                        boolean isEnabled=editText.getText().length()>0;
                        editText.setSelected(isEnabled);
                    }
                });
            }
        }
    }
@OnClick(R.id.login)
public void login(){
    for(TextInputEditText editText:views) {
    uname=views.get(0).getText().toString();
    password=views.get(1).getText().toString();
    }
   // Toast.makeText(getActivity(), uname + " "+ password, Toast.LENGTH_SHORT).show();
loginUser();
}

    private void loginUser() {
        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        Login api=adapter.create(Login.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setTitle("Logging you in...");
        progressDoalog.show();
        api.login(

                uname,
                password,




                new retrofit.Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try{
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                            if(progressDoalog.isShowing()) {
                                progressDoalog.dismiss();
                            }

                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }

                        try {
                            JSONObject jObj = new JSONObject(output);
                            session.setLogin(true);

                            // Now store the user in SQLite
                            String uname = "baap";
                            String token=jObj.getString("token");


                            // Inserting row in users table
                            db.addUser(uname, token);

                            // Launch main activity
                            Intent intent = new Intent(getActivity(),
                                    MainActivity.class);
                            startActivity(intent);
getActivity().finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        if(progressDoalog.isShowing()) {
                            progressDoalog.dismiss();
                        }

                    }
                }
        );

    }


    @Override
    public int authLayout() {
        return R.layout.login_fragment;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        final float padding=getResources().getDimension(R.dimen.folded_label_padding)/2;
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
        caption.setTranslationX(caption.getWidth()/8-padding);
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

}
