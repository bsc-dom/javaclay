
package es.bsc.dataclay.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeExec {
	public StreamWrapper getStreamWrapper(final InputStream is, final String type){
		return new StreamWrapper(is, type);
	}
	private class StreamWrapper extends Thread {
		InputStream is = null;
		String type = null;          
		StreamWrapper(final InputStream is, final String type) {
			this.is = is;
			this.type = type;
		}

		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ( (line = br.readLine()) != null) {
					if (type.equals("OUTPUT")) {
						System.out.println(line);
					} else { 
						System.err.println(line);
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();  
			}
		}
	}


	// this is where the action is
	public static void execCommand(final String command) {
		RuntimeExec rte = new RuntimeExec();
		StreamWrapper error, output;
		try {
			ProcessBuilder pb = new ProcessBuilder(command.split(" "));			
			Process process = pb.start();
			error = rte.getStreamWrapper(process.getErrorStream(), "ERROR");
			output = rte.getStreamWrapper(process.getInputStream(), "OUTPUT");
			int exitVal = 0;
			error.start();
			output.start();
			exitVal = process.waitFor();
			if (exitVal != 0) { 
				System.err.println("Process exit with code " + exitVal);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
