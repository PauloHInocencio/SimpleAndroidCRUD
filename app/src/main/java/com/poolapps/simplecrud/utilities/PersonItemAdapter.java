package com.poolapps.simplecrud.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.model.Person;

import java.util.List;

public class PersonItemAdapter extends RecyclerView.Adapter<PersonItemAdapter.PersonViewHolder> {

    public static final String PERSON_KEY = "item_id";
    private List<Person> mPersons;
    private Context mContext;

    public PersonItemAdapter(Context context, List<Person> persons){
        mContext = context;
        mPersons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View personItemView = inflater.inflate(R.layout.person_item_view, parent, false);
        return new PersonViewHolder(personItemView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        final Person p = mPersons.get(position);
        holder.tvName.setText(String.format(" %s %s", p.getFirstName(), p.getLastName()));
        holder.tvAge.setText( String.valueOf(p.getAge()));
        holder.tvGender.setText(p.getGender());

        holder.mPersonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }


     static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAge;
        TextView tvGender;
         View mPersonView;

        PersonViewHolder(View personView) {
            super(personView);
            tvName = (TextView) personView.findViewById(R.id.tvName);
            tvAge = (TextView) personView.findViewById(R.id.tvAge);
            tvGender = (TextView) personView.findViewById(R.id.tvGender);
            mPersonView = personView;
        }
    }


}
