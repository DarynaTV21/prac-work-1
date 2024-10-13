package MyShop;

import java.util.Random;

public class Admin implements Runnable {
    @Override
    public void run() {
        try {
            // Адміністратор отримує доступ до ресурсу (семафор admin) перед виконанням дій
            Main.admin.acquire();

            // Імітація процесу додавання товарів адміністратором
            System.out.print("Адміністратор додає товари в інтернет-магазин.\n");
            Thread.sleep(100); // Затримка на 100 мілісекунд для імітації роботи

            Random random = new Random();
            // Випадково визначаємо кількість футболок і джинсів для додавання (від 1 до 5)
            int t_shirts = random.nextInt(5) + 1;
            int jeans = random.nextInt(5) + 1;

            // Додаємо згенеровану кількість товарів до наявних семафорів
            Main.t_shirt.release(t_shirts);
            Main.jeans.release(jeans);
            System.out.printf("Адміністратор додав %d футболок та %d джинсів до інтернет-магазину. Доступно: %d футболок та %d джинсів.\n",
                    t_shirts, jeans, Main.t_shirt.availablePermits(), Main.jeans.availablePermits());

            // Адміністратор відпускає ресурс (семафор admin), дозволяючи іншим його використовувати
            Main.admin.release();

        } catch (InterruptedException e) {
            e.printStackTrace(); // Обробка виключення, якщо потік буде перервано
        }
    }
}
