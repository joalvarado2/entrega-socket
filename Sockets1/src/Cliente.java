import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

class Client {

    static Scanner strInput = null;
    private static int port = 9876;

    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
        strInput = new Scanner(System.in);
        String choice, cont = "s";

// limpio pantalla
        Client.CleanScreen();

        while (cont.equalsIgnoreCase("s")) {
            System.out.println("\t\t *** Sistema de Informacion Bancario *** \n\n");

            System.out.println("1 ===> Crear Nueva Cuenta ");
            System.out.println("2 ===> Buscar Una Cuenta Especifica ");

            System.out.print("\n\n");
            System.out.println("Ingresa la opcion: ");
            choice = strInput.nextLine();

            if (choice.equals("1")) {
                Client.AddRecord();
            } else if (choice.equals("2")) {
                Client.SearchRecordbyID();
            }

            System.out.println("¿Quieres continuar? S/N");
            cont = strInput.nextLine();

// limpio pantalla
            Client.CleanScreen();
        }

// libero recursos
        strInput.close();
    }

    private static void CleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void AddRecord() throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        strInput = new Scanner(System.in);
        String account, value, newRow;

// limpio pantalla
        Client.CleanScreen();

        System.out.println("\t\t [ Crear Cuenta Bancaria ]\n");

        System.out.print("Ingresa el Numero de Cuenta: ");
        account = strInput.nextLine();
        System.out.print("Ingresa el Valor: ");
        value = strInput.nextLine();

        newRow = "1," + account + "," + value;

// establecer conexion de socket al servidor
        socket = new Socket(host.getHostName(), port);

// escribo en el socket usando ObjectOutputStream
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(newRow);

// leo el mensaje de respuesta del servidor
        ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();
        System.out.print("\n");
        System.out.println("Respuesta del Servidor: '" + message + "'");
        System.out.print("\n");

// libero recursos
        ois.close();
        oos.close();
        Thread.sleep(100);
    }

    private static void SearchRecordbyID() throws IOException, ClassNotFoundException {
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        String ID;
        strInput = new Scanner(System.in);

// limpio pantalla
        Client.CleanScreen();

        System.out.println("\t\t [ Buscar Cuenta ]\n");

        System.out.println("Ingresa el Numero de Cuenta: ");
        ID = strInput.nextLine();

// establecer conexion de socket al servidor
        socket = new Socket(host.getHostName(), port);

// escribo en el socket usando ObjectOutputStream
        oos = new ObjectOutputStream(socket.getOutputStream());

        oos.writeObject("2," + ID);

// leo el mensaje de respuesta del servidor
        ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();

// imprimo la respuesta del servidor
        System.out.print("\n");
        System.out.println(" ------------------------------- ");
        System.out.println("|       Cuenta        Valor      ");
        System.out.println(" ------------------------------- ");

        System.out.print(message);

        System.out.println("|                                 |");
        System.out.println(" ------------------------------- ");
        System.out.print("\n");
    }
}
