/**
 * AdapterTuplaPessoaTelaDiaria é uma classe personalizada que estende ArrayAdapter
 * e é usada para preencher uma lista de exibição com elementos de PessoaResumoTelaDiaria.
 * Ele também gerencia a interação com os CheckBoxes para cada item na lista.
 */
package br.com.application.carbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTuplaPessoaTelaDiaria extends ArrayAdapter<PessoaResumoTelaDiaria> {

    // Contexto da aplicação
    private final Context context;
    // Lista de elementos a serem exibidos na lista
    private final ArrayList<PessoaResumoTelaDiaria> elementos;

    /**
     * Construtor da classe AdapterTuplaPessoaTelaDiaria.
     * @param context Contexto da aplicação.
     * @param elementos Lista de elementos a serem exibidos na lista.
     */
    public AdapterTuplaPessoaTelaDiaria(Context context, ArrayList<PessoaResumoTelaDiaria> elementos){
        super(context, R.layout.tupla_corrida_diaria, elementos);
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
        View rowView = inflater.inflate(R.layout.tupla_corrida_diaria, parent, false);
        TextView txt_nome = (TextView) rowView.findViewById(R.id.txt_nome);

        CheckBox checkBoxIda = (CheckBox) rowView.findViewById(R.id.checkBoxIda);
        CheckBox checkBoxVolta = (CheckBox) rowView.findViewById(R.id.checkBoxVolta);

        // Define o texto do nome do elemento na TextView correspondente
        txt_nome.setText(elementos.get(position).getNome());

        // Define os listeners de mudança de estado para os CheckBoxes
        checkBoxIda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                elementos.get(position).setIda(isChecked);
                checkBoxIda.setChecked(elementos.get(position).isIda());
            }
        });

        checkBoxVolta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                elementos.get(position).setVolta(isChecked);
                checkBoxVolta.setChecked(elementos.get(position).isVolta());
            }
        });

        return rowView;
    }
}