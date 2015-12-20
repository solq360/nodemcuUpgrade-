package org.solq.esp.upgrade;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * 
 * @author solq
 * */
public class RUNCommand implements ICommand {

	@Override
	public OP getType() {
		return OP.RUN;
	}

	@Override
	public void run(Socket client, List<String> args) {
		try {
			PrintWriter os = new PrintWriter(client.getOutputStream());
			String msg = null;
			for (String file : args) {
				msg = OP.buildCloseFile(file);

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
