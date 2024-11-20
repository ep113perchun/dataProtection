package ru.home.app.lib;

import java.security.SecureRandom;
import java.util.Random;

public class MathLib {
    private static final SecureRandom secureRandom = new SecureRandom();
    /**
     * Расширенный алгоритм Евклида. Находит НОД `a` и `b`, а также коэффициенты `x` и `y`,
     * такие, что a * x + b * y = gcd(a, b).
     *
     * cd + bp  = 1
     * cd = 1 + bp
     * cd mod p = 1
     *
     * @param a Первое число.
     * @param b Второе число.
     * @return Массив из трех элементов: НОД, коэффициент `x`, коэффициент `y`.
     */
    public static long[] extendedGCD(long a, long b) {
        long u1 = a, u2 = 1, u3 = 0;
        long v1 = b, v2 = 0, v3 = 1;

        while (v1 != 0) {
            long q = u1 / v1;
            long t1 = u1 % v1;
            long t2 = u2 - q * v2;
            long t3 = u3 - q * v3;

            u1 = v1;
            u2 = v2;
            u3 = v3;
            v1 = t1;
            v2 = t2;
            v3 = t3;
        }
        return new long[]{u1, u2, u3};
    }

    /**
     * Возводит число `a` в степень `x` по модулю `p`.
     *
     * @param a Основание.
     * @param x Показатель степени.
     * @param p Модуль.
     * @return Результат возведения в степень по модулю.
     */
    public static long powModule(long a, long x, long p) {
        long result = 1;

        if (a == 0 || p == 0) return 0;

        while (x > 0) {
            if ((x & 1) == 1) {
                result = (result * a) % p;
            }
            a = (a * a) % p;
            x >>= 1;
        }
        return result;
    }

    /**
     * Возвращает модульное значение `a % b`, учитывая отрицательные значения.
     *
     * @param a Делимое.
     * @param b Делитель.
     * @return Результат операции mod.
     */
    public static long mod(long a, long b) {
        if (b < 0) return -mod(-a, -b);
        long ret = a % b;
        if (ret < 0) ret += b;
        return ret;
    }

    /**
     * Находит число, взаимно простое с `phi`.
     *
     * @param phi Число, для которого нужно найти взаимно простое значение.
     * @return Взаимно простое значение.
     */
    public static long findCoprime(long phi) {
        Random random = new Random();
        while (true) {
            long candidate = 2 + (long)(random.nextDouble() * (phi - 2));
            long[] gcdResult = extendedGCD(candidate, phi);
            if (gcdResult[0] == 1) {  // Проверка на НОД = 1
                return candidate;
            }
        }
    }

    /**
     * Вычисляет мультипликативную обратную величину `a` по модулю `m`.
     *
     * @param a Число, для которого ищется обратное по модулю `m`.
     * @param m Модуль.
     * @return Обратное по модулю `m`.
     * @throws IllegalArgumentException если `a` и `m` не взаимно простые.
     */
    public static long modularInverse(long a, long m) {
        long[] gcdResult = extendedGCD(a, m);
        long gcd = gcdResult[0];
        long x = gcdResult[1];
        if (gcd != 1) {
            throw new IllegalArgumentException("Обратного элемента не существует");
        }
        return (x % m + m) % m;
    }

    /**
     * Выполняет тест Миллера-Рабина для определения вероятной простоты числа `n`.
     *
     * @param n Число для проверки.
     * @param iterations Количество итераций для теста.
     * @return true, если число вероятно простое, иначе false.
     */
    public static boolean millerRabinTest(long n, int iterations) {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        long s = 0;
        long r = n - 1;
        while (r % 2 == 0) {
            s++;
            r /= 2;
        }

        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            long a = 2 + random.nextLong() % (n - 4);
            long x = powModule(a, r, n);

            if (x == 1 || x == n - 1) continue;

            boolean continueOuter = false;
            for (int j = 1; j < s; j++) {
                x = powModule(x, 2, n);
                if (x == n - 1) {
                    continueOuter = true;
                    break;
                }
            }
            if (!continueOuter) return false;
        }
        return true;
    }

    /**
     * Генерирует примитивный корень `g` для данного простого числа `p`.
     *
     * @param p Простое число.
     * @return Примитивный корень или -1, если не найден.
     */
    public static long genG(long p) {
        long phi = p - 1;
        for (long g = 2; g < p; g++) {
            boolean isPrimitive = true;
            for (long i = 1; i < phi; i++) {
                if (powModule(g, i, p) == 1) {
                    isPrimitive = false;
                    break;
                }
            }
            if (isPrimitive) {
                return g;
            }
        }
        return -1;
    }

    /**
     * Выполняет тест Ферма для проверки вероятной простоты числа `p`.
     *
     * @param p Число для проверки.
     * @param k Количество итераций для теста.
     * @return true, если число вероятно простое, иначе false.
     */
    public static boolean testFerma(long p, int k) {
        if (p == 2) return true;
        if ((p & 1) == 0) return false;

        Random random = new Random();
        for (int i = 0; i < k; i++) {
            long a = 1 + random.nextLong() % (p - 1);
            if (gcd(a, p) != 1 || powModule(a, p - 1, p) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Генерирует случайное вероятно простое число в диапазоне [1_000_000, 1_000_000_000].
     *
     * @return Вероятно простое число.
     */
    public static long generateRandomPrime() {
        Random random = new Random();
        long p;
        do {
            p = 1_000_000 + random.nextInt(1_000_000_000 - 1_000_000);
        } while (!millerRabinTest(p, 100));
        return p;
    }

    /**
     * Вычисляет НОД двух чисел a и b.
     *
     * @param a Первое число.
     * @param b Второе число.
     * @return НОД чисел a и b.
     */
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long generatePrimeLong() {
        while (true) {
            long candidate = 100 + secureRandom.nextLong() % 999901;
            candidate = Math.abs(candidate) | 1;
            if (isPrime(candidate)) {
                return candidate;
            }
        }
    }

    /**
     * Проверка на простоту
     * @param n число на проверку
     * @return является ли число простым
     */
    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;

        long sqrt = (long) Math.sqrt(n);
        for (long i = 3; i <= sqrt; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}