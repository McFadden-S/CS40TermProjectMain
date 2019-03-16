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


public class Main
{  // begin class
	public static void main(String args[]) throws IOException
	{  // begin main
	// ***** declaration of constants *****
	
	// ***** declaration of variables *****
	
	// ***** create objects *****
		
		Communicator comm = new Communicator();
	
	// ***** Print Banner *****
	
		System.out.println("**********************************");
		System.out.println("NAME:        Shae McFadden");
		System.out.println("Class:       CS40S");
		System.out.println("Assignment:  Term Project");
		System.out.println("**********************************");
		
	// ***** main body *****
	
        comm.startUp();
        comm.calibration();
        
	// ***** closing message *****
	
		System.out.println("end of processing");
	
	}  // end main	
}  // end class