import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static ServerSocket server;
    private static int port = 9876;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        // crea el objeto del servidor de socket
        server = new ServerSocket(port);
        String responseMsj = null;
        // sigue escuchando indefinidamente hasta que recibe una llamada de 'exit'
        while (true) {
            // creando socket y esperando la conexion del cliente
            System.out.println("Esperando Solicitud del cliente...");
            Socket socket = server.accept();
            // leer desde el socket al objeto ObjectInputStream
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // convierte el objeto ObjectInputStream en String
            String message = (String) ois.readObject();
//---------------------------------------------------------------
            switch (message.split(",")[0]) {
                case "1":
                    System.out.println("===> Agregando una nueva Cuenta");
                    responseMsj = Server.AddRecord(message.substring(2));

                    break;
                case "2":
                    System.out.println("===> Consultando Cuenta");
                    responseMsj = Server.SearchRecordbyID(message.substring(2));
                    break;
                default:
                    System.out.println("La opcion no es valida");
            }
            //---------------------------------------------------
            // crea el objeto ObjectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            // escribir objeto en Socket
            oos.writeObject(responseMsj);

            // libero recursos
            ois.close();
            oos.close();
            socket.close();

            // finalizar el servidor si el cliente envi­a una solicitud de salida
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
        }
        System.out.println("Apagando el servidor de Socket!!");
        // cierra el objeto ServerSocket
        server.close();
    }

    private static String AddRecord(String record) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("datos.txt", true));
        try {
            // escribo los datos en el archivo
            bw.write(record);
            bw.flush();
            bw.newLine();
            // libero recursos
            bw.close();
        } catch (Exception e) {
            return "NO-OK";
        }
        return "Registro grabado OK";
    }

    private static String SearchRecordbyID(String ID) throws IOException {
        String record, account = "";
        BufferedReader br = new BufferedReader(new FileReader("datos.txt"));
        while ((record = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(record, ",");
            if (record.contains(ID)) {
                account += " " + st.nextToken() + " " + st.nextToken() + " \n";
            }
        }
        // libero recursos
        br.close();
        return account;
    }
}
