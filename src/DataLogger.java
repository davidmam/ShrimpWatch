import java.io.File;
import java.util.ArrayList;


public class DataLogger {
	File outputFile;
	SerialPortLogger serialport;
	boolean running=false;
	
	public DataLogger() {
		serialport=new SerialPortLogger();
		running=false;
	}
	
	public void startLogging(){
		if (running==false && outputFile != null){
			running=true;
			serialport.log(outputFile);
		}
	}
	
	public void stopLogging(){
		if (running ==true){
			serialport.stopLog();
			running=false;
		}
	}

	public boolean isLogging(){
		return running;
	}
	
	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public ArrayList <String>  getSerialport() {
		return serialport.getPortNames();
	}

	public void setSerialport(String serialportname) {
		serialport.setPort( serialportname);
	}
	
	
}
