package fpoly.account.myapplication.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private Context mContext = this;
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
        toDoDAO = new ToDoDAO(mContext);
        list = toDoDAO.getAllData();
        adapter = new ToDoAdapter(list);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20);
        recyclerView.addItemDecoration(spacingItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        edttype.setOnClickListener(v -> {
            String[] type = {"De", "Trung binh", "Kho"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Chon muc do kho cua cong viec");
            builder.setItems(type, (dialog, which) -> {
                edttype.setText(type[which]);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        add.setOnClickListener(v -> {
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(mContext, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                ToDo toDo = new ToDo(0, title, content, date, type, 0);
                toDoDAO.addToDo(toDo);
                list = toDoDAO.getAllData();
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "Thêm thành công", Toast.LENGTH_SHORT).show();
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
                builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xoá");
                builder.setIcon(R.drawable.baseline_warning_24);
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
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_edit_dialog, null);
                EditText edttitle = view.findViewById(R.id.tv_edit_title);
                EditText edtcontent = view.findViewById(R.id.tv_edit_content);
                EditText edtdate = view.findViewById(R.id.tv_edit_date);
                EditText edttype = view.findViewById(R.id.tv_edit_type);
                edttitle.setText(toDo.getTitle());
                edtcontent.setText(toDo.getContent());
                edtdate.setText(toDo.getDate());
                edttype.setText(toDo.getType());
                edttype.setOnClickListener(v -> {
                    String[] type = {"De", "Trung binh", "Kho"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setTitle("Chon muc do kho cua cong viec");
                    builder1.setItems(type, (dialog, which) -> {
                        edttype.setText(type[which]);
                    });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                });
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                btnUpdate.setOnClickListener(v -> {
                    String title = edttitle.getText().toString();
                    String content = edtcontent.getText().toString();
                    String date = edtdate.getText().toString();
                    String type = edttype.getText().toString();
                    if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        toDo.setTitle(title);
                        toDo.setContent(content);
                        toDo.setDate(date);
                        toDo.setType(type);
                        toDoDAO.updateToDo(toDo);
                        list = toDoDAO.getAllData();
                        adapter.notifyDataSetChanged();
                        onResume();
                        Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(v -> alertDialog.dismiss());
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                ToDo toDo = list.get(position);
                toDo.setStatus(isDone ? 1 : 0);
                toDoDAO.updateStatusToDo(toDo);
                list = toDoDAO.getAllData();
                onResume();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ToDoAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }
}