import java.io.*;
import java.net.*;
import java.util.*;
public class client {
    public static void main(String[] args) {
        try {
            if(args.length!=3){
                System.out.println("Tintolmarket <serverAddress> <userID> [password]");
            }
            String serverAddress = args[0];
            String userID = args[1];
            String password = args[2];
    
            Socket clientSocket = new Socket("localhost", 12345);
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
    
            out.writeObject(userID);
            out.writeObject(password);
           
            int login = (int) in.readObject();

            if(login==-1){
                System.out.println("User does not exist, automatically registed for you ");
            }else if(login==1){
                System.out.println("Wrong password");
                clientSocket.close();
            }else{
                System.out.println("login com sucesso");
            }

            while(true){
                System.out.println("------------------");
                System.out.println("Operation:\n"+
                ">> add <wine> <image>\n"+
                ">> sell <wine> <value> <quantity>\n"+
                ">> view <wine>\n"+
                ">> buy <wine> <seller> <quantity>\n"+
                ">> wallet\n"+
                ">> classify <wine> <stars>\n"+
                ">> talk <user> <message>\n"+
                ">> read");
                Scanner op= new Scanner(System.in); 
                System.out.print("Operation: ");  
                String operation= op.nextLine();
                out.writeObject(operation);

                String res = (String) in.readObject();
                System.out.println("------------------");
                System.out.println("*"+res);
                // op.close();
                // out.close();
                // in.close();
            }
            
        } catch (Exception e) {}
        
    }
}
