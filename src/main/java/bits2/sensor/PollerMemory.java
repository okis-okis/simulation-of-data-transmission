package bits2.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс памяти устройства опроса
 * @author olegk
 * @version 0.1
 * @since 22.03.2024
 * @see Данные взяты с сайта:
 * https://www.geeksforgeeks.org/quicksort-using-random-pivoting/
 */
public class PollerMemory {
	
	/**
	 * Список, иммитирующий память устройства, где одна ячейка 
	 * содержит следующее:
	 * 0 - ID датчика (от 0 до 255)
	 * 1 - температура с датчика (от 0 до 255)
	 */
	private List<int[]> memory;
	
	/**
	 * Конструктор класса
	 */
	public PollerMemory() {
		memory = new ArrayList<int[]>();
	}
	
	/**
	 * Получение информации о заполненности памяти в битах
	 */
	public void getMemoryInfo() {
		if(memory.size()>0) {
			System.out.println("Память устройства опроса датчиков загружена на "+memory.size()*32+" бит");
		}
	}
	
	/**
	 * Добавить новые данные в память устройства
	 * @param sensorID - идентификатор датчика, с которого считаны данные
	 * @param priority - преоритет датчика
	 * @param temperature - считанная датчиком температура
	 */
	public void addData(int sensorID, int priority, int temperature) {
		int[] data = new int[] {sensorID, priority, temperature}; 
		memory.add(data);
		if(sensorID == 0) {
			System.out.println("В память устройства опроса добавлены новые данные. Датчик: "+sensorID+". Температура: "+temperature);
			getMemoryInfo();
		}
	}
	
	/**
	 * Получение размера загруженности памяти устройства в битах
	 * @return Загрузка памяти устройства в битах
	 */
	public int getSize() {
		int memoryState = 0;
		if(memory.size()>0) {
			memoryState = memory.size()*memory.get(0).length;
		}
		return memoryState;
	}
	
	/**
	 * Получение данных с памяти устройства опроса. 
	 * Данные в памяти устройства отсортированны по приоритету.
	 * @return Пакет с данными в виде int[], где
	 * 0-й индекс содержит идентификатор датчика
	 * 1-й индекс содержит температуру датчика
	 */
	public int[] getData() {
		try {
			if(memory.size()>0) {
//				try {
//					quickSort(memory, 0, memory.size() - 1);
//				}catch(Exception e) {
//					System.out.println("Ошибка сортировки данных");
//				}
				System.out.println("Размер памяти: "+memory.size());
				int[] result = memory.get(0);
				memory.remove(0);
				getMemoryInfo();
				return result;
			}
		}catch(Exception e) {
			System.out.println("Ошибка получения данных памяти опроса: "+e.getMessage());
		}
		return null;
	}
	
	/**
	 * Функция быстрой сортировки массива по приоритету
	 * @param arr массив с исходными данными
	 * @param low нижняя граница анализируемых данных
	 * @param high верхняя граница анализируемых данных
	 */
	public void quickSort(List<int[]> arr, int low, int high) {
		if (low < high) {
			int pi = partition(arr, low, high);

	        quickSort(arr, low, pi - 1);
	        quickSort(arr, pi + 1, high);
	   }
	}
	
	/**
	 * Эта функция принимает последний элемент в качестве основного,
	 * помещает элемент pivot в его правильное положение
	 * в отсортированном массиве и помещает все
	 * меньшие элементы (меньше, чем основное) слева от
	 * pivot, а все большие элементы справа
	 * от основного
	 * @param arr Сортируемый массив
	 * @param low Нижняя граница анализируемых данных
	 * @param high Верхняя граница анализируемых данных
	 * @return Следующий индекс
	 */
	private int partition(List<int[]> arr, int low, int high) {
		int pivot = arr.get(high)[1];
	    int i = (low - 1);
	    
	    for (int j = low; j < high; j++) {
	    	if (arr.get(j)[1] < pivot) {
	    		i++;

	            int[] temp = arr.get(i);
	            arr.set(i, arr.get(j));
	            arr.set(j, temp);
	        }
	    }


	    int[] temp = arr.get(i+1);
	    arr.set(i+1, arr.get(high));
	    arr.set(high, temp);

	    return i + 1;
	}
}
