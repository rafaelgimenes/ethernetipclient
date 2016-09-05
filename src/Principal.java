import etherip.EtherNetIP;
import etherip.types.CIPData;


public class Principal {

    public static void main(String[] args) {
        //Shell.execComando("/usr/lib/jvm/jdk1.7.0_79/jre/bin/java -jar /home/rgimenes/ethernetip-master.jar 192.168.39.11 0 tempxxx,emfxxx,pppmxxx,carb 1543.9,-233,9.999,1.3444")
        CIPData[] valores = null;
        String ip = null;
        int porta = 44818;
        int slotX = -1;
        String tags[] = null;//Nomes das tags
        String tipos_tag[] = null; //tipo das tags real, int dint.
        String valores_str[]=null;
        EtherNetIP plc=null;
        try {
            ip = args[0];
            porta = Integer.parseInt(args [1]);
            slotX = Integer.parseInt(args [2]);
            tags = args[3].split("\\,");
            tipos_tag = args[4].split("\\,");
            valores_str = args[5].split("\\,");
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
            Utils.escreveTxt("EthernetIPClienteErroSeparandoArgs.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\nValues sizes does not match",true);
            System.exit(1);
        }
        
        if(tags.length!=valores.length) {
            Utils.escreveTxt("EthernetIPClienteErroSeparandoArgs.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\nValues sizes does not match",true);
            System.exit(1);
        }
        
        if(tags!=null&&valores_str!=null&&slotX!=-1&&ip!=null) {
            for (int i = 0; i < valores.length; i++) {
                
                //remove chars deixa só numero
                valores_str[i]=valores_str[i].replaceAll("[^0-9.-]", "");
                Number a = Double.parseDouble(valores_str[i]);
                try {
                    valores[i]=new CIPData(retornaType(tipos_tag[i]), 1);
                    valores[i].set(0,a);
                } catch (IndexOutOfBoundsException e) {
                    Utils.escreveTxt("EthernetIPClienteErroIndex.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\n Index OUT: "+e.toString(),true);
                   
                } catch (Exception e) {
                    Utils.escreveTxt("EthernetIPClienteErroGeneral.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\n Exception: "+e.toString(),true);
                }
            }
        }
        try {
            if(valores!=null) {
                plc = new EtherNetIP(ip, slotX, porta);
                plc.connect();
                plc.writeTags(tags, valores);
                plc.close();
            }

        } catch (Exception e) {
            Utils.escreveTxt("EthernetIPClienteErrConexao.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\n Conection: "+e.toString(),true);
        }
    }
    
    private static CIPData.Type retornaType(String tipo) {
        if(tipo.equals("DINT")) {
            return CIPData.Type.DINT;
        }else if (tipo.equals("REAL")) {
            return CIPData.Type.REAL;
        }else if (tipo.equals("INT")) {
            return CIPData.Type.INT;
        }else if (tipo.equals("SINT")) {
            return CIPData.Type.SINT;
        }
        return null;
        
    }

}
