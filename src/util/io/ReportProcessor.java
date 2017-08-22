package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportProcessor {
	private BufferedReader reader;
	private String model;
    private Map<String, BufferedWriter> loggers;
    
	public ReportProcessor() {
		this.model = model;
	}

	private void createReader(String filePath) {

		try {
			FileInputStream fs = new FileInputStream(filePath);
			InputStreamReader sr = new InputStreamReader(fs);
			this.reader = new BufferedReader(sr);

		} catch (FileNotFoundException e) {
		}

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

	public void createLoggers(String extension) {
		this.loggers = new TreeMap<String, BufferedWriter>();
		try {
			this.loggers.put("\"Number Waiting\" \"ResourceApp\"", createLogger("NumberWaitingAlterar" + model + extension));
			this.loggers.put("\"Number Waiting\" \"ResourceLogin\"", createLogger("NumberWaitingAlterar" + model + extension));
			this.loggers.put("\"Number Waiting\" \"ResourceConsultar\"", createLogger("NumberWaitingAlterar" + model +  extension));
			this.loggers.put("\"Number Waiting\" \"ResourceAlterar\"", createLogger("NumberWaitingAlterar" + model +  extension));
			this.loggers.put("\"Number Waiting\" \"ResourceBD\"", createLogger("NumberWaitingAlterar" + model +  extension));
			this.loggers.put("\"Instantaneous Utilization\" \"ResourceApp\"", createLogger("UtilizationApp" + model + extension));
			this.loggers.put("\"Instantaneous Utilization\" \"ResourceLogin\"", createLogger("UtilizationLogin" + model + extension));
			this.loggers.put("\"Instantaneous Utilization\" \"ResourceConsultar\"", createLogger("UtilizationConsultar" + model +  extension));
			this.loggers.put("\"Instantaneous Utilization\" \"ResourceAlterar\"", createLogger("UtilizationAlterar" + model +  extension));
			this.loggers.put("\"Instantaneous Utilization\" \"ResourceBD\"", createLogger("UtilizationBD" + model +  extension));
			this.loggers.put("\"Waiting Time\" \"ResourceApp\"", createLogger("NumberWaitingApp" + model + extension));
			this.loggers.put("\"Waiting Time\" \"ResourceLogin\"", createLogger("WaitingTimeLogin" + model + extension));
			this.loggers.put("\"Waiting Time\" \"ResourceConsultar\"", createLogger("WaitingTimeConsultar" + model +  extension));
			this.loggers.put("\"Waiting Time\" \"ResourceAlterar\"", createLogger("WaitingTimeAlterar" + model +  extension));
			this.loggers.put("\"Waiting Time\" \"ResourceBD\"", createLogger("WaitingTimeBD" + model +  extension));
			
			
		} catch (Exception e) {
		}
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String newModel) {
		this.model = newModel;
	}

	public void processReport(String filePath) {
		try {
			this.createReader(filePath);
			
			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");

			}

		} catch (Exception e) {
		}

	}

	public static void main(String[] args) throws IOException {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream("REPLICATION.csv");
			InputStreamReader sr = new InputStreamReader(fs);
			BufferedReader bf = new BufferedReader(sr);

			for (int i = 0; i < 100; i++) {
				String str = bf.readLine();
				String[] strList = str.split(",");
				System.out.println(strList[20] + " " + strList[26] + " " + strList[28]);
			}

		} catch (Exception e) {
		} finally {
			fs.close();
		}
	}

}
