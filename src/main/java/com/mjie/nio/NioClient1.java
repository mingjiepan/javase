package com.mjie.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author panmingjie
 * @date 2018/11/10 19:41
 */
public class NioClient1 {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress("localhost", 8899));

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SelectableChannel selectableChannel = selectionKey.channel();
                if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectableChannel;
                    boolean finishConnect = channel.finishConnect();
                    if (finishConnect) {
                        channel.write(ByteBuffer.wrap("你好，服务端".getBytes()));
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                }

                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectableChannel;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    channel.read(byteBuffer);

                    byteBuffer.flip();

                    System.out.println("收到来自服务器的消息：" + new String(byteBuffer.array()));
                }
                iterator.remove();
            }
        }
    }
}
