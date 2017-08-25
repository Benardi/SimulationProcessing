package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class MetricGenerator {
	private String batchName;
	private String deviceNames;
	private String metricNames;
	private Deque<String> deleteBin;


	public MetricGenerator(String deviceNames, String metricNames, String batchName) {
		this.deviceNames = deviceNames;
		this.metricNames = metricNames;
		this.batchName = batchName;
		this.deleteBin = new ArrayDeque<String>();


	}
	
	private void emptyDeleteBin(){
		while(!this.deleteBin.isEmpty()){
			try {
				Path path = Paths.get(this.deleteBin.pop());
				Files.delete(path);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	private BufferedReader createReader(String filePath) {
		BufferedReader reader = null;

		try {
			FileInputStream fs = new FileInputStream(filePath);
			InputStreamReader sr = new InputStreamReader(fs);
			reader = new BufferedReader(sr);

		} catch (FileNotFoundException e) {
		}
		return reader;

	}

	private BufferedWriter createLogger(String filePath) {
		BufferedWriter logger = null;
		try {
			FileOutputStream fs = new FileOutputStream(filePath);
			OutputStreamWriter sr = new OutputStreamWriter(fs);
			logger = new BufferedWriter(sr);

		} catch (FileNotFoundException e) {

		}
		return logger;

	}

	public void processDevice(String path) {
		String[] listOfMetrics = metricNames.split(",");
		
		
		

	}

}
