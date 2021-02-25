package gabemanfroi.com.notepad.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gabemanfroi.com.notepad.R;
import gabemanfroi.com.notepad.dao.NotaDAO;
import gabemanfroi.com.notepad.model.Nota;
import gabemanfroi.com.notepad.ui.recyclerview.adapter.ListaNotasAdapter;

import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class TaskListActivity extends AppCompatActivity {

    public static final int CODIGO_REQUISICAO_NOTA = 1;

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        iniciaNotas();
        configuraBotaoInsereNota();
    }

    private void iniciaNotas() {
        List<Nota> todasNotas = new NotaDAO().todos();
        configuraRecyclerView(todasNotas);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(v -> {
            navegaParaFormularioNota();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (isResultadoComNota(requestCode, resultCode, data)) {
            adicionaNota(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adicionaNota(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean isResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodigoRequisicaoInsereNota(requestCode) && isCodigoResultadoInsereNota(resultCode) && isNota(data);
    }

    private boolean isNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodigoResultadoInsereNota(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean isCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_NOTA;
    }

    private void navegaParaFormularioNota() {
        Intent intent = new Intent(TaskListActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_NOTA);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView taskList = findViewById(R.id.activity_task_list_lista_notas_recycler_view);
        configuraAdapter(todasNotas, taskList);
        configuraLayoutManager(taskList);
    }

    private void configuraLayoutManager(RecyclerView taskList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        taskList.setLayoutManager(linearLayoutManager);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView taskList) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        taskList.setAdapter(adapter);
    }

}