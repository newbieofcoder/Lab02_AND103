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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        TextView tv_title = convertView.findViewById(R.id.tv_title);
        TextView tv_content = convertView.findViewById(R.id.tv_content);
        TextView tv_date = convertView.findViewById(R.id.tv_date);

        Model model = getItem(position);
        tv_title.setText(model.getTitle());
        tv_content.setText(model.getContent());
        tv_date.setText(model.getDate());

        return convertView;
    }
}
