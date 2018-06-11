package io.iqube.srinath.imeddispenser.prescription;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.models.Prescription;
import io.iqube.srinath.imeddispenser.models.Schedule;
import io.iqube.srinath.imeddispenser.network.ServiceGenerator;
import io.realm.Realm;
import io.realm.RealmList;

public class PrescriptionActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    MedicationExpandableListViewAdapter adapter;
    Realm realm;
    List<String> medicationList;
    HashMap<String, List<String>> scheduleList;
    SimpleDraweeView doctorImage;
    TextView doctorName, doctorNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        realm = Realm.getDefaultInstance();
        int id = getIntent().getExtras().getInt("PrescriptionId");
        Prescription prescription = realm.where(Prescription.class).equalTo("id", id).findFirst();
        RealmList<Schedule> schedules = prescription.getSchedules();
        List<String> compositions = new ArrayList<>();
        for (int j = 0; j < schedules.size(); j++) {
            Schedule schedule = schedules.get(j);
            String composition = schedule.getComposition().getName();
            if (!compositions.contains(composition))
                compositions.add(composition);
        }
        medicationList = compositions;
        scheduleList = new HashMap<>();
        for (int i = 0; i < medicationList.size(); i++) {
            String composition = medicationList.get(i);
            List<Integer> temp = new ArrayList<>();
            temp.add(0);
            temp.add(0);
            temp.add(0);
            temp.add(0);
            temp.add(0);
            temp.add(0);
            for (int j = 0; j < schedules.size(); j++) {
                Schedule schedule = schedules.get(i);
                if (schedule.getComposition().getName().equals(composition)) {
                    int data = temp.get(schedule.getSlot());
                    temp.set(schedule.getSlot(), data + schedule.getQty());
                }
            }
            List<String> data = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0:
                        data.add("Before Breakfast-" + temp.get(j));
                        break;
                    case 1:
                        data.add("After Breakfast-" + temp.get(j));
                        break;
                    case 2:
                        data.add("Before Lunch-" + temp.get(j));
                        break;
                    case 3:
                        data.add("After Lunch-" + temp.get(j));
                        break;
                    case 4:
                        data.add("Before Dinner-" + temp.get(j));
                        break;
                    case 5:
                        data.add("After Dinner-" + temp.get(j));
                        break;
                }
            }
            scheduleList.put(composition, data);
        }
        adapter = new MedicationExpandableListViewAdapter(medicationList, scheduleList, this);
        expandableListView = findViewById(R.id.medication_list);
        expandableListView.setAdapter(adapter);

        doctorImage = findViewById(R.id.doctor_image);
        doctorName = findViewById(R.id.doctor_name);

        String doc_name = prescription.getDoctor().getFirst_name() + " " + prescription.getDoctor().getLast_name();
        String doc_notes = prescription.getDoctorNote();
        doctorImage.setImageURI(ServiceGenerator.BASE_URL + prescription.getDoctor().getProfile_pic_url());
        doctorName.setText(doc_name);
        doctorNotes = findViewById(R.id.doctor_note);
        doctorNotes.setText(doc_notes);
    }
}
