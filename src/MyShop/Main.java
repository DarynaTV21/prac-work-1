package MyShop;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {
    // Створюємо семафори для обмеження доступу до ресурсів
    static final Semaphore t_shirt = new Semaphore(1);
    static final Semaphore jeans = new Semaphore(1);
    static final Semaphore admin = new Semaphore(1);

    private static int activeCustomers = 0; // Змінна для підрахунку активних клієнтів

    // Об'єкт CountDownLatch використовується для синхронізації закриття магазину
    static final CountDownLatch latch = new CountDownLatch(1);

    static boolean isAvailableHours = true; // Змінна, що показує, чи магазин відкритий

    // Метод для відкриття магазину
    public static synchronized void openShop() {
        isAvailableHours = true;
        System.err.println("=============Інтернет-магазин відкрито================");
    }

    // Метод для перевірки, чи магазин відкритий
    public static synchronized boolean isOpen() {
        return isAvailableHours;
    }

    // Метод для закриття магазину
    public static synchronized void closeShop() {
        isAvailableHours = false;
        System.err.println("=============Інтернет-магазин закрито================");
    }

    // Метод, що збільшує кількість активних клієнтів
    public static synchronized void newCustomers() {
        activeCustomers++;
    }

    // Метод, що зменшує кількість активних клієнтів
    public static synchronized void customersLeft() {
        activeCustomers--;
        if (activeCustomers == 0 && !isAvailableHours) {
            notifyAdminLeftShop();
        }
    }

    // Метод для сповіщення адміністратора про завершення роботи
    public static synchronized void notifyAdminLeftShop() {
        System.err.println("=============Адміністратор завершив свою роботу та вийшов з інтернет-магазину================");
        latch.countDown(); // Зменшує лічильник CountDownLatch, дозволяючи програмі завершитися
    }

    public static void main(String[] args) throws InterruptedException {
        openShop();

        // Створюємо Runnable для моделювання роботи магазину
        Runnable shop = () -> {
            int i = 0;
            while (isOpen()) {
                new Thread(new Customer(), String.valueOf(i)).start();
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        // Запускаємо потік для моделювання роботи магазину
        Thread shopThread = new Thread(shop, "Інтернет-магазин");
        shopThread.start();

        // Створюємо Runnable для моделювання роботи адміністратора
        Runnable admin = () -> {
            while (isOpen()) {
                try {
                    Thread.sleep(2000);
                    new Thread(new Admin()).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Запускаємо потік для моделювання роботи адміністратора
        Thread adminThread = new Thread(admin, "Адміністратор");
        adminThread.start();

        Thread.sleep(6000); // Працюємо 6 секунд, після чого магазин закривається
        closeShop(); // Закриваємо магазин
        latch.await(); // Очікуємо, поки всі клієнти вийдуть з магазину та завершиться робота адміністратора
    }
}