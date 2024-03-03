/**
 * TelaDiaria é uma Activity que exibe uma lista de pessoas e permite registrar as viagens diárias de cada pessoa.
 * Esta tela mostra a data atual e uma lista de pessoas com caixas de seleção para indicar se a pessoa fez uma viagem de ida ou volta.
 * Os dados das viagens são armazenados no banco de dados e podem ser registrados clicando em um botão.
 */
package br.com.application.carbill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TelaDiaria extends AppCompatActivity {

    // Componentes da interface de usuário
    public TextView textViewDataAtual;
    public ListView listviewCorridasDiaria;
    public Button buttonRegistroDiario;

    // Variáveis de data
    public String _data, _mes, _ano;

    // Componentes para manipulação do banco de dados
    private SQLiteDatabase banco;
    private static final String DATABASE_NAME = "banco_de_dados_carbill";

    // Adaptador para preencher a lista de pessoas e viagens diárias
    private ArrayAdapter<PessoaResumoTelaDiaria> tupla_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_diaria);

        // Inicialização dos componentes da interface de usuário
        textViewDataAtual = (TextView) findViewById(R.id.textViewDataAtual);
        listviewCorridasDiaria = (ListView) findViewById(R.id.listviewCorridasDiaria);
        buttonRegistroDiario = (Button) findViewById(R.id.buttonRegistroDiario);

        // Obtém e exibe a data atual
        obterDataAtual();

        // Lista os dados das viagens diárias
        listarDadosCorridaDiaria();

        // Define o comportamento do botão de registro diário
        buttonRegistroDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCorridaDiaria();
                finish();
            }
        });

    }

    /**
     * Método para obter e exibir a data atual.
     */
    public void obterDataAtual(){

        SimpleDateFormat dia = new SimpleDateFormat("dd");
        Date data_dia = new Date();
        String str_dia = dia.format(data_dia);

        SimpleDateFormat mes = new SimpleDateFormat("MM");
        Date data_mes = new Date();
        String str_mes = mes.format(data_mes);

        SimpleDateFormat ano = new SimpleDateFormat("yyyy");
        Date data_ano = new Date();
        String str_ano = ano.format(data_ano);

        textViewDataAtual.setText(str_dia + "/" + str_mes + "/" + str_ano);
        _data = str_dia; _mes = str_mes; _ano = str_ano;
    }

    /**
     * Método para listar os dados das viagens diárias.
     * @return O adaptador de lista de pessoas e viagens diárias.
     */
    public ArrayAdapter<PessoaResumoTelaDiaria> listarDadosCorridaDiaria(){

        ArrayList<PessoaResumoTelaDiaria> pessoas = new ArrayList<PessoaResumoTelaDiaria>();
        tupla_adapter = new AdapterTuplaPessoaTelaDiaria(this, pessoas);

        try{
            banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            Cursor meuCursor = banco.rawQuery("SELECT id_pessoa,apelido FROM tb_pessoa order by apelido desc;", null);

            if (meuCursor.moveToFirst()) {
                do {
                    int id_pessoa = meuCursor.getInt((int) meuCursor.getColumnIndex("id_pessoa"));
                    String nome = meuCursor.getString((int) meuCursor.getColumnIndex("apelido"));
                    PessoaResumoTelaDiaria pessoa = new PessoaResumoTelaDiaria(nome, id_pessoa);
                    pessoas.add(pessoa);
                } while (meuCursor.moveToNext());

                listviewCorridasDiaria.setAdapter(tupla_adapter);
                banco.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return tupla_adapter;
    }

    /**
     * Método para registrar as viagens diárias no banco de dados.
     */
    void registrarCorridaDiaria(){
        for(int i=0; i<tupla_adapter.getCount(); i++){
            String nome = ((PessoaResumoTelaDiaria) tupla_adapter.getItem(i)).getNome();
            boolean ida = ((PessoaResumoTelaDiaria) tupla_adapter.getItem(i)).isIda();
            boolean volta = ((PessoaResumoTelaDiaria) tupla_adapter.getItem(i)).isVolta();
            int id = ((PessoaResumoTelaDiaria) tupla_adapter.getItem(i)).getId_pessoa();

            if(ida || volta){
                try{
                    banco = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

                    Cursor meuCursor = banco.rawQuery("SELECT valor_por_corrida FROM tb_pessoa WHERE id_pessoa = " + id +";", null);
                    double val = 0.1f;
                    if (meuCursor.moveToFirst()) {
                        do {
                            val = meuCursor.getDouble((int) meuCursor.getColumnIndex("valor_por_corrida"));
                        } while (meuCursor.moveToNext());
                    }

                    if(ida){
                        String sql = "INSERT INTO tb_viagem(id_pessoa, id_tipo, data, valor) VALUES(?,?,?,?);";
                        SQLiteStatement stmt = banco.compileStatement(sql);
                        stmt.bindString(1, String.valueOf(id));
                        stmt.bindString(2, "1");
                        String data_insert = _ano + "/" + _mes + "/" + _data;
                        stmt.bindString(3, data_insert);
                        stmt.bindString(4, String.valueOf(val));
                        stmt.executeInsert();
                    }
                    if(volta){
                        String sql = "INSERT INTO tb_viagem(id_pessoa, id_tipo, data, valor) VALUES(?,?,?,?);";
                        SQLiteStatement stmt = banco.compileStatement(sql);
                        stmt.bindString(1, String.valueOf(id));
                        stmt.bindString(2, "2");
                        String data_insert = _ano + "/" + _mes + "/" + _data;
                        stmt.bindString(3, data_insert);
                        stmt.bindString(4, String.valueOf(val));
                        stmt.executeInsert();
                    }

                    banco.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
