package io.iqube.srinath.imeddispenser.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import io.iqube.srinath.imeddispenser.network.API_interface;
import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.network.ServiceGenerator;
import io.iqube.srinath.imeddispenser.models.Prescription;
import io.iqube.srinath.imeddispenser.models.User;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PrescriptionListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Realm realm;
    private RealmResults<Prescription> prescriptionRealmResults;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PrescriptionListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PrescriptionListFragment newInstance(int columnCount) {
        PrescriptionListFragment fragment = new PrescriptionListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescription_list, container, false);

        realm = Realm.getDefaultInstance();
        prescriptionRealmResults = realm.where(Prescription.class).findAll();
        final MyPrescriptionRecyclerViewAdapter adapter = new MyPrescriptionRecyclerViewAdapter(prescriptionRealmResults, new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Prescription item) {
                Intent intent = new Intent(getContext(), PrescriptionActivity.class);
                intent.putExtra("PrescriptionId", item.getId());
                startActivity(intent);
            }
        });

        API_interface client = ServiceGenerator.getClient("", "", getContext()).create(API_interface.class);
        User user = realm.where(User.class).findFirst();
        String username = user.getUsername();
        int id = user.getId();
        client.fetchPrescription(username, id).enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    prescriptionRealmResults = realm.where(Prescription.class).findAll();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Prescription>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
        }
        return view;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Prescription item);
    }
}
