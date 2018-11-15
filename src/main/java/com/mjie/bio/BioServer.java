package com.mjie.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author panmingjie
 * @date 2018/11/13 21:53
 */
public class BioServer implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            while (!Thread.interrupted()) {
                Handler handler = new Handler(serverSocket.accept());
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new BioServer()).start();
    }
}

class Handler implements Runnable {
    final Socket socket;
    public Handler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {

        try (InputStream inputStream = socket.getInputStream();OutputStream outputStream = socket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                System.out.println("收到客户端的请求 :" + new String(buffer, 0, length));
            }

            outputStream.write("welcome".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
