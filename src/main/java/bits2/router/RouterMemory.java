package bits2.router;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Память роутера, выполненная в виде отдельного потока
 * @author olegk
 * @version 0.1
 * @since 26.02.2024
 * @revision 01.03.2024
 */
public class RouterMemory {
	
	/**
	 * Массив адресов, на которое будет отправлено сообщение
	 */
	private List<Integer> addressMemory;
	
	/**
	 * Массив сообщений для передачи
	 */
	private List<String> messageMemory;
	
	/**
	 * Инициализация новой памяти для роутера.
	 */
	public RouterMemory() {
		addressMemory = new ArrayList<Integer>();
		messageMemory = new ArrayList<String>();
	}
	
	/**
	 * Сохранить в память адрес и сообщение
	 * @param address Адрес, на которое будет отправлено сообщение
	 * @param message Отправляемое сообщение
	 */
	public void save(int address, String message) {
		addressMemory.add(address);
		messageMemory.add(message);
		getMemoryInfo();
	}
	
	/**
	 * Получить информацию о текущей загруженности памяти
	 */
	public void getMemoryInfo() {
		try {
			int memoryState = 0;
			if(messageMemory.size()>0) {
				memoryState = messageMemory.size()*messageMemory.get(0).length();
			}
			System.out.println("Загруженность памяти роутера: "+memoryState+" бит.");
//			Files.write(Paths.get("routerMemoryState.txt"), (String.valueOf(messageMemory.size()*21)+"\n").getBytes(), StandardOpenOption.APPEND);
		}catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
	}
	
	/**
	 * Получить массив адрессов
	 * @return Массив адрессов
	 */
	public List<Integer> getAddressMemory() {
		return addressMemory;
	}

	/**
	 * Получить массив сообщений
	 * @return Массив сообщений
	 */
	public List<String> getMessageMemory() {
		return messageMemory;
	}
	
	public Object[] getData() {
		try {
			if(addressMemory.size() > 0) {
				Object[] result = new Object[] {addressMemory.get(0), messageMemory.get(0)};
				addressMemory.remove(0);
				messageMemory.remove(0);
				return result;
			}
		}catch(Exception e) {
			System.out.println("Get data error: "+e.getMessage());
		}
		return null;
	}
}
