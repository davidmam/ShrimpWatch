import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javax.comm.*;

public class SerialPortLogger implements Runnable, SerialPortEventListener {
    CommPortIdentifier portId;
    Enumeration portList;
    ArrayList <String> portnames;
    InputStream portInputStream;
    OutputStream portOutputStream;
    BufferedWriter fileOutputWriter;
    SerialPort serialPort;
    Thread readThread;
    String serialportname;
    static String ABS="A\r\n";
    boolean logging;
	private boolean waiting=false;
	Date logstart;	


    public SerialPortLogger() {
    	logging=false;
    	portList = CommPortIdentifier.getPortIdentifiers();
    	portnames= new ArrayList<String>();
    	while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	portnames.add(portId.getName());
             //    if (portId.getName().equals("COM1")) {
			//                if (portId.getName().equals("/dev/term/a")) {
            //        SimpleRead reader = new SimpleRead();
            //    }
            }
        }
    }

    public void run() {
        try {
        	// this is where the polling should come in.
        	while (logging){
        		if (!waiting){
        			portOutputStream.write(ABS.getBytes());
        			waiting=true;
        			
        		} 
        		Thread.sleep(20);
        		
        	}
            
        } catch ( IOException | InterruptedException e) {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[20];

            try {
                while (portInputStream.available() > 0) {
                    int numBytes = portInputStream.read(readBuffer);
                }
                waiting=false;
                Date timestamp=new Date();
                long logtime=timestamp.getTime()-logstart.getTime();
                fileOutputWriter.write(""+logtime+"\t"+new String(readBuffer));
            } catch (IOException e) {System.out.println(e);}
            break;
        }
    }



	public ArrayList <String> getPortNames() {
		return portnames;
		
	}

	public void log(File outputFile) {
		try{
			logstart=new Date();
			fileOutputWriter= Files.newBufferedWriter(outputFile.toPath(),Charset.forName("UTF-8"));
			logging=true;	
			readThread = new Thread(this);
	        readThread.start();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void stopLog() {
		logging=false;
		try {
			fileOutputWriter.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void setPort(String serialportname) {
		// TODO Auto-generated method stub
		portList = CommPortIdentifier.getPortIdentifiers();
		
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals(serialportname)) {
			//                if (portId.getName().equals("/dev/term/a")) {
                    this.serialportname=serialportname;
                    break;
                }
            }
        }
        try {
            serialPort = (SerialPort) portId.open("ShrimpWatchApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            portInputStream = serialPort.getInputStream();
            portOutputStream = serialPort.getOutputStream();
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(1200,
                SerialPort.DATABITS_7,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_ODD);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        
	}
	public String getPort(){
		return serialportname;
	}
}
