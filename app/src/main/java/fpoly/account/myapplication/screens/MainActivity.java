package fpoly.account.myapplication.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.myapplication.DAO.ToDoDAO;
import fpoly.account.myapplication.ModelHelper.ToDoDatabaseHelper;
import fpoly.account.myapplication.R;
import fpoly.account.myapplication.adapters.SpacingItemDecoration;
import fpoly.account.myapplication.adapters.ToDoAdapter;
import fpoly.account.myapplication.models.ToDo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ToDoDatabaseHelper db;
    private ToDoAdapter adapter;
    private List<ToDo> list;
    private EditText edttitle, edtcontent, edtdate, edttype;
    private Button add;
    private ToDoDAO toDoDAO;
    private ToDo currentToDo = null;
    AlertDialog.Builder builder;

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
        recyclerView = findViewById(R.id.demo11list);
        edttitle = findViewById(R.id.tv_title);
        edtcontent = findViewById(R.id.tv_content);
        edtdate = findViewById(R.id.tv_date);
        edttype = findViewById(R.id.tv_type);
        add = findViewById(R.id.btnAdd);
        toDoDAO = new ToDoDAO(this);
        list = toDoDAO.getAllData();
        adapter = new ToDoAdapter(list);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20);
        recyclerView.addItemDecoration(spacingItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        add.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                ToDo toDo = new ToDo(0, title, content, date, type, 0);
                toDoDAO.addToDo(toDo);
                list = toDoDAO.getAllData();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                edttitle.setText("");
                edtcontent.setText("");
                edtdate.setText("");
                edttype.setText("");
                onResume();
            }
        });
        adapter.setOnItemClickListener(new ToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToDo toDo) {
                edttitle.setText(toDo.getTitle());
                edtcontent.setText(toDo.getContent());
                edtdate.setText(toDo.getDate());
                edttype.setText(toDo.getType());
                currentToDo = toDo;
            }

            @Override
            public void onDeleteClick(int position) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xoá");
                builder.setMessage("Bạn có chắc chắn muốn xóa?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    ToDo toDo = list.get(position);
                    toDoDAO.deleteToDo(toDo.getId());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    edttitle.setText("");
                    edtcontent.setText("");
                    edtdate.setText("");
                    edttype.setText("");
                    list = toDoDAO.getAllData();
                    onResume();
                });
                builder.setNegativeButton("Không", null);
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                ToDo toDo = list.get(position);
                String title = edttitle.getText().toString();
                String content = edtcontent.getText().toString();
                String date = edtdate.getText().toString();
                String type = edttype.getText().toString();
                if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xác nhận sửa");
                    builder.setMessage("Bạn có chắc chắn muốn sửa dòng " + String.valueOf(position + 1) +" ?");
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        toDo.setTitle(title);
                        toDo.setContent(content);
                        toDo.setDate(date);
                        toDo.setType(type);
                        toDoDAO.updateToDo(toDo);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        edttitle.setText("");
                        edtcontent.setText("");
                        edtdate.setText("");
                        edttype.setText("");
                        list = toDoDAO.getAllData();
                        onResume();
                    });
                    builder.setNegativeButton("Không", null);
                    builder.show();
                }
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                ToDo toDo = list.get(position);
                toDo.setStatus(isDone ? 1 : 0);
                toDoDAO.updateStatusToDo(toDo);
                adapter.notifyDataSetChanged();
                list = toDoDAO.getAllData();
                onResume();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ToDoAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}