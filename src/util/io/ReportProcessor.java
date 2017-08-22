package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ReportProcessor {
	private BufferedReader reader;
	private BufferedWriter logger;

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

	private void createLogger(String filePath) {
		try {
			FileOutputStream fs = new FileOutputStream(filePath);
			OutputStreamWriter sr = new OutputStreamWriter(fs);
			this.logger = new BufferedWriter(sr);

		} catch (FileNotFoundException e) {

		}

	}

	public void filterTargetMetrics(String inputFilePath, String outputFilePath) {
		try {
			this.createReader(inputFilePath);
			this.createLogger(outputFilePath);

			String line;
			while ((line = this.reader.readLine()) != null) {
				String[] fragmented = line.split(",");
				if (fragmented[20].equals("\"Waiting Time\"") || fragmented[20].equals("\"Number Waiting\"")
						|| fragmented[20].equals("\"Instantaneous Utilization\"")) {
					this.logger.write(fragmented[20].substring(1, fragmented[20].length() - 1) + ","
							+ fragmented[26].substring(1, fragmented[26].length() - 1) + "," + fragmented[28]);
					this.logger.newLine();
				}
			}

		} catch (Exception e) {
		} finally {
			try {
				this.logger.close();
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		ReportProcessor rp = new ReportProcessor();
		rp.filterTargetMetrics("COMPLETE_RESULTS_LOW.csv", "INTERMEDIARY_RESULTS_LOW.csv");
	}

}
