package com.mutualexclusion.rickartagarwala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPHandler extends Thread {

    Logger logger = LoggerFactory.getLogger(TCPHandler.class);

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private RiccartAgarwalaME riccartAgarwalaME;
    private ClusterOps clusterOps;

    public TCPHandler(Socket socket, RiccartAgarwalaME riccartAgarwalaME, ClusterOps clusterOps) {
        this.clientSocket = socket;
        this.clusterOps = clusterOps;
        this.riccartAgarwalaME = riccartAgarwalaME;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine = in.readLine();
            logger.info("Message Rcvd = " + inputLine);
            String[] msgs = inputLine.split("&");
            //logger.info(msgs[0]);
            Operation op = Operation.valueOf(msgs[0]);
            switch (op.name()) {
                case "OK":
                    riccartAgarwalaME.handleOKMsg(createMsgForOk(msgs));
                    break;

                case "REQUEST":
                    //logger.info("Handling REQUEST msg");
                    riccartAgarwalaME.reply(createMsg(msgs), "");
                    break;

                case "ADDNODE":
                    clusterOps.addProcess(null);
                    break;

                default:
                    logger.info("No operation to do!!!");
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Message createMsgForOk(String[] msgs) {
        Message m = new Message();
        m.setNodeId(msgs[1]);
        return m;
    }

    private Message createMsg(String[] msgs) {
        Message m = new Message();
        m.setPId(Integer.valueOf(msgs[1]));
        m.setTimestamp(Long.valueOf(msgs[2]));
        m.setNodeId(msgs[3]);
        m.setResourceName(msgs[4]);
        return m;
    }
}
