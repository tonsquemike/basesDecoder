package genGenetic;

import libs.ManejoER;
import java.util.TreeMap;
/**
 *
 * @author Miguel García-Calderón <mgcalderon@outlook.es>
 */
public class BaseIncrementos {
    String Cadena;
    StringBuilder objBuilder = new StringBuilder();
    String gSSalto = "\r\n";
    int tamanioIndividuo;
    int[] rangosNegativos;
    String regex = "(?<=INCREMENTS:)(((\\d+)|(\\[(-?\\d+)[-](-?\\d+)\\]));?)+";
    TreeMap<Integer, Integer> objTreeMapStructureNegativos;
    private TreeMap<Integer, Integer> mergedMaps;

    /**
     * Base
     * Constructor que valida la secuencia de caracteres de las bases por
     * medio de una expresion regular
     * @param lSCadenaGral Es la cadena donde contiene las codificaciones
     */
    public BaseIncrementos(String lSCadenaGral, int tamanioIndividuo)
    {
        this.Cadena = lSCadenaGral;
        this.tamanioIndividuo = tamanioIndividuo;
        objBuilder.append("La expresión es: "+ gSSalto + lSCadenaGral + gSSalto);
        var objER_1 = new ManejoER(regex);

        var lASContainer = "";
        var lEBase = 0;

        if (objER_1.ExistER(Cadena))
        {
            lASContainer = objER_1.Grupo(0);
        }
        var bases = lASContainer.split(";");

        if(tamanioIndividuo%bases.length!=0){
            int repeticiones = tamanioIndividuo/bases.length;
            objBuilder.append("Debes tener (NoGenes="+tamanioIndividuo+")%N==0 y tienes "+tamanioIndividuo+"%"+repeticiones+"=="+(tamanioIndividuo%bases.length) + gSSalto);
            System.exit(-1);
        }
        if (!objER_1.ExistER(Cadena))
        {
            objBuilder.append("La expresión introducida no es la correcta, revisar la expresión de bases" + gSSalto);
            System.exit(-1);
        }
    }

    /**
     * getMapBases
     * @return
     */
    public TreeMap<Integer, Integer> getMapBases()
    {
        var objER_1 = new ManejoER(regex);
        var lASContainer = "";
        var lEBase = 0;

        if (objER_1.ExistER(Cadena))
        {
            lASContainer = objER_1.Grupo(0);
        }

        var rangos = new ManejoER("(\\[(-?\\d+)[-](-?\\d+)\\])");

        var bases = lASContainer.split(";");
        var basesInt = new int[bases.length];

        rangosNegativos = new int[basesInt.length];

        var min = 0;
        var max = 1;
        for (var i = 0; i < basesInt.length; i++) {
            //REGEX (\[(-?\d+)[-](-?\d+)\])
            //si el token actual tiene un rango, en el TreeMap se va a guardar el valor positivo y en un ArraList se va
            // a guardar el valor negativo del rango
            if(rangos.ExistER(bases[i])){
                min = Integer.parseInt(rangos.Grupo(2));
                max = Integer.parseInt(rangos.Grupo(3));

                if(min>max){
                    objBuilder.append("Los valores de los rangos son incorrectos, el valor del lado derecho ("+max+") debe ser mayor al lado izquierdo ("+min+")");
                    System.exit(-1);
                }

                rangosNegativos[i] = min;
                basesInt[i]        = max;
            }
            else {
                try {
                    basesInt[i] = Integer.parseInt(bases[i]);
                }catch (Exception e){e.printStackTrace();
                objBuilder.append("Codificacion incorrecta de los rangos, revisa: "+bases[i]);}
            }
        }

        if(rangosNegativos!=null){
            this.objTreeMapStructureNegativos = new TreeMap<>();
            this.objTreeMapStructureNegativos = arrayToTreeMap(tamanioIndividuo, bases.length, rangosNegativos);
            setMergedMaps(arrayToTreeMap(tamanioIndividuo, bases.length, basesInt));
            getMergedMaps().putAll(this.objTreeMapStructureNegativos);
        }

        return arrayToTreeMap(tamanioIndividuo, bases.length, basesInt);
    }
    public TreeMap<Integer, Integer> arrayToTreeMap(int tamanioIndividuo, int basesLenght, int[]arregloAConvertir){
        TreeMap<Integer, Integer> objTreeMapStructure = new TreeMap<>();

        for (int i = 0, indexBase = 0; i < this.tamanioIndividuo; i++, indexBase++) {
            if(indexBase==basesLenght)
                indexBase = 0;
            objTreeMapStructure.put(i, arregloAConvertir[indexBase]);
        }
        return objTreeMapStructure;
    }
    public int[] getRangosNegativos(){
        return this.rangosNegativos;
    }
    public TreeMap<Integer, Integer> getObjTreeMapStructureNegativos(){
        return this.objTreeMapStructureNegativos;
    }

    public TreeMap<Integer, Integer> getMergedMaps() {
        return mergedMaps;
    }

    public void setMergedMaps(TreeMap<Integer, Integer> mergedMaps) {
        this.mergedMaps = mergedMaps;
    }
}
