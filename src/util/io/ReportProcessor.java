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

	public void filterTargetMetrics(String inputFilePath, String fileName, String demand) {
		File dir = new File("src/results/" + demand);
		dir.mkdirs();
		BufferedWriter logger = null;
		try {
			this.createReader(inputFilePath);
			logger = createLogger("src/results/" + demand +"/"+ fileName);

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

	public void segregateDevices(String inputFilePath, String demand) {
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

		BufferedWriter loggerApp = null;
		BufferedWriter loggerLogin = null;
		BufferedWriter loggerConsultar = null;
		BufferedWriter loggerAlterar = null;
		BufferedWriter loggerBD = null;

		try {
			this.createReader(inputFilePath);
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

	public static void main(String[] args) throws IOException {
		ReportProcessor rp = new ReportProcessor();
		rp.filterTargetMetrics("COMPLETE_RESULTS_LOW.csv", "INTERMEDIARY_RESULTS.csv", "LOW_DEMAND");
		rp.segregateDevices("src/results/INTERMEDIARY_RESULTS_LOW.csv", "LOW_DEMAND");

	}

}
