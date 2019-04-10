import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class Config {

    public static final String SETTINGS = "./settings.properties";
    public static int PARKING_SPACE;
    public static int TIME_TO_PARKING;

    public static void load(){
        try {
            Properties settings = new Properties();
            InputStream is = new FileInputStream(new File(SETTINGS));
            settings.load(is);
            is.close();
            Random random = new Random();

            PARKING_SPACE = Integer.parseInt(settings.getProperty("ParkingSpace", "20"));
            TIME_TO_PARKING = Integer.parseInt(settings.getProperty("TimeToParking", "0"));

            if (TIME_TO_PARKING < 1){
                TIME_TO_PARKING = random.nextInt((5 - 1) + 1) + 1;
            }else if (TIME_TO_PARKING > 5){
                TIME_TO_PARKING = 5;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Переместите файл настройки в одну папку с файлом запуска (taskIntertrust.jar)");
            System.exit(0);
        }
    }
}
