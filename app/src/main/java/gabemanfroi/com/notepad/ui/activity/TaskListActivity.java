package gabemanfroi.com.notepad.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import gabemanfroi.com.notepad.R;
import gabemanfroi.com.notepad.dao.NotaDAO;
import gabemanfroi.com.notepad.model.Nota;
import gabemanfroi.com.notepad.ui.adapter.ListaNotasAdapter;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ListView taskList = findViewById(R.id.listView);

        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10000; i++) {

            dao.insere(new Nota("Nota " + i, "Descrição " + i));

        }
        List<Nota> todasNotas = dao.todos();

        taskList.setAdapter(new ListaNotasAdapter(TaskListActivity.this, todasNotas));
    }
}