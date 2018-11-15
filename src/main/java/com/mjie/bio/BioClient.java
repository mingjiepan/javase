package com.mjie.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author panmingjie
 * @date 2018/11/13 21:59
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 8899));

        if (socket.isConnected()) {
            try (InputStream inputStream = socket.getInputStream();OutputStream outputStream = socket.getOutputStream()) {
                outputStream.write("我来自客户端".getBytes());

                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = inputStream.read(buffer)) > 0) {
                    System.out.println("收到来自服务端的消息 :" + new String(buffer, 0, length));
                }
            }
        }
        socket.close();
    }
}

