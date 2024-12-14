import java.io.*;
import java.util.*;

public class SortComparison {

    public static void main(String[] args) throws IOException {
        // 测试文件列表
        String[] fileNames = {"sort10.txt", "sort100.txt", "sort10000.txt"};

        // 调用排序比较函数
        sortComparison(fileNames);
    }

    // 对多个文件执行冒泡排序和归并排序性能比较
    public static void sortComparison(String[] fileNames) throws IOException {
        System.out.println("File\t\tBubbleSort (ms)\tMergeSort (ms)");

        for (String fileName : fileNames) {
            // 读取文件数据
            ArrayList<String> originalData = readFile(fileName);

            // 测试 BubbleSort
            ArrayList<String> dataForBubble = new ArrayList<>(originalData); // 深拷贝
            long startBubble = System.currentTimeMillis();
            measureBubbleSort(dataForBubble);
            long endBubble = System.currentTimeMillis();

            // 测试 MergeSort
            ArrayList<String> dataForMerge = new ArrayList<>(originalData); // 深拷贝
            long startMerge = System.currentTimeMillis();
            measureMergeSort(dataForMerge);
            long endMerge = System.currentTimeMillis();

            // 计算时间并打印
            long bubbleTime = endBubble - startBubble;
            long mergeTime = endMerge - startMerge;
            System.out.printf("%-12s\t%-16d\t%-16d%n", fileName, bubbleTime, mergeTime);
        }
    }

    // 从文件中读取数据
    public static ArrayList<String> readFile(String fileName) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            data.add(line);
        }
        reader.close();
        return data;
    }

    // 冒泡排序实现（使用 cardCompare）
    public static void measureBubbleSort(ArrayList<String> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            boolean swapped = false; // 提前终止标志
            for (int j = 0; j < data.size() - i - 1; j++) {
                if (cardCompare(data.get(j), data.get(j + 1)) > 0) {
                    // 交换元素
                    String temp = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // 如果本轮没有交换操作，则提前结束
        }
    }

    // 归并排序实现（使用 cardCompare）
    public static void measureMergeSort(ArrayList<String> data) {
        if (data.size() <= 1) {
            return;
        }
        int mid = data.size() / 2;
        ArrayList<String> left = new ArrayList<>(data.subList(0, mid));
        ArrayList<String> right = new ArrayList<>(data.subList(mid, data.size()));

        measureMergeSort(left);
        measureMergeSort(right);

        // 合并两个子数组
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (cardCompare(left.get(i), right.get(j)) <= 0) {
                data.set(k++, left.get(i++));
            } else {
                data.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            data.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            data.set(k++, right.get(j++));
        }
    }

    // 自定义卡牌比较方法
    public static int cardCompare(String card1, String card2) {
        // 检查输入是否合法
        if (card1 == null || card2 == null || card1.length() < 2 || card2.length() < 2) {
            throw new IllegalArgumentException("Invalid card format: " + card1 + ", " + card2);
        }

        // 定义卡牌顺序
        String order = "23456789TJQKA";
        char rank1 = card1.charAt(0); // 牌面
        char rank2 = card2.charAt(0);
        char suit1 = card1.charAt(card1.length() - 1); // 花色
        char suit2 = card2.charAt(card2.length() - 1);

        // 先比较牌面大小
        if (rank1 != rank2) {
            return order.indexOf(rank1) - order.indexOf(rank2);
        }
        // 如果牌面大小相同，比较花色
        return suit1 - suit2;
    }
}
