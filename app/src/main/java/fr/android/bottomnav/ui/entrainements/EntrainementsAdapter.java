package fr.android.bottomnav.ui.entrainements;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.android.bottomnav.R;
import fr.android.bottomnav.ui.Training;

public class EntrainementsAdapter extends RecyclerView.Adapter<EntrainementsAdapter.ViewHolder> {

    private ArrayList<Training> localDataSet;
    private String date, distance, duration;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView_date;
        private final TextView textView_distance;
        private final TextView textView_duration;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView_date = (TextView) view.findViewById(R.id.textView_date);
            textView_distance = (TextView) view.findViewById(R.id.textView_distance);
            textView_duration = (TextView) view.findViewById(R.id.textView_duration);

        }

        public TextView getTextViewDate() {
            return textView_date;
        }

        public TextView getTextViewDistance() {
            return textView_distance;
        }

        public TextView getTextViewDuration() {
            return textView_duration;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public EntrainementsAdapter(ArrayList<Training> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.training_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Training training = localDataSet.get(position);
        date = training.getDate();
        distance = training.getDistance();
        duration = training.getDuration();

        Log.d("test", date + " " + distance + " " + duration);

        viewHolder.getTextViewDate().setText(date);
        viewHolder.getTextViewDistance().setText(distance);
        viewHolder.getTextViewDuration().setText(duration);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
