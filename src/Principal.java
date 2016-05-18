import etherip.EtherNetIP;
import etherip.types.CIPData;


public class Principal {

    public static void main(String[] args) {
        //Shell.execComando("/usr/lib/jvm/jdk1.7.0_79/jre/bin/java -jar /home/rgimenes/ethernetip-master.jar 192.168.39.11 0 tempxxx,emfxxx,pppmxxx,carb 1543.9,-233,9.999,1.3444")
        CIPData[] valores = null;
        String ip = null;
        int slotX = -1;
        String tags[] = null;
        String valores_str[]=null;
        EtherNetIP plc=null;
        try {
            ip = args[0];
            slotX = Integer.parseInt(args [1]);
            tags = args[2].split("\\,");
            valores_str = args[3].split("\\,");
            valores = new CIPData[valores_str.length];
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
            for (int i = 0; i < tags.length; i++) {
                System.out.println(tags[i]);
            }
            for (int i = 0; i < valores_str.length; i++) {
                System.out.println(valores_str[i]);
            }
        } catch (Exception e) {
            System.out.println("Error reading args, setting variables");
        }
        if(tags!=null&&valores_str!=null&&slotX!=-1&&ip!=null) {
            for (int i = 0; i < valores.length; i++) {
                Number a = Double.parseDouble(valores_str[i]);
                try {
                    valores[i]=new CIPData(CIPData.Type.REAL, 1);
                    valores[i].set(0,a);
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                }
            }
        }
        
        
        try {
            if(valores!=null) {
                plc = new EtherNetIP(ip, slotX);
                plc.connect();
                plc.writeTags(tags, valores);
                plc.close();
            }
                        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
        }
        
       
        

    }

}
