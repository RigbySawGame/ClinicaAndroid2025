package com.example.clinica;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListarPaciente extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lista;
    private Button nuevo;

    final String servidor = "http://10.0.2.2/clinica/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_paciente);

        lista = (ListView) findViewById(R.id.lstPacientes);
        lista.setOnItemClickListener(this);
        nuevo = (Button) findViewById(R.id.btnNuevo);
        nuevo.setOnClickListener(this);

        MostrarDatos();
    }

    @Override
    public void onClick(View v) {
        if(v==nuevo) {
            // Crear el Intent para abrir ListaActivity
            Intent intent = new Intent(ListarPaciente.this, RegistrarPaciente.class);
            startActivity(intent); // Iniciar la actividad
        }
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
                //Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();
                // Parsear el JSON
                try {
                    JSONArray jsonArray = new JSONArray(respuesta);

                    // Crear una lista para almacenar los objetos
                    ArrayList<Paciente> patientsList = new ArrayList<>();

                    // Recorrer el array JSON y agregar cada contacto a la lista
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

                        // Crear un objeto Contact y agregarlo a la lista
                        Paciente paciente = new Paciente(id,
                                nombres,
                                ape_paterno,
                                ape_materno,
                                edad,
                                sexo,
                                altura,
                                peso,
                                detalle
                        );
                        patientsList.add(paciente);
                    }

                    // Crear el adaptador
                    PatientAdapter adapter = new PatientAdapter(getApplicationContext(), patientsList);
                    // Establecer el adaptador en el ListView
                    lista.setAdapter(adapter);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent==lista) //si doy click en un item de el listview "lista"
        {
            TextView tvId = (TextView) view.findViewById(R.id.tvId);
            String idPaciente = tvId.getText().toString();
            //Toast.makeText(getApplicationContext(),idCont,Toast.LENGTH_LONG).show();
            PopupMenu popupMenu = new PopupMenu(this,view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                     if(item.getItemId() == R.id.opc_editar)
                     {
                         new AlertDialog.Builder(ListarPaciente.this)
                                 .setTitle("Confirmacion")
                                 .setMessage("¿Estas seguro de que deseas editar contacto?")
                                 .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                 {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         EditarPaciente(idPaciente);
                                     }
                                 })
                                 .setNegativeButton("No", null)
                                 .show();
                     }
                     else if (item.getItemId() == R.id.opc_eliminar) {
                         // Mostrar el diálogo de confirmación
                         new AlertDialog.Builder(ListarPaciente.this)
                                 .setMessage("¿Estás seguro de que deseas eliminar este paciente?")
                                 .setCancelable(false)
                                 .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         // Si el usuario presiona "Sí", ejecutar EliminarContacto
                                         EliminarPaciente(idPaciente);
                                     }
                                 })
                                 .setNegativeButton("No", null) // Si el usuario presiona "No", simplemente cerrar el diálogo
                                 .show();
                         return true;
                     }
                    return false;
                }
            });
            popupMenu.show();
        }

    }

    private void EliminarPaciente(String idPaciente) {

        String url = servidor + "eliminar_paciente.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", idPaciente);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Toast.makeText(getApplicationContext(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ListarPaciente.this, ListarPaciente.class);
                startActivity(intent); // Iniciar la actividad
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void EditarPaciente(String idPaciente) {
        //Toast.makeText(getApplicationContext(),"Editar: "+idCont,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ListarPaciente.this, EditarPaciente.class);
        intent.putExtra("idPaciente", idPaciente);
        startActivity(intent); // Iniciar la actividad
    }

}