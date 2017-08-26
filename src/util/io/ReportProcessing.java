package util.io;

public class ReportProcessing {
	
	public static void main(String[] args) {
		ReportProcessor rp = new ReportProcessor();
		MetricGenerator mg = new MetricGenerator();
		String reportPath = "src/reports/highDemandResults.csv";
		String batchName = "highDemand";
		Double origRate = 1.3988;
		Double feedBackRate = 0.20;
		
		rp.processReport(reportPath, batchName);
		mg.processAllDevices(batchName, ReportProcessor.DEVICE_NAMES.split(","), origRate, feedBackRate);

	
	}

}
