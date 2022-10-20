import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server 
{   

    public static void main(String args[]) 
    {
        ArrayList<MyUser> registered = new ArrayList<MyUser>(); //edw apothikeuonte oi eggegramenoi xristes Signins
        
        DatagramSocket socket = null;
        
        //Dhmiourgeia socket gia send kai receive
        try 
        {
            socket = new DatagramSocket(4242);
        } 
        catch (SocketException ex) 
        {
            ex.printStackTrace();
        }
        
        while (true) 
        {
            try 
            {
                String s = NetworkTools.receiveData(socket);
                String []sp = s.split(":"); //sp[0] = keimeno mhnymatos, sp[1] = IP apostolea san String, sp[2]= port apostolea san String
                System.out.println("Message: "+sp[0]+" from "+sp[1]+":"+Integer.parseInt(sp[2]));
                if(sp[0].startsWith("Signin ")){ //an exei patisei Sigin
                    System.out.println("mpike sto if signin");
                    System.out.println("sp[0]"+sp[0]);
                    System.out.println("sp[1]"+sp[1]);
                    System.out.println("sp[2]"+sp[2]);
                    String []ps = sp[0].split("Signin ");
                    System.out.println("ps[0]"+ps[0]);
                    System.out.println("ps[1]"+ps[1]);

                    MyUser temp = new MyUser(ps[1],sp[1],sp[2]); //ps[1] name sp[1] ip sp[2] port
                    registered.add(temp);
                    System.out.println(registered);
                    // int len = registered.size();
                    for (int i=0;i<registered.size();i++) {
                        
                        System.out.println(registered.get(i).name); 
                        System.out.println(registered.get(i).ip); 
                        System.out.println(registered.get(i).port); 
                    }
                    
                }


                if(sp[0].startsWith("Message ")){
                    for (int i=0;i<registered.size();i++) {
                        System.out.println("mpika sti for port:"+registered.get(i).port);
                        System.out.println("stin for to sp[2]:"+sp[2]);

                        if(Integer.parseInt(registered.get(i).port) == Integer.parseInt(sp[2]) ){
                            System.out.println("Einai eggegramenos");
                            String ip = registered.get(i).ip.replace("/","");
                            NetworkTools.sendData(sp[0], InetAddress.getByName(ip), Integer.parseInt(registered.get(i).port), socket);
                        }
                    }
                    System.out.println("Patise Messagee");
                }


            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
}

class MyUser extends Server{
    String name;
    String ip;
    String port;
    public MyUser(String name, String ip,String port){
        this.name=name;
        this.ip=ip;
        this.port=port;
    }
}