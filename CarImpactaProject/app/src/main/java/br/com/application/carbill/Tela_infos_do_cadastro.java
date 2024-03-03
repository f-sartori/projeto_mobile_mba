/**
 * Tela_infos_do_cadastro é uma Activity que permite visualizar e editar as informações de um cadastro de pessoa.
 * Nesta tela, os usuários podem ver os detalhes de um cadastro existente e têm a opção de atualizar ou excluir o cadastro.
 */
package br.com.application.carbill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class Tela_infos_do_cadastro extends AppCompatActivity {

    // Componentes da interface de usuário
    public TextView txtTitulo;
    public EditText editTextNome, editTextSobrenome, editTextApelido, editTextTelefone, editTextRua, editTextBairro, editTextNumero, editTextValorPorCorrida;
    public Button buttonExcluir, buttonAtualizar;

    // ID da pessoa
    int id;

    //BANCO
    private SQLiteDatabase banco;
    private static final String DATABASE_NAME = "banco_de_dados_carbill";

    // Objeto pessoa
    ClassPessoa pessoa = new ClassPessoa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_do_cadastro);

        // Inicialização dos componentes da interface de usuário
        txtTitulo = findViewById(R.id.txtTitulo);
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobrenome = findViewById(R.id.editTextSobrenome);
        editTextApelido = findViewById(R.id.editTextApelido);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextRua = findViewById(R.id.editTextRua);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextNumero = findViewById(R.id.editTextNumero);
        editTextValorPorCorrida = findViewById(R.id.editTextValorPorCorrida);
        buttonExcluir = findViewById(R.id.buttonExcluir);
        buttonAtualizar = findViewById(R.id.ButtonAtualizar);

        // Define o comportamento do botão de exclusão
        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnExcluir();
            }
        });

        // Define o comportamento do botão de atualização
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePessoa();
                Toast.makeText(Tela_infos_do_cadastro.this, "Atualizado com sucesso.", Toast.LENGTH_SHORT).show();
            }
        });

        // Obtém o ID da pessoa passado como extra da Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id_pessoa");
        }

        // Define o ID da pessoa
        pessoa.setId(id);

        // Carrega as informações do cadastro da pessoa do banco de dados
        acessoBanco();

        // Exibe as informações do cadastro da pessoa na interface de usuário
        listoInfos();
    }

    /**
     * Método para acessar o banco de dados e obter as informações do cadastro da pessoa.
     */
    public void acessoBanco(){
        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            Cursor cursor = banco.rawQuery("SELECT id_pessoa, nome, sobrenome, apelido, telefone, rua, bairro, numero, valor_por_corrida FROM TB_PESSOA WHERE id_pessoa = " + pessoa.getId() + ";",null);

            if(cursor.moveToFirst()){
                do{
                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    String sobrenome = cursor.getString(cursor.getColumnIndex("sobrenome"));
                    String apelido = cursor.getString(cursor.getColumnIndex("apelido"));
                    String telefone = cursor.getString(cursor.getColumnIndex("telefone"));
                    String rua = cursor.getString(cursor.getColumnIndex("rua"));
                    String bairro = cursor.getString(cursor.getColumnIndex("bairro"));
                    int numero = cursor.getInt(cursor.getColumnIndex("numero"));
                    double valor_por_corrida = cursor.getDouble(cursor.getColumnIndex("valor_por_corrida"));

                    pessoa.setNome(nome);
                    pessoa.setSobrenome(sobrenome);
                    pessoa.setApelido(apelido);
                    pessoa.setTelefone(telefone);
                    pessoa.setRua(rua);
                    pessoa.setBairro(bairro);
                    pessoa.setNumero(numero);
                    pessoa.setValor_por_corrida(valor_por_corrida);

                }while (cursor.moveToNext());
            }

            banco.close();
        }catch (Exception e){
            Toast.makeText(this, "erro: acessobanco()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Método para exibir as informações do cadastro da pessoa na interface de usuário.
     */
    public void listoInfos(){
        txtTitulo.setText(pessoa.getNome() + " " + pessoa.getSobrenome());
        editTextNome.setText(pessoa.getNome());
        editTextSobrenome.setText(pessoa.getSobrenome());
        editTextApelido.setText(pessoa.getApelido());
        editTextTelefone.setText(pessoa.getTelefone());
        editTextRua.setText(pessoa.getRua());
        editTextBairro.setText(pessoa.getBairro());
        editTextNumero.setText(String.valueOf(pessoa.getNumero()));
        editTextValorPorCorrida.setText(String.valueOf(pessoa.getValor_por_corrida()));
    }

    /**
     * Método para verificar se a pessoa tem alguma dívida pendente.
     * @return true se a pessoa tem dívida, false caso contrário.
     */
    public boolean temDivida(){
        boolean temDivida = false;
        double deve = 0.0;

        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            Cursor cursor = banco.rawQuery("SELECT SUM(valor) Total FROM TB_PESSOA INNER JOIN tb_viagem ON tb_pessoa.id_pessoa = tb_viagem.id_pessoa INNER JOIN tb_tipo ON tb_viagem.id_tipo = tb_tipo.id_tipo where tb_pessoa.id_pessoa = "+ pessoa.getId() + ";", null);

            if(cursor.moveToFirst()){
                do{
                    deve = cursor.getDouble(cursor.getColumnIndex("Total"));
                    pessoa.setDivida_total(deve);
                }while (cursor.moveToNext());
            }

            temDivida = deve > 0;

            banco.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return temDivida;
    }

    /**
     * Método para excluir a pessoa do banco de dados.
     */
    public void deletarPessoa(){
        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            banco.execSQL("DELETE FROM TB_PESSOA WHERE id_pessoa = " + pessoa.getId());

            banco.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Método para atualizar as informações da pessoa no banco de dados.
     */
    public void updatePessoa(){
        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            String sql = "UPDATE TB_PESSOA SET nome = ? WHERE id_pessoa = ?;";
            SQLiteStatement stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextNome.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET sobrenome = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextSobrenome.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET apelido = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextApelido.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET telefone = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextTelefone.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET rua = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextRua.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET bairro = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextBairro.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET numero = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextNumero.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            sql = "UPDATE TB_PESSOA SET valor_por_corrida = ? WHERE id_pessoa = ?;";
            stmt = banco.compileStatement(sql);
            stmt.bindString(1, editTextValorPorCorrida.getText().toString());
            stmt.bindLong(2, pessoa.getId());
            stmt.executeUpdateDelete();

            banco.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Método chamado quando a Activity é finalizada. Realiza a transição de animação ao sair da tela.
     */
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}