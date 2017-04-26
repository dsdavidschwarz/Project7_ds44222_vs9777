//i think we should create a listener that organizes everything 

package client;

import UI.ChatController;
import UI.LoginController;
import messages.Message;
import messages.MessageType;
import messages.Status;
import java.io.*;
import java.net.Socket;

public class Listener implements Runnable{

    private Socket socket;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    public Listener(String username, ChatController controller) {
        Listener.username = username;
        this.controller = controller;
    }

    //im not really sure how to do this part
    public void run() {
        try {
            LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {}

        try {
            /*connect and while it is connected check if there is a message
             * switch case to get type of message and output certain message
             * */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Sending normal Message
     */
    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setMsg(msg);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* Like sending normal message, but send update on status
     *  */
    public static void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        oos.writeObject(createMessage);
        oos.flush();
    }

}
