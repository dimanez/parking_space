import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private Parking parking; //Парковка
    private int queue;       //Количество машин на въезд

    private void parameterization(){
        Config.load();
        System.out.println("Количество машиномест: " + Config.PARKING_SPACE);
        System.out.println("Время на заезд для каждой машины: " + Config.TIME_TO_PARKING);
    }

    public void initialize(){
        parameterization();
        ExecutorService service = Executors.newScheduledThreadPool(2);
        Scanner in = new Scanner(System.in);

        System.out.println("p:N - (park) чтобы припарковать машину, где N - количество машин на въезд\n" +
                "u:N - (unpark) чтобы выехать с парковки. N - номер парковочного билета\n" +
                "u:[1..n] - (unpark) чтобы выехать с парковки нескольким машинам, где в квадратных скобках, через запятую передаются номера парковочных билетов\n" +
                "l - (list) список машин, находящихся на парковке. Для каждой машины выводится ее порядковый номер и номер билета\n" +
                "c - (count) количество оставшихся мест на парковке\n" +
                "e - (exit) выход из приложения");

        //Создаем парковку и передаем туда список парк. мест и билеты.
        parking = new Parking(createTicketList());

        //Команды выполняются в цикле do-while пока не будет выполнена команда exit
        String command;
        do {
            command = in.nextLine();
            if (command.toLowerCase().startsWith("p:")){
                queue = Integer.parseInt(command.replaceAll("\\D+",""));

                service.submit(new Runnable() {
                    public void run() {
                        parking.findFreeParkingSpace(queue);
                    }
                });

            } else if (command.toLowerCase().startsWith("u:")){
                //Если ввели числа в квадратных скобках или через запятую, преобразуем их в массив int и передаем в parking
                String arr = command.replaceAll("[^0-9\\\\,\\[\\]]","");
                String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

                final int[] results = new int[items.length];

                for (int i = 0; i < items.length; i++) {
                    results[i] = Integer.parseInt(items[i]);
                }

                service.submit(new Runnable() {
                    public void run() {
                        parking.leaveParking(results);
                    }
                });

            } else if (command.equalsIgnoreCase("l")){
                parking.getCarList();

            } else if (command.equalsIgnoreCase("c")){
                System.out.println(parking.getFreeParkingSpace());

            } else if (command.equalsIgnoreCase("e")){
                //При выходе звкрываем принудительно все потоки.
                in.close();
                service.shutdownNow();
            } else {
                System.out.println("Команда " + command + " не существует.");
            }

        }while (!command.equalsIgnoreCase("e"));
    }

    /**
     * Создаем билеты для каждого парковочного места.
     * @return список билетов
     */
    private ArrayList<Ticket> createTicketList(){
        ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
        for (int i = 0; i < Config.PARKING_SPACE; i++){
            ticketList.add(new Ticket(i));
        }
        return ticketList;
    }

    public static void main(String[] args) {
        new Main().initialize();
    }
}
