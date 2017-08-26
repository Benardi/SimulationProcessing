package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class MetricGenerator {
	public static final String METRIC_NAMES = "utilization,numberWaiting,waitingTime";

	public void processDevice(String path, Double rate, String deviceName) {
		BufferedReader reader = null;
		BufferedWriter logger = LoggerReaderManager.getInstance().createLogger(path + "/" +deviceName + ".csv");
		try {
			reader = LoggerReaderManager.getInstance().createReader(path + "/" + ReportProcessor.SEGREGATED_PREFIX + deviceName + ".csv");
			DeletionBin.getInstance().queryFile(path + "/" + ReportProcessor.SEGREGATED_PREFIX + deviceName + ".csv");
			
			while (reader.ready()) {
				String[] processedLine = reader.readLine().split(",");
				Double utilization = Double.parseDouble(processedLine[0]);
				Double numberWaiting = Double.parseDouble(processedLine[1]);
				Double waitingTime = Double.parseDouble(processedLine[2]);
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
				reader.close();
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
			String path = "src/results/" + batchName;
			Double rate = calculatesRate(origRate, feedBackRate, deviceNames[i]);
			processDevice(path, rate, deviceNames[i]);
		}
		DeletionBin.getInstance().emptyDeleteBin();
	}

}
