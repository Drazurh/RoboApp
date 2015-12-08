package com.robodoot.roboapp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.robodoot.dr.RoboApp.PololuHandler;
import com.robodoot.dr.facetracktest.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServoControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServoControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServoControlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    MainActivity parent = (MainActivity)getActivity();

    private String mParam1;
    private String mParam2;

    private EditText[] servoTargets;
    private Spinner[] servoNames;

    private PololuHandler pololu;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServoControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServoControlFragment newInstance(String param1, String param2) {
        ServoControlFragment fragment = new ServoControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ServoControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);




        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_servocontrol, container, false);

        parent = (MainActivity)getActivity();
        pololu = parent.pololu;


        servoNames = new Spinner[4];
        servoTargets = new EditText[4];

        servoNames[0]=(Spinner)rootView.findViewById(R.id.ServoSelector1);
        servoNames[1]=(Spinner)rootView.findViewById(R.id.ServoSelector2);
        servoNames[2]=(Spinner)rootView.findViewById(R.id.ServoSelector3);
        servoNames[3]=(Spinner)rootView.findViewById(R.id.ServoSelector4);

        servoTargets[0]=(EditText)rootView.findViewById(R.id.TargetField1);
        servoTargets[1]=(EditText)rootView.findViewById(R.id.TargetField2);
        servoTargets[2]=(EditText)rootView.findViewById(R.id.TargetField3);
        servoTargets[3]=(EditText)rootView.findViewById(R.id.TargetField4);

        for(int i=0; i<4; i++)
        {

            List<String> names = new ArrayList<String>();
            names.add("-");
            for(PololuHandler.Motor m : PololuHandler.Motor.values())
            {
                names.add(m.name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item,names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            servoNames[i].setAdapter(adapter);

        }

        Button button = (Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                for(int i=0;i<servoNames.length;i++){

                    for(PololuHandler.Motor m:PololuHandler.Motor.values()){
                        int targetVal;
                        if(servoNames[i].getSelectedItem()==null)continue;
                        try{
                            targetVal=Integer.parseInt(servoTargets[i].getText().toString());
                        }
                        catch(NumberFormatException e)
                        {
                            continue;
                        }
                        if(m.name==servoNames[i].getSelectedItem())
                        {
                            pololu.setTarget(m.number,targetVal);
                            continue;
                        }


                    }

                }


            }



        });



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
