package util.io;

public class ReportProcessing {
	
	public static void main(String[] args) {
		ReportProcessor rp = new ReportProcessor();
		MetricGenerator mg = new MetricGenerator();
		String reportPath = "src/reports/lowDemandResults.csv";
		String batchName = "lowDemand";
		Double origRate = 0.738;
		Double feedBackRate = 0.08;
		
		rp.processReport(reportPath, batchName);
		mg.processAllDevices(batchName, ReportProcessor.DEVICE_NAMES.split(","), origRate, feedBackRate);

	
	}

}
