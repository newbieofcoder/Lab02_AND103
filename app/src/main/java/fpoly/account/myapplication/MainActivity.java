package fpoly.account.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import fpoly.account.myapplication.ModelHelper.ModelDatabaseHelper;
import fpoly.account.myapplication.adapters.ModelAdapter;
import fpoly.account.myapplication.models.Model;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ModelDatabaseHelper db;
    private ModelAdapter adapter;
    private List<Model> list;
    private EditText edttitle, edtdescription;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.demo11list);
        edttitle = findViewById(R.id.demo11txttitle);
        edtdescription = findViewById(R.id.demo11txtdescription);
        add = findViewById(R.id.demo11btnadd);
        add.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String description = edtdescription.getText().toString();
            Model model = new Model(title, description);
            db.them(model);
            list.add(model);
            adapter.notifyDataSetChanged();
        });
        db = new ModelDatabaseHelper(this);
        list = db.getAllData();
        adapter = new ModelAdapter(this, list);
        listView.setAdapter(adapter);
    }
}