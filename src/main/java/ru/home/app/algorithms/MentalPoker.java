package ru.home.app.algorithms;

import ru.home.app.lib.MathLib;

import java.util.*;
import java.security.SecureRandom;

public class MentalPoker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SecureRandom random = new SecureRandom();

    /**
     * Перемешивает колоду карт с использованием случайного генератора.
     *
     * @param deck Список карт, представляющий колоду.
     */
    public static void shuffleDeck(List<Long> deck) {
        Collections.shuffle(deck, new Random());
    }

    /**
     * Распределяет карты между игроками. Каждый игрок получает по две карты.
     *
     * @param deck        Список карт, представляющий колоду.
     * @param playersCards Список списков карт, где каждый внутренний список представляет карты отдельного игрока.
     * @param playersCount Количество игроков.
     */
    public static void distributeCards(List<Long> deck, List<List<Long>> playersCards, int playersCount) {
        int cardIndex = 0;
        for (int i = 0; i < playersCount; i++) {
            playersCards.get(i).add(deck.get(cardIndex++));
            playersCards.get(i).add(deck.get(cardIndex++));
        }

        for (int i = 0; i < playersCount; i++) {
            System.out.println("Игрок " + (i + 1) + " взял карты: " + playersCards.get(i).get(0) + ", " + playersCards.get(i).get(1));
        }
    }

    /**
     * Расшифровывает карту, переданную в зашифрованном виде.
     *
     * @param encryptedCard Зашифрованная карта.
     * @param playerIndex   Индекс игрока, который расшифровывает карту.
     * @param D             Список секретных ключей игроков.
     * @param playersCount  Количество игроков.
     * @param p             Простое число, используемое как модуль.
     * @return Расшифрованное значение карты.
     */
    public static long decryptCard(long encryptedCard, int playerIndex, List<Long> D, int playersCount, long p) {
        long k = encryptedCard;
        for (int j = 0; j < playersCount; j++) {
            if (j != playerIndex) {
                k = MathLib.powModule(k, D.get(j), p);
            }
        }
        k = MathLib.powModule(k, D.get(playerIndex), p);
        return k;
    }

    /**
     * Преобразует числовое значение карты в строковое представление (ранг и масть).
     *
     * @param card Числовое значение карты.
     * @return Строковое представление карты в формате "Ранг Масть".
     */
    public static String cardToString(int card) {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"Черви", "Бубны", "Трефы", "Пики"};

        int rankIndex = (card - 2) % 13;
        int suitIndex = (card - 2) / 13;

        return ranks[rankIndex] + " " + suits[suitIndex];
    }

    /**
     * Добавляет карты на стол (общие карты) из колоды.
     *
     * @param deck       Список карт, представляющий колоду.
     * @param tableCards Список карт, представляющий общие карты на столе.
     * @param cardIndex  Индекс текущей карты в колоде.
     */
    public static void dealCommunityCards(List<Long> deck, List<Long> tableCards, int cardIndex) {
        for (int i = 0; i < 5; i++) {
            tableCards.add(deck.get(cardIndex++));
        }
    }

    public static void main(String[] args) {
        int playersCount;
        int countCards = 52;

        do {
            System.out.println("Количество игроков (2-23)");
            playersCount = scanner.nextInt();
        } while (playersCount < 2 || playersCount > 23);

        long q;
        long p;

        while (true) {
            q = MathLib.generatePrimeLong();
            if (q > (Long.MAX_VALUE - 1) / 2) {
                continue;
            }

            p = 2 * q + 1;

            if (MathLib.millerRabinTest(p, 50)) {
                break;
            }
        }

        System.out.println("q = " + q);
        System.out.println("p = " + p);

        List<Long> C = new ArrayList<>(playersCount);
        List<Long> D = new ArrayList<>(playersCount);
        for (int i = 0; i < playersCount; i++) {
            C.add(MathLib.findCoprime(p - 1));
            D.add(MathLib.modularInverse(C.get(i).intValue(), (int) (p - 1)));
            System.out.println("Игрок " + (i + 1) + ": C = " + C.get(i) + " ; D = " + D.get(i));
        }

        List<Long> deck = new ArrayList<>(countCards);
        System.out.println("\nИсходная колода:");
        for (int i = 0; i < countCards; i++) {
            deck.add((long) (i + 2));
            System.out.print(deck.get(i) + " ");
        }

        long K;
        for (int i = 0; i < playersCount; i++) {
            System.out.println("\n\nПеремешанная колода после шифровки игрока" + (i + 1));
            for (int j = 0; j < countCards; j++) {
                K = MathLib.powModule(deck.get(j), C.get(i), p);
                deck.set(j, K);
                System.out.print(deck.get(j) + " ");
            }
            shuffleDeck(deck);
        }

        System.out.println("\n\nПеремешанная колода после шифрования:");
        for (Long card : deck) {
            System.out.print(card + " ");
        }
        System.out.println();

        List<List<Long>> playersCards = new ArrayList<>(playersCount);
        for (int i = 0; i < playersCount; i++) {
            playersCards.add(new ArrayList<>());
        }
        distributeCards(deck, playersCards, playersCount);

        System.out.println("\nРасшифрованные карты для каждого игрока:");
        for (int i = 0; i < playersCount; i++) {
            for (int j = 0; j < 2; j++) {
                long decryptedCard = decryptCard(playersCards.get(i).get(j), i, D, playersCount, p);
                System.out.println("Игрок " + (i + 1) + " расшифрованная карта " + (j + 1) + ": " + decryptedCard + " (" + cardToString((int) decryptedCard) + ")");
            }
        }

        List<Long> tableCards = new ArrayList<>();
        int cardIndex = playersCount * 2;
        dealCommunityCards(deck, tableCards, cardIndex);

        System.out.println("\nКарты на столе (расшифровано):");
        for (int i = 0; i < 5; i++) {
            long decryptedCard = tableCards.get(i);
            for (int j = 0; j < playersCount; j++) {
                decryptedCard = MathLib.powModule(decryptedCard, D.get(j), p);
            }
            System.out.println("Карта " + (i + 1) + ": " + decryptedCard + " (" + cardToString((int) decryptedCard) + ")");
        }
    }
}



