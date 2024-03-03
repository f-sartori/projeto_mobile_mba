/**
 * AdapterTuplaPessoaTelaInicial é uma classe personalizada que estende ArrayAdapter
 * e é usada para preencher uma lista de exibição com elementos de PessoaResumoTelaInical.
 * Este adaptador exibe o apelido de uma pessoa e sua dívida total em uma tela inicial.
 */
package br.com.application.carbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AdapterTuplaPessoaTelaInicial extends ArrayAdapter<PessoaResumoTelaInical> {

    // Contexto da aplicação
    private final Context context;
    // Lista de elementos a serem exibidos na lista
    private final ArrayList<PessoaResumoTelaInical> elementos;

    /**
     * Construtor da classe AdapterTuplaPessoaTelaInicial.
     * @param context Contexto da aplicação.
     * @param elementos Lista de elementos a serem exibidos na lista.
     */
    public AdapterTuplaPessoaTelaInicial(Context context, ArrayList<PessoaResumoTelaInical> elementos){
        super(context, R.layout.tupla_pessoa_e_divida_total, elementos);
        this.context = context;
        this.elementos = elementos;
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
        View rowView = inflater.inflate(R.layout.tupla_pessoa_e_divida_total, parent, false);
        TextView txt_apelido = (TextView) rowView.findViewById(R.id.txt_apelido);
        TextView txt_divida_total = (TextView) rowView.findViewById(R.id.txt_divida_total);

        // Define o apelido da pessoa na TextView correspondente
        txt_apelido.setText(elementos.get(position).getNome());
        
        // Formata o valor da dívida total e exibe-o na TextView correspondente
        String valorFormatado = NumberFormat.getCurrencyInstance().format(elementos.get(position).getTotal());
        txt_divida_total.setText(String.valueOf(valorFormatado));

        return rowView;
    }
}