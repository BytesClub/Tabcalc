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

public class Tab1 extends Fragment{ //implements View.OnClickListener {

    //IDs of all numeric buttons
    private int[] numBut = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnBracOpen, R.id.btnBracClose};

    //IDs of all operation buttons
    private int[] opBut = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnExpo};

    public interface onSomeEventListener {
        void someEvent(String s);
        void txtClear(boolean a);
        void erase(boolean a);
        void equal(boolean a);
    }

    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    final String LOG_TAG = "myLogs";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.tab1, null);

        //Setting listener on numbers
        for (int id : numBut) {
            myView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View myView) {
                    Button button = (Button) myView;
                    if (MainActivity.stateError) {
                        //If current state is error, replace the error message
                        //txtScreen.setText(button.getText());
                        someEventListener.txtClear(true);
                        someEventListener.someEvent(button.getText().toString()); ////***
                        //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        /*intent.putExtra("data", button.getText());
                        startActivity(intent);*/
                        someEventListener.someEvent(button.getText().toString()); ////***
                        MainActivity.stateError = false;
                    } else {
                        //if no error, the entered expression is correct, so append to it
                        if (MainActivity.lastClear || MainActivity.lastAns) {
                            //txtScreen.setText("");
                            someEventListener.txtClear(true);
                            /*intent.putExtra("data", "");
                            startActivity(intent);*/
                            MainActivity.lastClear = MainActivity.lastAns = false;
                        }
                        //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        //txtScreen.append(button.getText());
                        someEventListener.someEvent(button.getText().toString());
                    }
                    //set the flag
                    MainActivity.lastNumeric = true;
                    MainActivity.checkno=true;
                }
            });
        }

        //Setting listener on operators
        for (int id : opBut) {
            myView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View myView) {
                    Button button = (Button) myView;
                    if (!MainActivity.stateError)
                    {
                            if (!MainActivity.ans.equals("") && MainActivity.lastClear) {
                                //txtScreen.setText("");
                                someEventListener.txtClear(true);
                                someEventListener.someEvent(MainActivity.ans);
                                //txtScreen.append(ans);
                                someEventListener.someEvent(button.getText().toString());
                            } else someEventListener.someEvent(button.getText().toString());;
                                //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                                MainActivity.lastClear = MainActivity.lastAns = false;
                                MainActivity.lastNumeric = false;
                                MainActivity.lastDot = false; //resetting the dot flag
                    }
                    MainActivity.checkop=true;
                }
            });
        }

        //Setting listener on dot
        myView.findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                if (MainActivity.lastNumeric && !MainActivity.stateError && !MainActivity.lastDot) {
                    //txtScreen.append(".");
                    someEventListener.someEvent(".");
                    //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                    MainActivity.lastNumeric = false;
                    MainActivity.lastDot = true;
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

        //Setting listener on erase button
        myView.findViewById(R.id.btnErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                someEventListener.erase(true);
                /*String txtInBox=txtScreen.getText().toString();
                if(txtInBox.length()>0)txtInBox=txtInBox.substring(0,txtInBox.length()-1);
                else txtInBox="";
                txtScreen.setText(txtInBox);*/

            }
        });

        myView.findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                someEventListener.equal(true);
            }
        });

        myView.findViewById(R.id.btnAns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                someEventListener.someEvent(MainActivity.ans);
            }
        });
        return myView;

    }
}
