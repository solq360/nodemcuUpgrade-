package org.solq.esp.upgrade;

import java.net.Socket;
import java.util.List;

/**
 * 
 * @author solq
 * */
public interface ICommand {
	// static ObjectMapper OBJECTMAPPER = new ObjectMapper();

	public OP getType();

	public void run(Socket client, List<String> args);

	public static enum OP {
		/** �ϴ��ļ� **/
		UP("�ϴ��ļ� UP [fileName or dir]"),
		/** ɾ���ļ� **/
		RM("ɾ���ļ� RM [fileName or dir]"),
		/** ���� **/
		RB("���� RB"),
		/** ���� **/
		RUN("ִ���ļ� RUN [fileName1 fileName2 fileName3]"), ;

		private String des;

		private OP(String des) {
			this.des = des;
		}

		public String getDes() {
			return des;
		}

		public static String buildCreateFile(String fileName) {
			return buildMessage("FILE_CREATE", fileName);
		}

		public static String buildWirteLine(String line) {
			return buildMessage("FILE_UP", line);
		}

		public static String buildRemoveFile(String fileName) {
			return buildMessage("FILE_REMOVE", fileName);
		}

		public static String buildCloseFile(String fileName) {
			return buildMessage("FILE_CLOSE", fileName);
		}

		public static String buildReboot() {
			return buildMessage("RE_BOOT", "");
		}

		public static String buildRun(String fileName) {
			return buildMessage("RUN", fileName);
		}

		// public static String buildJSON(String op, String data) {
		// Map<String, String> obj = new HashMap<String, String>(2);
		// obj.paut("OP", op);
		// obj.put("data", data);
		// return toJson(obj);
		// }

		/**
		 * �Զ���Э��
		 * */
		public static String buildMessage(String op, String data) {
			return op + ":abcde123834:" + data;
		}

		// public static String toJson(Object obj) {
		// try {
		// return OBJECTMAPPER.writeValueAsString(obj);
		// } catch (JsonProcessingException e) {
		// throw new RuntimeException(e);
		// }
		// }
	}
}
