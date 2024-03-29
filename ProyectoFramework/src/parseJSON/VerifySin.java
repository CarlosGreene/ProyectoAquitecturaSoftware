package parseJSON;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VerifySin{
    public JSONObject json_Object;
    public VerifySin(String path, String transaccion) throws FileNotFoundException, IOException, ParseException{
        try {    
            this.json_Object = jParse(path,transaccion);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public JSONObject jParse(String path, String transaccion) throws FileNotFoundException, IOException, ParseException{
        Object obj = new JSONParser().parse(new FileReader(path));
        String jsonTransaccion;
        boolean existRegistrar = false;
        boolean existUMLComponent = false;
        JSONArray je = (JSONArray) obj;
        Iterator ltrTransaction = je.iterator();

        while (ltrTransaction.hasNext()){

            JSONObject jo = (JSONObject) ltrTransaction.next();
            jsonTransaccion  = (String) jo.get("transaccion");  
            if(jsonTransaccion.equals(transaccion)){
                String componentsKeys[] = new String[2];
                String componentsValue[]= new String[2];
        
                Map componentes = ((Map)jo.get("componentes"));   
                if(componentes==null){
                    System.out.println("No esta definido componentes");
                    return null;
                }       
                Iterator<Map.Entry> itrComponents = componentes.entrySet().iterator();
                int i=0;
                while (itrComponents.hasNext()) { 
                    Map.Entry pair = itrComponents.next();
                    componentsKeys[i]= (String) pair.getKey();
                    componentsValue[i]= (String) pair.getValue();
                    i++;
                }

                Map registrar = ((Map)jo.get("registrar"));

                if(registrar==null){
                    System.out.println("No esta definido ANotificar");
                    return null;
                }
                
                Iterator<Map.Entry> itrNotificar = registrar.entrySet().iterator();
                while (itrNotificar.hasNext()) {
                    Map.Entry pair = itrNotificar.next();
                    existRegistrar = true;
                    if( ((String) pair.getKey()).equals("")) {
                        System.out.println("clase a registrar no definida");
                        return null;
                    }
                    if(((String) pair.getValue()).equals("")) {
                        System.out.println("El valor de "+ pair.getKey()+ " a registrar no definida");
                        return null;
                    }
                }

                Map dataBase = ((Map)jo.get("DataBase"));

                if(dataBase==null){
                    System.out.println("No esta definido La base de datos");
                    return null;
                }
                
                Iterator<Map.Entry> itrUML = dataBase.entrySet().iterator();
                while (itrUML.hasNext()) {
                    Map.Entry pair = itrUML.next();
                    existUMLComponent = true;
                    if( ((String) pair.getKey()).equals("")) {
                        System.out.println("Componente no definida");
                        return null;
                    }
                    if(((String) pair.getValue()).equals("")) {
                        System.out.println("El valor de "+ pair.getKey()+ " no definida");
                        return null;
                    }
                }
                
                if(syntax(componentsKeys,componentsValue)==false){
                    return null;
                }

                if(existRegistrar==false){
                    System.out.println("No se encontró una clase a registrar");
                    return null;
                }

                if(existUMLComponent==false){
                    System.out.println("No se encontró una clase a registrar");
                    return null;
                }

                return jo;
            }  
        } 
        System.out.println("No se encontró una transaccion con ese nombre");
       return null;
    }  
    public boolean syntax(String components[], String compoName[]){
        String controlador="";
        String modelo="";

        for(int i=0; i<components.length; i++){
            if(components[i].equals("Control")){
                if(compoName[i].equals("")){
                    System.out.println("No está definido un valor para control  en los componentes");
                }
                controlador = components[i];
            }
            if(components[i].equals("Modelo")){
                if(compoName[i].equals("")){
                    System.out.println("No está definido un valor para modelo en los componentes");
                }
                modelo = components[i];
            }
        }
        if(controlador.equals("")){
            System.out.println("No está definido un control en los componentes");
            return false;
        }else if(modelo.equals("")){
            System.out.println("No está definido un modelo en los componentes");
            return false;
        }else{
            return true;
        }

    }

    public JSONObject getJsonObject(){
        return this.json_Object;
    }
}
