import java.util.*;
public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();

    static int vida = 100;
    static int energia = 100;
    static int hambre = 50;
    static int dias = 1;
    static final int DIA_MAX = 10;

    static ArrayList<String> inventario = new ArrayList<>();
    static ArrayList<String> objetos = new ArrayList<>(Arrays.asList(
            "Vendas", "Comida", "Bebida energética"
    ));

    public static void main(String[] args) {

        inventario.add("Machete");
        inventario.add("Vendas");

        ArrayList<String> lugares = new ArrayList<>(Arrays.asList(
                "Hospital", "Estación de policía", "Gasolinera", "Granja", "Pueblo"
        ));

        ArrayList<String> enemigos = new ArrayList<>(Arrays.asList(
                "Corredor", "Gordo", "Mutante"
        ));
        ArrayList<Integer> vidaEnemigos = new ArrayList<>(Arrays.asList(
                50, 80, 120
        ));

        System.out.println("-- BIENVENIDO AL APOCALIPSIS ️--");
        System.out.println("En este mundo despiertas en una gasolinera abandonada.");
        System.out.println("Al lado de la mesa encuentras ropa limpia, un machete y vendas");
        System.out.println("Sales de la gasolinera y encuentras un mundo habitado por Zombies.");
        System.out.println("Ahora estas solo y tu aventura de supervivencia empieza");

        System.out.print("\nIntroduce tu nombre: ");
        String nombre = scanner.nextLine();

        while (vida > 0 && dias <= DIA_MAX) {

            System.out.println("\n-- DÍA " + dias + " / " + DIA_MAX + " --");
            System.out.println("1. Ver estado");
            System.out.println("2. Ver inventario");
            System.out.println("3. Explorar");
            System.out.println("4. Dormir (pasar día)");
            System.out.println("5. Usar objeto");
            System.out.println("0. Salir");

            int opcion;

            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(" Debes introducir un número.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {

                case 1:
                    verEstado(nombre);
                    break;

                case 2:
                    verInventario();
                    break;

                case 3:
                    explorar(lugares, enemigos, vidaEnemigos);
                    energia -= 10;
                    hambre += 5;
                    break;

                case 4:
                    dormir();
                    break;

                case 5:
                    usarObjeto();
                    break;

                case 0:
                    System.out.println("Has abandonado la partida...");
                    return;

                default:
                    System.out.println("Opción inválida.");
            }

            if (energia <= 0 || hambre >= 100) {
                vida -= 10;
                System.out.println("El cansancio y el hambre te dañan...");
            }
        }

        if (vida > 0) {
            System.out.println("\nHAS SOBREVIVIDO 10 DÍAS AL APOCALIPSIS ");
        } else {
            System.out.println("\n Has muerto antes del día 10...");
        }
    }



    public static void verEstado(String nombre) {
        System.out.println("\n-- ESTADO DE " + nombre + " --");
        System.out.println("Vida: " + vida);
        System.out.println("Energía: " + energia);
        System.out.println("Hambre: " + hambre);
    }

    public static void verInventario() {
        System.out.println("\n-- INVENTARIO --");
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println(i + ". " + inventario.get(i));
        }
    }

    public static void explorar(ArrayList<String> lugares,
                                ArrayList<String> enemigos,
                                ArrayList<Integer> vidaEnemigos) {

        if (lugares.isEmpty()) {
            System.out.println("No quedan lugares por explorar.");
            return;
        }

        String lugar = lugares.remove(rand.nextInt(lugares.size()));
        System.out.println("Llegas al " + lugar);

        encontrarObjetoLugar();

        int indice = rand.nextInt(enemigos.size());
        String enemigo = enemigos.get(indice);
        int vidaEnemigo = vidaEnemigos.get(indice);

        System.out.println("¡Un " + enemigo + " aparece!");

        while (vida > 0 && vidaEnemigo > 0) {

            System.out.println("1. Atacar");
            System.out.println("2. Huir");

            int accion;
            try {
                accion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Introduce un número válido.");
                scanner.nextLine();
                continue;
            }

            if (accion == 2) {
                System.out.println("Huyes del combate...");
                return;
            }

            int danoJugador = ataqueJugador();
            vidaEnemigo -= danoJugador;
            System.out.println("Haces " + danoJugador + " de daño");

            if (vidaEnemigo <= 0) {
                System.out.println("Has derrotado al " + enemigo);
                lootEnemigo();
                enemigos.remove(indice);
                vidaEnemigos.remove(indice);
                return;
            }

            int danoEnemigo = ataqueEnemigo(enemigo);
            vida -= danoEnemigo;
            System.out.println("El enemigo te hace " + danoEnemigo + " de daño");
            System.out.println("Vida: " + vida);
        }
    }


    public static int ataqueJugador() {
        return rand.nextInt(11) + 10;
    }

    public static int ataqueEnemigo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "corredor": return rand.nextInt(8) + 8;
            case "gordo": return rand.nextInt(9) + 12;
            case "mutante": return rand.nextInt(13) + 18;
            default: return rand.nextInt(10) + 5;
        }
    }


    public static void usarObjeto() {

        if (inventario.isEmpty()) {
            System.out.println("Inventario vacío.");
            return;
        }

        verInventario();
        System.out.print("Elige un objeto: ");

        int opcion;

        try {
            opcion = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(" Debes introducir un número.");
            scanner.nextLine();
            return;
        }

        if (opcion < 0 || opcion >= inventario.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        String objeto = inventario.get(opcion);

        if (objeto.equals("Machete")) {
            System.out.println("No puedes usar el arma desde aquí.");
            return;
        }

        switch (objeto) {
            case "Vendas":
                vida = Math.min(100, vida + 25);
                System.out.println("Te curas con vendas.");
                break;
            case "Comida":
                hambre = Math.max(0, hambre - 20);
                System.out.println("Comes algo.");
                break;
            case "Bebida energética":
                energia = Math.min(100, energia + 20);
                System.out.println("Recuperas energía.");
                break;
        }

        inventario.remove(opcion);
    }

    public static void lootEnemigo() {
        if (rand.nextInt(100) < 40) {
            String objeto = objetos.get(rand.nextInt(objetos.size()));
            inventario.add(objeto);
            System.out.println("Obtienes: " + objeto);
        }
    }

    public static void encontrarObjetoLugar() {
        if (rand.nextInt(100) < 50) {
            String objeto = objetos.get(rand.nextInt(objetos.size()));
            inventario.add(objeto);
            System.out.println("Encuentras: " + objeto);
        }
    }

    public static void dormir() {
        System.out.println("Duermes y pasa un día...");
        energia = Math.min(100, energia + 30);
        hambre = Math.min(100, hambre + 10);
        dias++;
    }
}
