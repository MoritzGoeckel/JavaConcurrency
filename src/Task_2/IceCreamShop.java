package Task_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class IceCreamShop {
    static int timeUnit = 5;

    private List<Waiter> waiters = new ArrayList<>();
    private final int SEATS_IN_SHOP = 10;

    private Executor threadExecutor = Executors.newCachedThreadPool();
    private Semaphore seatsSemaphore = new Semaphore(SEATS_IN_SHOP, true);

    private IceCreamShop(){
        waiters.add(new Waiter("James"));
        waiters.add(new Waiter("John"));
        waiters.add(new Waiter("Josef"));
        waiters.add(new Waiter("Jeremiah"));

        for(Waiter w : waiters)
            threadExecutor.execute(w);
    }

    private int customerID = 0;
    private void addWaveOfCustomers(){
        int numberNewCustomers = (int)(3 + Math.random() * 4);

        for(int i = 0; i < numberNewCustomers; i++)
            threadExecutor.execute(new Customer("Customer_" + customerID++, this));
    }

    void enterShop() throws InterruptedException{
        seatsSemaphore.acquire();
    }

    void leaveShop(){
        seatsSemaphore.release();
        System.out.format("In the shop there is now %s seats available and %s people waiting [%s]%n", seatsSemaphore.availablePermits(), seatsSemaphore.getQueueLength(), Thread.currentThread().getName());
    }

    Waiter getAWaiter(){
        return waiters.get((int)(Math.random() * waiters.size()));
    }

    public static void main(String... args) throws InterruptedException{
        IceCreamShop shop = new IceCreamShop();
        while (true) {
            System.out.format("Adding customers [%s]%n", Thread.currentThread().getName());
            shop.addWaveOfCustomers();
            Thread.sleep(6 * timeUnit);
        }
    }
}
