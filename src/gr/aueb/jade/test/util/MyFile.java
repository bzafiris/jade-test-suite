package gr.aueb.jade.test.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class MyFile {
	String m_sFile = "";

	BufferedReader fileReader = null;

	BufferedWriter fileWriter = null;
	
	FileInputStream fileInputStream = null;

	public MyFile(String sFile) {
		m_sFile = sFile;
	}

	public MyFile() {

	}

	public String readFileIntoString(String strInputFile) {
		File file;
		RandomAccessFile streamIn;
		long filePointer = 0, fileLength = 0;
		String strInput = "";

		try {
			file = new File(strInputFile);
			if (file.exists()) {
				streamIn = new RandomAccessFile(file, "r");

				fileLength = streamIn.length();
				while (filePointer < fileLength) {// While more to be read...
					strInput = strInput + streamIn.readLine() + "\n";
					filePointer = streamIn.getFilePointer();
				}// While more to be read...

				streamIn.close();
			} else {
				System.out.println("The specified file does not exist.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return strInput;
	}

	public void writeStringToFile(String strResult, String strOutputFile) {
		try {
			RandomAccessFile outFile;
			File file = new File(strOutputFile);
			outFile = new RandomAccessFile(file, "rw");
			outFile.writeBytes(strResult);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public void write(String s){
		try {
			fileWriter.write(s);
		} catch (IOException e) {

			e.printStackTrace();
		}		
	}
	
	public boolean openFile(boolean bWrite) {
		try {
			if (bWrite) {
				if (fileWriter != null)
					fileWriter.close();
				fileWriter = new BufferedWriter(new FileWriter(m_sFile));
				
			} else {
				if (fileReader != null)
					fileReader.close();
				fileInputStream = new FileInputStream(m_sFile);
				fileReader = new BufferedReader(new InputStreamReader(fileInputStream));
			}
			return true;

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	public Properties readProperties(){
		Properties p = new Properties();
		try {
			p.load(fileInputStream);
		} catch (IOException e) {
		
			e.printStackTrace();
			return null;
		}				
		return p;
	}
	
	public Vector readCSV(String separator) {
		if (separator == null)
			separator = ",";
		StringTokenizer tokenizer = null;
		Vector ret = new Vector();

		try {
			String sLine = fileReader.readLine();
			if (sLine == null)
				return null;
			tokenizer = new StringTokenizer(sLine, separator);
			while (tokenizer.hasMoreElements()) {
				ret.add(tokenizer.nextElement());
			}
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public void closeFile(boolean bWrite) {
		try {
			if (bWrite){
				if (fileWriter != null)
					fileWriter.close();
			} else 
			if (fileReader != null) {
				fileReader.close();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		MyFile xf = new MyFile("delays.txt");
		xf.openFile(false);
		Vector rec = null;
		double d;
		int n;
		while( (rec = xf.readCSV("\t")) != null){
			d = Double.parseDouble((String)rec.get(0));
			n = Integer.parseInt((String)rec.get(1));
			System.out.println(d + "," + n);
		}
	}
	
}