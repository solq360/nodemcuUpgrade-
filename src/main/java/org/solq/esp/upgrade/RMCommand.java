package org.solq.esp.upgrade;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * 
 * @author solq
 * */
public class RMCommand implements ICommand {

	@Override
	public OP getType() {
		return OP.RM;
	}

	@Override
	public void run(Socket client, List<String> args) {
		try {
			PrintWriter os = new PrintWriter(client.getOutputStream());
			for (String file : args) {
				String msg = OP.buildRun(file);
				os.println(msg);
				os.flush();
				if (Main.DEBUG) {
					System.out.println(msg);
				}
			}
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
