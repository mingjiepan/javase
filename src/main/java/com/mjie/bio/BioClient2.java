package com.mjie.bio;

import java.io.*;
import java.net.Socket;

/**
 * @author panmingjie
 * @date 2018/11/15 22:41
 */
public class BioClient2 {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8899);
        new Thread(new ClientInputHandler(socket)).start();
        new Thread(new ClientOutputHandler(socket)).start();
    }
}


class ClientInputHandler implements Runnable {
    private Socket socket;
    public ClientInputHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream()) {

            while (true) {
                byte[] buffer = new byte[2048];
                int read = inputStream.read(buffer);
                System.out.println("收到来自客户端的请求：" + new String(buffer, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientOutputHandler implements Runnable {
    private Socket socket;
    public ClientOutputHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream()) {
            while (true) {
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                outputStream.write(line.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}