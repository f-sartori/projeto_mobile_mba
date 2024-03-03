/**
 * AdpterTuplaHistoricoTelaInfo é uma classe personalizada que estende ArrayAdapter
 * e é usada para preencher uma lista de exibição com elementos de HistoricoDeViagem.
 * Este adaptador exibe informações sobre viagens históricas, como data, tipo e valor.
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

public class AdpterTuplaHistoricoTelaInfo extends ArrayAdapter<HistoricoDeViagem> {

    // Contexto da aplicação
    private final Context context;
    // Lista de viagens históricas a serem exibidas na lista
    private final ArrayList<HistoricoDeViagem> viagem;

    /**
     * Construtor da classe AdpterTuplaHistoricoTelaInfo.
     * @param context Contexto da aplicação.
     * @param viagem Lista de viagens históricas a serem exibidas na lista.
     */
    public AdpterTuplaHistoricoTelaInfo(Context context, ArrayList<HistoricoDeViagem> viagem){
        super(context, R.layout.tupla_historico, viagem);
        this.context = context;
        this.viagem = viagem;
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
        View rowView = inflater.inflate(R.layout.tupla_historico, parent, false);

        TextView data = (TextView) rowView.findViewById(R.id.txtData);
        TextView tipo = (TextView) rowView.findViewById(R.id.txtTipo);
        TextView valor = (TextView) rowView.findViewById(R.id.txtValor);

        // Define a data da viagem na TextView correspondente
        data.setText(viagem.get(position).getData());
        
        // Define o tipo da viagem (IDA ou VOLTA) na TextView correspondente
        if(viagem.get(position).getId_tipo() == 1)
            tipo.setText("IDA");
        else
            tipo.setText("VOLTA");

        // Formata o valor da viagem e exibe-o na TextView correspondente
        String valorFormatado = NumberFormat.getCurrencyInstance().format(viagem.get(position).getValor());
        valor.setText(valorFormatado);

        return rowView;
    }
}
