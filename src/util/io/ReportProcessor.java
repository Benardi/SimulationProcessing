package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ReportProcessor {
	private BufferedReader reader;

	public ReportProcessor() {
	}

	public void processReport(String inputFilePath, String batchName, String deviceNames) {
		filterTargetMetrics(inputFilePath, batchName);
		segregateDevices(batchName);
		segregateMetrics(batchName, deviceNames);

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

	private void createFolderHierarchyByDemand(String demand) {
		File dir = new File("src/results/" + demand);
		dir.mkdirs();

	}

	public void filterTargetMetrics(String inputFilePath, String demand) {
		createFolderHierarchyByDemand(demand);

		BufferedWriter logger = null;
		try {
			this.createReader(inputFilePath);
			logger = createLogger("src/results/" + demand + "/INTERMEDIARY_RESULTS.csv");

			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");
				if (fragmented[20].equals("\"Waiting Time\"") || fragmented[20].equals("\"Number Waiting\"")
						|| fragmented[20].equals("\"Instantaneous Utilization\"")) {
					logger.write(fragmented[20].substring(1, fragmented[20].length() - 1) + ","
							+ fragmented[26].substring(1, fragmented[26].length() - 1) + "," + fragmented[28]);
					logger.newLine();
				}
			}

		} catch (Exception e) {
		} finally {
			try {
				logger.close();
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void createFolderHierarchyByDevice(String demand) {

		File dir = new File("src/results/" + demand + "/APP");
		dir.mkdirs();
		dir = new File("src/results/" + demand + "/LOGIN");
		dir.mkdirs();
		dir = new File("src/results/" + demand + "/CONSULTAR");
		dir.mkdirs();
		dir = new File("src/results/" + demand + "/ALTERAR");
		dir.mkdirs();
		dir = new File("src/results/" + demand + "/BD");
		dir.mkdirs();

	}

	public void segregateDevices(String demand) {
		createFolderHierarchyByDevice(demand);

		BufferedWriter loggerApp = null;
		BufferedWriter loggerLogin = null;
		BufferedWriter loggerConsultar = null;
		BufferedWriter loggerAlterar = null;
		BufferedWriter loggerBD = null;

		try {
			this.createReader("src/results/" + demand + "/INTERMEDIARY_RESULTS.csv");
			loggerApp = createLogger("src/results/" + demand + "/APP/INTERMEDIARY_APP.csv");
			loggerLogin = createLogger("src/results/" + demand + "/LOGIN/INTERMEDIARY_LOGIN.csv");
			loggerConsultar = createLogger("src/results/" + demand + "/CONSULTAR/INTERMEDIARY_CONSULTAR.csv");
			loggerAlterar = createLogger("src/results/" + demand + "/ALTERAR/INTERMEDIARY_ALTERAR.csv");
			loggerBD = createLogger("src/results/" + demand + "/BD/INTERMEDIARY_BD.csv");

			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");

				switch (fragmented[1]) {
				case "ResourceApp":
				case "APP.Queue":
					loggerApp.write(fragmented[0] + "," + fragmented[2]);
					loggerApp.newLine();
					break;

				case "ResourceLogin":
				case "LOGIN.Queue":
					loggerLogin.write(fragmented[0] + "," + fragmented[2]);
					loggerLogin.newLine();
					break;

				case "ResourceConsultar":
				case "CONSULTAR.Queue":
					loggerConsultar.write(fragmented[0] + "," + fragmented[2]);
					loggerConsultar.newLine();
					break;

				case "ResourceAlterar":
				case "ALTERAR.Queue":
					loggerAlterar.write(fragmented[0] + "," + fragmented[2]);
					loggerAlterar.newLine();
					break;

				case "ResourceBD":
				case "BD.Queue":
					loggerBD.write(fragmented[0] + "," + fragmented[2]);
					loggerBD.newLine();
					break;

				default:
					break;

				}
			}

		} catch (Exception e) {
		} finally {
			try {
				loggerApp.close();
				loggerLogin.close();
				loggerConsultar.close();
				loggerAlterar.close();
				loggerBD.close();

				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void segregateMetrics(String demand, String deviceNames) {
		String[] listOfDevices = deviceNames.split(",");
		for (int i = 0; i < listOfDevices.length; i++) {
			segregateMetricsOfDevice(demand, listOfDevices[i]);
		}

	}

	private void segregateMetricsOfDevice(String demand, String device) {

		BufferedWriter loggerUtlz = null;
		BufferedWriter loggerNumber = null;
		BufferedWriter loggerTime = null;

		try {
			this.createReader("src/results/" + demand + "/" + device + "/INTERMEDIARY_" + device + ".csv");
			loggerUtlz = createLogger("src/results/" + demand + "/" + device + "/UTILIZATION.csv");
			loggerNumber = createLogger("src/results/" + demand + "/" + device + "/NUMBER_WAITING.csv");
			loggerTime = createLogger("src/results/" + demand + "/" + device + "/WAITING_TIME.csv");

			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");

				switch (fragmented[0]) {
				case "Instantaneous Utilization":
					loggerUtlz.write(fragmented[1]);
					loggerUtlz.newLine();
					break;

				case "Number Waiting":
					loggerNumber.write(fragmented[1]);
					loggerNumber.newLine();
					break;

				case "Waiting Time":
					loggerTime.write(fragmented[1]);
					loggerTime.newLine();
					break;

				default:
					break;

				}
			}

		} catch (Exception e) {
		} finally {
			try {
				loggerUtlz.close();
				loggerNumber.close();
				loggerTime.close();

				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		ReportProcessor rp = new ReportProcessor();
		String deviceNames = "ALTERAR,APP,BD,CONSULTAR,LOGIN";
		rp.processReport("COMPLETE_RESULTS_LOW.csv", "lowDemand", deviceNames);

	}

}
