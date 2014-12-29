package com.example.gangwang.clientdemo;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {
    TextView textResponse;
    EditText editTextAddress;
    EditText editTextPort;
    Button btnConnect, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnClear = (Button)findViewById(R.id.btnClear);
        textResponse = (TextView)findViewById(R.id.response);

        btnConnect.setOnClickListener(btnConnectOnClickListener);
        btnClear.setOnClickListener(btnClearOnClickListener);
    }

    Button.OnClickListener btnConnectOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));

                    myClientTask.execute();
                }
            };

    Button.OnClickListener btnClearOnClickListener =
            new Button.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    textResponse.setText("");
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void>
    {
        String dstAddress;
        int dstPort;
        String response = "test";

        MyClientTask(String addr, int port)
        {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            Socket socket = null;

            try
            {
                //int bytesRead;
                socket = new Socket(dstAddress, dstPort);

                OutputStream outputStream = socket.getOutputStream();

                byte data [] = response.getBytes();
                int temp = 4;
                outputStream.write(data, 0, temp);
                outputStream.flush();
            }
            catch (UnknownHostException e)
            {
                response = "UnknownHostException: " + e.toString();
            }
            catch (IOException e)
            {
                response = "IOException: " + e.toString();
            }
            finally {
                if (socket != null)
                {
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        //
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            textResponse.setText(response);
            super.onPostExecute(result);
        }
    }
}

