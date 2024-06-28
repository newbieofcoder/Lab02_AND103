package fpoly.account.myapplication.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import fpoly.account.myapplication.ModelHelper.ModelDatabaseHelper;
import fpoly.account.myapplication.R;
import fpoly.account.myapplication.adapters.ModelAdapter;
import fpoly.account.myapplication.models.Model;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ModelDatabaseHelper db;
    private ModelAdapter adapter;
    private List<Model> list;
    private EditText edttitle, edtcontent, edtdate, edttype;
    private Button add, update, delete;
    private String oldTitle;

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
        edttitle = findViewById(R.id.tv_title);
        edtcontent = findViewById(R.id.tv_content);
        edtdate = findViewById(R.id.tv_date);
        edttype = findViewById(R.id.tv_type);
        add = findViewById(R.id.btnAdd);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        add.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Model model = new Model(title, content, date, type);
                db.add(model);
                list.add(model);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
        db = new ModelDatabaseHelper(this);
        list = db.getAllData();
        adapter = new ModelAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            edttitle.setText(list.get(position).getTitle());
            edtcontent.setText(list.get(position).getContent());
            edtdate.setText(list.get(position).getDate());
            edttype.setText(list.get(position).getType());
            oldTitle = list.get(position).getTitle();
        });
        update.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                List<Model> listData = db.getAllData();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getTitle().equals(oldTitle)) {
                        Model model = new Model(title, content, date, type);
                        db.update(model, oldTitle);
                        Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        edttitle.setText("");
                        edtcontent.setText("");
                        edtdate.setText("");
                        edttype.setText("");
                        onResume();
                        break;
                    }
                }
            }
        });
        delete.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            List<Model> listData = db.getAllData();
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getTitle().equals(title) && listData.get(i).getContent().equals(content) && listData.get(i).getDate().equals(date) && listData.get(i).getType().equals(type)) {
                    db.delete(listData.get(i));
                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    onResume();
                    break;
                }
            }
            edttitle.setText("");
            edtcontent.setText("");
            edtdate.setText("");
            edttype.setText("");
        });
    }

    @Override
    protected void onResume() {
        list = db.getAllData();
        adapter = new ModelAdapter(this, list);
        listView.setAdapter(adapter);
        super.onResume();
    }
}