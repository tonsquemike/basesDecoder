package genGenetic;

import libs.ManejoER;
import java.util.TreeMap;

/**
 *
 * @author Jonathan Rojas Simón <ids_jonathan_rojas@hotmail.com>
 */
public class Base
{
    String[] lASCadenas;
    StringBuilder objBuilder = new StringBuilder();
    String gSSalto = "\r\n";

    /**
     * Base
     * Constructor que valida la secuencia de caracteres de las bases por
     * medio de una expresion regular
     * @param lSCadenaGral Es la cadena donde contiene las codificaciones
     */
    public Base(String lSCadenaGral)
    {
        objBuilder.append("La expresión es: "+ gSSalto + lSCadenaGral + gSSalto);
        String[] lASBases = lSCadenaGral.split(",");
        String lSRegex = "\\d+[{](\\d+([;]\\d+|[-]\\d+)*)[}]";
        ManejoER objManejoER  = new ManejoER(lSRegex);
        int lECounter = 0;
        for (int i = 0; i < lASBases.length; i++)
        {
            if (objManejoER.ExistER(lASBases[i]))
            {
                lECounter++;
            }
        }
        if (lECounter < lASBases.length)
        {
            objBuilder.append("La expresión introducida no es la correcta, revisar la expresión de bases" + gSSalto);
            System.exit(-1);
        }

        objBuilder.append("Las bases introducidas son correctas" + gSSalto);
        lASCadenas = lASBases;
    }

    /**
     * getMapBases
     * @return
     */
    public TreeMap<Integer, Integer> getMapBases()
    {
        TreeMap<Integer, Integer> objTreeMapStructure = new TreeMap<>();
        ManejoER objER_1 = new ManejoER("((\\d+)[{](\\d+([;]\\d+|[-]\\d+)*)[}])");
        for (int i = 0; i < lASCadenas.length; i++)
        {
            String lASContainer = "";
            int lEBase = 0;
            if (objER_1.ExistER(lASCadenas[i]))
            {
                lASContainer = objER_1.Grupo(3);
                lEBase = Integer.parseInt(objER_1.Grupo(2).trim());
            }

            String[] lASFormats = lASContainer.split(";");
            ManejoER objER_2 = new ManejoER("((\\d+)[-](\\d+))");
            for (int j = 0; j < lASFormats.length; j++)
            {
                int lEMin = 0, lEMax = 0;
                if (objER_2.ExistER(lASFormats[j]))
                {
                    lEMin = Integer.parseInt(objER_2.Grupo(2).trim());
                    lEMax = Integer.parseInt(objER_2.Grupo(3).trim());
                    int lETmp;
                    if (lEMin > lEMax)
                    {
                        lETmp = lEMin;
                        lEMin = lEMax;
                        lEMax = lETmp;
                    }
                    for (int k = lEMin; k <= lEMax; k++)
                    {
                        if (objTreeMapStructure.containsKey(k))
                        {
                            objBuilder.append("Conflictos en la generación de bases, revisar la expresion de entrada" + gSSalto);
                            objBuilder.append("Salida: -1" + gSSalto);
                            System.exit(-1);
                        }else
                        {
                            objTreeMapStructure.put(k, lEBase);
                        }
                    }
                } else
                {
                    if (objTreeMapStructure.containsKey(Integer.parseInt(lASFormats[j].trim())))
                    {
                        objBuilder.append("Conflictos en la generación de bases, revisar la expresion de entrada" + gSSalto);
                        objBuilder.append("Salida: -1" + gSSalto);
                        System.exit(-1);
                    } else
                    {
                        objTreeMapStructure.put(Integer.parseInt(lASFormats[j].trim()), lEBase);
                    }
                }
            }
        }

        objBuilder.append("Las bases generadas son correctas, las bases son: " + gSSalto);
        objBuilder.append(objTreeMapStructure.toString() + gSSalto);
        objBuilder.append("Número de genes (-NGEN) " + objTreeMapStructure.size() + gSSalto);

        return objTreeMapStructure;
    }
}
