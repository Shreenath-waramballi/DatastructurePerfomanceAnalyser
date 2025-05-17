package com.performance;

import java.util.*;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.NumberAxis;

public class Main {

    private static final Map<String, Long[]> summaryMap = new LinkedHashMap<>();
    private static final int NUM_SEARCHES = 1000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice, size;

        System.out.print("Enter number of elements to test with: ");
        size = scanner.nextInt();

        do {
            System.out.println("\n=== Choose Data Structure to Test ===");
            System.out.println("1. ArrayList");
            System.out.println("2. LinkedList");
            System.out.println("3. HashSet");
            System.out.println("4. TreeSet");
            System.out.println("5. Stack");
            System.out.println("6. Queue");
            System.out.println("7. Show Summary & Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> testListPerformance(new ArrayList<>(), "ArrayList", size);
                case 2 -> testListPerformance(new LinkedList<>(), "LinkedList", size);
                case 3 -> testSetPerformance(new HashSet<>(), "HashSet", size);
                case 4 -> testSetPerformance(new TreeSet<>(), "TreeSet", size);
                case 5 -> testStackPerformance(new Stack<>(), "Stack", size);
                case 6 -> testQueuePerformance(new LinkedList<>(), "Queue", size);
                case 7 -> {
                    showSummary();
                    showChartFromSummary();
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 7);

        scanner.close();
    }

    public static void testListPerformance(List<Integer> list, String name, int size) {
        System.out.println("\n>> Testing " + name);
        long startAdd = System.nanoTime();
        for (int i = 0; i < size; i++) list.add(i);
        long endAdd = System.nanoTime();
        long addTime = (endAdd - startAdd) / 1_000_000;
        System.out.println("✅ Added " + size + " elements in " + addTime + " ms");

        Random rand = new Random();
        long startSearch = System.nanoTime();
        for (int i = 0; i < NUM_SEARCHES; i++) {
            list.contains(rand.nextInt(size));
        }
        long endSearch = System.nanoTime();
        long searchTime = (endSearch - startSearch) / 1_000_000;
        System.out.println("⏱️ Search Time for " + NUM_SEARCHES + " lookups: " + searchTime + " ms");

        summaryMap.put(name, new Long[]{addTime, searchTime});
    }

    public static void testSetPerformance(Set<Integer> set, String name, int size) {
        System.out.println("\n>> Testing " + name);
        long startAdd = System.nanoTime();
        for (int i = 0; i < size; i++) set.add(i);
        long endAdd = System.nanoTime();
        long addTime = (endAdd - startAdd) / 1_000_000;
        System.out.println("✅ Added " + size + " elements in " + addTime + " ms");

        Random rand = new Random();
        long startSearch = System.nanoTime();
        for (int i = 0; i < NUM_SEARCHES; i++) {
            set.contains(rand.nextInt(size));
        }
        long endSearch = System.nanoTime();
        long searchTime = (endSearch - startSearch) / 1_000_000;
        System.out.println("⏱️ Search Time for " + NUM_SEARCHES + " lookups: " + searchTime + " ms");

        summaryMap.put(name, new Long[]{addTime, searchTime});
    }

    public static void testStackPerformance(Stack<Integer> stack, String name, int size) {
        System.out.println("\n>> Testing " + name);
        long startAdd = System.nanoTime();
        for (int i = 0; i < size; i++) stack.push(i);
        long endAdd = System.nanoTime();
        long addTime = (endAdd - startAdd) / 1_000_000;
        System.out.println("✅ Pushed " + size + " elements in " + addTime + " ms");

        Random rand = new Random();
        long startSearch = System.nanoTime();
        for (int i = 0; i < NUM_SEARCHES; i++) {
            stack.contains(rand.nextInt(size));
        }
        long endSearch = System.nanoTime();
        long searchTime = (endSearch - startSearch) / 1_000_000;
        System.out.println("⏱️ Search Time for " + NUM_SEARCHES + " lookups: " + searchTime + " ms");

        summaryMap.put(name, new Long[]{addTime, searchTime});
    }

    public static void testQueuePerformance(Queue<Integer> queue, String name, int size) {
        System.out.println("\n>> Testing " + name);
        long startAdd = System.nanoTime();
        for (int i = 0; i < size; i++) queue.offer(i);
        long endAdd = System.nanoTime();
        long addTime = (endAdd - startAdd) / 1_000_000;
        System.out.println("✅ Offered " + size + " elements in " + addTime + " ms");

        Random rand = new Random();
        long startSearch = System.nanoTime();
        for (int i = 0; i < NUM_SEARCHES; i++) {
            queue.contains(rand.nextInt(size));
        }
        long endSearch = System.nanoTime();
        long searchTime = (endSearch - startSearch) / 1_000_000;
        System.out.println("⏱️ Search Time for " + NUM_SEARCHES + " lookups: " + searchTime + " ms");

        summaryMap.put(name, new Long[]{addTime, searchTime});
    }

    public static void showSummary() {
        if (summaryMap.isEmpty()) {
            System.out.println("❌ No tests run.");
            return;
        }

        System.out.println("\n=== 📊 Performance Summary ===");
        String fastest = null, slowest = null;
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;

        for (Map.Entry<String, Long[]> entry : summaryMap.entrySet()) {
            long total = entry.getValue()[0] + entry.getValue()[1];
            System.out.println(entry.getKey() + " ➤ Add: " + entry.getValue()[0] + " ms | Search: " + entry.getValue()[1] + " ms | Total: " + total + " ms");

            if (total < min) {
                min = total;
                fastest = entry.getKey();
            }
            if (total > max) {
                max = total;
                slowest = entry.getKey();
            }
        }

        System.out.println("\n🏁 Fastest Overall: " + fastest + " (" + min + " ms)");
        System.out.println("🐢 Slowest Overall: " + slowest + " (" + max + " ms)");
    }

    public static void showChartFromSummary() {
        if (summaryMap.isEmpty()) {
            System.out.println("No data to plot.");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        long maxY = 10;
        for (Map.Entry<String, Long[]> entry : summaryMap.entrySet()) {
            String dsName = entry.getKey();
            Long[] times = entry.getValue();
            dataset.addValue(times[0], "Add Time", dsName);
            dataset.addValue(times[1], "Search Time", dsName);
            maxY = Math.max(maxY, Math.max(times[0], times[1]));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Add vs Search Performance",
                "Data Structure",
                "Time (ms)",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setRange(0.0, maxY + 10.0);

        ChartFrame frame = new ChartFrame("Add vs Search Time Chart", barChart);
        frame.pack();
        frame.setVisible(true);

        try {
            ChartUtils.saveChartAsPNG(new File("add_vs_search_chart.png"), barChart, 900, 600);
            System.out.println("📁 Chart saved as add_vs_search_chart.png");
        } catch (IOException e) {
            System.out.println("❌ Could not save chart: " + e.getMessage());
        }
    }
}
