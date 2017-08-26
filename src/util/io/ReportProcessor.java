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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class ReportProcessor {
	private BufferedReader reader;
	public final static String DEVICE_NAMES = "appDevice,loginDevice,consultarDevice,alterarDevice,bdDevice";
	public final static String TEMP_FILE_PREFIX = "TMPY";
	private Deque<String> deleteBin;

	public ReportProcessor() {
		this.deleteBin = new ArrayDeque<String>();
	}

	public void processReport(String inputFilePath, String batchName) {
		filterTargetMetrics(inputFilePath, batchName);
		segregateDevices(batchName);
		segregateMetrics(batchName);
		emptyDeleteBin();

	}

	private void createReader(String filePath) {
		this.reader = LoggerReaderManager.getInstance().createReader(filePath);
		
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
			logger = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + "RESULTS.csv");
			this.deleteBin.add("src/results/" + demand + "/" + TEMP_FILE_PREFIX + "RESULTS.csv");

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
		String[] listOfDevices = this.DEVICE_NAMES.split(",");
		for (int i = 0; i < listOfDevices.length; i++) {
			File dir = new File("src/results/" + demand);
			dir.mkdirs();

		}

	}

	private void emptyDeleteBin() {
		while (!this.deleteBin.isEmpty()) {
			try {
				Path path = Paths.get(this.deleteBin.pop());
				Files.delete(path);
			} catch (Exception e) {
			}
		}

	}

	public void segregateDevices(String demand) {
		String[] listOfDevices = this.DEVICE_NAMES.split(",");
		createFolderHierarchyByDevice(demand);

		BufferedWriter loggerApp = null;
		BufferedWriter loggerLogin = null;
		BufferedWriter loggerConsultar = null;
		BufferedWriter loggerAlterar = null;
		BufferedWriter loggerBD = null;

		try {
			this.createReader("src/results/" + demand + "/" + TEMP_FILE_PREFIX + "RESULTS.csv");
			loggerApp = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[0] + ".csv");
			this.deleteBin.add("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[0] + ".csv");

			loggerLogin = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[1] + ".csv");
			this.deleteBin.add("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[1] + ".csv");

			loggerConsultar = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[2] + ".csv");
			this.deleteBin.add("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[2] + ".csv");

			loggerAlterar = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[3] + ".csv");
			this.deleteBin.add("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[3] + ".csv");

			loggerBD = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[4] + ".csv");
			this.deleteBin.add("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[4] + ".csv");

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

	public void segregateMetrics(String demand) {
		String[] listOfDevices = this.DEVICE_NAMES.split(",");
		for (int i = 0; i < listOfDevices.length; i++) {
			segregateMetricsOfDevice(demand, listOfDevices[i]);
		}

	}

	private void segregateMetricsOfDevice(String demand, String device) {

		BufferedWriter loggerUtlz = null;
		BufferedWriter loggerNumber = null;
		BufferedWriter loggerTime = null;

		try {
			this.createReader("src/results/" + demand + "/" + TEMP_FILE_PREFIX + device + ".csv");
			loggerUtlz = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + device + "_utilization.csv");
			loggerNumber = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + device + "_numberWaiting.csv");
			loggerTime = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + device + "_waitingTime.csv");

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

}
