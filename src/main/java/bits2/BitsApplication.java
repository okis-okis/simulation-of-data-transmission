package bits2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bits2.sensor.SensorPoller;
import bits2.router.RouterM;
import bits2.sensor.PollerMemory;
import bits2.sensor.SensorTimer;
import bits2.sensor.TemperatureSensor;

public class BitsApplication {
	
	private static List<RouterM> routers = new ArrayList<RouterM>();
	
	public static void main(String[] args) {
		System.out.println("�����: ��������� �.�.");
		System.out.println("������ ���������...");
		
		routers.add(new RouterM(0, 115200));
		routers.add(new RouterM(1, 115200));
		
		routers.get(0).addRouter(routers.get(1));
		routers.get(1).addRouter(routers.get(0));
		
		SensorPoller poller = new SensorPoller(routers.get(0).getMemory());
		poller.setPollerMemory(new PollerMemory());
		
		for(int i=0;i<5000;i++) {
			SensorTimer timer = new SensorTimer(new TemperatureSensor(20, 5, 1, 255), poller.getPollerMemory(), i, 1);
			timer.addRule(0, 30, 1000);
			timer.addRule(30, 60, 10000);
			timer.addRule(60, 256, 1000);
			timer.start();
		}
		poller.start();
		
		routers.get(0).start();
		routers.get(1).start();
		
		System.out.println("��� �������� ���������.");
		
		Scanner scan = new Scanner(System.in);
		scan.next();
	}
	
	/**
	 * ��������������� � ��� ��������.
	 * @param message ��������� ��� ��������. ������ ��������� ����� 16 ���.
	 * @return ������������� ����� �������� ���������. ����� 21 ���.
	 * @throws Exception ���������������� � ����������� ������, ���� ����� �� ����� 16 �����.
	 */
	public static String getHemmingCode(String message){
		String result = "";
		try {
			int counter = 0, degree = 0;
			int messageSize = 1+message.length()+(int)Math.sqrt(message.length());
			System.out.println("��� �������� ����� ����� ��������� ������: "+messageSize+" ���.");
			for(int i=0;i<messageSize;i++) {
				if(i==(Math.pow(2, degree)-1)) {
					result+="0";
					degree++;
				}else {
					result+=message.charAt(counter++);
				}
			}
			//result="00"+message.charAt(0)+"0"+message.substring(1, 4)+"0"+message.substring(4, 11)+"0"+message.substring(11, 16);
			
			for(int i=2;i<=32;i*=2)
				result = changeBit(result, i);
		}catch(Exception e) {
			System.out.println("������ ������������ ���� ��������");
		}
		return result;
	}
	
	/**
	 * �������� �������� ���� ��� ���� ��������
	 * @param message �������� ��������� (����� 21 ���)
	 * @param period ������, � ������� ������ ��������� ���� (��� 0-�� ���������� ���� ������ ���������� 2, ��� 1-�� - 4 � �.�.)
	 * @return ������ � ��������� ����������� �����
	 */
	public static String changeBit(String message, int period) {
		int counter = 0;
		int position = period/2 - 1;
		for(int i=position;i<message.length();i+=period)
			for(int j=0;j<period/2&&i+j<message.length();j++)
				if(message.charAt(i+j) == '1') {counter++;}
		if(counter%2==1) {
			message = message.substring(0, position)+"1"+message.substring(position+1, message.length());
		}
		return message;
	}
	
	public static String invertBit(String message, int position) throws Exception {
		
		if(position>message.length()) {
			throw new Exception("���������� ������� ������ ����� ���������!");
		}
		
		String result = message.substring(0, position)+(message.charAt(position)=='1'?'0':'1')+message.substring(position+1, message.length());
		
		return result;
	}

}
