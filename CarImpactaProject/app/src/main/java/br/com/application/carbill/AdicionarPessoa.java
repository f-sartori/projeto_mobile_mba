/**
 * AdicionarPessoa é uma Activity que permite adicionar um novo cadastro de pessoa ao banco de dados.
 * Os usuários podem preencher os campos com as informações da nova pessoa e cadastrar no sistema.
 */
package br.com.application.carbill;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdicionarPessoa extends AppCompatActivity {
    //LEMBRAR DE FAZER O OVERRIDE ONRESUME PARA ATUALIZAR

    // Componentes da interface de usuário
    public TextView textAddNome, textAddSobrenome, textAddApelido, textAddTelefone, textAddBairro, textAddRua, textAddNumero, textValorPagoPorViagem;
    public Button buttonAddPessoa;
    
    // Banco de dados
    private SQLiteDatabase banco;
    private static final String DATABASE_NAME = "banco_de_dados_carbill";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pessoa);

        // Inicialização dos componentes da interface de usuário
        textAddNome = findViewById(R.id.textAddNome);
        textAddSobrenome = findViewById(R.id.textAddSobrenome);
        textAddApelido = findViewById(R.id.textAddApelido);
        textAddRua = findViewById(R.id.textAddRua);
        textAddNumero= findViewById(R.id.textAddNumero);
        textAddTelefone= findViewById(R.id.textAddTelefone);
        textAddBairro = findViewById(R.id.textAddBairro);
        textValorPagoPorViagem = findViewById(R.id.textValorPagoPorViagem);
        buttonAddPessoa = findViewById(R.id.buttonAddPessoa);

        // Define o comportamento do botão de adicionar pessoa
        buttonAddPessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarNovoUsuario();
                //uma função de limpar os campos
            }
        });
    }

    /**
     * Método para cadastrar uma nova pessoa no banco de dados.
     */
    public void cadastrarNovoUsuario(){

        // Obtém os valores dos campos de texto
        String strNome = textAddNome.getText().toString();
        String strSobrenome = textAddSobrenome.getText().toString();
        String strApelido = textAddApelido.getText().toString();
        String strTelefone = textAddTelefone.getText().toString();
        String strRua = textAddRua.getText().toString();
        String strBairro = textAddBairro.getText().toString();
        String strNumero = textAddNumero.getText().toString();
        String strValorPagoPorViagem = textValorPagoPorViagem.getText().toString();

        // Verifica se todos os campos foram preenchidos
        if(TextUtils.isEmpty(strNome) ||TextUtils.isEmpty(strSobrenome) || TextUtils.isEmpty(strApelido) || TextUtils.isEmpty(strTelefone) ||TextUtils.isEmpty(strRua) || TextUtils.isEmpty(strBairro) || TextUtils.isEmpty(strNumero) || TextUtils.isEmpty(strValorPagoPorViagem)){
            Toast.makeText(this, "Falha no cadastro.\nPreencha todos os campos.", Toast.LENGTH_SHORT).show();
        }else{
            try{
                // Abre ou cria o banco de dados
                banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                
                // Prepara e executa a query SQL para inserir os dados no banco de dados
                String sql = "INSERT INTO tb_pessoa (nome, sobrenome, apelido, telefone, rua, bairro, numero, valor_por_corrida) VALUES (?,?,?,?,?,?,?,?);";
                SQLiteStatement stmt = banco.compileStatement(sql);
                stmt.bindString(1, strNome);
                stmt.bindString(2, strSobrenome);
                stmt.bindString(3, strApelido);
                stmt.bindString(4, strTelefone);
                stmt.bindString(5, strRua);
                stmt.bindString(6, strBairro);
                stmt.bindString(7, strNumero);
                stmt.bindString(8, strValorPagoPorViagem);
                stmt.executeInsert();
                
                // Fecha o banco de dados
                banco.close();
                
                // Exibe uma mensagem de sucesso
                Toast.makeText(this, "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
                
                // Limpa os campos de texto
                limparCampodeTexto();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para limpar os campos de texto após o cadastro ser realizado com sucesso.
     */
    public void limparCampodeTexto(){
        textAddNome.setText("");
        textAddSobrenome.setText("");
        textAddApelido.setText("");
        textAddRua.setText("");
        textAddNumero.setText("");
        textAddTelefone.setText("");
        textAddBairro.setText("");
        textValorPagoPorViagem.setText("");
    }

    /**
     * Método chamado quando a Activity é finalizada. Realiza a transição de animação ao sair da tela.
     */
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
