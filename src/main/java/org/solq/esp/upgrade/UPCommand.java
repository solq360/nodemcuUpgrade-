package org.solq.esp.upgrade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author solq
 * */
public class UPCommand implements ICommand {

	@Override
	public OP getType() {
		return OP.UP;
	}

	@Override
	public void run(Socket client, List<String> args) {
		try {
			PrintWriter os = new PrintWriter(client.getOutputStream());
			List<File> files = new LinkedList<File>();
			// 提取所有file
			for (String file : args) {
				files.addAll(getFiles(new File(file)));
			}
			String msg = null;
			// 每个文件处理
			for (File file : files) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file),
								"UTF-8"));

				// 创建文件
				msg = OP.buildCreateFile(file.getName());
				os.println(msg);
				os.flush();
				if (Main.DEBUG) {
					System.out.println(msg);
				}
				try {
					for (String line = bufferedReader.readLine(); line != null; line = bufferedReader
							.readLine()) {
						msg = OP.buildWirteLine(line);
						os.println(msg);
						if (Main.DEBUG) {
							System.out.println(msg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				msg = OP.buildCloseFile(file.getName());
				os.println(msg);
				os.flush();
				if (Main.DEBUG) {
					System.out.println(msg);
				}

				// 关闭文件
				bufferedReader.close();
			}
			// 等侍socket回写完毕
			Thread.sleep(5000);
			os.close();
			System.out
					.println("run end =======================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	List<File> getFiles(File file) {
		final List<File> result = new LinkedList<File>();
		File[] list = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					List<File> tmp = getFiles(f);
					if (tmp != null) {
						result.addAll(tmp);
					}
					return false;
				}
				return true;
			}
		});
		if (list != null && list.length != 0) {
			Collections.addAll(result, list);
		}
		return result;
	}
}
