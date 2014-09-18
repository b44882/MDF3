package com.fullsail.widget;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by administrator on 9/17/14.
 */
public class ListFragment extends android.app.ListFragment{

    ArrayList adapterList;
    public static final String TAG = "ListFragment.TAG";
    private static final String ARG_TEXT = "ListFragment.ARG_ARRAY";

    private Callbacks mCallbacks;

    public interface Callbacks {

        public void onItemSelected(ListActivity.PassableObject object, int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (Callbacks) activity;
        } catch (ClassCastException ex) {
            Log.e(TAG, "Casting the activity as a Callbacks listener failed"
                    + ex);
            mCallbacks = null;
        }
    }

    public static ListFragment newInstance(ArrayList listArray) {
        ListFragment frag = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_TEXT, listArray);
        frag.setArguments(args);

        return frag;
    }

    private void setListArray(ArrayList listArray){
        Bundle args = getArguments();
        if(args != null && args.containsKey(ARG_TEXT)) {
            args.putParcelableArrayList("master", listArray);
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listArray);
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        Bundle args = getArguments();
        if(args != null && args.containsKey(ARG_TEXT)) {
            adapterList = args.getParcelableArrayList(ARG_TEXT);
            setListArray(adapterList);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListActivity.PassableObject object = (ListActivity.PassableObject) this.getListAdapter().getItem(position);

        if (mCallbacks != null) {
            mCallbacks.onItemSelected(object, position);
        }


        Log.i(TAG, "Breakpoint");

    }
}
