package com.m3.fmessias.projetorequisicao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnEuro;
    private Button btnPeso;
    private Button btnDolar;
    private Button btnBitcoin;

    private TextView txtResultado;
    private TextView txtMoedaEscolhida;

    private static final String PARAM_MOEDA = "[moeda]";
    private String URL_CONSULTA_COTACAO = "http://api.promasters.net.br/cotacao/v1/valores?moedas=[moeda]&alt=json";
    private static final String EURO = "EUR";
    private static final String DOLAR = "USD";
    private static final String BIT = "BTC";
    private static final String PESO = "ARS";
    private static String MOEDA = null;

    JsonCotacao jsonCotacao = new JsonCotacao();
    Moeda moeda = new Moeda();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        btnEuro = findViewById(R.id.btnEuro);
        btnPeso = findViewById(R.id.btnPeso);
        btnDolar = findViewById(R.id.btnDolar);
        btnBitcoin = findViewById(R.id.btnBitcoin);
        txtResultado = findViewById(R.id.txtValor);
        txtMoedaEscolhida = findViewById(R.id.txtMoeda);

        //Eventos de cliques
        btnEuro.setOnClickListener(this);
        btnPeso.setOnClickListener(this);
        btnDolar.setOnClickListener(this);
        btnBitcoin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEuro:
                MOEDA = EURO;
                executaAsync();
                break;

            case R.id.btnPeso:
                MOEDA = PESO;
                executaAsync();
                break;

            case R.id.btnDolar:
                MOEDA = DOLAR;
                executaAsync();
                break;

            case R.id.btnBitcoin:
                MOEDA = BIT;
                executaAsync();
                break;
        }
    }

    private void executaAsync() {
        URL_CONSULTA_COTACAO = URL_CONSULTA_COTACAO.replace("[moeda]", MOEDA);
        jsonCotacao.execute(URL_CONSULTA_COTACAO);
    }


    public class JsonCotacao extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Aguarde um pouco!", "Buscando cotação...");

        }

        @Override
        protected String doInBackground(String... url) {
            return Network.getCotacaoMomento(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                dialog.dismiss();
                moeda = getMoedaJson(s);
                txtResultado.setText(String.valueOf(moeda.getValor()));
                txtMoedaEscolhida.setText(moeda.getNome());
            }
        }
    }

    private Moeda getMoedaJson(String s) {
        Moeda moedaJson = new Moeda();

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jsonValores = jsonObject.getJSONObject("valores");
            JSONObject jsonMoeda = jsonValores.getJSONObject(MOEDA);
            moedaJson.setNome(jsonMoeda.getString("nome"));
            moedaJson.setValor(jsonMoeda.getDouble("valor"));
        } catch (JSONException e) {
            Toast.makeText(this, "Erro ao obter a cotação!", Toast.LENGTH_SHORT).show();
        }
        return moedaJson;
    }
}
