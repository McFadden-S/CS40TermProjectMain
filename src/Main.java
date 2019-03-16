// **********************************************************************
// Programmer:	Shae McFadden
// Class:	CS40S
//
// Assignment:	Term Project
//
// Description:	This is the main class of the term project and will be the
//              the file required to start the entire program
//
//
// Input:       Some basic inputs will be required during calibration phase
//              of arduino
//
// Output:	Will output required steps user needs to take
// ***********************************************************************

import java.io.IOException;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Scanner;


public class Main
{  // begin class
	public static void main(String args[]) throws IOException
	{  // begin main
	// ***** declaration of constants *****
	
	// ***** declaration of variables *****
	
            int[] hand = new int[5];
            String incoming = "XXX";
	// ***** create objects *****
		
		Communicator comm = new Communicator();
                Scanner scan  = new Scanner(System.in);
	
	// ***** Print Banner *****
	
		System.out.println("**********************************");
		System.out.println("NAME:        Shae McFadden");
		System.out.println("Class:       CS40S");
		System.out.println("Assignment:  Term Project");
		System.out.println("**********************************");
		
	// ***** main body *****
	
        comm.startUp();
        comm.calibration();
        
        System.out.println("Enter 'STOP' to end program...");
        System.out.println("*************************************************\n");
        
        
        
        while(!incoming.equals("STOP")){
            hand = comm.retrieveDataPack();
            for(int i = 0; i < hand.length; i++){
                System.out.println("Finger " + i + ": " + hand[i] + " ");
            }//end of for each finger
            System.out.println("\n*************************************************\n");
            
            if(scan.hasNext()){
                incoming = scan.next();
            }//end of if input
        }//end of main while loop
        
	// ***** closing message *****
	
		System.out.println("end of processing");
	
	}  // end main	
}  // end class
