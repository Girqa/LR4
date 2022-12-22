import Models.Producer.ProducerData;
import AdditionalClasses.Timer;
import lombok.SneakyThrows;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        Timer t = Timer.getInstance();
        ProducerData d1 = new ProducerData(h -> h*5.0);
        t.addListener(d1);
        while (t.getCurHour() != 24) {
            System.out.println(t.getCurHour());
            System.out.println(t.getMillisToNextHour());
            System.out.println(d1.getCurrentPower());
            System.out.println();
            Thread.sleep(1000);
        }
    }
}
