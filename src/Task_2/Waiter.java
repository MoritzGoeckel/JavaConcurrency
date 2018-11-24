package Task_2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter implements Runnable{
    private final String name;

    private Lock customerLock = new ReentrantLock();
    private Condition newCustomer = customerLock.newCondition();
    private Condition noCustomer = customerLock.newCondition();

    Waiter(String name){
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while (true){
                customerLock.lock();
                while (currentCustomer == null)
                    newCustomer.await();

                System.out.format("%s got order from %s [%s]%n", getName(), currentCustomer.getName(), Thread.currentThread().getName());
                prepareOrder();
                currentCustomer = null;
                noCustomer.signal();
                customerLock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Customer currentCustomer;
    void submitOrder(Customer customer) throws InterruptedException{
        customerLock.lock();

        while (currentCustomer != null)
            noCustomer.await();

        currentCustomer = customer;
        System.out.format("%s gave order to %s [%s]%n", currentCustomer.getName(), getName(), Thread.currentThread().getName());
        newCustomer.signal();
        customerLock.unlock();
    }

    private void prepareOrder() throws InterruptedException{
        System.out.format("%s is preparing order from %s in [%s]%n", getName(), currentCustomer.getName(), Thread.currentThread().getName());
        Thread.sleep((long)(2d + Math.random() * 4d) * IceCreamShop.timeUnit);
        System.out.format("%s is delivering ice to %s [%s]%n", getName(), currentCustomer.getName(), Thread.currentThread().getName());
        currentCustomer.receiveIce();
    }

    String getName() {
        return name;
    }
}
