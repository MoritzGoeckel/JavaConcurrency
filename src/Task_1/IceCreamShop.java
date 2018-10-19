package Task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IceCreamShop {
    public static int timeUnit = 10;

    private List<Waiter> waiters = new ArrayList<>();
    private int availableSeats = 10;
    private ArrayList<Thread> threads = new ArrayList<>();


    public IceCreamShop(){
        waiters.add(new Waiter("James"));
        waiters.add(new Waiter("John"));
        waiters.add(new Waiter("Josef"));
        waiters.add(new Waiter("Jeremiah"));

        for(Waiter w : waiters) {
            threads.add(new Thread(w, w.getName() + "_Thread"));
            threads.get(threads.size() - 1).start();
        }
    }

    private int customerID = 0;
    public void addCustomers(){
        int numberNewCustomers = (int)(3 + Math.random() * 4);
        for(int i = 0; i < numberNewCustomers; i++){
            String name = "Customer_" + customerID++;
            threads.add(new Thread(new Customer(name, this), name + "_Thread"));
            threads.get(threads.size() - 1).start();
        }
    }

    public synchronized void enterCafe() throws InterruptedException{
        while (availableSeats == 0)
            wait();

        availableSeats--;
    }

    public synchronized void leaveCafe(){
        availableSeats++;
        System.out.format("In the shop there is now %s seats available [%s]%n", availableSeats, Thread.currentThread().getName());
        notify();
    }

    public Waiter getAWaiter(){
        return waiters.get((int)(Math.random() * waiters.size()));
    }

    public void removeDeadThreads(){
        threads = threads.stream()
                .filter(Thread::isAlive)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.format("----> In and in front of the shop are 4 waiters and %s customers [%s]%n", threads.size() - 4, Thread.currentThread().getName());
    }

    public static void main(String... args) throws InterruptedException{
        IceCreamShop shop = new IceCreamShop();
        while (true) {
            System.out.format("Adding customers [%s]%n", Thread.currentThread().getName());
            shop.addCustomers();
            shop.removeDeadThreads();
            Thread.sleep(6 * timeUnit);
        }
    }
}
