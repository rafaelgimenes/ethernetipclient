import etherip.EtherNetIP;
import etherip.types.CIPData;

/**
 * 
 */

/**
 * @author Rafael Gimenes Leite <rafael.leite@vesuvius.com>
 * 4 de dez de 2019
 */
public class Teste1 {

	/**
	 * @param args
	 */
	//@SuppressWarnings("resource")
/*	public static void main(String[] args) {
		EtherNetIP plc=null;
		plc = new EtherNetIP("127.0.0.1", 0, 44818);
		
		try {
			plc.connect();
		  String stringID_LenTag = "Celox_West[0][0]"; // Tag for length
		    CIPData tagData;
			
				tagData = plc.readTag(stringID_LenTag);
			
		    int tagDataIDLength = tagData.getNumber(0).intValue();  // string length
		      
		    StringBuilder stringSb = new StringBuilder();  
		      
		    for(int i = 0; i < tagDataIDLength; i++)  
		    {  
		      CIPData tempData = plc.readTag(String.format("S1_ScreenID.DATA[%d]", i));  
		      stringSb.append(Character.toString ((char) tempData.getNumber(0).intValue()));  
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		    

	}
*/
}
