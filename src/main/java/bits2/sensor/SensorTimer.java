package bits2.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для опроса датчика по заданным для него правилам
 * @author olegk
 * @version 0.1
 * @since 29.02.2024
 */
public class SensorTimer extends Thread{
	
	/**
	 * Опрашиваемый по правилам датчик
	 * @see TemperatureSensor
	 */
	private TemperatureSensor sensor;
	
	/**
	 * Память, в которую записываются значения от датчика
	 * @see RouterMemory
	 */
	private PollerMemory pollerMemory;
	
	/**
	 * Приоритет данного датчика, где 0 - самый высокий приоритет.
	 * Значения выше означают меньший приоритет.
	 */
	private int priority;
	
	/**
	 * Идентификатор устройства опроса
	 */
	private int timerID;
	
	/**
	 * Список правил для опроса датчика, где
	 * int[0] - минимальная температура
	 * int[1] - максимальная температура
	 * int[2] - задержка, с которой необходимо отправлять данные в память устройства
	 */
	List<int[]> rules; 
	
	/**
	 * Конструктор класса
	 * @param sensor Опрашиваемый датчик температуры
	 * @param routerMemory Память устройства, в которую будут записаны данные о температуре
	 */
	public SensorTimer(TemperatureSensor sensor, PollerMemory pollerMemory, int timerID, int priority) {
		rules = new ArrayList<int[]>();
		
		this.sensor = sensor;
		this.pollerMemory = pollerMemory;
		this.timerID = timerID;
		this.priority = priority;
	}
	
	/**
	 * Функция запуска работы потока
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
	 * Добавить новое правило
	 * @param minTemperature минимальная температура, при которой будет отправлено значение в память устройства
	 * @param maxTemperature максимальная температура, до которой будет отправленозначение в память устройства
	 * @param delay частота отправления данных в память устройства
	 */
	public void addRule(int minTemperature, int maxTemperature, int delay) {
		int[] rule  = new int[] {minTemperature, maxTemperature, delay};
		rules.add(rule);
	}
	
	/**
	 * Получить датчик температуры
	 * @return опрашиваемый датчик температуры
	 */
	public TemperatureSensor getSensor() {
		return sensor;
	}

	/**
	 * Задать новый датчик температуры
	 * @param sensor Новый опрашиваемый датчик температуры
	 */
	public void setSensor(TemperatureSensor sensor) {
		this.sensor = sensor;
	}
}
