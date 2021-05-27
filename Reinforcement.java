public class Reinforcement {

    int summary;
    int diameter;
    int length;
    int position;
    RFClass rfClass;
    double mass;

    public Reinforcement(int summary, int diameter, int length, int position, RFClass rfClass, double mass) {
        this.summary = summary;
        this.diameter = diameter;
        this.length = length;
        this.position = position;
        this.rfClass = rfClass;
        this.mass = mass;
    }

    public String toString(RFClass rfClass) {
        switch (rfClass) {
            case A240 -> {
                return "А240";// Output russian letters
            }
            case A400 -> {
                return "А400";
            }
            case A500 -> {
                return "А500";
            }
            case A500S -> {
                return "А500С";
            }
            case A600 -> {
                return "А600";
            }
            default -> {
                return "-";
            }
        }
    }

    public enum RFClass {
        A240,
        A400,
        A500,
        A500S,
        A600
    }
}