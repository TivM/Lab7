package clientModule.main;


import clientModule.client.Client;
import common.exceptions.ConnectionException;
import common.exceptions.InvalidPortException;
import common.exceptions.InvalidProgramArgumentsException;

import java.io.PrintStream;

import static common.io.OutputManager.print;


public class MainClient {
    //public static Logger logger = LogManager.getLogger("logger");
    //static final Logger logger = LogManager.getRootLogger();
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        print("CLIENT STARTED");

        args = new String[]{"localhost","4445"};
        String addr  = "";
        int port = 0;
        try {
            if (args.length != 2) throw new InvalidProgramArgumentsException("no address passed by arguments");
            addr = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                throw new InvalidPortException();
            }
            Client client = new Client(addr,port);
            client.start();
        }
        catch (InvalidProgramArgumentsException| ConnectionException e){
            print(e.getMessage());
        }
        //System.out.println(res.getMessage());
    }
}
