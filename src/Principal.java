import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import etherip.EtherNetIP;
import etherip.types.CIPData;
import etherip.util.Utils;


public class Principal {
	
    public static void main(String[] args) {
       // enip_server -v  TEMP=REAL
    	//Shell.execComando("/usr/lib/jvm/jdk1.7.0_79/jre/bin/java -jar /home/rgimenes/ethernetip-master.jar 192.168.39.11 0 tempxxx,emfxxx,pppmxxx,carb 1543.9,-233,9.999,1.3444")
    	String versao = "0.7";
        CIPData[] valores = null;
        String ip = null;
        int porta = 44818;
        int slotX = -1;
        String tags[] = null;//Nomes das tags
        String tags_aux[] = null;//Nomes das tags
        String tipos_tag[] = null; //tipo das tags real, int dint.
        String valores_str[]=null;
        EtherNetIP plc=null;
        String argumentos="";
        String invalidos = "";
        
        System.out.println("EtheIPClient:"+versao);
        
        try {
        	for (int i = 0; i < args.length; i++) {
        		argumentos+=args[i]+" ";
			}
            ip = args[0];
            porta = Integer.parseInt(args [1]);
            slotX = Integer.parseInt(args [2]);
            tags = args[3].split("\\,");
            tipos_tag = args[4].split("\\,");
            valores_str = args[5].split("\\,");
            valores = new CIPData[valores_str.length];
           
          /*  for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
            for (int i = 0; i < tags.length; i++) {
                System.out.println(tags[i]);
            }
            for (int i = 0; i < valores_str.length; i++) {
                System.out.println(valores_str[i]);
            }*/
        } catch (Exception e) {
        	StackTraceElement l = e.getStackTrace()[0];
			String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+e.getMessage() +""+ e.getStackTrace();
            Utils.escreveTxt("EthernetIPClienteErroSeparandoArgs.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\nValues sizes does not match\n"+erro+"\n"+argumentos+"\n",true);
            System.out.println("Error: Getting the args.");
            System.exit(1);
        }
        
        if(tags.length!=valores.length) {
            Utils.escreveTxt("EthernetIPClienteErroSeparandoArgs.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\nValues sizes does not match"+":"+argumentos,true);
            System.out.println("Error: Getting the args.");
            System.exit(1);
        }
        
        if(tags!=null&&valores_str!=null&&slotX!=-1&&ip!=null) {
            for (int i = 0; i < valores.length; i++) {
                //remove chars deixa só numero
                valores_str[i]=valores_str[i].replaceAll("[^0-9.-]", "");
                Number a = 99999.9;
                if(valores_str[i].equals("")||(!Utils.isNumeric(valores_str[i]))){
                		invalidos=invalidos+i+",";
                }else{
                	a = Double.parseDouble(valores_str[i]);
                }
                
                try {
                		valores[i]=new CIPData(retornaType(tipos_tag[i]), 1);
                		valores[i].set(0,a);
                } catch (IndexOutOfBoundsException e) {
                	StackTraceElement l = e.getStackTrace()[0];
        			String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+" "+e.getMessage() +" "+ e.toString();
                    Utils.escreveTxt("EthernetIPClienteErroIndex.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\n Index OUT: "+erro + " " +argumentos ,true);
                    System.out.println("Error: Handingle the data");
                } catch (Exception e) {
                	StackTraceElement l = e.getStackTrace()[0];
        			String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+" "+e.getMessage() +" "+ e.toString();
                    Utils.escreveTxt("EthernetIPClienteErroGeneral.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+"\n Exception: "+erro + " " + argumentos,true);
                    System.out.println("Error: Handingle/Parsing the data");
                }
            }
        }
        try {
        	 //remover invalidos
        	if(invalidos.length()>0){
	            String remover[] = invalidos.split("\\,");
	            int cntRemove=0;
	            for (int j = 0; j < remover.length; j++) {
	            	int x = 0;
	            		x = Integer.parseInt(remover[j]);
	            		x=x-cntRemove;
	            	tags =  removeElement(tags, x);
	            	tipos_tag =  removeElement(tipos_tag, x);
	            	valores =  removeElement(valores, x);
	            	cntRemove++;
	            }
        	}
        	if(valores!=null) {
                plc = new EtherNetIP(ip, slotX, porta);
                try {
					plc.connect();
					System.out.println("Connected: "+plc.toString());
				} catch (Exception e) {
					Writer writer = new StringWriter();
					e.printStackTrace(new PrintWriter(writer));
					StackTraceElement l = e.getStackTrace()[0];
					String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+" "+e.getMessage() +" "+ e.toString() + writer.toString(); ;
		            Utils.escreveTxt("EthernetIPClienteErrConnection.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+" Conection: "+erro +" "+ argumentos,true);
		            System.out.println("Error: Opening the Connection.");
					System.exit(1);
				}
                
                try {
					plc.writeTags(tags, valores);
					String aux="";
					String auxVal="";
					for (int j = 0; j < tags.length; j++) {
						aux+=tags[j]+" ";
						auxVal+=valores_str[j]+" ";
					}
					System.out.println("Data Sent:"+ aux + " values: " + auxVal);
				} catch (Exception e) {
					Writer writer = new StringWriter();
					e.printStackTrace(new PrintWriter(writer));
					StackTraceElement l = e.getStackTrace()[0];
					String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+" "+e.getMessage() +" "+ e.toString() + writer.toString(); ;
					Utils.escreveTxt("EthernetIPClienteErrWriteTags.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+" Conection: "+erro +" "+ argumentos,true);
					System.out.println("Error: Writing the values");
				}
				plc.close();
            }

        } catch (Exception e) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			StackTraceElement l = e.getStackTrace()[0];
			String erro = l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber()+" "+l.getFileName()+" "+e.getMessage() +" "+ e.toString() + writer.toString(); ;
            Utils.escreveTxt("EthernetIPClienteErrConexaoGeral.txt","\n"+Utils.pegarData2()+" "+Utils.pegarHora()+" Conection: "+erro +" "+ argumentos,true);
            System.out.println("Error: General " + erro);
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
    public static String[] removeElement(String[] original, int element){
    	String[] n = new String[original.length - 1];
        System.arraycopy(original, 0, n, 0, element );
        System.arraycopy(original, element+1, n, element, original.length - element-1);
        return n;
    }
    
    public static CIPData[] removeElement(CIPData[] original, int element){
    	CIPData[] n = new CIPData[original.length - 1];
        System.arraycopy(original, 0, n, 0, element );
        System.arraycopy(original, element+1, n, element, original.length - element-1);
        return n;
    }
    
}
