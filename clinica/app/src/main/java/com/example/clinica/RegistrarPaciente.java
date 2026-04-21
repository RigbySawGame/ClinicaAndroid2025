package com.example.clinica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class RegistrarPaciente extends AppCompatActivity implements View.OnClickListener {

    private EditText nom,ap,am,eda,sex,altu,pes,info;
    private Button guardar, mostrar, listar;

    final String servidor = "http://10.0.2.2/clinica/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_paciente);

        nom = (EditText) findViewById(R.id.etNombres);
        ap = (EditText) findViewById(R.id.etApaterno);
        am = (EditText) findViewById(R.id.etAmaterno);
        eda = (EditText) findViewById(R.id.etEdad);
        sex = (EditText) findViewById(R.id.etSexo);
        altu = (EditText) findViewById(R.id.etAltura);
        pes = (EditText) findViewById(R.id.etPeso);
        info = (EditText) findViewById(R.id.etDetalle);

        guardar = (Button) findViewById(R.id.btnGuardar);
        guardar.setOnClickListener(this);

        mostrar = (Button) findViewById(R.id.btnMostrar);
        mostrar.setOnClickListener(this);

        listar = (Button) findViewById(R.id.btnListar);
        listar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==guardar) {

            String nombres = nom.getText().toString().trim();
            String apaterno = ap.getText().toString().trim();
            String amaterno = am.getText().toString().trim();
            String edadStr = eda.getText().toString().trim();
            String sexo = sex.getText().toString().trim();
            String alturaStr = altu.getText().toString().trim();
            String pesoStr = pes.getText().toString().trim();
            String informacion = info.getText().toString().trim();

            if (nombres.isEmpty() || apaterno.isEmpty() || amaterno.isEmpty() ||
                    edadStr.isEmpty() || sexo.isEmpty() || alturaStr.isEmpty() ||
                    pesoStr.isEmpty() || informacion.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                return;
            }

            int edad;
            float altura, peso;

            try {
                edad = Integer.parseInt(edadStr);
                if (edad <= 0 || edad > 120) {
                    Toast.makeText(getApplicationContext(), "Edad debe estar entre 1 y 120", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Edad no válida", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                altura = Float.parseFloat(alturaStr);
                if (altura < 0.5 || altura > 2.5) {
                    Toast.makeText(getApplicationContext(), "Altura debe estar entre 0.5 y 2.5 metros", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Altura no válida", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                peso = Float.parseFloat(pesoStr);
                if (peso < 3 || peso > 300) {
                    Toast.makeText(getApplicationContext(), "Peso debe estar entre 3 y 300 kg", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Peso no válido", Toast.LENGTH_LONG).show();
                return;
            }
            // Mostrar alerta de confirmación
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Confirmar")
                    .setMessage("¿Deseas guardar este registro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        GuardarPaciente(nombres, apaterno, amaterno, String.valueOf(edad),
                                sexo, String.valueOf(altura), String.valueOf(peso), informacion);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        else if(v==mostrar)
        {
            //Creamos metodo mostrar
            MostrarDatos();
        }
        else if(v==listar)
        {
            //crear el intent para abrir el listActivity
            Intent intent = new  Intent(RegistrarPaciente.this, ListarPaciente.class);
            startActivity(intent); //incia la actividad

        }
    }

    private void MostrarDatos() {

        //Declarar la URL
        String url = servidor + "mostrar_paciente.php";

        //Sera un request vacio
        RequestParams requestParams = new RequestParams();

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String respuesta = new String(responseBody);
                Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });
    }


    private void GuardarPaciente(String nombres, String apaterno, String amaterno,
                                 String edad, String sexo, String altura, String peso, String informacion)
    {
        //Declarar la URL
        String url = servidor + "guardar_paciente.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();
        requestParams.put("nombres",nombres);
        requestParams.put("apaterno",apaterno);
        requestParams.put("amaterno",amaterno);
        requestParams.put("edad",edad);
        requestParams.put("sexo",sexo);
        requestParams.put("altura",altura);
        requestParams.put("peso", peso);
        requestParams.put("detalle",informacion);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String respuesta = new String(responseBody);
                Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });
    }

}