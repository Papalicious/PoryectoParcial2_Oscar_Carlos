package hichat.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

//Hilo encargado de atender a un cliente.

//Guarda toda la charla */
public class HiloDeCliente implements Runnable, ListDataListener {
    private DefaultListModel<String> charla;

    // Socket al que est conectado el cliente
    private Socket socket;

    // Lectura de datos en el socket
    private DataInputStream dataInput;

    // Para escritura de datos en el socket
    private DataOutputStream dataOutput;

    // Crea una instancia de esta clase y se suscribe a cambios en la charla.
    // charla=textos del chat

    public HiloDeCliente(DefaultListModel<String> charla, Socket socket) {
        this.charla = charla;
        this.socket = socket;
        try {
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
            charla.addListDataListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Atiende el socket. Todo lo que llega lo mete en la charla.

    public void run() {
        try {
            while (true) {
                String texto = dataInput.readUTF();
                synchronized (charla) {
                    charla.addElement(texto);
                    System.out.println(texto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia el ultimo texto de la charla por el socket. Se avisa a este metodo cada
     * vez que se mete algo en charla, incluido cuando lo mete este mismo hilo. De
     * esta manera, lo que un cliente escriba, se le reenviara para que se muestre
     * en el textArea.
     */
    public void intervalAdded(ListDataEvent e) {
        String texto = charla.getElementAt(e.getIndex0());
        try {
            dataOutput.writeUTF(texto);
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    // No hace nada
    public void intervalRemoved(ListDataEvent e) {
    }

    // No hace nada
    public void contentsChanged(ListDataEvent e) {
    }
}
