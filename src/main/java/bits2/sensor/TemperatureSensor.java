package bits2.sensor;

import java.util.Random;

/**
 * ����� �������, ����������� �����������
 * @author olegk
 * @version 0.2
 * @since 26.02.2024
 * ���������: 
 * 29.02.2024 - ��������� �������� ������. ������� ��������� ������� � SensorTimer. 
 * TemperatureSensor ������ �� �������� �������. �������� ��������.
 * 22.03.2024 - ��������� ������ � �������� � ������ �������� ���������� �� �����������
 */
public class TemperatureSensor{
	/**
	 * ������� �������� �����������
	 */
	private int temperature;
	/**
	 * ����������� ������� �����������
	 */
	private int k;
	
	/**
	 * ����������� �������� �����������
	 */
	private int temperatureMIN;
	
	/**
	 * ������������ �������� �����������
	 */
	private int temperatureMAX;
	
	/**
	 * ����������� ������ �������
	 * @param temperature ��������� �������� �����������
	 * @param k ����������� ������� �����������
	 * @param temperatureMIN ����������� �������� �����������
	 * @param temperatureMAX ������������ �������� �����������
	 */
	public TemperatureSensor(int temperature, int k, int temperatureMIN, int temperatureMAX) {
		this.temperature = temperature;
		this.k = k;
		this.temperatureMIN = temperatureMIN;
		this.temperatureMAX = temperatureMAX;
		
	}
	
	public void updateTemperature() {
		Random rnd = new Random();
		
		int delta = rnd.nextInt(k);
			
		if((delta-temperature)>=temperatureMIN &&
			(delta+temperature)<=temperatureMAX) {
			
			if((new Random()).nextBoolean()) {
				temperature += delta;
			}else {
				temperature -= delta;
			}
		}
	}
	
	/**
	 * �������� �������� ������� �����������
	 * @return �������� ������� �����������
	 */
	public int getTemperature() {
		return this.temperature;
	}
	
	/**
	 * ������ �������� ����������� � ������ ������
	 * @param temperature ����� �������� �����������
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	/**
	 * �������� ����������� �������� �����������
	 * @return ����������� �������� �����������
	 */
	public int getTemperatureMIN() {
		return temperatureMIN;
	}

	/**
	 * ������ ����������� �������� �����������
	 * @param temperatureMIN ����� �������� ������ ������� ����������� (������).
	 */
	public void setTemperatureMIN(int temperatureMIN) {
		this.temperatureMIN = temperatureMIN;
	}

	/**
	 * �������� ����������� ��������� �������� �����������
	 * @return �������� ������� ������� �����������
	 */
	public int getTemperatureMAX() {
		return temperatureMAX;
	}

	/**
	 * ������ ����������� ��������� �������� �����������
	 * @return ����� �������� ������� ������� �����������
	 */
	public void setTemperatureMAX(int temperatureMAX) {
		this.temperatureMAX = temperatureMAX;
	}
}