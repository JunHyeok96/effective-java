import java.util.Comparator;
import java.util.HashMap;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

class Main {

    public static class BasicCompareFruits implements Comparable<BasicCompareFruits> {
        private Integer price;

        @Override
        public int compareTo(BasicCompareFruits basicCompareFruits) {
            return Integer.compare(price, basicCompareFruits.price);
        }
    }

    public static class RelationalCompareFruits implements Comparable<RelationalCompareFruits> {
        private Integer price;

        @Override
        public int compareTo(RelationalCompareFruits relationalCompareFruits) {
            if (price == relationalCompareFruits.price) {
                return 0;
            }
            else if (price > relationalCompareFruits.price) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] argc){
        String s1 = new String("wow");
        String s2 = new String("wow");

        Object.hashCode(s1);

    }
}
