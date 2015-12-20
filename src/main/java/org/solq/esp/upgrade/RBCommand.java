package org.solq.esp.upgrade;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * 
 * @author solq
 * */
public class RBCommand implements ICommand {

	@Override
	public OP getType() {
		return OP.RB;
	}

	@Override
	public void run(Socket client, List<String> args) {
		try {
			PrintWriter os = new PrintWriter(client.getOutputStream());
			String msg = OP.buildReboot();
			os.println(msg);
			os.flush();
			if (Main.DEBUG) {
				System.out.println(msg);
			}
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
