package io.iqube.srinath.imeddispenser.prescription;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.iqube.srinath.imeddispenser.prescription.PrescriptionListFragment.OnListFragmentInteractionListener;
import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.network.ServiceGenerator;
import io.iqube.srinath.imeddispenser.models.Prescription;

public class MyPrescriptionRecyclerViewAdapter extends RecyclerView.Adapter<MyPrescriptionRecyclerViewAdapter.ViewHolder> {

    private final List<Prescription> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPrescriptionRecyclerViewAdapter(List<Prescription> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.prescription = mValues.get(position);
        holder.doctor.setText(holder.prescription.getDoctor().getFirst_name() + " " + holder.prescription.getDoctor().getLast_name());
        holder.doctorNote.setText(holder.prescription.getDoctorNote());
        holder.doctorImage.setImageURI(ServiceGenerator.BASE_URL + holder.prescription.getDoctor().getProfile_pic_url());
        holder.iView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.prescription);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View iView;
        public final TextView doctor;
        public final TextView doctorNote;
        public Prescription prescription;
        public final SimpleDraweeView doctorImage;

        public ViewHolder(View view) {
            super(view);
            iView = view.findViewById(R.id.list_item_view);
            doctor = view.findViewById(R.id.doctor);
            doctorNote = view.findViewById(R.id.doctor_note);
            doctorImage = view.findViewById(R.id.doctor_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + doctor.getText() + "'";
        }
    }
}
