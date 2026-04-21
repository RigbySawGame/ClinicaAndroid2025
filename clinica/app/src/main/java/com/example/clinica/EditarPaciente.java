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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditarPaciente extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreE, apePaternoE, apeMaternoE, edadE, sexoE, alturaE, pesoE, detalleE;
    private Button guardar, mostrar, listar;

    final String servidor = "http://10.0.2.2/clinica/";
    String idPaciente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_paciente);


        nombreE = (EditText) findViewById(R.id.etNombresE);
        apePaternoE = (EditText) findViewById(R.id.etApaternoE);
        apeMaternoE = (EditText) findViewById(R.id.etAmaternoE);
        edadE = (EditText) findViewById(R.id.etEdadE);
        alturaE = (EditText) findViewById(R.id.etAlturaE);
        sexoE = (EditText) findViewById(R.id.etSexoE);
        pesoE = (EditText) findViewById(R.id.etPesoE);
        detalleE = (EditText) findViewById(R.id.etDetalleE);

        guardar = (Button) findViewById(R.id.btnGuardar);
        guardar.setOnClickListener(this);
        mostrar = (Button) findViewById(R.id.btnMostrar);
        mostrar.setOnClickListener(this);
        listar = (Button) findViewById(R.id.btnListar);
        listar.setOnClickListener(this);

        idPaciente = getIntent().getStringExtra("idPaciente");
        //Toast.makeText(this,idCont,Toast.LENGTH_LONG).show();
        ConsultarPaciente(idPaciente);
    }

    private void ConsultarPaciente(String idPaciente) {
        //Declarar la URL
        String url = servidor + "consultar_paciente.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", idPaciente);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                //Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();
                // Parsear el JSON
                try {
                    JSONArray jsonArray = new JSONArray(respuesta);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pacienteJson = jsonArray.getJSONObject(i);

                        String id = pacienteJson.getString("id");
                        String nombres = pacienteJson.getString("nombres");
                        String ape_paterno = pacienteJson.getString("ape_paterno");
                        String ape_materno = pacienteJson.getString("ape_materno");
                        Integer edad = pacienteJson.getInt("edad");
                        String sexo = pacienteJson.getString("sexo");
                        Double peso = pacienteJson.getDouble("peso");
                        Double altura = pacienteJson.getDouble("altura");
                        String detalle = pacienteJson.getString("detalle");

                        nombreE.setText(nombres);
                        apePaternoE.setText(ape_paterno);
                        apeMaternoE.setText(ape_materno);
                        edadE.setText(String.valueOf(edad));
                        sexoE.setText(sexo);
                        pesoE.setText(String.valueOf(peso));
                        alturaE.setText(String.valueOf(altura));
                        detalleE.setText(detalle);
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void MostrarDatos() {
        //Declarar la URL
        String url = servidor + "mostrar_paciente.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ActualizarPaciente(String idPaciente, String nombres, String apaterno, String amaterno, Integer edad, String sexo, Double altura, Double peso, String detalle) {

        //Declarar la URL
        String url = servidor + "actualizar_paciente.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", idPaciente);
        requestParams.put("nombres", nombres);
        requestParams.put("apaterno", apaterno);
        requestParams.put("amaterno", amaterno);
        requestParams.put("edad", edad);
        requestParams.put("sexo", sexo);
        requestParams.put("altura", altura);
        requestParams.put("peso", peso);
        requestParams.put("detalle", detalle);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditarPaciente.this, ListarPaciente.class);
                startActivity(intent); // Iniciar la actividad

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == guardar) {
            String nombres = nombreE.getText().toString().trim();
            String apaterno = apePaternoE.getText().toString().trim();
            String amaterno = apeMaternoE.getText().toString().trim();
            String edadStr = edadE.getText().toString().trim();
            String alturaStr = alturaE.getText().toString().trim();
            String pesoStr = pesoE.getText().toString().trim();
            String sexo = sexoE.getText().toString();
            String detalle = detalleE.getText().toString().trim();

            if (nombres.isEmpty() ||
                apaterno.isEmpty() ||
                amaterno.isEmpty() ||
                edadStr.isEmpty() ||
                sexo.isEmpty() ||
                alturaStr.isEmpty() ||
                pesoStr.isEmpty() ||
                detalle.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Completar los datos requeridos", Toast.LENGTH_LONG).show();
            } else {
                try {
                    int edad = Integer.parseInt(edadStr);
                    double altura = Double.parseDouble(alturaStr);
                    double peso = Double.parseDouble(pesoStr);

                    ActualizarPaciente(idPaciente, nombres, apaterno, amaterno, edad, sexo, altura, peso, detalle);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Edad, altura o peso inválidos", Toast.LENGTH_LONG).show();
                }
            }
        } else if (v == mostrar) {
            MostrarDatos();
        } else if (v == listar) {
            Intent intent = new Intent(EditarPaciente.this, ListarPaciente.class);
            startActivity(intent);
        }
    }
}