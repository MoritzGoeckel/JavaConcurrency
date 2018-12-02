package Task_3;

public class Waiter implements Runnable{
    private final String name;
    private IceCreamShop shop;

    Waiter(String name, IceCreamShop shop){
        this.name = name;
        this.shop = shop;
    }

    @Override
    public void run() {
        try {
            while (true){
                Customer c = shop.getWaitingCustomer();
                System.out.format("%s got assigned to %s and is now trying to order [%s]%n", getName(), this.getName(), Thread.currentThread().getName());
                System.out.format("%s got order from %s [%s]%n", getName(), c.getName(), Thread.currentThread().getName());
                System.out.format("%s is preparing order from %s in [%s]%n", getName(), c.getName(), Thread.currentThread().getName());
                Thread.sleep((long)(2d + Math.random() * 4d) * IceCreamShop.timeUnit);
                System.out.format("%s is delivering ice to %s [%s]%n", getName(), c.getName(), Thread.currentThread().getName());
                c.receiveIce();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String getName() {
        return name;
    }
}
