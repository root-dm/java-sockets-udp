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
                    String []ps = sp[0].split("Signin ");
                    MyUser temp = new MyUser(ps[1],sp[1],sp[2]); //ps[1] name sp[1] ip sp[2] port
                    registered.add(temp);
                }


                if(sp[0].startsWith("Message ")){
                    String msg = "";
                    for (int i=0;i<registered.size();i++) {
                        if(Integer.parseInt(registered.get(i).port) == Integer.parseInt(sp[2]) ){
                            msg = registered.get(i).name+": "+sp[0];
                        }
                    }

                    for (int i=0;i<registered.size();i++) {
                        String ip = registered.get(i).ip.replace("/","");
                        System.out.println(ip+":"+registered.get(i).port+","+msg);
                        NetworkTools.sendData(msg, InetAddress.getByName(ip), Integer.parseInt(registered.get(i).port), socket);
                    }
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