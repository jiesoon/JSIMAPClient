package com.jiesoon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IMAPClient {
    private final static int PORT = 143;
    private final static String HOST = "localhost";
    private Logger mLogger = Logger.getLogger("IMAPClient");
    private Socket mClientSocket;

    public IMAPClient() {
        mLogger.setLevel(Level.ALL);

        try {
            mClientSocket = new Socket(HOST, PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        mLogger.log(Level.INFO, "IMAP client started");

        if (mClientSocket != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream()));
                final BufferedReader reader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));

                new Thread() {
                    public void run() {
                        while (true) {
                            String line;
                            try {
                                line = reader.readLine();
                                if (line == null) {
                                    mLogger.log(Level.WARNING, "EOF");
                                    break;
                                }

                                System.out.println("READ: " + line);

                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            }
                        }

                    };
                }.start();

                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String line = console.readLine();
                    if (line == null) {
                        mLogger.log(Level.WARNING, "QUIT");
                        break;
                    }

                    writer.write(line);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
