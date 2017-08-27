package util.io;

public class ReportProcessing {
	
	public static void main(String[] args) {
		ReportProcessor rp = new ReportProcessor();
		MetricGenerator mg = new MetricGenerator();
		String reportPath = "src/reports/batch8.csv";
		String batchName = "batch8";
		Double origRate = 1.538;
		Double feedBackRate = 0.14;
		
		rp.processReport(reportPath, batchName);
		mg.processAllDevices(batchName, ReportProcessor.DEVICE_NAMES.split(","), origRate, feedBackRate);

	
	}

}
