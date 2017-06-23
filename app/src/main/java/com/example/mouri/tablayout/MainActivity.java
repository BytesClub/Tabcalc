package com.example.mouri.tablayout;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    //To store previous 5 operations
    String[] hist=new String[5];
    //To point to current operation in hist[]
    int histIndex=0;
    //IDs of all numeric buttons
    private int[] numBut={R.id.btnZero,R.id.btnOne,R.id.btnTwo,R.id.btnThree,R.id.btnFour,R.id.btnFive,R.id.btnSix,R.id.btnSeven,R.id.btnEight,R.id.btnNine,R.id.btnBracOpen,R.id.btnBracClose};

    //IDs of all operation buttons
    private int[] opBut={R.id.btnAdd,R.id.btnSubtract,R.id.btnMultiply,R.id.btnDivide,R.id.btnExpo};

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
    private String ans="";

    //To save the last line of calculation
    private String line="";

    //To see if ans is obtained
    private boolean lastAns;

    //To see if ans button is clicked or not
    private boolean ansclick;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the textview
        this.txtScreen=(TextView) findViewById(R.id.txtScreen);
        txtScreen.setText("");
        lastClear=true;

        //Find and set OnClickListener to numeric buttons
        setNumOnClickListener();

        //Find and set OnClickListener to operator buttons, equal to button and decimal button
        setOpOnClickListener();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
                case 2:
                    Tab3 tab3 = new Tab3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    //Find and set OnClickListener to numeric buttons
    private void setNumOnClickListener(){
        //create a common OnClickListener
        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //just append/or set the text of clicked button
                Button button=(Button)v;
                if(stateError){
                    //If current state is error, replace the error message
                    txtScreen.setText(button.getText());
                    stateError=false;
                }else{
                    //if no error, the entered expression is correct, so append to it
                    if(lastClear || lastAns){
                        txtScreen.setText("");
                        lastClear=lastAns=false;
                    }
                    txtScreen.append(button.getText());
                }
                //set the flag
                lastNumeric=true;
            }
        };

        //assign listener to all numeric buttons
        for (int id:numBut){
            findViewById(id).setOnClickListener(listener);
        }
    }

    //Find and set OnClickListener to operator buttons, equal button and decimal button
    private void setOpOnClickListener(){
        //create a common OnClickListener for all operators
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if current state is error, do not append the operator, else append it
                if(!stateError){
                    Button button=(Button) v;
                    if(!ans.equals("") && lastClear){
                        txtScreen.setText("");
                        txtScreen.append(ans);
                        txtScreen.append(button.getText());
                    }
                    else txtScreen.append(button.getText());
                    lastClear=lastAns=false;
                    lastNumeric=false;
                    lastDot=false; //resetting the dot flag
                }
            }
        };

        //assign listener to all operator buttons
        /*for(int id:opBut){
            findViewById(id).setOnClickListener(listener);
        }*/

        //decimal point
        /*findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastNumeric && !stateError && !lastDot){
                    txtScreen.append(".");
                    lastNumeric=false;
                    lastDot=true;
                }
            }
        });

        //clear button
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");//clears screen
                //reset all stages and flags
                lastDot=false;
                lastNumeric=false;
                stateError=false;
                lastClear=true;
            }
        });*/

        //changes screen to show more functionalities
        /*findViewById(R.id.btnXtra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ExtraActivity.class);
                startActivity(intent);
            }
        });*/

        //equal button
        /*findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });

        findViewById(R.id.btnAns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText(ans);
                ansclick=true;
            }
        });

        findViewById(R.id.btnErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtInBox=txtScreen.getText().toString();
                if(txtInBox.length()>0)txtInBox=txtInBox.substring(0,txtInBox.length()-1);
                else txtInBox="";
                txtScreen.setText(txtInBox);
            }
        });

        findViewById(R.id.btnPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(histIndex < 0) histIndex = 5 + histIndex;
                txtScreen.setText(hist[histIndex % 5]);
                histIndex--;
            }
        });*/

        /*findViewById(R.id.btnGoRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trav<txtScreen.getMaxLines()-1)txtScreen.setText(vect.elementAt(++trav));
                else trav=0;
            }
        });*/
    }
/*
    private void onEqual(){
        //if the current state is an error, nothing to do.
        //A solution can only be found if the last character is a number
        if(lastNumeric && !stateError){
            //read the expression
            String txt=txtScreen.getText().toString();
            hist[histIndex % 5]=txt;
            histIndex++;
            //create an Expression(a class from exp4j library)
            Expression expression=new ExpressionBuilder(txt).build();
            try{
                //calculate the result and display
                double result=expression.evaluate();
                txtScreen.setText(Double.toString(result));
                ans=txtScreen.getText().toString();
                lastDot=lastAns=true;
            }catch (ArithmeticException e){
                //display an error message
                txtScreen.setText("Error");
                stateError=true;
                lastNumeric=false;
            }
            Toast.makeText(getApplicationContext(),"Please clear screen to continue to next operation",Toast.LENGTH_SHORT).show();
        }
    }*/
}
