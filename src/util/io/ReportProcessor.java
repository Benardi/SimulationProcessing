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

public class ReportProcessor {
	private BufferedReader reader;

	public ReportProcessor() {
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

	public void filterTargetMetrics(String inputFilePath, String fileName) {
		File dir = new File("src/results/");
		dir.mkdirs();
		BufferedWriter logger = null;
		try {
			this.createReader(inputFilePath);
			logger = createLogger("src/results/" + fileName);

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

	public void segregateDevices(String inputFilePath) {
		File dir = new File("src/results/APP");
		dir.mkdirs();
		dir = new File("src/results/LOGIN");
		dir.mkdirs();
		dir = new File("src/results/CONSULTAR");
		dir.mkdirs();
		dir = new File("src/results/ALTERAR");
		dir.mkdirs();
		dir = new File("src/results/BD");
		dir.mkdirs();

		BufferedWriter loggerApp = null;
		BufferedWriter loggerLogin = null;
		BufferedWriter loggerConsultar = null;
		BufferedWriter loggerAlterar = null;
		BufferedWriter loggerBD = null;

		try {
			this.createReader(inputFilePath);
			loggerApp = createLogger("src/results/APP/INTERMEDIARY_APP.csv");
			loggerLogin = createLogger("src/results/LOGIN/INTERMEDIARY_LOGIN.csv");
			loggerConsultar = createLogger("src/results/CONSULTAR/INTERMEDIARY_CONSULTAR.csv");
			loggerAlterar = createLogger("src/results/ALTERAR/INTERMEDIARY_ALTERAR.csv");
			loggerBD = createLogger("src/results/BD/INTERMEDIARY_BD.csv");

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

	public static void main(String[] args) throws IOException {
		ReportProcessor rp = new ReportProcessor();
//		rp.filterTargetMetrics("COMPLETE_RESULTS_LOW.csv", "INTERMEDIARY_RESULTS_LOW.csv");
		rp.segregateDevices("src/results/INTERMEDIARY_RESULTS_LOW.csv");
	}

}
