package bits2.router;

import java.util.ArrayList;
import java.util.List;

import bits2.BitsApplication;

/**
 * Класс роутера
 * @author olegk
 * @version 0.1
 * @since 27.02.2024
 */
public class RouterM extends Thread{
	/**
	 * Значение адреса роутера
	 */
	private int address;
	
	/**
	 * Максимальная пропускная способность роутера в байтах
	 */
	private int maxLoadBytes;
	
	/**
	 * Подключенные к данному устройству роутеры. </br>
	 * Необходимо для передачи данных на другие устройства.
	 */
	private List<RouterM> connectedRouters;
	
	/**
	 * Память устройства, выполненная в виде отдельного потока
	 */
	private RouterMemory memory = new RouterMemory();
	
	/**
	 * Класс роутера модернизированный.
	 * @param address Адрес текущего роутера
	 * @param maxLoadBytes Максимальная пропускная способность роутера
	 * @param memory Объект памяти устройства (класс RouterMemory)
	 * @see RouterMemory
	 */
	public RouterM(int address, int maxLoadBytes) {
		this.address = address;
		this.maxLoadBytes = maxLoadBytes;
		
		connectedRouters = new ArrayList<RouterM>();
	}

	/**
	 * Получить адрес текущего устройства
	 * @return Строка с адресом текущего устройства
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * Задать адрес текущего устройства
	 * @param address Адрес устройства
	 */
	public void setAddress(int address) {
		this.address = address;
	}

	/**
	 * Получить значение максимально пропускной способности устройства
	 * @return Значение текущей пропускной способности
	 */
	public int getMaxLoadBytes() {
		return maxLoadBytes;
	}

	/**
	 * Задать значение максимально пропускной способности устройства
	 * @param maxLoadBytes Новое значение пропускной способности
	 */
	public void setMaxLoadBytes(int maxLoadBytes) {
		this.maxLoadBytes = maxLoadBytes;
	}
	
	/**
	 * Добавить новой роутер. Служит для передачи данных на этот роутер. Может служит как ретранслятор для следующего узла.
	 * @param router Объект нового роутера
	 */
	public void addRouter(RouterM router) {
		connectedRouters.add(router);
	}
	
	/**
	 * Получить роутер по адресу
	 * @param data Адрес искомого роутера
	 * @return Объект с роутером. Если по заданному адресу роутер не найден, возвращается значение null.
	 */
	public RouterM getRouter(int routerID) {
		for(RouterM router: connectedRouters)
			if(router.getAddress() == routerID)
				return router;
		return null;
	}
	
	int getCountPacket = 0;
	int sendCountPacket = 0;
	
	/**
	 * Функция принятия роутером сообщением. При получении нового сообщение оно выводится в отдельный файл.
	 * @param message Передаваемое на роутер сообщение
	 */
	public void getMessage(String message) {
		try {
			getCountPacket++;
			System.out.println("Роутер получил данные: "+message);
			System.out.println("Всего получено "+getCountPacket+" пакетов.");
//			String infoCode = getInfoCode(message);
//			String messageHemmingCode = getHemmingCode(infoCode);
//			
//			if(messageHemmingCode.equals(message)) {
//				LogManager.getLogger(this).info("Initial hemming code and calculat hemming code is equal");
//			}else {
//				LogManager.getLogger(this).info("Initial hemming code and calculat hemming code isn't equal");
//			}
//			
//			int[] positions = new int[] {0, 1, 3, 7, 15};
//			int counter = 0;
//			for(int i=0;i<positions.length;i++)
//				if(messageHemmingCode.charAt(positions[i])!=message.charAt(positions[i])) {
//					counter += Math.pow(2, i);
//				}
//
//			String correctedMessage = Main.invertBit(message, counter-1);
//			
//			infoCode = getInfoCode(correctedMessage);
//			LogManager.getLogger(this).info("Sensor: "+Integer.parseInt((String) infoCode.subSequence(0, 8), 2));
//			LogManager.getLogger(this).info("Value: "+Integer.parseInt((String) infoCode.subSequence(8, infoCode.length()), 2));
//			LogManager.getLogger(this).info("Router get information: " + message);
		}catch (Exception e) {
			System.out.println("Ошибка при получении данных: "+e.getMessage());
		}
	}
	
	public String getInfoCode(String message) throws Exception {
		String result = "";
		if(message.length()!=21) {
			throw new Exception("Message not equal 21 bits. Message length is "+message.length());
		}
		result = message.charAt(2)+message.substring(4, 7)+message.substring(8, 15)+message.substring(16, 21);
		return result;
	}
	
	/**
	 * Получить память устройства
	 * @return Объект памяти устройства
	 * @see RouterMemory
	 */
	public RouterMemory getMemory() {
		return memory;
	}

	/**
	 * Задать новую память устройства
	 * @param memory Новая память устройства
	 */
	public void setMemory(RouterMemory memory) {
		this.memory = memory;
	}
	
	/**
	 * Функция работу потока. Производит отправку данных из памяти на следующее устройство.
	 */
	public void run() {
		//Бесконечный цикл
		while(true) {
			try {
				Object[] data = memory.getData();
				
				//Если в памяти роутера есть данные - выполнить
				if(data != null && data.length>0) {	
					System.out.println("Роутер получил данные для обработки");
					
					//Получить роутер, на который необходимо отправить данные
					RouterM router = getRouter((Integer)data[0]);
					
					//Если ошибка получения роутера, повторить цикл
					if(router == null) {
						System.out.println("Ошибка: конечный роутер не найден");
						continue;
					}
					
					String hemmingCode = null;
							
					try {
						hemmingCode = BitsApplication.getHemmingCode((String)data[1]);
					}catch(Exception e) {
						System.out.println("Ошибка конвертации в код Хэмминга");
						continue;
					}
					System.out.println("Отправляемое сообщение в коде Хэмминга: "+hemmingCode);

					//Отправить сообщение на роутер
					router.getMessage(hemmingCode);
					sendCountPacket++;
					System.out.println("Всего отправлено "+sendCountPacket+" пакетов.");
					
					int sleepTime = hemmingCode.length()*1000000/maxLoadBytes;
					System.out.println("Пауза: "+sleepTime);
					
					memory.getMemoryInfo();
					
					try {
						Thread.sleep(0, sleepTime);
					} catch (InterruptedException e) {
						System.out.println("Error sleep: "+e.getMessage());
					}
				}else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						System.out.println("Error sleep: "+e.getMessage());
					}
				}
			}catch(Exception e) {
				System.out.println("Error run function: "+e.getMessage());
			}
		}
	}
}
