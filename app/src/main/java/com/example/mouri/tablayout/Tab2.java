package com.example.mouri.tablayout;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by mouri on 22/6/17.
 */

public class Tab2 extends Fragment{ //implements View.OnClickListener {

    //IDs of all numeric buttons
    private int[] opBut = {R.id.btnASin, R.id.btnACos, R.id.btnATan, R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnSinh, R.id.btnCosh, R.id.btnCeil, R.id.btnFloor, R.id.btnSqrt};

    public interface onSomeEventListener2 {
        void someEvent2(String s);
        void txtClear(boolean a);
    }

    onSomeEventListener2 someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    final String LOG_TAG = "myLogs";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View myView = inflater.inflate(R.layout.tab2, null);

        //Setting listener on operators
        for (int id : opBut) {
            myView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View myView) {
                    Button button = (Button) myView;
                    someEventListener.txtClear(true);
                    if (!MainActivity.stateError) {
                        if (!MainActivity.ans.equals("") && MainActivity.lastClear) {
                            //txtScreen.setText("");
                            //txtScreen.append(ans);
                            someEventListener.someEvent2(button.getText().toString());
                        } else someEventListener.someEvent2(button.getText().toString());;
                        //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        MainActivity.lastClear = MainActivity.lastAns = false;
                    }
                }
            });
        }

        myView.findViewById(R.id.btnMod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                if (!MainActivity.stateError) {
                    if (!MainActivity.ans.equals("") && MainActivity.lastClear) {
                        //txtScreen.setText("");
                        someEventListener.txtClear(true);
                        //txtScreen.append(ans);
                        someEventListener.someEvent2(button.getText().toString());
                    } else someEventListener.someEvent2(button.getText().toString());;
                    //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                    MainActivity.lastClear = MainActivity.lastAns = false;
                }
            }
        });

        //Setting listener on clear button
        myView.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                //txtScreen.setText("");//clears screen
                someEventListener.txtClear(true);
                //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                //reset all stages and flags
                MainActivity.lastDot=false;
                MainActivity.lastNumeric=false;
                MainActivity.stateError=false;
                MainActivity.lastClear=true;
            }
        });

        //Setting listener on PI button
        myView.findViewById(R.id.btnPi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                String s = String.format("%.9f", Math.PI);
                someEventListener.someEvent2(s);
                //txtScreen.setText("");//clears screen
                //someEventListener.txtClear(true);
                //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                //reset all stages and flags
                MainActivity.lastDot=false;
                MainActivity.lastNumeric=false;
                MainActivity.stateError=false;
                MainActivity.lastClear=true;
            }
        });
        return myView;
    }
}