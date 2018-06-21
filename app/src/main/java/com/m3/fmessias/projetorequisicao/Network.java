package com.m3.fmessias.projetorequisicao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Network {

    private static final int CONNECT_TIMEOUT = 2000;
    private static final int READ_CONNECT = 2000;
    private static final String REQUEST_METHOD = "GET";

    public static String getCotacaoMomento (String url){

        String result = null;

        try {
            URL endPoint = new URL(url); //endPoint é uma rota (o endereço literalmente)

            HttpURLConnection httpURLConnection; //o httpConnection é responsável por manipular a conexão
            InputStream inputStream; //Onde armazena o retorno da requisição

            httpURLConnection = (HttpURLConnection) endPoint.openConnection();  //abrindo a conexão
            httpURLConnection.setRequestMethod(REQUEST_METHOD); //Define o tipo da requisição (GET, POST, PUT, DELETE)
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT); //Define o tempo para conectar o servidor
            httpURLConnection.setReadTimeout(READ_CONNECT); //Define o tempo de leitura dos dados

            httpURLConnection.connect(); //Conecta

            inputStream = httpURLConnection.getInputStream(); //Recebendo o retorno da requisição (GET) stream

            result = lerStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    private static String lerStream(InputStream inputStream) {
        //Cria um objeto do tipo BufferedReade (passando paramentro inputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String linha;

        StringBuffer bufffer = new StringBuffer();

        try {
            while ((linha = reader.readLine()) != null){
                bufffer.append(linha);
                bufffer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufffer.toString();

    }
}
