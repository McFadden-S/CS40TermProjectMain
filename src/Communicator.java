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
        final private int Threshhold = 40;//number at which contraction starts
     
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
            //RCPIn.skip(RCPIn.available());
            
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
        boolean notFound  = true;
        
        System.out.println("Press reset button on glove...");
        
        for(int sensor = 0; sensor < SM; sensor++){
            do{
            while(ICPIn.available()==0){}
            incoming = ICPIn.read();
            if(incoming == '#'){
                System.out.println("Sensor Module " + sensor + ": Responding...");
                notFound  = false;
            }//end of if sensor is working
            else if (incoming == '!'){
                System.out.println("Sensor Module " + sensor + ": Not Responding...");
                while(true){}
            }//end of else not responding
            }while(notFound);
        }//end of for each sensor module
        System.out.println("*************************************************\n");
        
        //notifies robotic hand, port established & requests can be added to queue
        RCPOut.write('#'); 
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
                        + "\nWhen complete enter a character. Round: " + sensor + "/2");
                while(!scan.hasNext()){}
                scan.next();
                ICPOut.write('#'); //tells arduino to continue
            }//end of if prompted for calibration
            else{
                System.out.println("Calibration Error");
                while(true){}
            }//end of else if error
        }//end of for each module
        System.out.println("*************************************************\n");
    }//end of calibration
    
   /*
    Purpose: Receives the data packet from the arduino with the sensors
    Input: None
    Output: int[5] holds the contraction values 
    */
    public int[] retrieveDataPack() throws IOException{
        int[] out = new int[5];
        String combine = "";
        char in;
        
        ICPOut.write('#');
        for(int finger = 0; finger < 5; finger++){
            while((ICPIn.available()==0)){}
            in = (char) ICPIn.read();
            
            while(Character.isDigit(in)){
                combine += (char) in;
                
                while(ICPIn.available()==0){}
                in = (char) ICPIn.read();
            }//end of while digit
            
            try{
            out[finger] = Integer.parseInt(combine);
            }//if not proper data
            catch(NumberFormatException e){
                //This will execute if reset button is pressed which is acting
                //as this programs kill switch
                System.out.println("End of Processing");
                RCPOut.write("2".getBytes());
                System.exit(0); //end program
            }//ends program
            
            combine = "";
        }//end of for each finger
        return out;
    }//end of retrieve data packet
    
    /*
    Purpose: takes the set of data and compairs it to the threshold to make a
                simplified data packet to send
    Input: the sensor data for each finger as an array (int[5])
    Output: simplified data for each finger(byte[]) through wired serial port
    */
    public void sendDataPack(int[] data) throws IOException{
        String output = "";
        
        while(RCPIn.available()==0){} //waits if request for data not sent
        RCPIn.read(); //clears the request 
        
        for(int finger = 0; finger<5; finger++){
            if(data[finger]<=Threshhold){
                output += "1_";
            }//end of if passes threshhold
            else{
                output+= "0_";
            }//end of else under threshhold
        }//end of for each finger loop
        
        RCPOut.write(output.getBytes()); //sends the data as bytes to robot's arduino
    }//end of send data pack
    
    /*
    Purpose: prints the current data from sensors, used for testing and
                troubleshooting
    */
    public void printData() throws IOException{
        int[] hand  = new int[5];
        
        hand = retrieveDataPack();
            for(int i = 0; i < hand.length; i++){
                System.out.println("Finger " + i + ": " + hand[i] + " ");
            }//end of for each finger
            System.out.println("\n*************************************************\n");
    }//end of print data
    
 }  // end class