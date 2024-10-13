package MyShop;

import java.util.Random;

public class Customer implements Runnable {
    @Override
    public void run() {
        Random random = new Random();
        // Генеруємо випадкове бажання купити або футболку, або джинси
        boolean wantsTshirt = random.nextBoolean(); // Випадково визначаємо, чи клієнт хоче футболку
        boolean wantsJeans = !wantsTshirt; // Якщо клієнт не хоче футболку, він хоче джинси

        try {
            // Якщо клієнт хоче купити футболку
            if (wantsTshirt) {
                Main.newCustomers(); // Додаємо клієнта до активних
                System.out.printf("Покупець %s хоче купити футболку.\n", Thread.currentThread().getName());

                // Перевіряємо, чи доступна футболка і чи магазин відкритий
                if (Main.t_shirt.tryAcquire() && Main.isAvailableHours) {
                    System.out.printf("Покупець %s купив футболку.\n", Thread.currentThread().getName());
                    Thread.sleep(2000); // Імітуємо час, необхідний для покупки
                    Main.t_shirt.release(); // Відпускаємо семафор після покупки
                } else if (Main.t_shirt.tryAcquire() && !Main.isAvailableHours) {
                    // Якщо магазин закритий, але клієнт намагається купити товар
                    System.out.printf("Покупець %s намагається купити футболку, але інтернет-магазин зачинений.\n", Thread.currentThread().getName());
                } else {
                    // Якщо товару немає в наявності
                    System.out.printf("Покупець %s намагається купити футболку, але товару немає в наявності.\n", Thread.currentThread().getName());
                }
            }

            // Якщо клієнт хоче купити джинси
            if (wantsJeans) {
                Main.newCustomers(); // Додаємо клієнта до активних
                System.out.printf("Покупець %s хоче купити джинси.\n", Thread.currentThread().getName());

                // Перевіряємо, чи доступні джинси і чи магазин відкритий
                if (Main.jeans.tryAcquire() && Main.isAvailableHours) {
                    System.out.printf("Покупець %s купив джинси.\n", Thread.currentThread().getName());
                    Thread.sleep(2000); // Імітуємо час, необхідний для покупки
                    Main.jeans.release(); // Відпускаємо семафор після покупки
                } else if (Main.jeans.tryAcquire() && !Main.isAvailableHours) {
                    // Якщо магазин закритий, але клієнт намагається купити товар
                    System.out.printf("Покупець %s намагається купити джинси, але інтернет-магазин зачинений.\n", Thread.currentThread().getName());
                } else {
                    // Якщо товару немає в наявності
                    System.out.printf("Покупець %s намагається купити джинси, але товару немає в наявності.\n", Thread.currentThread().getName());
                }
            }

            // Клієнт залишає магазин
            System.out.printf("Покупець %s залишив інтернет-магазин.\n", Thread.currentThread().getName());
            Main.customersLeft(); // Зменшуємо кількість активних клієнтів

        } catch (InterruptedException e) {
            e.printStackTrace(); // Обробка виключення, якщо потік буде перервано
        }
    }
}
