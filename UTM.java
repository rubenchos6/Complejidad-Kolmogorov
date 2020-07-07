/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ruben
 */
public class UTM {
    
    public static String[] NewTape(String TT, String Cinta, int N, int posInit)
    {
        int currentState=0; //Estado actual
        int iteraciones=0; //Contador para numero de iteraciones
        int[] base=new int[Cinta.length()]; //Cinta
        //Definimos la posición máxima y mínima de escritura
        int posMin=posInit; 
        int posMax=posInit;
        //Arreglo de Strings para regresar la informacion
        String[] resultado=new String[5];
        
        
        //Se llena el arreglo base con la cadena Cinta. 
        //En el arreglo es mas sencillo manejar posiciones y cambiar valores.
        for (int c=0;c<Cinta.length();c++){
            base[c]=Integer.parseInt(Cinta.charAt(c)+"");
        }
        
        String[] states=new String[TT.length()/8]; //TT
        
        //Arreglo que marcará los estados utilizados durante el procedimiento
        int[] nstates=new int[64];
        for (int c=0; c<64;c++){
            nstates[c]=0;
        }
        
        //Se lleba el arreglo states con las definiciones de estados, estas definiciones tienen 8 bits
        for (int c=0;c<TT.length()/8;c++){
            states[c]=TT.substring(c*8, c*8+8); //Se toma el substring de 8 bits, los parametros de substring son de tipo [a,b)
        }
        
       
        String aux,res; //variables auxiliares

        
        while (currentState!=63 && iteraciones<=N){ //while el estado final no sea HALT y el número de iteraciones no se pase del límite
            iteraciones++; //aumentamos iteraciones
            if(posInit<0 || posInit>=base.length){ //Si nos pasamos del tamaño de la cinta, nos salimos
                //System.out.println("Cinta con tamaño insuficiente");
                break;
            }
            //Anotar que ya pasamos por este estado
            nstates[currentState]=1;/////////////////////////////////////////////////////////////////////////
            //Estado se encontrara en states[currentState*2] y states[currentState*2+1]
            if(base[posInit]==0){
                aux=states[currentState*2];
            }
            else{
                aux=states[currentState*2+1];
            }
            //Escribimos el valor en la posInit
            base[posInit]=Integer.parseInt(aux.charAt(0)+"");
            //Si cambiamos de valor 0 a 1 o 1 a 0 la productividad se ve afectada
            //if(flag && aux.charAt(0)=='1'){productividad++;}
            //if(!flag && aux.charAt(0)=='0'){productividad--;}

            //Cambiamos la posicion del arreglo dependiendo del segundo caracter. 0 D, 1 I
            if(aux.charAt(1)=='0'){
                posInit++;
                if(posInit>posMax && posInit < base.length)
                    posMax=posInit;
            }
            else{
                posInit--;
                if(posInit<posMin && posInit >= 0)
                    posMin=posInit;
            }            
            //Calculamos el siguiente estado convirtiendo el número de base 2 a base 10
            currentState=Integer.parseInt(aux.substring(2,8),2);

        }
        //Guardamos una cadena de caracteres con los resultados
        res="";
        for (int i=0;i<base.length;i++ )
            res+=base[i];
        int numEstadosUsados=0;
        //Condiciones finales: Se alcanza HALT
        if(iteraciones<=N && posInit<base.length){
            //System.out.println("HALT state was reached");
            //System.out.println("Total of "+iteraciones+" transitions");
            resultado[2]="1";
        }
        //Condiciones finales: No se alcanza HALT
        else{
            //System.out.println("HALT state was not reached");
            resultado[2]="0";
        }
        
        //Se guarda una cadena binaria para marcar los estados utilizados
        String turingMachineAcotada="";
        for (int k=0;k<64;k++){
            if(nstates[k]==1){
                numEstadosUsados++; //Si se utilizó, aumentamos el num de estados usados
                turingMachineAcotada+="1";
                
            }
            else{
                turingMachineAcotada += "0";
            }
        }
        resultado[0]=res; //Cadena resultante
        resultado[1]=numEstadosUsados+""; //NumEstadosUsados
        resultado[2]=turingMachineAcotada; //Cadena binaria con edos usados
        resultado[3]=posMin+""; //Posicion minima alcanzada
        resultado[4]=posMax+""; //Posicion máxima alcanzada
        return resultado;
    }
    
    
    public static double[] Kolmogorov(String goal, String tM, String posInit, String posFinal){ 
        
        int matches; //Numero de valores encontrados en la cinta resultado de la MT
        int posMin=0;  //Posicion Mínima para el análisis
        
        //Aqui guarda la cadena que realmente escribió la TM
        String tMres=tM.substring(Integer.parseInt(posInit), Integer.parseInt(posFinal)+1); 

        //Inicialización de variables de posición
        int posInicialTM=0;
        int posInicialRes=0;
        int bestMatch=0;
           
        //Si la cinta de TM es mayor que la de la meta, se recorrerá una por una hasta encontrar el mayor numero de matches
        if(tMres.length()-goal.length()>=0){
            for(int i=0;i<tMres.length()-goal.length();i++){
                posInicialTM=i;
                matches=0;
                posInicialRes=0;
                while(posInicialRes<goal.length()){
                    if(tMres.charAt(posInicialTM)==goal.charAt(posInicialRes))
                        matches++;
                    posInicialTM++;
                    posInicialRes++;
                }
                //Actualizamos información si se logró un mejor resultado
                if( matches>bestMatch){
                    posMin = posInicialTM-goal.length(); //Guardamos donde empieza el valor
                    bestMatch=matches;
                }
                    
                if(bestMatch == goal.length()){
                    break;
                }
            }
        }
        //Si la cinta de TM es menor, solo checamos una vez
        else{    
            while(posInicialTM<tMres.length()){
                if(tMres.charAt(posInicialTM)==goal.charAt(posInicialRes))
                    bestMatch++;
                posInicialTM++;
                posInicialRes++;
            }
        }
        
        double[] res=new double[4];
        res[0]= bestMatch; //Numero maximo de matches
        res[1]=1-(double)bestMatch/goal.length(); //error
        res[2] = posMin; //Posicion donde se inicia
        res[3] = goal.length(); //Longitud del goal
        
        return res;
    }
    
    public static void imprime(String TT, String edos, int numEdos){ 
        int NumStates = TT.length()/ 16;
        int ix16,
        x0_I,
        x1_I,
        Estado;
        String x0_M,
        x1_M;
        //Se imprimen solo los estados utilizados
        System.out.println("Hay " + numEdos + " estados en la Maquina de Turing");
        System.out.println("***************************************");
        System.out.println("Complejidad de Kolmogorov: "+ numEdos*16);
        System.out.println("***************************************");
        System.out.println("");
        System.out.println(" EA | O | M | SE || O | M | SE |");
        System.out.println(" -------------------------------");
        for (int i = 0; i < NumStates; i++) {
            if(edos.charAt(i)=='1'){
                System.out.printf("%4.0f|", (float) i);
                ix16 = i * 16;
                x0_I = Integer.parseInt(TT.substring(ix16, ix16 + 1));
                x0_M = TT.substring(ix16 + 1, ix16 + 2);
                if (x0_M.equals("0")) x0_M = " R |";
                else x0_M = " L |";
                System.out.printf("%3.0f|" + x0_M, (float) x0_I);
                Estado = 0;
                for (int j = ix16 + 2; j < ix16 + 8; j++) {
                        Estado = Estado * 2;
                        if (TT.substring(j, j + 1).equals("1")) Estado++;
                        //endif
                } //endFor
                if (Estado == 63) System.out.print("   H||");
                else System.out.printf("%4.0f||", (float) Estado);
                //endif
                x1_I = Integer.parseInt(TT.substring(ix16 + 8, ix16 + 9));
                x1_M = TT.substring(ix16 + 9, ix16 + 10);
                if (x1_M.equals("0")) x1_M = " R |";
                else x1_M = " L |";
                System.out.printf("%3.0f|" + x1_M, (float) x1_I);
                Estado = 0;
                for (int j = ix16 + 10; j < ix16 + 16; j++) {
                        Estado = Estado * 2;
                        if (TT.substring(j, j + 1).equals("1")) Estado++;
                        //endif
                } //endFor
                if (Estado == 63) {
                        System.out.print("   H|\n");
                } else {
                        System.out.printf("%4.0f|\n", (float) Estado);
                } //endif
            }
        } //endFor
        
    }
    
}