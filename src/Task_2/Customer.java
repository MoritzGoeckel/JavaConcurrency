package Task_2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Customer implements Runnable{
    private final String name;
    private final IceCreamShop shop;

    private Lock mutex = new ReentrantLock();
    private Condition gotIce = mutex.newCondition();

    Customer(String name, IceCreamShop shop){
        this.name = name;
        this.shop = shop;
    }

    private boolean hasIce = false;
    void receiveIce(){
        mutex.lock();

        hasIce = true;
        gotIce.signal();

        mutex.unlock();
    }

    String getName() {
        return name;
    }

    private void eatIce() throws InterruptedException{
        Thread.sleep((long)(3d) * IceCreamShop.timeUnit);
    }

    private void chooseIce() throws InterruptedException{
        Thread.sleep((long)(1d + Math.random() * 1d) * IceCreamShop.timeUnit);
    }

    @Override
    public void run() {
        mutex.lock();
        try {
            System.out.format("%s is waiting to enter the shop [%s]%n", getName(), Thread.currentThread().getName());
            shop.enterShop();

            System.out.format("%s entered the shop and checks the menu [%s]%n", getName(), Thread.currentThread().getName());
            chooseIce();
            System.out.format("%s choose vanilla ice cream [%s]%n", getName(), Thread.currentThread().getName());

            Waiter w = shop.getAWaiter();
            System.out.format("%s got assigned to %s and is now trying to order [%s]%n", getName(), w.getName(), Thread.currentThread().getName());

            w.submitOrder(this);
            System.out.format("%s submitted his order and is now waiting for ice [%s]%n", getName(), Thread.currentThread().getName());

            while (!hasIce)
                gotIce.await();

            System.out.format("%s got ice and is eating now [%s]%n", getName(), Thread.currentThread().getName());
            eatIce();

            System.out.format("%s ate his ice and leaves now [%s]%n", getName(), Thread.currentThread().getName());
            shop.leaveShop();

            System.out.format("%s left the shop [%s]%n", getName(), Thread.currentThread().getName());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.unlock();
    }
}
