package util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoggerReaderManager {

	private static LoggerReaderManager instance = null;

	private LoggerReaderManager() {
	}

	public static LoggerReaderManager getInstance() {
		if (instance == null) {
			instance = new LoggerReaderManager();
		}
		return instance;
	}

	public BufferedReader createReader(String filePath) {
		BufferedReader reader = null;

		try {
			FileInputStream fs = new FileInputStream(filePath);
			InputStreamReader sr = new InputStreamReader(fs);
			reader = new BufferedReader(sr);

		} catch (FileNotFoundException e) {
		}
		return reader;

	}

	public BufferedWriter createLogger(String filePath) {
		BufferedWriter logger = null;
		try {
			FileOutputStream fs = new FileOutputStream(filePath);
			OutputStreamWriter sr = new OutputStreamWriter(fs);
			logger = new BufferedWriter(sr);

		} catch (FileNotFoundException e) {

		}
		return logger;

	}

}
