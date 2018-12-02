package Task_3;

import java.util.Comparator;
import java.util.concurrent.*;

public class IceCreamShop {
    static int timeUnit = 10;

    private final int SEATS_IN_SHOP = 10;

    private Executor threadExecutor = Executors.newCachedThreadPool();
    private Comparator<Customer> stammkundenComp = (c1, c2) -> {
        if(c1.isStammkunde() == c2.isStammkunde())
            return 0;
        return c2.isStammkunde() ? 1 : -1;
    };

    private BlockingQueue<Customer> inShop = new PriorityBlockingQueue<>(SEATS_IN_SHOP, stammkundenComp);
    private BlockingQueue<Customer> waitingForWaiter = new PriorityBlockingQueue<>(100 * 1000, stammkundenComp);


    private IceCreamShop(){
        threadExecutor.execute(new Waiter("James", this));
        threadExecutor.execute(new Waiter("John", this));
        threadExecutor.execute(new Waiter("Josef", this));
        threadExecutor.execute(new Waiter("Jeremiah", this));
    }

    private int customerID = 0;
    private void addWaveOfCustomers(){
        int numberNewCustomers = (int)(3 + Math.random() * 4);

        for(int i = 0; i < numberNewCustomers; i++)
            threadExecutor.execute(new Customer("Customer_" + customerID++, this, Math.random() < 0.4));
    }

    void enterShop(Customer c) throws InterruptedException {
        inShop.put(c);
    }

    void leaveShop(Customer c){
        inShop.remove(c);
        System.out.format("In the shop there is now %s people in the shop [%s]%n", inShop.size(), Thread.currentThread().getName());
    }

    void callAWaiter(Customer c) throws InterruptedException {
        System.out.format("There is %s people waiting for a waiter [%s]%n", waitingForWaiter.size(), Thread.currentThread().getName());
        waitingForWaiter.put(c);
    }

    Customer getWaitingCustomer() throws InterruptedException {
        return waitingForWaiter.take();
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
