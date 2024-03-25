package bits2.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * ����� ������ ���������� ������
 * @author olegk
 * @version 0.1
 * @since 22.03.2024
 * @see ������ ����� � �����:
 * https://www.geeksforgeeks.org/quicksort-using-random-pivoting/
 */
public class PollerMemory {
	
	/**
	 * ������, ������������ ������ ����������, ��� ���� ������ 
	 * �������� ���������:
	 * 0 - ID ������� (�� 0 �� 255)
	 * 1 - ����������� � ������� (�� 0 �� 255)
	 */
	private List<int[]> memory;
	
	/**
	 * ����������� ������
	 */
	public PollerMemory() {
		memory = new ArrayList<int[]>();
	}
	
	/**
	 * ��������� ���������� � ������������� ������ � �����
	 */
	public void getMemoryInfo() {
		if(memory.size()>0) {
			System.out.println("������ ���������� ������ �������� ��������� �� "+memory.size()*32+" ���");
		}
	}
	
	/**
	 * �������� ����� ������ � ������ ����������
	 * @param sensorID - ������������� �������, � �������� ������� ������
	 * @param priority - ��������� �������
	 * @param temperature - ��������� �������� �����������
	 */
	public void addData(int sensorID, int priority, int temperature) {
		int[] data = new int[] {sensorID, priority, temperature}; 
		memory.add(data);
		if(sensorID == 0) {
			System.out.println("� ������ ���������� ������ ��������� ����� ������. ������: "+sensorID+". �����������: "+temperature);
			getMemoryInfo();
		}
	}
	
	/**
	 * ��������� ������� ������������� ������ ���������� � �����
	 * @return �������� ������ ���������� � �����
	 */
	public int getSize() {
		int memoryState = 0;
		if(memory.size()>0) {
			memoryState = memory.size()*memory.get(0).length;
		}
		return memoryState;
	}
	
	/**
	 * ��������� ������ � ������ ���������� ������. 
	 * ������ � ������ ���������� �������������� �� ����������.
	 * @return ����� � ������� � ���� int[], ���
	 * 0-� ������ �������� ������������� �������
	 * 1-� ������ �������� ����������� �������
	 */
	public int[] getData() {
		try {
			if(memory.size()>0) {
//				try {
//					quickSort(memory, 0, memory.size() - 1);
//				}catch(Exception e) {
//					System.out.println("������ ���������� ������");
//				}
				System.out.println("������ ������: "+memory.size());
				int[] result = memory.get(0);
				memory.remove(0);
				getMemoryInfo();
				return result;
			}
		}catch(Exception e) {
			System.out.println("������ ��������� ������ ������ ������: "+e.getMessage());
		}
		return null;
	}
	
	/**
	 * ������� ������� ���������� ������� �� ����������
	 * @param arr ������ � ��������� �������
	 * @param low ������ ������� ������������� ������
	 * @param high ������� ������� ������������� ������
	 */
	public void quickSort(List<int[]> arr, int low, int high) {
		if (low < high) {
			int pi = partition(arr, low, high);

	        quickSort(arr, low, pi - 1);
	        quickSort(arr, pi + 1, high);
	   }
	}
	
	/**
	 * ��� ������� ��������� ��������� ������� � �������� ���������,
	 * �������� ������� pivot � ��� ���������� ���������
	 * � ��������������� ������� � �������� ���
	 * ������� �������� (������, ��� ��������) ����� ��
	 * pivot, � ��� ������� �������� ������
	 * �� ���������
	 * @param arr ����������� ������
	 * @param low ������ ������� ������������� ������
	 * @param high ������� ������� ������������� ������
	 * @return ��������� ������
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
