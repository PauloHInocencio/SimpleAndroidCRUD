package com.poolapps.simplecrud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poolapps.simplecrud.model.Person;
import com.poolapps.simplecrud.utilities.PersonItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        List<Person> persons = new ArrayList<>();

        RecyclerView rvPerson = (RecyclerView) view.findViewById(R.id.rvPerson);
        PersonItemAdapter adapter = new PersonItemAdapter(getContext(), persons);
        rvPerson.setAdapter(adapter);

        return view;
    }
}
