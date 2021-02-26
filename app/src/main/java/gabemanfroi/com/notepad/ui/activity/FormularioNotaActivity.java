package gabemanfroi.com.notepad.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import gabemanfroi.com.notepad.R;
import gabemanfroi.com.notepad.model.Nota;

import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static gabemanfroi.com.notepad.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida = POSICAO_INVALIDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        inicializaNota();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isMenuSalvaNota(item)) {
            Nota nota = criaNota();
            retornaNota(nota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializaNota() {
        Intent dadosRecebidos = getIntent();
        if (dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

            inicializaCampos(notaRecebida);
        }
    }

    private Nota criaNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);

        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private void inicializaCampos(Nota notaRecebida) {
        inicializaTitulo(notaRecebida);
        inicializaDescricao(notaRecebida);
    }

    private void inicializaDescricao(Nota notaRecebida) {
        TextView descricao = findViewById(R.id.formulario_nota_descricao);
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaTitulo(Nota notaRecebida) {
        TextView titulo = findViewById(R.id.formulario_nota_titulo);
        titulo.setText(notaRecebida.getTitulo());
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(RESULT_OK, resultadoInsercao);
    }

    private boolean isMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salva;
    }
}