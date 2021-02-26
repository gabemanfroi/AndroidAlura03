package gabemanfroi.com.notepad.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gabemanfroi.com.notepad.R;
import gabemanfroi.com.notepad.dao.NotaDAO;
import gabemanfroi.com.notepad.model.Nota;
import gabemanfroi.com.notepad.ui.recyclerview.adapter.ListaNotasAdapter;
import gabemanfroi.com.notepad.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ADICIONAD_NOTA;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        iniciaNotas();
        configuraBotaoInsereNota();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(v -> {
            navegaParaFormularioNotaAdicao();
        });
    }

    private void iniciaNotas() {
        NotaDAO dao = new NotaDAO();
        List<Nota> todasNotas = dao.todos();
        configuraRecyclerView(todasNotas);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isResultadoComNota(requestCode, data)) {
            if (isResultadoOk(resultCode)) {
                adicionaNota(data);
            }
        }

        if (isResultadoAlteraNota(requestCode, data)) {
            if (isResultadoOk(resultCode)) {

                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (isPosicaoValida(posicaoRecebida)) {
                    alterNota(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um erro na alteração da nota.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void alterNota(Nota notaRecebida, int posicaoRecebida) {
        new NotaDAO().altera(posicaoRecebida, notaRecebida);
        adapter.altera(posicaoRecebida, notaRecebida);
    }

    private boolean isPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean isResultadoAlteraNota(int requestCode,
                                          @Nullable Intent data) {
        return isCodigoRequisicaoAlteraNota(requestCode)
                && hasNota(data) && hasPosicao(data);
    }

    private boolean isCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean hasPosicao(@Nullable Intent data) {
        return data.hasExtra(CHAVE_POSICAO);
    }

    private void adicionaNota(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean isResultadoComNota(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicaoInsereNota(requestCode) && hasNota(data);
    }

    private boolean hasNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean isResultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean isCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ADICIONAD_NOTA;
    }

    private void navegaParaFormularioNotaAdicao() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_ADICIONAD_NOTA);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.activity_task_list_lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
        configuraLayoutManager(listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView taskList) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(taskList);
    }

    private void navegaParaFormularioNotaActivityEdicao(Nota nota, int posicao) {
        Intent intentNavegaParaFormulario = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        intentNavegaParaFormulario.putExtra(CHAVE_NOTA, nota);
        intentNavegaParaFormulario.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(intentNavegaParaFormulario, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    private void configuraLayoutManager(RecyclerView taskList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        taskList.setLayoutManager(linearLayoutManager);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView taskList) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        taskList.setAdapter(adapter);
        adapter.setOnItemClickListener((nota, posicao) -> {
            navegaParaFormularioNotaActivityEdicao(nota, posicao);
        });
    }


}