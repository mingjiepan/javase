package com.mjie.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author panmingjie
 * @date 2018/11/10 19:20
 */
public class NioServer1 {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8899));

        Selector selector = Selector.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SelectableChannel selectableChannel = selectionKey.channel();

                if (selectionKey.isAcceptable()) {

                    ServerSocketChannel channel = (ServerSocketChannel)selectableChannel;

                    System.out.println("新的客户端连接了......");

                    SocketChannel socketChannel = channel.accept();

                    System.out.println("socketChannel = " + socketChannel.hashCode());

                    socketChannel.write(ByteBuffer.wrap("你好，欢迎".getBytes()));
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }

                if (selectionKey.isReadable()) {

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    SocketChannel socketChannel = (SocketChannel) selectableChannel;
                    socketChannel.read(byteBuffer);

                    byteBuffer.flip();

                    System.out.println("读取数据" + new String(byteBuffer.array()));

                    socketChannel.write(ByteBuffer.wrap("收到消息了".getBytes()));
                }
                iterator.remove();
            }
        }
    }
}
