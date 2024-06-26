package fpoly.account.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import fpoly.account.myapplication.R;
import fpoly.account.myapplication.models.Model;

public class ModelAdapter extends ArrayAdapter<Model> {
    public ModelAdapter(Context context, List<Model> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.demo11_item_view, parent, false);
        }
        TextView title = convertView.findViewById(R.id.demo11_item_tvtitle);
        TextView description = convertView.findViewById(R.id.demo11_item_tvdescription);

        Model model = getItem(position);
        title.setText(model.getTitle());
        description.setText(model.getDescription());

        return convertView;
    }
}
