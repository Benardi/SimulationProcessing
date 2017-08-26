package util.io;

public class ReportProcessing {
	
	public static void main(String[] args) {
		ReportProcessor rp = new ReportProcessor();
		MetricGenerator mg = new MetricGenerator();
		String reportPath = "src/reports/averageDemandResults.csv";
		String batchName = "averageDemand";
		Double origRate = 1.172;
		Double feedBackRate = 0.14;
		
		rp.processReport(reportPath, batchName);
		mg.processAllDevices(batchName, ReportProcessor.DEVICE_NAMES.split(","), origRate, feedBackRate);

	
	}

}
