import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                String route = generateRoute("RLRFR", 100);

                int countR = 0;
                for (char c : route.toCharArray()) {
                    if (c == 'R') {
                        countR++;
                    }
                }

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
                // System.out.println("Поворотов 'R' было " + countR);
            }));
            threads.get(threads.size() - 1).start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        int maxCount = -1;
        int maxKey = -1;
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxKey = entry.getKey();
                System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxCount + " раз)");
            } else {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}

