package util.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class DeletionBin {

	private static DeletionBin instance = null;
	private Deque<String> bin;

	private DeletionBin() {
		this.bin = new ArrayDeque<String>();

	}

	public static DeletionBin getInstance() {
		if (instance == null) {
			instance = new DeletionBin();
		}
		return instance;
	}

	public void emptyDeleteBin() {
		while (!this.bin.isEmpty()) {
			try {
				Path path = Paths.get(this.bin.pop());
				Files.delete(path);
			} catch (Exception e) {
			}
		}

	}
	public void queryFile(String path){
		this.bin.add(path);
		
	}
	

}
