package Task_1;

public class Customer implements Runnable{
    private final String name;
    private final IceCreamShop shop;

    public Customer(String name, IceCreamShop shop){
        this.name = name;
        this.shop = shop;
    }

    boolean hasIce = false;
    public synchronized void receiveIce(){
        hasIce = true;
        notifyAll();
    }

    public String getName() {
        return name;
    }

    private void eatIce() throws InterruptedException{
        Thread.sleep((long)(3d) * IceCreamShop.timeUnit);
    }

    private void chooseIce() throws InterruptedException{
        Thread.sleep((long)(1d + Math.random() * 1d) * IceCreamShop.timeUnit);
    }

    @Override
    public synchronized void run() {
        try {
            System.out.format("%s is waiting to enter the shop [%s]%n", getName(), Thread.currentThread().getName());
            shop.enterCafe();
            System.out.format("%s entered the shop and checks the menu [%s]%n", getName(), Thread.currentThread().getName());
            chooseIce();
            System.out.format("%s choose vanilla ice cream [%s]%n", getName(), Thread.currentThread().getName());
            Waiter w = shop.getAWaiter();
            System.out.format("%s got assigned to %s and is now trying to order [%s]%n", getName(), w.getName(), Thread.currentThread().getName());
            w.submitOrder(this);
            System.out.format("%s submitted his order and is now waiting for ice [%s]%n", getName(), Thread.currentThread().getName());
            while (!hasIce)
                wait();
            System.out.format("%s got ice and is eating now [%s]%n", getName(), Thread.currentThread().getName());
            eatIce();
            System.out.format("%s ate his ice and leaves now [%s]%n", getName(), Thread.currentThread().getName());
            shop.leaveCafe();
            System.out.format("%s left the shop [%s]%n", getName(), Thread.currentThread().getName());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
