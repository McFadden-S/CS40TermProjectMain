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
import java.util.Scanner;
 
 public class Communicator {  // begin class
 	
    // *********** class constants **********
     
        final private int SM = 3; //number of sensor modules
     
    // ********** instance variable **********
     
        public static SerialPort InterfaceComPort;
        public boolean InterfacePortFound = false;
        
        private OutputStream ICPOut;
        private InputStream ICPIn;
        
        public static SerialPort RobotComPort;
        public boolean RobotPortFound = false;
        
        private OutputStream RCPOut;
        private InputStream RCPIn;
        
        private Scanner scan = new Scanner(System.in);
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
                if (eachComPort.getDescriptivePortName().equals("HC-05-DevB")){
                    InterfaceComPort = eachComPort;
                    InterfacePortFound = true;
                }//end of if 
            }//end of for
    } //end of find port
    
    /*
     Purpose:   returns console updates on sensor start ups and puts into infinite
                loop if sensor is not responding
     Input:     None
     Output:    Whether each sensor is responding or not
    */
    public void startUp() throws IOException{
        int incoming = 0;
        
        for(int sensor = 0; sensor < SM; sensor++){
            while(ICPIn.available()==0){}
            incoming = ICPIn.read();
            if(incoming == '#'){
                System.out.println("Sensor Module " + sensor + ": Responding...\n");
            }//end of if sensor is working
            else{
                System.out.println("Sensor Module " + sensor + ": Not Responding...\n");
                while(true){}
            }//end of else not responding
        }//end of for each sensor module
    }// end of start up
    
    /*
     Purpose:   returns console updates on sensor calibration and puts into infinite
                loop if problem
     Input:     a character after command followed
     Output:    instructions
    */
    public void calibration() throws IOException{
        int incoming = 0;
        
        for(int sensor = 0; sensor < SM; sensor++){
            while(ICPIn.available()==0){}
            incoming = ICPIn.read();
            if(incoming == '?'){
                System.out.println("Please open and close your hand a couple times."
                        + "\nWhen complete enter a character. Round: " + sensor + "/3");
                while(!scan.hasNext()){}
                ICPOut.write('#'); //tells arduino to continue
            }//end of if prompted for calibration
            else{
                System.out.println("Calibration Error");
                while(true){}
            }//end of else if error
        }//end of for each module
    }//end of calibration
    
   /*
    Purpose: Receives the data packet from the arduino with the sensors
    Input: None
    Output: int[5] holds the contraction values 
    */
    public int[] retrieveDataPack() throws IOException{
        int[] out = new int[5];
        String delim = "[_]+";
        
        ICPOut.write('#');
        
        
        return out;
    }//end of retrieve data packet
 }  // end class