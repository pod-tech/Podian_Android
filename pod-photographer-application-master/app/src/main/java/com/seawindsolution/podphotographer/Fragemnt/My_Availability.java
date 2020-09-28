package com.seawindsolution.podphotographer.Fragemnt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.seawindsolution.podphotographer.Pojo.Availability;
import com.seawindsolution.podphotographer.R;

import java.util.ArrayList;
import java.util.List;

public class My_Availability extends RecyclerView.Adapter<My_Availability.ViewHolder> {

    public Context context;
    private List<Availability> arrayList;
    ArrayList<String> stringArrayList = new ArrayList<>();

    public My_Availability(Context context, List<Availability> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.availability, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        stringArrayList.clear();
        Availability availability = arrayList.get(position);
        holder.r_date.setText(availability.getDate());
        holder.r_date.setVisibility(View.VISIBLE);

        if (availability.getA().equals("1")) {
            stringArrayList.add("12 - 12:30 AM");
        }

        if (availability.getB().equals("1")) {
            stringArrayList.add("12:30 - 01 AM");
        }

        if (availability.getC().equals("1")) {
            stringArrayList.add("01 - 01:30 AM");
        }

        if (availability.getD().equals("1")) {
            stringArrayList.add("1:30 - 02 AM");
        }

        if (availability.getE().equals("1")) {
            stringArrayList.add("02 - 02:30 AM");
        }

        if (availability.getF().equals("1")) {
            stringArrayList.add("2:30 - 03 AM");
        }

        if (availability.getG().equals("1")) {
            stringArrayList.add("03 - 3:30 AM");
        }

        if (availability.getH().equals("1")) {
            stringArrayList.add("3:30 - 04 AM");
        }

        if (availability.getI().equals("1")) {
            stringArrayList.add("04 - 4:30 AM");
        }

        if (availability.getJ().equals("1")) {
            stringArrayList.add("4:30 - 05 AM");
        }

        if (availability.getK().equals("1")) {
            stringArrayList.add("05 - 5:30 AM");
        }

        if (availability.getL().equals("1")) {
            stringArrayList.add("5:30 - 06 AM");
        }

        if (availability.getM().equals("1")) {
            stringArrayList.add("06 - 6:30 AM");
        }

        if (availability.getN().equals("1")) {
            stringArrayList.add("6:30 - 07 AM");
        }

        if (availability.getO().equals("1")) {
            stringArrayList.add("07 - 7:30 AM");
        }

        if (availability.getP().equals("1")) {
            stringArrayList.add("7:30 - 08 AM");
        }

        if (availability.getQ().equals("1")) {
            stringArrayList.add("08 - 8:30 AM");
        }

        if (availability.getR().equals("1")) {
            stringArrayList.add("8:30 - 09 AM");
        }

        if (availability.getS().equals("1")) {
            stringArrayList.add("09 - 9:30 AM");
        }

        if (availability.getT().equals("1")) {
            stringArrayList.add("9:30 - 10 AM");
        }

        if (availability.getU().equals("1")) {
            stringArrayList.add("10 - 10:30 AM");
        }

        if (availability.getV().equals("1")) {
            stringArrayList.add("10:30 - 11 AM");
        }

        if (availability.getW().equals("1")) {
            stringArrayList.add("11 - 11:30 AM");
        }

        if (availability.getX().equals("1")) {
            stringArrayList.add("11:30 - 12 AM");
        }








        if (availability.getAa().equals("1")) {
            stringArrayList.add("12 - 12:30 PM");
        }

        if (availability.getBb().equals("1")) {
            stringArrayList.add("12:30 - 13 PM");
        }

        if (availability.getCc().equals("1")) {
            stringArrayList.add("13 - 13:30 PM");
        }

        if (availability.getDd().equals("1")) {
            stringArrayList.add("13:30 - 14 PM");
        }

        if (availability.getEe().equals("1")) {
            stringArrayList.add("14 - 14:30 PM");
        }

        if (availability.getFf().equals("1")) {
            stringArrayList.add("14:30 - 15 PM");
        }

        if (availability.getGg().equals("1")) {
            stringArrayList.add("15 - 15:30 PM");
        }

        if (availability.getHh().equals("1")) {
            stringArrayList.add("15:30 - 16 PM");
        }

        if (availability.getIi().equals("1")) {
            stringArrayList.add("16 - 16:30 PM");
        }

        if (availability.getJj().equals("1")) {
            stringArrayList.add("16:30 - 17 PM");
        }

        if (availability.getKk().equals("1")) {
            stringArrayList.add("17 - 17:30 PM");
        }

        if (availability.getLl().equals("1")) {
            stringArrayList.add("17:30 - 18 PM");
        }

        if (availability.getMm().equals("1")) {
            stringArrayList.add("18 - 18:30 PM");
        }

        if (availability.getNn().equals("1")) {
            stringArrayList.add("18:30 - 19 PM");
        }

        if (availability.getOo().equals("1")) {
            stringArrayList.add("19 - 19:30 PM");
        }

        if (availability.getPp().equals("1")) {
            stringArrayList.add("19:30 - 20 PM");
        }

        if (availability.getQq().equals("1")) {
            stringArrayList.add("20 - 20:30 PM");
        }

        if (availability.getRr().equals("1")) {
            stringArrayList.add("20:30 - 21 PM");
        }

        if (availability.getSs().equals("1")) {
            stringArrayList.add("21 - 21:30 PM");
        }

        if (availability.getTt().equals("1")) {
            stringArrayList.add("21:30 - 22 PM");
        }

        if (availability.getUu().equals("1")) {
            stringArrayList.add("22 - 22:30 PM");
        }

        if (availability.getVv().equals("1")) {
            stringArrayList.add("22:30 - 23 PM");
        }

        if (availability.getWw().equals("1")) {
            stringArrayList.add("23 - 23:30 PM");
        }

        if (availability.getXx().equals("1")) {
            stringArrayList.add("23:30 - 24 PM");
        }

        GridAdapter gridAdapter = new GridAdapter(context, R.layout.grid_view_items, stringArrayList);
        holder.gridView.setAdapter(gridAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView r_date;
        GridView gridView;

        ViewHolder(View view) {
            super(view);

            r_date = view.findViewById(R.id.r_date);

            gridView = view.findViewById(R.id.grid);
            gridView.setVerticalScrollBarEnabled(false);
            gridView.setHorizontalScrollBarEnabled(false);
            gridView.setClickable(false);
        }
    }
}
