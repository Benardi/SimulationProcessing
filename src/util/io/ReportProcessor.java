package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class ReportProcessor {
	private BufferedReader reader;
	public final static String DEVICE_NAMES = "appDevice,loginDevice,consultarDevice,alterarDevice,bdDevice";
	public final static String TEMP_FILE_PREFIX = "TMPY";
	public final static String SEGREGATED_PREFIX = "SEGR";

	public void processReport(String inputFilePath, String batchName) {
		filterTargetMetrics(inputFilePath, batchName);
		segregateDevices(batchName);
		segregateMetrics(batchName);
		DeletionBin.getInstance().emptyDeleteBin();

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
			DeletionBin.getInstance().queryFile("src/results/" + demand + "/" + TEMP_FILE_PREFIX + "RESULTS.csv");

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
		String[] listOfDevices = DEVICE_NAMES.split(",");
		for (int i = 0; i < listOfDevices.length; i++) {
			File dir = new File("src/results/" + demand);
			dir.mkdirs();

		}

	}

	public void segregateDevices(String demand) {
		String[] listOfDevices = DEVICE_NAMES.split(",");
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
			DeletionBin.getInstance()
					.queryFile("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[0] + ".csv");

			loggerLogin = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[1] + ".csv");
			DeletionBin.getInstance()
					.queryFile("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[1] + ".csv");

			loggerConsultar = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[2] + ".csv");
			DeletionBin.getInstance()
					.queryFile("src/results/" + demand + "/" + "/" + TEMP_FILE_PREFIX + listOfDevices[2] + ".csv");

			loggerAlterar = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[3] + ".csv");
			DeletionBin.getInstance()
					.queryFile("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[3] + ".csv");

			loggerBD = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[4] + ".csv");
			DeletionBin.getInstance()
					.queryFile("src/results/" + demand + "/" + TEMP_FILE_PREFIX + listOfDevices[4] + ".csv");

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
		String[] listOfDevices = DEVICE_NAMES.split(",");
		for (int i = 0; i < listOfDevices.length; i++) {
			segregateMetricsOfDevice(demand, listOfDevices[i]);
		}

	}

	private void segregateMetricsOfDevice(String demand, String device) {
		BufferedWriter logger = null;
		Deque<String> utlzStack = new ArrayDeque<String>();
		Deque<String> numbWtStack = new ArrayDeque<String>();
		Deque<String> timeWtStack = new ArrayDeque<String>();

		try {
			this.createReader("src/results/" + demand + "/" + TEMP_FILE_PREFIX + device + ".csv");
			logger = LoggerReaderManager.getInstance()
					.createLogger("src/results/" + demand + "/" + SEGREGATED_PREFIX + device + ".csv");

			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");

				switch (fragmented[0]) {
				case "Instantaneous Utilization":
					utlzStack.push(fragmented[1]);
					break;

				case "Number Waiting":
					numbWtStack.push(fragmented[1]);
					break;

				case "Waiting Time":
					timeWtStack.push(fragmented[1]);
					break;

				default:
					break;

				}
			}

			while (!utlzStack.isEmpty()) {
				logger.write(utlzStack.pollLast() + "," + numbWtStack.pollLast() + "," + timeWtStack.pollLast());
				logger.newLine();
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

}
