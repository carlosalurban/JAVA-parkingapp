/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eac4p5;

/**
 *
 * @author Carlos
 */
import java.util.Scanner;

public class AppEAC4 {

    static final short ID_MATRICULA = 0;
    static final short ID_HORA = 1;
    private static final int MATRICULA_LENGTH = 7;
    private static final int MAX_COTXES = 10;
    static final String ENTRADA = "1";
    static final String SORTIDA = "2";
    static final String OCUPACIO = "3";
    static final String SORTIDA_PROGRAMA = "4";
    public String resposta = null;
    /**
     * En aquest aplicatiu l`hora d`entrada es a les 08:00 i la de sortida a
     * les 22:00 El preu d`hora s`estableix en minuts al passar el String a int
     * per tant es fa una operació simple per calcular les hores en minuts el
     * que siginifica que el minut mìnim serà el 480 i el mìnut maxim el 1320
     * sent aquest ultim les 22:00, mes endevant es veu comentat el metodes de
     * la transformació
     */
    private static final int HORA_MINIMA = 480;
    private static final int HORA_MAXIMA = 1320;
    private static final double TARIFA = 0.045;
    String[][] cotxesAparcats = {
        {"3130JPG", "11:10"},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""},
        {"", ""}
    };
    int numCotxesAparcats = 1;

    String[] horesEntradaAleatories = {
        "08:00",
        "08:30",
        "09:00",
        "09:30",
        "10:00",
        "10:30",
        "11:00",
        "11:30",
        "12:00",
        "12:30",
        "13:00",
        "13:30",
        "14:00",
        "14:30",
        "15:00",
        "15:30",
        "16:00",
        "16:30",
        "17:00",
        "17:30",
        "18:00",
        "18:30",
        "19:00",
        "19:30",
        "20:00",
        "20:30",
        "21:00",
        "21:30",
        "22:00",};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AppEAC4 prg = new AppEAC4();
        prg.inici();
    }

    private void inici() {
        menu();
    }

    /**
     * METODES GENERALS
     */
    private void sortida() {
        String matricula = demanarMatricula();
        int posicio = cercarPosicio(matricula);
        pagament(posicio);
        eliminarVehicleArray(posicio);
        menu();
    }

    private void entrada() {
        entrarCotxes();
        menu();
    }

    /**
     * METODES DE DEMANDA DE POSICIÓ I HORA D`ENTRADA
     */
    public String demanarMatricula() {
        mostrarPregunta();
        resposta = esperarResposta();
        boolean respostaCerca = cercarMatricula(resposta);
        if (!respostaCerca) {
            repetirDemanda();
        }
        return resposta;
    }

    public void mostrarPregunta() {
        String pregunta = "Quina es la teva matrícula?\n";
        System.out.print(pregunta);
    }

    private String esperarResposta() {
        Scanner lector = new Scanner(System.in);
        boolean lecturaOk = false;
        String matricula = null;
        String response = null;
        do {
            matricula = lector.next();
            lector.nextLine();
            if (matricula.length() == MATRICULA_LENGTH) {
                matricula = matricula.toUpperCase();
                response = matricula;
                lecturaOk = true;
            } else {
                System.out.print("\nLa matrícula ha de tindre " + MATRICULA_LENGTH + " digits\n");
                matricula = null;
            }
        } while (!lecturaOk);
        return response;
    }

    private boolean cercarMatricula(String respostaClient) {
        boolean cotxeAparcat = false;
        for (int i = 0; i < cotxesAparcats.length; i++) {
            if (cotxesAparcats[i][ID_MATRICULA].equals(respostaClient)) {
                System.out.print("\nLa matricula " + respostaClient + " es correcta\n");
                cotxeAparcat = true;
            }
        }
        if (!cotxeAparcat) {
            System.out.print("\nLa matricula no es correcta " + respostaClient + " no es troba dins del parking\n");
        }
        return cotxeAparcat;
    }

    public String repetirDemanda() {

        return demanarMatricula();
    }

    public int cercarPosicio(String resposta) {
        int posicio = recorrerFiles(resposta);
        return posicio;
    }

    private int recorrerFiles(String respostaClient) {
        int posicio = -1;
        for (int i = 0; i < cotxesAparcats.length; i++) {
            if (cotxesAparcats[i][ID_MATRICULA].equals(respostaClient)) {
                posicio = i;
                System.out.print("El vehice es troba al aparcament nùmero: " + posicio + " del Parking\n");
                System.out.print("La seva hora d`entrada ha sigut a les: " + cotxesAparcats[i][ID_HORA] + "\n");
            }
        }
        if (posicio == -1) {
            System.out.print("\nLa matricula no es correcta " + respostaClient + " no es troba dins del parking\n");
        }
        return posicio;
    }

    /**
     * METODES DE PAGAMENT I SORTIDA
     */
    private void pagament(int posicio) {
        int minutsFinal = 0;
        int minutsEntrada = minutsEntradaTotals(posicio);

        demanarHoraSortida();
        int minutsSortida = esperarHoraSortida(minutsEntrada);
        double preu = minutsPerPagar(minutsEntrada, minutsSortida);
        renderPagament(preu);
    }

    private void renderPagament(double preuFinal) {
        Scanner lector = new Scanner(System.in);
        System.out.print("El preu final a pagar es de: " + preuFinal + "€\n");
        System.out.print("Per realitzar el pagament premi Enter");
        lector.nextLine();
        System.out.print("\nPagament de: " + preuFinal + " realitzat\n");
    }

    /**
     * Metodes del tractament d`hores
     */
    public void demanarHoraSortida() {
        String preguntaHoraSortida = "Indica l`hora de sortida del vehicle\n";
        System.out.print(preguntaHoraSortida);
    }

    private int esperarHoraSortida(int horaEntrada) {
        Scanner lector = new Scanner(System.in);
        int response = 0;
        boolean lecturaOk = false;
        String horaSortida = null;
        do {
            horaSortida = lector.next();
            lector.nextLine();
            int conversorHoresSortida = minutsSortidaTotals(horaSortida);
            if (conversorHoresSortida >= HORA_MINIMA && conversorHoresSortida <= HORA_MAXIMA && horaEntrada < conversorHoresSortida) {
                response = conversorHoresSortida;
                lecturaOk = true;
            } else {
                System.out.print("L`hora introduida " + horaSortida + " no es correcta.\n");
                System.out.print("L`hora ha de ser compresa entre les 08:00 i les 22:00 i posterior a l`hora d`entrada\n");
                horaSortida = null;
            }
        } while (!lecturaOk);

        return response;
    }

    private String horaPosicio(int posicio) {
        String horaEntrada = cotxesAparcats[posicio][ID_HORA];
        return horaEntrada;
    }

    private int conversorHores(String hora) {
        int horaEnMinuts = 0;
        String[] vectorHoraEntrada = hora.split(":");
        int hores = (Integer.parseInt(vectorHoraEntrada[0]));
        horaEnMinuts = hores * 60;
        return horaEnMinuts;
    }

    private int conversorMinuts(String hora) {
        int minutsEnter = 0;
        String[] vectorMinutsEntrada = hora.split(":");
        int minuts = (Integer.parseInt(vectorMinutsEntrada[1]));
        minutsEnter = minuts;
        return minutsEnter;
    }

    private int calculadorMinuts(int hora, int minuts) {
        int minutsTotals = hora + minuts;
        return minutsTotals;
    }

    private int minutsEntradaTotals(int posicio) {
        String hora = horaPosicio(posicio);
        int horaEnter = conversorHores(hora);
        int minutsEnter = conversorMinuts(hora);
        int minutsEntrada = calculadorMinuts(horaEnter, minutsEnter);
        return minutsEntrada;
    }

    private int minutsSortidaTotals(String hora) {
        int horaEnter = conversorHores(hora);
        int minutsEnter = conversorMinuts(hora);
        int minutsSortida = calculadorMinuts(horaEnter, minutsEnter);
        return minutsSortida;
    }

    private double minutsPerPagar(int minutsEntrada, int minutsSortida) {
        int minutsFinals = minutsSortida - minutsEntrada;
        System.out.print("El vehice ha estat estacionat un total de: " + minutsFinals + " minuts\n");
        double pagament = minutsFinals * TARIFA;
        double pagamentArrodonit = Math.round(pagament * 100.0) / 100.0;
        return pagamentArrodonit;
    }

    /**
     * METODES DEL MENU
     */
    public void menu() {
        Scanner lector = new Scanner(System.in);
        boolean lecturaOk = false;
        String opcio = null;
        do {

            System.out.println("\n--------- Benvinguts al Parking ----------");
            System.out.println("------------------ PEP -------------------");
            System.out.println("------------------------------------------");
            System.out.println("[1] Per aparcar instrodueix: " + ENTRADA + "\n");
            System.out.println("------------------------------------------");
            System.out.println("[2] sortir y pagar instrodueix: " + SORTIDA + "\n");
            System.out.println("------------------------------------------");
            System.out.println("[3] Per mostrar ocupació introdueix: " + OCUPACIO + "\n");
            System.out.println("------------------------------------------");
            System.out.println("[4] Per sortir del programa introdueix: " + SORTIDA_PROGRAMA + "\n");
            System.out.println("------------------------------------------\n");
            opcio = lector.nextLine();
            paginador();
            /*He utilitzat string en comptes de Enters degut a que si introduiem alfanumerics al scanner donaba error y sortia del programa*/
            /*M`ha semblat que per aquesta estructura de control era mes adient*/
            if (opcio.equals(ENTRADA)) {
                lecturaOk = true;
                entrada();
            } else if (opcio.equals(SORTIDA)) {
                lecturaOk = true;
                sortida();
            } else if (opcio.equals(OCUPACIO)) {
                lecturaOk = true;
                mostrarOcupacioParking();
            } else if (opcio.equals(SORTIDA_PROGRAMA)) {
                System.out.print("Has sortit del programa\n");
                break;
            } else if (opcio != ENTRADA && opcio != SORTIDA && opcio != OCUPACIO && opcio != SORTIDA_PROGRAMA) {
                lecturaOk = false;
                System.out.print("\nOpcio no valida " + opcio + " No es una opció a escollir\n");
            }
        } while (!lecturaOk);
    }

    private void paginador() {
        int lineasPagina = 20;
        int i = 0;
        do {
            System.out.print("\n");
            i++;
        } while (i < lineasPagina);
    }

    /**
     * ELIMINAR COTXES DEL ARRAY
     */
    private void eliminarVehicleArray(int posicio) {
        cotxesAparcats[posicio][ID_MATRICULA] = "";
        cotxesAparcats[posicio][ID_HORA] = "";
        numCotxesAparcats--;
        System.out.print("Vehicle fora del Parking numero: " + posicio + "\n");
        moureCotxesArray(posicio);
    }

    private void moureCotxesArray(int posicio) {
        for (int i = posicio + 1; i < MAX_COTXES; i++) {
            cotxesAparcats[i - 1][ID_MATRICULA] = cotxesAparcats[i][ID_MATRICULA];
            cotxesAparcats[i - 1][ID_HORA] = cotxesAparcats[i][ID_HORA];
        }
    }

    /**
     * METODES D`INSERCIO DE COTXES
     */
    private String generarHoraEntrada() {
        return horesEntradaAleatories[(int) (Math.random() * horesEntradaAleatories.length)];
    }

    private void afegirNouCotxe(String matricula, String hora) {
        if (numCotxesAparcats == cotxesAparcats.length) {
            System.out.println("------------------------------------------");
            System.out.println("  Aparcament ple, " + matricula + " es queda fora");
            System.out.println("------------------------------------------");
        } else {
            matricula = matricula.toUpperCase();
            int i = 0;
            while (!matricula.equals(cotxesAparcats[i][ID_MATRICULA]) && i < numCotxesAparcats) {
                i++;
            }
            if (i == numCotxesAparcats) {  // S'han recorregut tots els vehicles i no s'ha trobat coincidencia
                cotxesAparcats[numCotxesAparcats][ID_MATRICULA] = matricula;
                cotxesAparcats[numCotxesAparcats][ID_HORA] = hora;
                System.out.print("Vehicle " + matricula + " aparcat al aparcament " + i + " a les " + hora + "\n");
                numCotxesAparcats++;
            } else {
                System.out.println("ERROR! El vehicle amb matrícula " + matricula + " ja es troba aparcat\n");
            }
        }
    }

    private boolean cercarMatriculaEntrada(String respostaClient) {
        boolean cotxeAparcat = false;
        for (int i = 0; i < cotxesAparcats.length; i++) {
            if (cotxesAparcats[i][ID_MATRICULA].equals(respostaClient)) {
                cotxeAparcat = true;
                System.out.print("\nLa matricula no es correcta " + respostaClient + " es troba dins del parking\n");
                respostaClient = null;
            }
        }
        if (!cotxeAparcat) {
            System.out.print("\nLa matricula " + respostaClient + " es correcta\n");
        }
        return cotxeAparcat;
    }

    private String entrarCotxes() {
        mostrarPregunta();
        String resposta = esperarResposta();
        boolean respostaCerca = cercarMatriculaEntrada(resposta);
        if (respostaCerca) {
            resposta = null;
            repetirEntrada();
        } else {
            String hora = generarHoraEntrada();
            afegirNouCotxe(resposta, hora);
        }
        return resposta;
    }

    private void repetirEntrada() {
        entrarCotxes();
    }

    /**
     * Mostra un llistat dels cotxes que hi ha dins l'aparcament.
     */
    private void mostrarOcupacioParking() {
        System.out.println("-----------------------------------------------");
        System.out.println("--------------LLISTAT DE VEHICLES--------------");
        System.out.println("-----------------------------------------------");
        for (int i = 0; i < numCotxesAparcats; i++) {
            System.out.print(cotxesAparcats[i][ID_MATRICULA]);
            System.out.print("                                   ");
            System.out.println(cotxesAparcats[i][ID_HORA]);
        }
        menu();
    }
}
