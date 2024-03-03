/**
 * TelaContatos é uma Activity que permite visualizar e gerenciar os contatos cadastrados.
 * Nesta tela, os usuários podem ver uma lista de contatos, excluir todos os contatos e acessar
 * informações detalhadas de cada contato.
 */
package br.com.application.carbill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TelaContatos extends AppCompatActivity {

    // Componentes da interface de usuário
    public ImageButton btnExcluir;
    public TextView txtNumContatos;
    public ListView listPessoas;

    // Banco de dados
    private SQLiteDatabase banco;
    private static final String DATABASE_NAME = "banco_de_dados_carbill";

    // Lista de pessoas
    public ArrayList<ClassPessoa> pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_contatos);

        // Inicialização dos componentes da interface de usuário
        btnExcluir = (ImageButton) findViewById(R.id.btnExcluir);
        listPessoas = (ListView) findViewById(R.id.listPessoas);
        txtNumContatos = (TextView) findViewById(R.id.txtNumContatos);

        // Lista os contatos
        listarPessoas();

        // Define o comportamento do botão de exclusão
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacao();
            }
        });

        // Define o comportamento ao clicar em um item da lista de contatos
        listPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Abre a tela de informações do cadastro do contato selecionado
                Intent intent = new Intent(TelaContatos.this, Tela_infos_do_cadastro.class);
                try {
                    intent.putExtra("id_pessoa", pessoas.get(i).getId());
                } catch (Exception e) {
                    Toast.makeText(TelaContatos.this, "erro: itent.putExtra(id)", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    /**
     * Método para listar os contatos cadastrados.
     */
    public void listarPessoas(){
        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            Cursor cursor = banco.rawQuery("SELECT id_pessoa, nome, sobrenome, apelido, telefone, rua, bairro, numero, valor_por_corrida FROM TB_PESSOA order by nome asc;",null);

            pessoas = new ArrayList<ClassPessoa>();

            if(cursor.moveToFirst()){
                do{
                    int id_pessoa = cursor.getInt((int) cursor.getColumnIndex("id_pessoa"));
                    String nome = cursor.getString((int) cursor.getColumnIndex("nome"));
                    String sobrenome = cursor.getString((int) cursor.getColumnIndex("sobrenome"));
                    String apelido = cursor.getString((int) cursor.getColumnIndex("apelido"));

                    ClassPessoa p = new ClassPessoa(id_pessoa, nome, sobrenome, apelido);

                    pessoas.add(p);

                }while (cursor.moveToNext());
            }

            ArrayAdapter<ClassPessoa> adapter = new AdapterTuplaTodasAsPessoas(this, pessoas);
            txtNumContatos.setText(pessoas.size() + " pessoas cadastradas");
            listPessoas.setAdapter(adapter);

            banco.close();
        }catch (Exception e){
            Toast.makeText(this, "erro: listarPessoas()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Método para deletar todos os contatos do banco de dados.
     */
    public void deletarTudo(){
        try {
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            banco.execSQL("DELETE FROM TB_VIAGEM;");
            banco.execSQL("DELETE FROM TB_PESSOA;");
            banco.close();
        } catch (Exception e) {
            Toast.makeText(this, "erro: deletarTudo()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Método para exibir uma caixa de diálogo de confirmação antes de excluir todos os contatos.
     */
    public void confirmacao(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("DELETAR TUDO");
        msgBox.setIcon(R.drawable.ic_lixeira);
        msgBox.setMessage("Essa é uma ação extrema! Todos os contatos e tudo relacionado a eles será deletado permanentemente. Deseja continuar?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletarTudo();
                finish();
                Toast.makeText(TelaContatos.this, "Tudo foi deletado.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TelaContatos.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();            
            }
        });
        msgBox.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        listarPessoas();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}