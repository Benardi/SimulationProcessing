package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class MetricGenerator {
	public static final String METRIC_NAMES = "utilization,numberWaiting,waitingTime";
	private Deque<String> deleteBin;

	public MetricGenerator() {
		this.deleteBin = new ArrayDeque<String>();

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



	public void processDevice(String path, Double rate, String deviceName) {
		BufferedReader utilzRd = null;
		BufferedReader numbwtRd = null;
		BufferedReader waitRd = null;
		String[] listOfMetrics = METRIC_NAMES.split(",");
		BufferedWriter logger = LoggerReaderManager.getInstance().createLogger(path + "Metrics.csv");
		try {
			utilzRd = LoggerReaderManager.getInstance().createReader(path + "_" + listOfMetrics[0] + ".csv");
			this.deleteBin.add(path + "_" + listOfMetrics[0] + ".csv");
			numbwtRd = LoggerReaderManager.getInstance().createReader(path + "_" + listOfMetrics[1] + ".csv");
			this.deleteBin.add(path + "_" + listOfMetrics[1] + ".csv");
			waitRd = LoggerReaderManager.getInstance().createReader(path + "_" + listOfMetrics[2] + ".csv");
			this.deleteBin.add(path + "_" + listOfMetrics[2] + ".csv");

			while (utilzRd.ready() && numbwtRd.ready() && waitRd.ready()) {
				Double utilization = Double.parseDouble(utilzRd.readLine());
				Double numberWaiting = Double.parseDouble(numbwtRd.readLine());
				Double waitingTime = Double.parseDouble(waitRd.readLine());
				Double Ts = utilization / rate;
				Double Ttotal = waitingTime + Ts;
				Double Ntotal = numberWaiting + utilization;

				String line = utilization + "," + Ntotal + "," + Ttotal;
				logger.write(line);
				logger.newLine();
			}
		} catch (Exception e) {
		} finally {
			try {
				utilzRd.close();
				numbwtRd.close();
				waitRd.close();
				logger.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public Double calculatesRate(Double origRate, Double feedBackRate, String deviceName) {
		if (deviceName.equals("appDevice") || deviceName.equals("loginDevice")) {
			return origRate;
		} else if (deviceName.equals("consultarDevice")) {
			return origRate * (1 + feedBackRate) * 0.6;
		} else if (deviceName.equals("alterarDevice")) {
			return origRate * (1 + feedBackRate) * 0.4;
		} else if (deviceName.equals("bdDevice")) {
			return origRate * (1 + feedBackRate);
		}
		return 0.0;

	}

	public void processAllDevices(String batchName, String[] deviceNames, Double origRate, Double feedBackRate) {
		for (int i = 0; i < deviceNames.length; i++) {
			String path = "src/results/" + batchName + "/" + deviceNames[i];
			Double rate = calculatesRate(origRate, feedBackRate, deviceNames[i]);
			processDevice(path, rate, deviceNames[i]);
		}
		emptyDeleteBin();
	}

}
