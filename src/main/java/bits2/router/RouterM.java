package bits2.router;

import java.util.ArrayList;
import java.util.List;

import bits2.BitsApplication;

/**
 * ����� �������
 * @author olegk
 * @version 0.1
 * @since 27.02.2024
 */
public class RouterM extends Thread{
	/**
	 * �������� ������ �������
	 */
	private int address;
	
	/**
	 * ������������ ���������� ����������� ������� � ������
	 */
	private int maxLoadBytes;
	
	/**
	 * ������������ � ������� ���������� �������. </br>
	 * ���������� ��� �������� ������ �� ������ ����������.
	 */
	private List<RouterM> connectedRouters;
	
	/**
	 * ������ ����������, ����������� � ���� ���������� ������
	 */
	private RouterMemory memory = new RouterMemory();
	
	/**
	 * ����� ������� �����������������.
	 * @param address ����� �������� �������
	 * @param maxLoadBytes ������������ ���������� ����������� �������
	 * @param memory ������ ������ ���������� (����� RouterMemory)
	 * @see RouterMemory
	 */
	public RouterM(int address, int maxLoadBytes) {
		this.address = address;
		this.maxLoadBytes = maxLoadBytes;
		
		connectedRouters = new ArrayList<RouterM>();
	}

	/**
	 * �������� ����� �������� ����������
	 * @return ������ � ������� �������� ����������
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * ������ ����� �������� ����������
	 * @param address ����� ����������
	 */
	public void setAddress(int address) {
		this.address = address;
	}

	/**
	 * �������� �������� ����������� ���������� ����������� ����������
	 * @return �������� ������� ���������� �����������
	 */
	public int getMaxLoadBytes() {
		return maxLoadBytes;
	}

	/**
	 * ������ �������� ����������� ���������� ����������� ����������
	 * @param maxLoadBytes ����� �������� ���������� �����������
	 */
	public void setMaxLoadBytes(int maxLoadBytes) {
		this.maxLoadBytes = maxLoadBytes;
	}
	
	/**
	 * �������� ����� ������. ������ ��� �������� ������ �� ���� ������. ����� ������ ��� ������������ ��� ���������� ����.
	 * @param router ������ ������ �������
	 */
	public void addRouter(RouterM router) {
		connectedRouters.add(router);
	}
	
	/**
	 * �������� ������ �� ������
	 * @param data ����� �������� �������
	 * @return ������ � ��������. ���� �� ��������� ������ ������ �� ������, ������������ �������� null.
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
	 * ������� �������� �������� ����������. ��� ��������� ������ ��������� ��� ��������� � ��������� ����.
	 * @param message ������������ �� ������ ���������
	 */
	public void getMessage(String message) {
		try {
			getCountPacket++;
			System.out.println("������ ������� ������: "+message);
			System.out.println("����� �������� "+getCountPacket+" �������.");
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
			System.out.println("������ ��� ��������� ������: "+e.getMessage());
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
	 * �������� ������ ����������
	 * @return ������ ������ ����������
	 * @see RouterMemory
	 */
	public RouterMemory getMemory() {
		return memory;
	}

	/**
	 * ������ ����� ������ ����������
	 * @param memory ����� ������ ����������
	 */
	public void setMemory(RouterMemory memory) {
		this.memory = memory;
	}
	
	/**
	 * ������� ������ ������. ���������� �������� ������ �� ������ �� ��������� ����������.
	 */
	public void run() {
		//����������� ����
		while(true) {
			try {
				Object[] data = memory.getData();
				
				//���� � ������ ������� ���� ������ - ���������
				if(data != null && data.length>0) {	
					System.out.println("������ ������� ������ ��� ���������");
					
					//�������� ������, �� ������� ���������� ��������� ������
					RouterM router = getRouter((Integer)data[0]);
					
					//���� ������ ��������� �������, ��������� ����
					if(router == null) {
						System.out.println("������: �������� ������ �� ������");
						continue;
					}
					
					String hemmingCode = null;
							
					try {
						hemmingCode = BitsApplication.getHemmingCode((String)data[1]);
					}catch(Exception e) {
						System.out.println("������ ����������� � ��� ��������");
						continue;
					}
					System.out.println("������������ ��������� � ���� ��������: "+hemmingCode);

					//��������� ��������� �� ������
					router.getMessage(hemmingCode);
					sendCountPacket++;
					System.out.println("����� ���������� "+sendCountPacket+" �������.");
					
					int sleepTime = hemmingCode.length()*1000000/maxLoadBytes;
					System.out.println("�����: "+sleepTime);
					
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
