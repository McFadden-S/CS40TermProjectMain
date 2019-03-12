/* **********************************************************
 * Programmer:  Shae McFadden
 * Class:	CS40S
 * 
 * Assignment:	Term Project
 *
 * Description:	This class will communicate between the 
                two arduino objects
 *
 * 
 * *************************************************************
 */
 
 // import files here as needed
 import com.fazecast.jSerialComm.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
 public class Communicator {  // begin class
 	
    // *********** class constants **********
    // ********** instance variable **********

        public static SerialPort InterfaceComPort;
        public boolean InterfacePortFound = false;
        
        private OutputStream ICPOut;
        private InputStream ICPIn;
        
        public static SerialPort RobotComPort;
        public boolean RobotPortFound = false;
        
        private OutputStream RCPOut;
        private InputStream RCPIn;
     
    // ********** constructors ***********

    /*
     Purpose: Constructs Communicator which will establish port connections
     Input: None
     Ouput: None
    */
     public Communicator() throws IOException{
            findPorts();
            
            if (InterfacePortFound){
            
            InterfaceComPort.openPort();
            
            ICPOut = InterfaceComPort.getOutputStream();
            ICPIn = InterfaceComPort.getInputStream();
            ICPIn.skip(ICPIn.available());
            
            }//end of if interface port found
            else{
                System.out.println("Interface Port Not Found");
                while(true){}
            } //end of else not found
            
            if (RobotPortFound){
            
            RobotComPort.openPort();
            
            RCPOut = RobotComPort.getOutputStream();
            RCPIn = RobotComPort.getInputStream();
            RCPIn.skip(RCPIn.available());
            
            }//end of if port found
            else{
                System.out.println("Robotic Port Not Found");
                while(true){}
            }
     }//end of default constructor
     
    // ********** Methods **********

    /*
     Purpose: finds and assigns the port to the port variable for both ports required
     Input: None
     Output: None
    */
    private void findPorts(){
        SerialPort[] AComPorts = SerialPort.getCommPorts();
            
            for(SerialPort eachComPort:AComPorts){
                if (eachComPort.getDescriptivePortName().equals("IOUSBHostDevice")){
                    RobotComPort = eachComPort;
                    RobotPortFound = true;
                }//end of if 
            }//end of for
            for(SerialPort eachComPort:AComPorts){
                if (eachComPort.getDescriptivePortName().equals("BLUETOOTH FIND PORT NAME")){
                    InterfaceComPort = eachComPort;
                    InterfacePortFound = true;
                }//end of if 
            }//end of for
    } //end of find port
 }  // end class