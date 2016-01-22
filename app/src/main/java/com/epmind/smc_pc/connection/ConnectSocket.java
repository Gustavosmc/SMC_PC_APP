package com.epmind.smc_pc.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by gustavosmc on 13/12/15.
 */

public class ConnectSocket extends Thread implements Serializable{
    public static final  String EXIT_SERVER = "EXITIN";
    public static final String  INIT_SERVER = "INITIN";
    private static boolean connected;
    private String ip;
    private int port;
    private static ArrayList<String> serverMessages = new ArrayList<String>();
    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;

    public ConnectSocket(String ip, int port) {
        this.setIp(ip);
        this.setPort(port);
        this.setSocket(null);
        this.setInput(null);
        this.setOutput(null);
        setConnected(false);
    }

    public static synchronized String getFirstMessage(){
        if(serverMessages.size() > 0) {
            String msg = serverMessages.remove(0);
            return msg;
        }
        return null;
    }

    public static synchronized int getSizeMessages(){
        return serverMessages.size();
    }

    public static synchronized Socket getSocket() {
        return socket;
    }

    public static synchronized void setSocket(Socket socket) {
        ConnectSocket.socket = socket;
    }

    public static synchronized DataInputStream getInput() {
        return input;
    }

    public static synchronized void setInput(DataInputStream input) {
        ConnectSocket.input = input;
    }

    public static synchronized DataOutputStream getOutput() {
        return output;
    }

    public static synchronized void setOutput(DataOutputStream output) {
        ConnectSocket.output = output;
    }

    public synchronized boolean connect(){
       if(this.isConnected()) return false;
               try {
                   setSocket(new Socket(getIp(), getPort()));
                   setInput(new DataInputStream(getSocket().getInputStream()));
                   setOutput(new DataOutputStream(getSocket().getOutputStream()));
                   setConnected(true);
                   this.start();
               } catch (IOException e) {
                    e.printStackTrace();
               }
       return isConnected();
   }

    public synchronized boolean desconnect(){
        if(!isConnected()) return false;
                try {
                    getOutput().write((Comands.SERVER_COMAND+Comands.LEFT_SEPARATOR+EXIT_SERVER+Comands.RIGHT_SEPARATOR).getBytes("UTF-8"));
                    getSocket().close();
                    getInput().close();
                    getOutput().close();
                    setConnected(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        getOutput().close();
                        getSocket().close();
                        getInput().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return true;
                    }
                    setConnected(false);
                }
        return !isConnected();
    }

    private synchronized void send(String msg) throws IOException{
        getOutput().write(msg.getBytes("UTF-8"));
        getOutput().flush();
    }

    public  void sendMessage(final String msg){
        if(!isConnected()) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   send(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }


    @Override
    public String toString() {
        return "ConnectSocket{" +
                "connected=" + isConnected() +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", socket=" + getSocket() +
                ", input=" + getInput() +
                ", output=" + getOutput() +
                '}';
    }

    public synchronized boolean isConnected() {
        return connected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        this.sendMessage(Comands.SERVER_COMAND+Comands.LEFT_SEPARATOR+INIT_SERVER+Comands.RIGHT_SEPARATOR);
        while (this.isConnected()){
            byte bytes[] = new byte[1024];
            String msg;
            try {
                int size = input.read(bytes);
                msg = new String(bytes, 0 , size,  "UTF-8");
                serverMessages.add(msg);
                Thread.sleep(10);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void setConnected(boolean connected) {
        ConnectSocket.connected = connected;
    }
}