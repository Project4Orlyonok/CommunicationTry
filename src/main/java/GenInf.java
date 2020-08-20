import java.util.ArrayList;

public class GenInf {
    public ArrayList<Double> pri = new ArrayList<>();
    public ArrayList<Double> pow = new ArrayList<>();
    public void setPri() {
        for (int i = 0; i < 24; i++) {
            pri.add(Math.random()+2);
        }
    }

    public double pri(int time) {
        return pri.get(time);
    }


    public void setPow() {
        for (int i = 0; i < 24; i++) {
            pow.add(Math.random()*1000);
        }
    }

    public double pow(int time) {
        return pow.get(time);
    }

    public void powmin(int time,double elem){
        double lem=pow.get(time);
        pow.remove(time);
        pow.add(time,lem-elem);
    }

}
