package Connectivity;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final String ip;
    private final String port;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;
    private String username;

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        this.ip = String.valueOf(socket.getInetAddress());
        this.port = String.valueOf(socket.getPort());
        this.username = "";
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public String getUsername() {
        return (username == null) ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username.equals("null") ? "" : username;
    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            if(!value.equals("close"))
                disconnect();
        }
    }

    public synchronized void sendUser(String user) {
        try {
            out.write(user);
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized String receiveUser() {
        try {
            System.out.println(in.readLine());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void disconnect() {
        try {
            sendString("close");
            rxThread.interrupt();
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public String toString() {
        return socket.getInetAddress() + "[" + socket.getPort() + "]";
    }
}
