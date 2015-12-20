package org.solq.esp.upgrade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solq.esp.upgrade.ICommand.OP;

/**
 * esp socket upgrade
 * 
 * @author solq
 * */
public class Main {

	public final static boolean DEBUG = false;
	private final static Map<OP, ICommand> COMMANDS = new HashMap<OP, ICommand>(
			OP.values().length);
	static {
		REGISTER(new UPCommand());
		REGISTER(new RMCommand());
		REGISTER(new RBCommand());
		REGISTER(new RUNCommand());
	}

	/***
	 * 参数1 操作指令 <br>
	 * 参数2 esp socket ip:port <br>
	 * 参数3-N 传送参数1-N
	 * */
	public static void main(String[] args) {
		SHOWCOMMAND();
		//System.out.println(OP.toJson(args));
		final String addr = args[0].split(":")[0];
		final int port = Integer.valueOf(args[0].split(":")[1]);
		final ICommand command = COMMANDS.get(OP.valueOf(args[1].toUpperCase()));
		try {
			List<String> argsList = Arrays.asList(args);
			List<String> newArgs = argsList.subList(2, args.length);
			final Socket client = new Socket(addr, port);
			if (!client.isConnected()) {
				System.err.println("socket is no connect");
			}

			command.run(client, newArgs);
			// 等侍socket回写完毕
			Thread.sleep(1000);
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void REGISTER(ICommand c) {
		COMMANDS.put(c.getType(), c);
	}

	static void SHOWCOMMAND() {
		for (OP op : OP.values()) {
			System.out.println(op.getDes());
		}
	}

	final static class READ_TASK implements Runnable {
		private Socket client;

		public READ_TASK(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(client.getInputStream(), "UTF-8"));
				for (String line = bufferedReader.readLine(); line != null; line = bufferedReader
						.readLine()) {
					System.out.println("receive data : " + line);
				}
				bufferedReader.close();

				// int c;
				// InputStream ips = client.getInputStream();
				// while ( !client.isClosed() && (c = ips.read()) !=
				// -1) {
				// System.out.print((char) c);
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
