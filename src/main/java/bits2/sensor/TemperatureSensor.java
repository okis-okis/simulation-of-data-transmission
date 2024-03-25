package bits2.sensor;

import java.util.Random;

/**
 * Класс датчика, передающего температуру
 * @author olegk
 * @version 0.2
 * @since 26.02.2024
 * Изменения: 
 * 29.02.2024 - Изменение название класса. Перенос некоторых функций в SensorTimer. 
 * TemperatureSensor больше не является потоком. Удаление задержки.
 * 22.03.2024 - Внедрение модуля с датчиком в модель передачи информации на предприятии
 */
public class TemperatureSensor{
	/**
	 * Текущее значение температуры
	 */
	private int temperature;
	/**
	 * Коэффициент расчёта температуры
	 */
	private int k;
	
	/**
	 * Минимальное значение температуры
	 */
	private int temperatureMIN;
	
	/**
	 * Максимальное значение температуры
	 */
	private int temperatureMAX;
	
	/**
	 * Конструктор класса датчика
	 * @param temperature Начальное значение температуры
	 * @param k Коэффициент расчёта температуры
	 * @param temperatureMIN Минимальное значение температуры
	 * @param temperatureMAX Максимальное значение температуры
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
	 * Получить значение текущей температуры
	 * @return Значение текущей температуры
	 */
	public int getTemperature() {
		return this.temperature;
	}
	
	/**
	 * Задать значение температуры в данный момент
	 * @param temperature Новое значение температуры
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	/**
	 * Получить минимальное значение температуры
	 * @return Минимальное значение температуры
	 */
	public int getTemperatureMIN() {
		return temperatureMIN;
	}

	/**
	 * Задать минимальное значение температуры
	 * @param temperatureMIN Новое значение нижней границы температуры (предел).
	 */
	public void setTemperatureMIN(int temperatureMIN) {
		this.temperatureMIN = temperatureMIN;
	}

	/**
	 * Получить максимально возможное значение температуры
	 * @return Значение верхней границы температуры
	 */
	public int getTemperatureMAX() {
		return temperatureMAX;
	}

	/**
	 * Задать максимально возможное значение температуры
	 * @return Новое значение верхней границы температуры
	 */
	public void setTemperatureMAX(int temperatureMAX) {
		this.temperatureMAX = temperatureMAX;
	}
}