package bits2.sensor;

import java.util.ArrayList;
import java.util.List;

import bits2.BitsApplication;
import bits2.router.RouterMemory;

/**	
* ����� �������
* @author olegk
* @version 0.1
* @since 27.02.2024
*/
public class SensorPoller extends Thread{
	
	/**
	 * ������������ ���������� ���������� ��������
	 */
	private List<SensorTimer> sensorTimers;
	
	/**
	 * ������������ ������ ���������� ������
	 */
	private PollerMemory pollerMemory;

	/**
	 * ������ �������, � ������� ������������ ��������� ��� ��������
	 */
	private RouterMemory routerMemory; 
	
	/**
	 * ����������� ���������� ������
	 * @param routerMemory ������ �������, � ������� ����� ������������ ������ ��� ���������� ��������
	 */
	public SensorPoller(RouterMemory routerMemory) {
		sensorTimers = new ArrayList<SensorTimer>();
		this.routerMemory = routerMemory;
	}
	
	/**
	 * ����������� ���������� ������
	 * @param routerMemory ������ �������, � ������� ����� ������������ ������ ��� ���������� ��������
	 * @param sensorTimers ������ ��������� ���������� ��������
	 */
	public SensorPoller(RouterMemory routerMemory, ArrayList<SensorTimer> sensorTimers) {
		this.routerMemory = routerMemory;
		this.sensorTimers = sensorTimers;
	}
	
	/**
	 * ������� ������� ������ ������
	 */
	public void run(){
		while(true) {
			try {
				int[] data = pollerMemory.getData();
				
				if(data != null) {
					String sendMessage = "";
					try {
					
						String temperaturePacket = getPacket(data[2], 16);
					
						String sensorIDPacket = getPacket(data[0], 16);
						
						sendMessage = sensorIDPacket+temperaturePacket;
						
						System.out.println("������������ �� ��������� �����: "+sendMessage+". ������ ������: "+sendMessage.length()+" ���.");
						try {
							BitsApplication.invertBit(sendMessage, 10);
							routerMemory.save(1, sendMessage);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						System.out.println("������ � ���������� ������ ��������: "+e.getMessage());
					}
				}
			}catch(Exception e) {
				System.out.println("������ ��������� ������ � ���������� ������: "+e.getMessage());
//				try {
//					Thread.sleep(1000);
//				} catch (Exception err) {
//					System.out.println("������ � ���������� ������ ��������: "+err.getMessage());
//				}
			}
		}
		
	}
	
	/**
	 * ������������ ������ �� 8 ���
	 * @param value �������� ������ ��� ������������ ������. �������� ��� �������� 0<=value<=255
	 * @return ��������� ��������, ���������� ����� �� ���
	 * @throws Exception ����� ������� �� ������
	 */
	public String getPacket(int value, int bits) throws Exception {
		String temperaturePacket = "";
		if(value>=0&&value<Math.pow(2, bits)) {
			temperaturePacket = Integer.toBinaryString(value);
			
			for(int i=temperaturePacket.length();i<bits;i++)
				temperaturePacket = "0"+temperaturePacket;
		}else {
			throw new Exception("�������� ������ "+Math.pow(2, bits)+". ������� ��������: "+value);
		}
		return temperaturePacket;
	}
	
	/**
	 * ��������� ������ ��������� �������� ��������
	 * @return ������ ��������� �������� ��������
	 */
	public List<SensorTimer> getSensorsList() {
		return sensorTimers;
	}

	public void setSensorsList(List<SensorTimer> threads) {
		this.sensorTimers = threads;
	}
	
	public void addTimer(SensorTimer timer) {
		if(timer.isInterrupted()) {
			timer.start();
		}
		sensorTimers.add(timer);
	}
	
	public TemperatureSensor getSensorById(int id) {
		return sensorTimers.get(id).getSensor();
	}
	
	public SensorTimer getSensorTimerById(int id) {
		return sensorTimers.get(id);
	}
	
	public void removeSensorTimerById(int id) {
		sensorTimers.remove(id);
	}
	
	public PollerMemory getPollerMemory() {
		return pollerMemory;
	}

	public void setPollerMemory(PollerMemory pollerMemory) {
		this.pollerMemory = pollerMemory;
	}
}
