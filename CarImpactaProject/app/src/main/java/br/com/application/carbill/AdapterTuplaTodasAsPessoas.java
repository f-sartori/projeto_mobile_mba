/**
 * AdapterTuplaTodasAsPessoas é uma classe personalizada que estende ArrayAdapter
 * e é usada para preencher uma lista de exibição com elementos de ClassPessoa.
 * Este adaptador exibe o nome completo e o apelido de cada pessoa, bem como suas iniciais.
 */
package br.com.application.carbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTuplaTodasAsPessoas extends ArrayAdapter<ClassPessoa> {

    // Contexto da aplicação
    private final Context context;
    // Lista de pessoas a serem exibidas na lista
    private final ArrayList<ClassPessoa> pessoas;

    /**
     * Construtor da classe AdapterTuplaTodasAsPessoas.
     * @param context Contexto da aplicação.
     * @param pessoas Lista de pessoas a serem exibidas na lista.
     */
    public AdapterTuplaTodasAsPessoas(Context context, ArrayList<ClassPessoa> pessoas){
        super(context, R.layout.tupla_contatos, pessoas);
        this.context = context;
        this.pessoas = pessoas;
    }

    /**
     * Método sobrescrito para obter a exibição de cada item na lista.
     * @param position A posição do item na lista.
     * @param convertView A view do item a ser reciclada, se disponível.
     * @param parent O pai ao qual esta view será anexada.
     * @return A view do item na posição especificada.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tupla_contatos, parent, false);

        TextView txtLetras = (TextView) rowView.findViewById(R.id.txtLetras);
        TextView txtNomeCompleto = (TextView) rowView.findViewById(R.id.txtNomeCompleto);
        TextView textApelido = (TextView) rowView.findViewById(R.id.textApelido);

        // Obtém o nome e sobrenome da pessoa na posição especificada
        String nome = pessoas.get(position).getNome();
        String sobrenome = pessoas.get(position).getSobrenome();
        
        // Define as iniciais da pessoa nas TextViews correspondentes
        String primeira_letra = String.valueOf(nome.charAt(0));
        String segunda_letra = String.valueOf(sobrenome.charAt(0));
        txtLetras.setText(primeira_letra.toUpperCase() + segunda_letra.toUpperCase());

        // Define o nome completo e o apelido da pessoa nas TextViews correspondentes
        txtNomeCompleto.setText(nome + " " + sobrenome);
        textApelido.setText(pessoas.get(position).getApelido());

        return rowView;
    }
}