package com.example.clinica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PatientAdapter extends BaseAdapter {
    private Context context;
    private List<Paciente> pacientes;

    public PatientAdapter(Context context, List<Paciente> pacientes) {
        this.context = context;
        this.pacientes = pacientes;
    }

    @Override
    public int getCount() {
        return pacientes.size();
    }

    @Override
    public Object getItem(int position) {
        return pacientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflar el layout del item
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_paciente, parent, false);
        }

        // Obtener los datos del contacto
        Paciente paciente = pacientes.get(position);

        // Referenciar las vistas
        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvNombres = convertView.findViewById(R.id.tvNombres);
        TextView tvEdad = convertView.findViewById(R.id.tvEdad);
        TextView tvIMC = convertView.findViewById(R.id.tvIMC);
        TextView tvClasificacionIMC = convertView.findViewById(R.id.tvClasificacionIMC);

        // Establecer los valores
        tvId.setText(paciente.getId());
        tvNombres.setText("Nombres y Apellidos: "+ paciente.getNombres()+" "+ paciente.getApaterno()+" "+ paciente.getAmaterno());
        tvEdad.setText("Edad: "+ paciente.getEdad());

        try {
            double peso = paciente.getPeso();
            double altura = paciente.getAltura();
            double imc = peso / (altura * altura);

            String clasificacion;

            if (imc < 18.5) {
                clasificacion = "Bajo peso";
            } else if (imc < 25.0) {
                clasificacion = "Peso normal";
            } else if (imc < 30.0) {
                clasificacion = "Sobrepeso";
            } else {
                clasificacion = "Obesidad";
            }

            tvIMC.setText(String.format("IMC: %.2f", imc));
            tvClasificacionIMC.setText("Clasificación: " + clasificacion);

        } catch (NumberFormatException e) {
            tvIMC.setText("IMC: Error");
            tvClasificacionIMC.setText("Clasificación: Desconocida");
        }
        return convertView;
    }
}
