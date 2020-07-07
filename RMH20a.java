
import java.io.*;
import java.util.Scanner;

public class RMH20a {

    static PrintStream so = System.out;
    static String Resp;
    static int E, D, V, L, FN = 1, G, MM, iTmp, posMinRes = 0;
    /*	E 	- Bits para enteros
   *	D 	- Bits para decimales
   *	V 	- N�mero de variables
   *	L	- Longitud del genoma
   *	FN	- N�mero de funci�n a optimizar
   *	G	- N�mero de iteraciones/generaciones
   *	MM	- Minimiza(0)/Maximiza(1)
   *	iTmp - Temporal para actualizaci�n de par�metros
     */
    static double Norm, fTmp;
    /*
 *		LA SIGUIENTE VARIABLE REGULA EL N�MERO DE FUNCIONES DEFINIDAS
     */
    static int maxF = 33, minF = 1;
    static int maxN, minN = 1;
    static int maxE = 1023, minE = 0;
    static int maxD = 60, minD = 0;
    static int maxV = 50, minV = 1;
    static int maxG = 1000000000, minG = 1;
    static int maxM = 1, minM = 0;
//
    static double fitnessOfBest;	// Fitness del mejor individuo
    static double fitness;		// Fitness del individuo �nico
    static String genoma;			// Genoma del individuo
    static String genomaOfBest;	// Genoma del mejor individuo
    static double Var[] = new double[maxV];	// Arreglo (vector) de valores para c/u de las variables
    static BufferedReader Fbr, Kbr;
    static double[] auxK;
    static String goal = "";

    /*
   *		PASO 1:
   *			GENERA CADENA BINARIA INICIAL
   *
     */
    public static void IndividuoInicial() {
        /*
	 *Genera TOP of Hill aleatoriamente
         */
        genoma = "";
        for (int j = 1; j <= L; j++) {
            if (Math.random() < 0.5) {
                genoma = genoma + "0";
            } else {
                genoma = genoma + "1";
            }
            //endIf
        }//endFor
        genomaOfBest = genoma;
        if (MM == 0) {					// Minimize - worst case
            fitness = +1000000000;
            fitnessOfBest = +1000000000;
        } else {						// Maximize - worst case
            fitness = -1000000000;
            fitnessOfBest = -1000000000;
        }//endif
    }//endIndividuoInicial

    public static void IndividuoInicial2() {
        /*
	 *Genera TOP of Hill aleatoriamente
         */
        genoma = "";
        for (int j = 1; j <= L; j++) {
            if (Math.random() < 0.5) {
                genoma = genoma + "0";
            } else {
                genoma = genoma + "1";
            }
            //endIf
        }//endFor

    }//endIndividuoInicial

    /*
   *		PASO 2:
   *			PASA DE BINARIO SED A REAL
   *			A  i) Signo: "S" (0 -->"+"; 1 -->"-")
   *			  ii) Bits para entero "E"
   *			 iii) Bits para decimal "D"
   *
   *	A LA ENTRADA:
   *		"V" variables codificadas en L bits
   *		--> En el 0-�simo elemento del arreglo de genomas
   *
   *	A LA SALIDA:
   *		Valores reales en c/u de los V elementos de Var[V]
     */
    public static void GetFenotipoDelTOH() {
        double Var_k;
        String G = genoma;								// Trae el genoma para procesarlo
        int j = 0;
        for (int k = 0; k < V; k++) {							// Variable
            String s = G.substring(j, j + 1);				// Guarda el Signo que viene en el primer bit
            j++;
            if (G.substring(j, j + 1).equals("0")) {
                Var_k = 0; // Empieza con 0
            } else {
                Var_k = 1; // Empieza con 1
            }
            for (int l = 1; l < (E + D); l++) {
                Var_k = Var_k * 2;							// Multiplica el bit x2 para obtener su potencia
                j++;
                if (G.substring(j, j + 1).equals("1")) {
                    Var_k = Var_k + 1;						// Si bit!=0 suma 1 al parcial
                }			//endIf
            }//endFor									** Otro bit
            if (s.equals("1")) {
                Var_k = -Var_k;			// Recupera el signo y as�gnalo
            }
            Var[k] = Var_k / Norm;							//Normaliza para los decimales
            j++;
        }//endFor										** Otra variable
        return;
    }//endGetFenotipoDelTOH

    /*
   *		PASO 3:
   *			PREPARA EL C�LCULO DE C/U DE LAS FUNCIONES
   *			LAS FUNCIONES SE ENCUENTRAN EN LA CLASE "RMF"
     */
    public static void Evalua() throws Exception {
        double F = 0;

        if (FN != 23 || FN != 33) {
            GetFenotipoDelTOH();
        }
        switch (FN) {
            case 1:
                F = RMF.F01(Var[0], Var[1]);
                break;
            case 2:
                F = RMF.F02(Var[0]);
                break;
            case 3:
                F = RMF.F03(Var[0], Var[1]);
                break;
            case 4:
                F = RMF.F04(Var[0], Var[1]);
                break;
            case 5:
                F = RMF.F05(Var[0], Var[1]);
                break;
            case 6:
                F = RMF.F06(Var[0], Var[1], Var[2]);
                break;
            case 7:
                F = RMF.F07(Var[0], Var[1], Var[2]);
                break;
            case 8:
                F = RMF.F08(Var[0]);
                break;
            case 9:
                F = RMF.Rosenbrock(Var[0], Var[1]);
                break;
            case 10:
                F = RMF.Proy2(Var[0], Var[1]);
                break;
            case 11:
                F = RMF.Proy1(Var[0], Var[1]);
                break;
            case 12:
                F = RMF.EqsLin(Var[0], Var[1], Var[2]);
                break;
            case 13:
                F = RMF.EqsNoLin(Var[0], Var[1], Var[2]);
                break;
            case 14:
                F = RMF.Transporte(0);
                break;
            case 15:
                F = RMF.Sphere(Var[0], Var[1], Var[2],
                        Var[3], Var[4], Var[5],
                        Var[6], Var[7], Var[8],
                        Var[9]);
                break;
            case 16:
                F = RMF.F_X(Var[0]);
                break;
            case 17:
                F = RMF.F_XY(Var[0], Var[1]);
                break;
            // ****   INTERVALOS EN EL MINIBANCO ****
            case 18:
                F = RMF.IntMB(V, 0, G);
                break;
            case 19:
                F = RMF.FromWeb(Var[0], Var[1], Var[2]);
                break;
            case 20:
                F = RMF.RR1(genoma);
                break;
            case 21:
                F = RMF.Deceptive(genoma);
                break;
            case 22:
                F = RMF.F6V019V02(Var[0], Var[1]);
                break;
            case 23:
                F = RMF.DecepFromFile(genoma);
                break;
            case 24:
                F = RMF.DeJong1(Var[0], Var[1], Var[2]);
                break;
            case 25:
                F = RMF.WSphere(Var[0], Var[1], Var[2]);
                break;
            case 26:
                F = RMF.RotHE(Var[0], Var[1], Var[2]);
                break;
            case 27:
                F = RMF.SVM(Var[0], Var[1]);
                break;
            case 28:
                F = RMF.quintic(Var[0]);
                break;
            case 29:
                F = RMF.quintic2(Var[0]);
                break;
            case 30:
                F = RMF.DelLibroAGs(Var[0], Var[1]);
                break;
            case 31:
                F = RMF.CyC2015(Var[0], Var[1], Var[2]);
                break;
            case 32:
                F = RMF.BJ(Var[0], Var[1], Var[2], Var[3],
                        Var[4], Var[5], Var[6]);
                break;
            //Caso 33 es la Función de Kolmogorov, se llama con el goal y con el genoma
            case 33:
                auxK = RMF.Kolmogorov(genoma, goal);
                F = auxK[1];  //La funcion de Kolmogorov regresa muchas cosas, solo deseamos el error
                break;
        }//endSwitch
        fitness = F;
        //endFor
        return;
    }//endEvalua

    /*
   *		PASO 4:
   *			** MUTA **
   *			CAMBIA 0<-->1 UN BIT ELEGIDO ALEATORIAMENTE
     */
    public static void Muta() {
        // BIT A MUTAR
        int nBit = -1;
        while (nBit < 0 | nBit >= L) {
            nBit = (int) (Math.random() * L);
        }
        String G = genoma;
        String mBit = "0";	// Por default
        // 1) SI EL BIT EST� EN UN LUGAR INTERMEDIO
        while (true) {
            if (nBit != 0 & nBit != L - 1) {
                if (G.substring(nBit, nBit + 1).equals("0")) {
                    mBit = "1";
                }
                genoma = G.substring(0, nBit) + (mBit) + (G.substring(nBit + 1));
                break;
            }//endif
            // 2) SI EL BIT ES EL PRIMERO
            if (nBit == 0) {
                if (G.substring(0, 1).equals("0")) {
                    mBit = "1";
                }
                genoma = mBit + (G.substring(1));
                break;
            }//endif
            // 3) SI EL BIT ES EL �LTIMO
//	if (nBit==L-1){
            if (G.substring(L - 1).equals("0")) {
                mBit = "1";
            }
            genoma = G.substring(0, L - 1) + (mBit);
            break;
            //}endif
        }//endWhile
        return;
    }//endMuta

    /*		Selecciona seg�n se
 *      Minimice/Maximice
     */
    public static void Selecciona() {
        if (MM == 0) {					// Minimiza
            if (fitness < fitnessOfBest) {
                fitnessOfBest = fitness;
                //Imprimimos toda la información cada vez que se cambia a un error menor 
                if (FN == 33) {
                    posMinRes = (int) auxK[2];
                    so.println("BEST new error: " + fitnessOfBest + "     Tamaño: " + auxK[3] + "     Matches: " + auxK[0] + "     Pos Inicial: " + auxK[2]);
                }

                genomaOfBest = genoma;
            }//endIf
        } else {						// Maximiza
            if (fitness > fitnessOfBest) {
                fitnessOfBest = fitness;

                //so.println("NEW Fitness of best:     "+fitnessOfBest);
                genomaOfBest = genoma;
            }//endIf
        }//endIf
        return;
    }//endSelecciona

    public static void main(String[] args) throws Exception {
        BufferedReader Fbr, Kbr;
        Kbr = new BufferedReader(new InputStreamReader(System.in));
        CreaParams();							//Crea archivo si no existe
        GetParams();							//Lee parametros de archivo
        for (int loop = 1; loop <= 10000; loop++) {
            if (loop > 1) {
                so.println("\nOtra funcion (S/N)?");
                Resp = Kbr.readLine().toUpperCase();
                if (!Resp.equals("S")) {
                    so.println("\n*** FIN DE ESCALADOR ***\n");
                    return;
                }//endIf		
            }//endIf
            Modify();								//Modifica valores desde el escritorio
            Norm = Math.pow(2, D);					// Calcula el factor de Normalizaci�n
            L = V * (1 + E + D);							// Long. del individuo
            UpdateParams();						//Graba en archivo
            if (FN == 33) {
                lectura();
            }

            /*
 *		EMPIEZA EL ESCALADOR
             */
            int Gtemp = G;
            IndividuoInicial();					//Genera el individuo inicial
            GetFenotipoDelTOH();					//Decodifica la priemera cadena
            //so.println(genoma);
            Evalua();								//Evalua al individuo
            int cuenta = 0;
            for (int i = 0; i < G; i++) {
                Muta();								//Muta
                GetFenotipoDelTOH();				//Decodifica la i-�sima cadena
                Evalua();							//Evalua
                Selecciona();						//Selecciona al mejor
                //so.println(cuenta);
                if (cuenta == 500) {					//Reporta cada 5,000 iteraciones
                    //String _i=N2bS.N2S((float)i/1000000,9,6,0);
                    double j = (double) i / 1000000;
                    //so.print(_i+" MegaIters\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
                    so.println(j + " MegaIters");
                    cuenta = 1;
                } else {
                    cuenta++;
                }
                //endif
            }//endFor
            //Imprime resultados
            String sOptimo = "\nÓptimo:    " + N2bS.N2S((float) fitnessOfBest, 15, 7, 1);
            so.println(sOptimo);
            if (FN == 33) {
                String Cinta = "";
                int N = 1000000,
                        posInit = 5000;
                for (int i = 0; i < 10000; i++) {
                    Cinta = Cinta + "0";
                }
                String[] out = UTM.NewTape(genomaOfBest, Cinta, N, posInit);
                UTM.imprime(genomaOfBest, out[2], Integer.parseInt(out[1]));
                so.println("Cadena esperada: " + goal);
                try {
                    so.println("Cadena encontrada: " + out[0].substring(Integer.parseInt(out[3]) + posMinRes, Integer.parseInt(out[3]) + goal.length() + posMinRes ));
                } catch (Exception e) {
                    so.println("Cadena encontrada: " + out[0].substring(Integer.parseInt(out[3]) + posMinRes, out[0].length()));
                }
            }
            genoma = genomaOfBest;
            GetFenotipoDelTOH();
            /*for (int i=0;i<V;i++){
			String Var_i=N2bS.N2S((float)Var[i],15,7,1);
	  		so.println("Variable "+i+": "+Var_i);
	  }//endFor*/
            G = Gtemp;
        }//endLoop

        System.out.println(genomaOfBest);
    }//endMain

    public static void CreaParams() throws Exception {
        try {
            Fbr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("RMHParams.dat"))));
        }//endTry
        catch (Exception e) {
            PrintStream Fps = new PrintStream(new FileOutputStream(new File("RMHParams.dat")));
            Fps.println("1");	//1) Funcion
            Fps.println("4");	//2) Bits para Enteros
            Fps.println("25");	//3) Bits para Decimales
            Fps.println("2");	//4) Variables
            Fps.println("100");	//5) Iteraciones
            Fps.println("0");	//6) Minimiza
        }//endCatch
    }//endCreaParams

    public static void GetParams() throws Exception {
        Fbr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("RMHParams.dat"))));
        FN = Integer.parseInt(Fbr.readLine());
        E = Integer.parseInt(Fbr.readLine());
        D = Integer.parseInt(Fbr.readLine());
        V = Integer.parseInt(Fbr.readLine());
        G = Integer.parseInt(Fbr.readLine());
        MM = Integer.parseInt(Fbr.readLine());
    }//endGetParams

    public static void UpdateParams() throws Exception {
        PrintStream Fps = new PrintStream(new FileOutputStream(new File("RMHParams.dat")));
        Fps.println(FN);			//1) Funcion
        Fps.println(E);				//2) Bits para Enteros
        Fps.println(D);				//3) Bits para Decimales
        Fps.println(V);				//4) Variables
        Fps.println(G);				//5) Iteraciones
        Fps.println(MM);			//6) Minimiza
    }//endUpdateParams

    public static void DispParams() throws Exception {
        so.println();
        so.println("1) Funcion a optimizar:     " + FN);
        so.println("2) Bits para enteros:       " + E);
        so.println("3) Bits para decimales:     " + D);
        so.println("4) Numero de variables:     " + V);
        so.println("** Long. del genoma:        " + L);
        so.println("5) Numero de iteraciones:   " + G);
        so.println("6) Minimiza[0]/Maximiza[1]: " + MM);
    }//endDispParams

    public static boolean CheckParams(int Opcion) {
        switch (Opcion) {
            case 1: {
                FN = iTmp;
                if (FN < minF | FN > maxF) {
                    return false;
                }
                break;
            }
            case 2: {
                E = iTmp;
                if (E < minE | E > maxE) {
                    return false;
                }
                break;
            }
            case 3: {
                D = iTmp;
                if (D < minD | D > maxD) {
                    return false;
                }
                break;
            }
            case 4: {
                V = iTmp;
                if (V < minV | V > maxV) {
                    return false;
                }
                break;
            }
            case 5: {
                G = iTmp;
                if (G < minG | G > maxG) {
                    return false;
                }
                break;
            }
            case 6: {
                MM = iTmp;
                if (MM < minM | MM > maxM) {
                    return false;
                }
                break;
            }
        }//endSwitch 
        return true;
    }//endCheckParams

    public static void Modify() throws Exception {
        Kbr = new BufferedReader(new InputStreamReader(System.in));
        String Resp;
        while (true) {
            Norm = Math.pow(2, D);					// Calcula el factor de Normalizaci�n
            L = V * (1 + E + D);						// Long. del individuo
            DispParams();
            so.print("\nModificar (S/N)? ");
            Resp = Kbr.readLine().toUpperCase();
            if (!Resp.equals("S") & !Resp.equals("N")) {
                continue;
            }
            if (Resp.equals("N")) {
                return;
            }
            if (Resp.equals("S")) {
                int tFN = FN, tE = E, tD = D, tV = V, tG = G, tMM = MM;	//Registra temporales
                while (true) {
                    so.print("Opcion No:       ");
                    int Opcion;
                    try {
                        Opcion = Integer.parseInt(Kbr.readLine());
                    }//endTry
                    catch (Exception e) {
                        continue;							//No tecle� un d�gito
                    }//endCatch
                    if (Opcion < 1 | Opcion > 6) //No est� en rango
                    {
                        continue;
                    }
                    //endIf
                    so.print("Nuevo valor:     ");
                    iTmp = Integer.parseInt(Kbr.readLine());	//Par�metrp de CheckParams
                    boolean OK = CheckParams(Opcion);
                    if (!OK) {
                        FN = tFN;
                        E = tE;
                        D = tD;
                        V = tV;
                        G = tG;
                        MM = tMM;
                        so.println("Error en la opcion # " + Opcion);
                        continue;
                    }//endIf
                    break;
                }//endWhile
            }//endIf
        }//endWhile
    }//endModify

    public static void lectura() throws IOException {
        //Lee el archivo para el calculo de Kolmogorov
        BufferedReader Kbr;
        Kbr = new BufferedReader(new InputStreamReader(System.in));
        String sResp;
        RandomAccessFile Datos = null;
        String aux = "";
        String aux2 = "";
        String aux3 = "";
        Boolean flag = false;

        int i;
        boolean ask = false;
        while (true) {
            if (ask) { // No preguntes la primera vez
                System.out.println("\nDesea leer otro archivo?");
                System.out.println("\"S\" para continuar; otro para terminar...)");
                sResp = Kbr.readLine().toUpperCase();
                if (!sResp.equals("S")) {
                    break;
                }
                //endIf
            } else {
                ask = true; // Pregunta de la segunda en adelante
            } //endif
            System.out.println("Deme el nombre del archivo de datos que quiere leer:");
            String FName = Kbr.readLine().toUpperCase();
            try {
                //Abre el documento y lo convierte de ASCII a binario si encuentra un caracter distinto de 0 ó 1
                File file = new File(FName);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    aux += sc.nextLine();
                }
                for (int j = 0; j < aux.length(); j++) {
                    aux2 += aux.charAt(j);
                    aux3 += Integer.toBinaryString(Character.getNumericValue(aux.charAt(j)));
                    if (aux.charAt(j) != '0' || aux.charAt(j) != '1') {
                        flag = true;
                    }
                }
                //Actualiza el valor del flag
                if (flag) {
                    goal = aux3;
                } else {
                    goal = aux2;
                }
                //System.out.println(goal);
                break;
            } //endTry
            catch (Exception e1) {
                System.out.println("No se encontro \"" + FName + "\"");
            } //endCatch
        }

    }
}
	 //endClass
