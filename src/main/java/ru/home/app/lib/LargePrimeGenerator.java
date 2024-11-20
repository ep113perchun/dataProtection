//package ru.home.app.lib;
//import java.security.SecureRandom;
//
//public class LargePrimeGenerator {
//    private static final SecureRandom random = new SecureRandom();
//
//    public static void main(String[] args) {
//        long q, p;
//
//        while (true) {
//            // Генерация случайного простого числа q
//            q = generatePrimeLong();
//
//            // Проверка, не превышает ли 2q + 1 значение long
//            if (q > (Long.MAX_VALUE - 1) / 2) {
//                continue; // Пропускаем, если p выйдет за пределы long
//            }
//
//            // Вычисление p = 2q + 1
//            p = 2 * q + 1;
//
//            // Проверка, является ли p простым
//            if (isPrime(p)) {
//                break; // Найдены подходящие p и q
//            }
//        }
//
//        System.out.println("q = " + q);
//        System.out.println("p = " + p);
//    }
//
//    // Метод для генерации случайного простого числа типа long
//    private static long generatePrimeLong() {
//        while (true) {
//            // Генерация случайного числа в диапазоне положительных long
//            long candidate = (random.nextLong() & Long.MAX_VALUE) | 1; // Делаем число нечетным
//            if (isPrime(candidate)) {
//                return candidate;
//            }
//        }
//    }
//
//    // Проверка, является ли число простым
//    private static boolean isPrime(long n) {
//        if (n < 2) return false;
//        if (n == 2 || n == 3) return true;
//        if (n % 2 == 0) return false;
//
//        long sqrt = (long) Math.sqrt(n);
//        for (long i = 3; i <= sqrt; i += 2) {
//            if (n % i == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//}