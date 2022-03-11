package gimnas;

import java.util.Scanner;

import javax.management.Query;
import javax.management.RuntimeErrorException;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

    private Dni dni;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String sexe;
    private String comunicaciocomercial;
    private LocalDate datanaixement;
    private int edat;
    private CorreuElectronic email;
    private String condicioFisica;
    private Telefon telefon;
    private CompteBancari CCC;
    private String username;
    private String contrasenya;
    private int numReserves;

    private ArrayList<Client> clients;

    public Client() {
        this.dni = new Dni();
        this.email = new CorreuElectronic();
        this.telefon = new Telefon();
        this.CCC = new CompteBancari();
        this.datanaixement = LocalDate.now();
    }

    public Client(String dni) {
        this.dni = new Dni();
        this.email = new CorreuElectronic();
        this.telefon = new Telefon();
        this.CCC = new CompteBancari();
        this.datanaixement = LocalDate.now();
    }

    public void setDni(Dni dni) {
        this.dni = dni;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setDatanaixement(LocalDate datanaixement) {
        this.datanaixement = datanaixement;
    }

    public void setEmail(CorreuElectronic email) {
        this.email = email;
    }

    public void setCondicioFisica(String condicioFisica) {
        this.condicioFisica = condicioFisica;
    }

    public void setTelefon(Telefon telefon) {
        this.telefon = telefon;
    }

    public void setCCC(CompteBancari cCC) {
        CCC = cCC;
    }

    public void setComunicaciocomercial(String comunicaciocomercial) {
        this.comunicaciocomercial = comunicaciocomercial;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public void setNumReserves(int numReserves) {
        this.numReserves = numReserves;
    }

    /**
     * Aquest metode calcula del client i la assigna a l'atribut edat
     * 
     * @param ara Data actual que conseguim amb la classe LocalDate
     * @return void No retorna un resultat sino que el guarda a la edat.
     **/
    private void calcularEdad() {
        LocalDate ara = LocalDate.now();
        this.edat = Period.between(this.datanaixement, ara).getYears();
    }

    public void consultarClient() throws SQLException {
        Scanner teclat = new Scanner(System.in);

        System.out.println("*** CONSULTAR LES DADES D'UN CLIENT ***");
        System.out.println("Introdueix el DNI del client que vols consultar: ");

        String dni = teclat.next();

        Client cl1 = consultaClientBD(dni);

        if (cl1 == null) {
            System.out.println("\n*ERROR: El client amb dni: " + dni + " no existeix a la base de dades.");
        } else {
            System.out.println(cl1);
        }

    }

    private Client consultaClientBD(String dni) throws SQLException {
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "SELECT * FROM clients WHERE dni = ?";
        PreparedStatement sentencia = conexio.prepareStatement(consulta);

        sentencia.setString(1, dni);

        ResultSet rs = sentencia.executeQuery();

        if (rs.next()) {
            Client cli = new Client();
            cargarDadesSentenciaClient(rs);

            System.out.println("______________________________________________");
            System.out.println("|   DNI: " + this.dni.getDni());
            System.out.print("|   Nom: " + nom + " ");
            System.out.println(cognom1 + " " + cognom2);
            System.out.println("|   Data de naixement: " + datanaixement);
            System.out.println("|   Email: " + this.email.getEmail());
            System.out.println("|   Telèfon: " + this.telefon.getTelefon());
            System.out.println("|   Condició física: " + condicioFisica);
            System.out.println("|   Compte bancari: " + this.CCC.getCCC());
            System.out.println("|_____________________________________________");

            // clients.add(cli);
        }

        con.tancarConnexioBD();
        // return clients;
        return null;
    }

    public void altaClient() throws SQLException {
        Scanner teclat = new Scanner(System.in);

        System.out.println("\n*Donar d'alta un client*");

        String dni;
        Dni dniobj = new Dni();
        // boolean dniCorrecte() = false;
        // String dni;
        do {
            System.out.println("\nIntrodueix el DNI del client que vols donar d'alta: ");
            dni = teclat.next();

        } while (!dniobj.validarDni(dni));
        // fiquem el dni en l'objecte DNI
        dniobj.setDni(dni);

        // UN COP VALIDAT ASSIGNEM EL DNI AL ATRIBUT DNI DEL OBJECTE CLIENT
        setDni(dniobj);

        if (consultaClientBD(dniobj.getDni()) != null) {
            System.out.println("\nEl client amb aquest DNI ja existeix");
        } else {
            System.out.println("\nNom: ");
            this.nom = teclat.next();

            System.out.println("\nPrimer cognom: ");
            this.cognom1 = teclat.next();

            System.out.println("\nSegon cognom: ");
            this.cognom2 = teclat.next();

            System.out.println("\nSexe (M, H): ");
            this.sexe = teclat.next();

            System.out.println("\nComunicació comercial(SI, NO): ");
            this.comunicaciocomercial = teclat.next();

            System.out.println("\nCondició física: ");
            this.condicioFisica = teclat.next();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            boolean dataCorrecta;

            do {
                dataCorrecta = true;
                System.out.println("\nIntrodueix data de naixement en format correcte(yyyy-MM-dd)");
                try {
                    this.datanaixement = LocalDate.parse(teclat.next(), formatter);
                } catch (Exception ex) {
                    dataCorrecta = false;
                }
            } while (!dataCorrecta);

            Telefon telefon = new Telefon();
            do {
                System.out.println("\nIntrodueixi el numero de telèfon");
            } while (!telefon.setTelefon(teclat.next()));
            setTelefon(telefon);

            CorreuElectronic email = new CorreuElectronic();
            do {
                System.out.println("\nIntrodueixi el correu electrònic");
            } while (!email.setEmail(teclat.next()));
            setEmail(email);

            CompteBancari CCC = new CompteBancari();
            do {
                System.out.println("\nIntrodueixi el compte bancari");
            } while (!CCC.setCCC(teclat.next()));
            setCCC(CCC);

            altaClientBD();

            System.out.println("\nEl client ha sigut donat d'alta: " + this);

            // con.tancarConnexioBD();
        }

        // con.tancarConnexioBD();
    }

    private void altaClientBD() throws SQLException {
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "INSERT INTO Clients (DNI, nom, cognom1, cognom2, sexe, comunicaciocomercial, datanaixement, email, telefon, condiciofisica, ccc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement sentencia = conexio.prepareStatement(consulta);
            cargarDadesClientSentencia(sentencia);
            sentencia.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("**ERROR AL DONAR D'ALTA EL CLIENT AMB DNI: " + this.dni.getDni());

        }
    }

    public void cargarDadesClientSentencia(PreparedStatement sentencia) throws SQLException {
        sentencia.setString(1, this.dni.getDni());
        sentencia.setString(2, this.nom);
        sentencia.setString(3, this.cognom1);
        sentencia.setString(4, this.cognom2);
        sentencia.setString(5, this.sexe);
        sentencia.setString(6, this.comunicaciocomercial);
        sentencia.setString(7, datanaixement.toString());
        sentencia.setString(8, this.email.getEmail());
        sentencia.setString(9, this.telefon.getTelefon());
        sentencia.setString(10, this.condicioFisica);
        sentencia.setString(11, this.CCC.getCCC());
        // sentencia.setString(12, this.username);
        // sentencia.setString(13, this.contrasenya);
    }

    public void cargarDadesSentenciaClient(ResultSet rs) throws SQLException {
        this.setDni(new Dni(rs.getString("dni")));
        this.setNom(rs.getString("nom"));
        this.setCognom1(rs.getString("cognom1"));
        this.setCognom2(rs.getString("cognom2"));
        this.setSexe(rs.getString("sexe"));
        this.setComunicaciocomercial(rs.getString("comunicaciocomercial"));
        this.setDatanaixement(rs.getDate("datanaixement").toLocalDate());
        calcularEdad();
        this.setEmail(new CorreuElectronic(rs.getString("email")));
        this.setTelefon(new Telefon(rs.getString("telefon")));
        this.setCondicioFisica(rs.getString("condicioFisica"));
        this.setCCC(new CompteBancari(rs.getString("CCC")));

        calcularEdad();

    }

    public void visualitzarClients() throws SQLException {
        Scanner teclat = new Scanner(System.in);
        boolean sortir = false;
        System.out.println("Visualitzar tots els clients");
        do {
            System.out.println("\nSELECCIONA EL CRITERI DE ORDENACIÓ\n");
            System.out.println("1. Ordenats per cognoms");
            System.out.println("2. Ordenats per edat del client i visualitzant l'edat");
            System.out.println("3. Ordenats per més reserves fetes fins el moment actual");
            System.out.println("4. Sortir");
            System.out.println("\nTRIA UNA OPCIÓ:");

            while (!teclat.hasNextInt()) {
                System.out.println("El valor no és valid. Introdueixi un numero enter.");
                teclat.next();
            }

            int opcio = teclat.nextInt();
            Client cli = new Client();

            switch (opcio) {
                case 1:
                    this.clients = cli.ordenarCognom();
                    mostrarClients();
                    break;
                case 2:
                    this.clients = cli.ordenarEdat();
                    mostrarClients();
                    break;
                case 3:
                    this.clients = cli.ordenarReserves();
                    // mostrarClients();
                    break;
                case 4:
                    sortir = true;
                    break;
                default:
                    System.out.println("\n" + opcio + " NO ÉS UNA OPCIÓ NO VÀLIDA");
            }
        } while (!sortir);
    }

    public ArrayList<Client> ordenarCognom() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();

        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "SELECT * FROM clients ORDER BY cognom1, cognom2, nom;";
        PreparedStatement ps = conexio.prepareStatement(consulta);
        ResultSet rs = ps.executeQuery();

        System.out.println("Llista de tots els clients ordenats per cognom: \n");

        while (rs.next()) {
            Client cli = new Client();
            cli.cargarDadesSentenciaClient(rs);
            // System.out.print("DNI: " + rs.getString("dni") + " ");
            // System.out.print("Nom: " + rs.getString("nom") + " ");
            // System.out.print("Cognoms: " + rs.getString("cognom1") + " " +
            // rs.getString("cognom2") + " ");
            // System.out.print("Telefon: " + rs.getInt("telefon") + " ");
            // System.out.print("Condició física: " + rs.getString("condiciofisica") + "
            // \n");
            clients.add(cli);
        }

        con.tancarConnexioBD();
        return clients;
    }

    public ArrayList<Client> ordenarEdat() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "SELECT * FROM Clients ORDER BY datanaixement, cognom1, cognom2, nom ASC;";
        PreparedStatement ps = conexio.prepareStatement(consulta);

        ResultSet rs = ps.executeQuery();

        System.out.println("Llista de tots els clients ordenats per edat\n");

        while (rs.next()) {
            Client cli = new Client();
            cli.cargarDadesSentenciaClient(rs);

            clients.add(cli);
        }
        con.tancarConnexioBD();
        return clients;
    }

    public ArrayList<Client> ordenarReserves() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "SELECT count(*) as num_reserves, C.* FROM realitzacio R, Clients C WHERE R.data is not null AND R.DNI=C.DNI GROUP BY DNI order by count(*) desc, DNI;";
        PreparedStatement ps = conexio.prepareStatement(consulta);

        ResultSet rs = ps.executeQuery();

        System.out.println("Llista de tots els clients ordenats per numero de reserves totals: \n");

        while (rs.next()) {
            Client cli = new Client();
            cli.cargarDadesSentenciaClient(rs);

            cli.setNumReserves(rs.getInt("num_reserves"));
            System.out.print("\nDNI: " + rs.getString("C.DNI") + " ");
            System.out.print("Nom: " + rs.getString("nom") + " ");
            System.out.print("Cognom: " + rs.getString("cognom1") + " ");
            System.out.print("Numero de reserves: " + rs.getString("num_reserves") +
                    "\n");

            clients.add(cli);
        }
        con.tancarConnexioBD();
        return clients;

    }

    public void mostrarClients() {
        if (clients.isEmpty()) {
            System.out.println("No hi ha clients per visualitzar.");
        } else {
            for (Client client : clients) {
                System.out.println(client);
            }
        }

    }

    public void menuActDia() throws SQLException {
        Scanner teclat = new Scanner(System.in);
        boolean sortir = false;

        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        do {
            System.out.println("______________________________________________");
            System.out.println("|     Introdueixi el dia de la setmana       |");
            System.out.println("|                                            |");
            System.out.println("|             1. Dilluns                     |");
            System.out.println("|             2. Dimarts                     |");
            System.out.println("|             3. Dimecres                    |");
            System.out.println("|             4. Dijous                      |");
            System.out.println("|             5. Divendres                   |");
            System.out.println("|             6. Sortir                      |");
            System.out.println("|____________________________________________|");
            System.out.println("\nTRIA UNA OPCIÓ:");

            int opcio = teclat.nextInt();

            switch (opcio) {
                case 1:
                    mostrarActivitatDelDia(opcio);
                    break;
                case 2:
                    mostrarActivitatDelDia(opcio);
                    break;
                case 3:
                    mostrarActivitatDelDia(opcio);
                    break;
                case 4:
                    mostrarActivitatDelDia(opcio);
                    break;
                case 5:
                    mostrarActivitatDelDia(opcio);
                    break;
                case 6:
                    sortir = true;
                    break;
                default:
                    System.out.println("\n" + opcio + " NO ÉS UNA OPCIÓ NO VÀLIDA. Introdueixi un numero del 1 al 6");
            }
        } while (!sortir);

    }

    public void mostrarActivitatDelDia(int opcio) throws SQLException {
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        String consulta = "SELECT A.*, S.*, count(R.id_act) as num_reserves FROM Realitzacio R, Activitat A, Sala S WHERE R.id_act=A.id_act AND A.id_sala=S.id_sala AND A.dia ="
                + opcio + " AND R.data is not null GROUP BY R.id_act ORDER BY count(R.id_act) DESC;";
        PreparedStatement ps = conexio.prepareStatement(consulta);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id_act = rs.getInt("A.id_act");
            String nom = rs.getString("A.nom");
            double aforament = rs.getInt("S.aforament");
            double Percentatge = (aforament / 40) * 100;
            numReserves = rs.getInt("num_reserves");
            System.out
                    .println("\n NOM: " + nom + " ID ACTIVITAT: " + id_act + "    PERCENTATGE D'AFORAMENT DISPONIBLE: "
                            + Percentatge + "%" + " PERSONES INSCRITES: " + numReserves);
        }
    }

    public void crearUsuariWeb() throws SQLException {
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        Scanner teclat = new Scanner(System.in);
        System.out.println("\n*Crear un Usuari Web per al client*");

        System.out.println("\nIntrodueixi el DNI del Client: ");
        String dni = teclat.next();

        System.out.println("\nIntrodueixi la contrasenya per a iniciar sessió: ");
        String contrasenya = teclat.next();

        System.out.println("\nIntrodueixi un nom d'usuari: ");
        String username = teclat.next();

        String modifica = "UPDATE Clients SET username = ?, passwd = md5(?) WHERE dni = ?";
        PreparedStatement sentencia = null;
        // PreparedStatement sentencia = conexio.prepareStatement(modifica);

        try {
            sentencia = conexio.prepareStatement(modifica);
            sentencia.setString(3, dni);
            sentencia.setString(1, username);
            sentencia.setString(2, contrasenya);
            sentencia.executeUpdate();
            System.out.println("\nS'ha creat un Usuari Web per al Client amb dni: " + dni);
            System.out.println("Nom d'usuari: " + username);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("*Hi ha hagut un error en crear l'Usuari Web pel Client amb dni: " + dni);
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    
    @Override
    public String toString() {
        return "[nom=" + nom + " " + cognom1 + " " + cognom2 + ", dni=" + this.dni.getDni() + ", edat="
                + edat + ", email=" + this.email.getEmail() + ", telefon=" + this.telefon.getTelefon()
                + "]\n";
    }


    public void baixaClient() throws SQLException {
        Connexio con = new Connexio();
        Connection conexio = con.connectarBD();

        Scanner teclat = new Scanner(System.in);
        System.out.println("\n*Donar de baixa el client*");

        System.out.println("\nIntrodueixi el DNI del Client: ");
        String dni = teclat.next();

        DateTimeFormatter databaixa = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Data de baixa: " + databaixa.format(LocalDateTime.now()));

        String modifica = "UPDATE Altes SET data_baixa = ? WHERE dni = ?";
        PreparedStatement sentencia = null;
        // PreparedStatement sentencia = conexio.prepareStatement(modifica);

        try {
            sentencia = conexio.prepareStatement(modifica);
            sentencia.setString(1, databaixa.format(LocalDateTime.now()));
            sentencia.setString(2, dni);
            sentencia.executeUpdate();
            System.out.println("\nS'ha donat de baixa el Client amb DNI: " + dni);
            System.out.println("Data de baixa : " + databaixa.format(LocalDateTime.now()));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("*Hi ha hagut un error en donar de baixa el Client amb dni: " + dni);
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }


    // public void modificarClient() throws SQLException {
    //     Connexio con = new Connexio();
    //     Connection conexio = con.connectarBD();

    //     Scanner teclat = new Scanner(System.in);
    //     System.out.println("__________________________________________");
    //     System.out.println(" MODIFICAR DADES CLIENT \n");

    //     System.out.println("\nIntrodueixi el DNI del client a modificar");
    //     String dni = teclat.next();

    //     Client cl1 = consultaClientBD(dni);

    //     if (cl1 == null) {
    //         System.out.println("\n*ERROR: El client amb dni: " + dni + " no existeix a la base de dades.");
    //     } else {

    //         Telefon telefon = new Telefon();
    //         do {
    //             System.out.println("\nIntrodueixi el nou numero de telèfon");
    //         } while (!telefon.setTelefon(teclat.next()));
    //         setTelefon(telefon);

    //         CorreuElectronic email = new CorreuElectronic();
    //         do {
    //             System.out.println("\nIntrodueixi el nou correu electrònic");
    //         } while (!email.setEmail(teclat.next()));
    //         setEmail(email);

    //         CompteBancari CCC = new CompteBancari();
    //         do {
    //             System.out.println("\nIntrodueixi el nou compte bancari");
    //         } while (!CCC.setCCC(teclat.next()));
    //         setCCC(CCC);

    //         System.out.println("\nIntrodueixi la condició física: ");
    //         String condicioFisica = teclat.next();

    //         System.out.println("\nIntrodueixi si vol comunicació comercial (SI, NO): ");
    //         String comunicaciocomercial = teclat.next();

    //         String modifica = "UPDATE Clients SET WHERE dni = ?";
    //         PreparedStatement sentencia = null;

    //         try {
    //             sentencia = conexio.prepareStatement(modifica);
    //             sentencia.setString(3, this.dni);
    //             sentencia.setString(1, this.telefon.setTelefon());
    //             sentencia.setString(2, condiciofisica);
    //             sentencia.executeUpdate();
    //             System.out.println("\nS'han modificat correctament les dades del client amb DNI: " + dni);
    //         } catch (SQLException sqle) {
    //             sqle.printStackTrace();
    //             System.out.println("\n*Hi ha hagut un error al modificar les dades del client: " + dni);
    //         } finally {
    //             if (sentencia != null)
    //                 try {
    //                     sentencia.close();
    //                 } catch (SQLException sqle) {
    //                     sqle.printStackTrace();
    //                 }
    //         }
    //     }
    // }

}
