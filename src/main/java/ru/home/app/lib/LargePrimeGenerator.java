package ru.home.app.lib;
import java.security.SecureRandom;

public class FermatPrimeCheck {
    private static final SecureRandom random = new SecureRandom();

    public static void main(String[] args) {
        long number = 2147483647L; // Пример числа для проверки
        int iterations = 10; // Количество итераций проверки

        if (isPrimeFermat(number, iterations)) {
            System.out.println(number + " вероятно простое (по тесту Ферма).");
        } else {
            System.out.println(number + " составное.");
        }
    }

    /**
     * Тест Ферма для проверки простоты числа
     *
     * @param n          Число для проверки
     * @param iterations Количество итераций (чем больше, тем выше точность)
     * @return true, если число вероятно простое, иначе false
     */
    public static boolean isPrimeFermat(long n, int iterations) {
        // Числа меньше 2 не являются простыми
        if (n < 2) return false;

        // Числа 2 и 3 сразу считаются простыми
        if (n == 2 || n == 3) return true;

        // Четные числа не могут быть простыми
        if (n % 2 == 0) return false;

        for (int i = 0; i < iterations; i++) {
            // Случайное число a в диапазоне [2, n-2]
            long a = 2 + random.nextLong() % (n - 3);
            if (a < 2) a = 2; // Корректируем отрицательное значение, если возникло

            // Проверяем условие теста Ферма: a^(n-1) ≡ 1 (mod n)
            if (modularExponentiation(a, n - 1, n) != 1) {
                return false; // Число составное
            }
        }

        return true; // Вероятно простое
    }

    /**
     * Быстрое возведение в степень по модулю
     *
     * @param base  Основание
     * @param exp   Показатель степени
     * @param mod   Модуль
     * @return (base^exp) % mod
     */
    public static long modularExponentiation(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;

        while (exp > 0) {
            // Если степень нечетная, умножаем на результат
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }

            // Увеличиваем степень (делим на 2)
            exp >>= 1;
            base = (base * base) % mod;
        }

        return result;
    }
}