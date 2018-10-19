package Task_1;

public class Waiter implements Runnable{
    private final String name;

    Waiter(String name){
        this.name = name;
    }

    @Override
    public synchronized void run() {
        try {
            while (true){
                while (currentCustomer == null)
                    wait();

                System.out.format("%s got order from %s [%s]%n", getName(), currentCustomer.getName(), Thread.currentThread().getName());
                prepareOrder();
                currentCustomer = null;
                notifyAll();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Customer currentCustomer;
    synchronized void submitOrder(Customer customer) throws InterruptedException{
        while(currentCustomer != null)
            wait();

        currentCustomer = customer;
        System.out.format("%s gave order to %s [%s]%n", currentCustomer.getName(), getName(), Thread.currentThread().getName());
        notifyAll();
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
