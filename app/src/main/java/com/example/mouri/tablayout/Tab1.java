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

    //Textview used to display the output
    private TextView txtScreen;

    //Represents whether the last pressed key is numeric or not
    private boolean lastNumeric;

    //Represent that current state is in error or not
    private boolean stateError;

    //To check for only one dot in a number
    private boolean lastDot;

    //To check if screen is cleared before next operation
    private boolean lastClear;

    //To save the last calculated answer
    private String ans = "";

    //To save the last line of calculation
    private String line = "";

    //To see if ans is obtained
    private boolean lastAns;

    //To pass values to textbox
    //Intent intent = new Intent(getActivity(), MainActivity.class);

    public interface onSomeEventListener {
        public void someEvent(String s);
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
                    if (stateError) {
                        //If current state is error, replace the error message
                        //txtScreen.setText(button.getText());
                        //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        /*intent.putExtra("data", button.getText());
                        startActivity(intent);*/
                        someEventListener.someEvent(button.getText().toString());
                        stateError = false;
                    } else {
                        //if no error, the entered expression is correct, so append to it
                        if (lastClear || lastAns) {
                            //txtScreen.setText("");
                            /*intent.putExtra("data", "");
                            startActivity(intent);*/
                            lastClear = lastAns = false;
                        }
                        //Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        //txtScreen.append(button.getText());
                        someEventListener.someEvent(button.getText().toString());
                    }
                    //set the flag
                    lastNumeric = true;
                }
            });
        }

        //Setting listener on operators
        for (int id : opBut) {
            myView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View myView) {
                    Button button = (Button) myView;
                    if (!stateError) {
                /*if (!ans.equals("") && lastClear) {
                    txtScreen.setText("");
                    txtScreen.append(ans);
                    txtScreen.append(button1.getText());
                } else txtScreen.append(button1.getText());*/
                        Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                        lastClear = lastAns = false;
                        lastNumeric = false;
                        lastDot = false; //resetting the dot flag
                    }
                }
            });
        }

        //Setting listener on dot
        myView.findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                if (lastNumeric && !stateError && !lastDot) {
                    //txtScreen.append(".");
                    Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });

        //Setting listener on clear button
        myView.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                //txtScreen.setText("");//clears screen
                Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                //reset all stages and flags
                lastDot=false;
                lastNumeric=false;
                stateError=false;
                lastClear=true;
            }
        });

        //Setting listener on erase button
        myView.findViewById(R.id.btnErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                Button button = (Button) myView;
                /*String txtInBox=txtScreen.getText().toString();
                if(txtInBox.length()>0)txtInBox=txtInBox.substring(0,txtInBox.length()-1);
                else txtInBox="";
                txtScreen.setText(txtInBox);*/

            }
        });

        return myView;

    }

    /*@Override
    public void onClick(View v) {
        //just append/or set the text of clicked button

        boolean op = false; // To see if a numerical button is pressed

        boolean num = false;// To see if an operational button is pressed

        Integer id = new Integer(v.getId());
        if (Arrays.asList(numBut).contains(id)) num = true;
        else op = true;

        Button button = (Button) v;

        /*if (num) {
            if (stateError) {
                //If current state is error, replace the error message
                //txtScreen.setText(button.getText());
                Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                /*intent.putExtra("data", button.getText());
                startActivity(intent);
                stateError = false;
            } else {
                //if no error, the entered expression is correct, so append to it
                if (lastClear || lastAns) {
                    //txtScreen.setText("");
                    lastClear = lastAns = false;
                }
                Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                //txtScreen.append(button.getText());
            }
            //set the flag
            lastNumeric = true;
        } else
        if (op) {
            if (!stateError) {
                /*if (!ans.equals("") && lastClear) {
                    txtScreen.setText("");
                    txtScreen.append(ans);
                    txtScreen.append(button1.getText());
                } else txtScreen.append(button1.getText());
                Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
                lastClear = lastAns = false;
                lastNumeric = false;
                lastDot = false; //resetting the dot flag
            }
        } else if (id == R.id.btnDot && lastNumeric && !stateError && !lastDot) {
            //txtScreen.append(".");
            Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
            lastNumeric = false;
            lastDot = true;
        } else if (id == R.id.btnClear && !lastClear) {
            //txtScreen.setText("");//clears screen
            Toast.makeText(getActivity(), button.getText(), Toast.LENGTH_SHORT).show();
            //reset all stages and flags
            lastDot = false;
            lastNumeric = false;
            stateError = false;
            lastClear = true;
        }
    }*/
}