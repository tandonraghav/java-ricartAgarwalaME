package com.myntra.com.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Component
public class TCPConnection {

    private Map<Processes, ServerSocket> socketMap=new HashMap<>();
    @Autowired SystemSettings systemSettings;
    @Autowired private RiccartAgarwalaME riccartAgarwalaME;
    @Autowired private ClusterOps clusterOps;


    @PostConstruct
    public void init(){
        try {
            receive();
        } finally {

        }
    }

    public void send(String message,Integer port) throws IOException {
        PrintWriter out=null;
        Socket clientSocket=null;
        BufferedReader in=null;
        try{
            clientSocket = new Socket("localhost", port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(message);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            clientSocket.close();
            out.close();
            in.close();
        }

    }


    public void receive() {
        Thread t=new Thread(()->{
            try{
                ServerSocket socket = new ServerSocket(systemSettings.getTcpPort());
                while (true) {
                    new TCPHandler(socket.accept(),riccartAgarwalaME,clusterOps).start();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        t.start();
    }


//    public static void main(String[] args) throws Exception{
//        TCPConnection tcpConnection=new TCPConnection();
//        System.out.println(Operation.OK.name());
//        tcpConnection.send(Operation.OK.toString()+"&test",6666);
//
//    }


}
