import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WorkerTest {
	
	public static List lines=new ArrayList();
	
	

	public  List readCsv(String csvFileName) {
		List lines=new ArrayList();
		try {  
        	InputStreamReader isr = new InputStreamReader(new FileInputStream(csvFileName), "UTF-8");  
        	BufferedReader reader = new BufferedReader(isr);
            String line = null;  
            int index=0;  
            while((line=reader.readLine())!=null){  
            	lines.add(line);
                index++;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		return lines;
	}
	
	
	
	public  String getJsonParam(int order,int batchPrefix,String address,String csvFileName) {

		
		JSONObject workOrder=new JSONObject();
		int num=batchPrefix+order;
		String batch=num+"";
		workOrder.put("batch", batch);
		workOrder.put("orderNum", batch);
		workOrder.put("businessType", 1);
		workOrder.put("posName", "������������̨·5��Ժ12");
		workOrder.put("address", address+order);
		workOrder.put("cityCode", "110000");
		workOrder.put("cityName", "����");
		workOrder.put("employeeName", "��ӨӨ");
		workOrder.put("employeePhone", "18510688904");
		workOrder.put("businessType", "1");
		workOrder.put("houseSourceCode", num+"");
		//workOrder.put("district", "������");
		
		
		JSONArray productArrayJson=new JSONArray();
		
		
		if(lines.size()<1) {
			lines=readCsv(csvFileName);
		}
		for (int i = 0; i < lines.size()-1; i++) {
		   String line=(String) lines.get(i+1);
		   String [] item = line.split(",");
		   JSONObject product=new JSONObject();
		   product.put("jobOrderId", num);
		   product.put("orderDetailId", num+"00"+(i+54));
		   product.put("prodTypeId", item[0]);
		   product.put("posRank1", num);
		   product.put("posRank2", item[1]==""?null:item[1]);
		   product.put("posRank3", null);
		   product.put("posRank4", null);
		   product.put("posRank5", null);
		   product.put("areaTypeCode", item[2]==""?null:item[2]);
		   product.put("roomCode", "01");
		   product.put("roomId", "50000"+order);
		   product.put("posRankName",item[4]==""?null:item[4]);
		   product.put("roomName", item[5]==""?null:item[5]);
			   
		   productArrayJson.add(product);
			
		}
		workOrder.put("prodAndLocations", productArrayJson);
		
		return JSONObject.toJSONString(workOrder);
//		return "1231231";
		
		
	}
	
	public static void main(String[] args) {
		
		
		WorkerTest worker=new WorkerTest();
		for (int i = 0; i < 4; i++) {
			String jsonStr=worker.getJsonParam(i+1,20196000,"Z�Ѽ�ģ�����ר��","C:\\Users\\chun\\Downloads\\create-w\\create-w\\data2.csv");
			System.out.println("========================================");
			System.out.println(jsonStr);
		}
	}
	

}
