import java.util.ArrayList;
import java.util.HashMap;

public class Parking {
    private int numCar = 0;
    private ArrayList<Ticket> tickets;
    private HashMap<Ticket, Car> parkingSpace;

    Parking(ArrayList<Ticket> tickets){
        this.tickets = tickets;
        initializeParkingSpace();
    }

    private void initializeParkingSpace(){
        parkingSpace = new HashMap<Ticket, Car>();
        for (Ticket ticket : tickets) {
            if (!parkingSpace.containsKey(ticket)){
                parkingSpace.put(ticket, null);
            }
        }
    }

    /**
     * Поиск свободных мест на парковке
     * @param queue количество машин на въезде на парковку.
     */
    public void findFreeParkingSpace(int queue){

        //Сообщаем о нехватке парк. мест, если в очереди машин больше, чем оставшихся парк. мест
        if (queue > getFreeParkingSpace()) {
            System.out.println("На парковке не хватает мест: " + (queue - getFreeParkingSpace()));
            queue = getFreeParkingSpace();
        }

        for (int i = 0; i < queue; i++){
            numCar++;//Каждой машине добавляем свой номер.
            Car car = new Car(Integer.toString(numCar));

            try {
                //Выполняем условие "На заезд машине требуется от 1 до 5 секунд, задается настройками."
                Thread.sleep(Config.TIME_TO_PARKING * 1000);
                doParkingCar(car);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Выезд из парковки
     * Билет можно использовать повторно.
     * @param ticket номера билетов
     */
    public void leaveParking(int ... ticket){

        for (int i = 0; i < parkingSpace.size(); i++){
            if (parkingSpace.containsKey(tickets.get(i))){
                /*
                Если key соответствует значению ticket (номер билета)
                и его значение не является null, удаляем запись из HashMap
                с последующим возвратом в хэш-таблицу ключа (нашего билета), и присваиваем null его значению.
                 */
                for (int aTicket : ticket) {
                    if (tickets.get(i).getIdentifier() == aTicket) {
                        if (parkingSpace.get(tickets.get(i)) != null) {
                            parkingSpace.remove(tickets.get(i));//Удаляем машину
                            parkingSpace.put(tickets.get(i), null);//Возвращаем билет
                            System.out.println("Освободилось парковочное место № " + aTicket);
                        } else {
                            System.out.println("Парковочное место №" + aTicket + " не было занято.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Получаем список припаркованых машин, и их билеты
     */
    public void getCarList(){
        for (int i = 0; i < parkingSpace.size(); i++){
            if (parkingSpace.get(tickets.get(i)) != null) {
                System.out.println("Ticket: " + tickets.get(i).getIdentifier() + "| Car: " + parkingSpace.get(tickets.get(i)).getNumber());
            }
        }
    }

    /**
     * Получаем количество свободных мест на парковке
     */
    public int getFreeParkingSpace(){
        int free = 0;
        //Ищем свободные места
        for (Ticket ticket : tickets) {
            if (parkingSpace.get(ticket) == null){
                free++;
            }
        }
        return free;
    }

    /**
     * Занимаем парковочное место и получаем билет
     * @param car машина
     */
    private void doParkingCar(Car car){
        for (Ticket ticket : tickets) {
            if (parkingSpace.get(ticket) == null){
                parkingSpace.put(ticket, car);
                System.out.println("На парковочное место № " + ticket.getIdentifier() + " заехала машина " + car.getNumber());
                return;
            }
        }
    }
}
