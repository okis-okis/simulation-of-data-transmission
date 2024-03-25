package bits2.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * ����� ��� ������ ������� �� �������� ��� ���� ��������
 * @author olegk
 * @version 0.1
 * @since 29.02.2024
 */
public class SensorTimer extends Thread{
	
	/**
	 * ������������ �� �������� ������
	 * @see TemperatureSensor
	 */
	private TemperatureSensor sensor;
	
	/**
	 * ������, � ������� ������������ �������� �� �������
	 * @see RouterMemory
	 */
	private PollerMemory pollerMemory;
	
	/**
	 * ��������� ������� �������, ��� 0 - ����� ������� ���������.
	 * �������� ���� �������� ������� ���������.
	 */
	private int priority;
	
	/**
	 * ������������� ���������� ������
	 */
	private int timerID;
	
	/**
	 * ������ ������ ��� ������ �������, ���
	 * int[0] - ����������� �����������
	 * int[1] - ������������ �����������
	 * int[2] - ��������, � ������� ���������� ���������� ������ � ������ ����������
	 */
	List<int[]> rules; 
	
	/**
	 * ����������� ������
	 * @param sensor ������������ ������ �����������
	 * @param routerMemory ������ ����������, � ������� ����� �������� ������ � �����������
	 */
	public SensorTimer(TemperatureSensor sensor, PollerMemory pollerMemory, int timerID, int priority) {
		rules = new ArrayList<int[]>();
		
		this.sensor = sensor;
		this.pollerMemory = pollerMemory;
		this.timerID = timerID;
		this.priority = priority;
	}
	
	/**
	 * ������� ������� ������ ������
	 */
	public void run(){
		int delay = 0;
		int delayTimer = 0;
		while(true) {
			sensor.updateTemperature();
			int temperature = sensor.getTemperature();
			
			for(int[] rule : rules) {
				if(temperature>=rule[0] && temperature<rule[1]) {
					delay = rule[2];
				}
			}
			
			if(delayTimer >= delay) {
				delayTimer = 0;
				pollerMemory.addData(timerID, priority, temperature);
			}else {
				delayTimer += 1000;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �������� ����� �������
	 * @param minTemperature ����������� �����������, ��� ������� ����� ���������� �������� � ������ ����������
	 * @param maxTemperature ������������ �����������, �� ������� ����� ������������������ � ������ ����������
	 * @param delay ������� ����������� ������ � ������ ����������
	 */
	public void addRule(int minTemperature, int maxTemperature, int delay) {
		int[] rule  = new int[] {minTemperature, maxTemperature, delay};
		rules.add(rule);
	}
	
	/**
	 * �������� ������ �����������
	 * @return ������������ ������ �����������
	 */
	public TemperatureSensor getSensor() {
		return sensor;
	}

	/**
	 * ������ ����� ������ �����������
	 * @param sensor ����� ������������ ������ �����������
	 */
	public void setSensor(TemperatureSensor sensor) {
		this.sensor = sensor;
	}
}
