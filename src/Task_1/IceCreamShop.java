package Task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IceCreamShop {
    static int timeUnit = 1000;

    private List<Waiter> waiters = new ArrayList<>();
    private int availableSeats = 10;
    private ArrayList<Thread> threads = new ArrayList<>();


    private IceCreamShop(){
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
    private void addWaveOfCustomers(){
        int numberNewCustomers = (int)(3 + Math.random() * 4);
        for(int i = 0; i < numberNewCustomers; i++){
            String name = "Customer_" + customerID++;
            threads.add(new Thread(new Customer(name, this), name + "_Thread"));
            threads.get(threads.size() - 1).start();
        }
    }

    synchronized void enterShop() throws InterruptedException{
        while (availableSeats == 0)
            wait();

        availableSeats--;
    }

    synchronized void leaveShop(){
        availableSeats++;
        System.out.format("In the shop there is now %s seats available [%s]%n", availableSeats, Thread.currentThread().getName());
        notify();
    }

    Waiter getAWaiter(){
        return waiters.get((int)(Math.random() * waiters.size()));
    }

    private void removeDeadThreads(){
        threads = threads.stream()
                .filter(Thread::isAlive)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.format("----> In and in front of the shop are 4 waiters and %s customers [%s]%n", threads.size() - 4, Thread.currentThread().getName());
    }

    public static void main(String... args) throws InterruptedException{
        IceCreamShop shop = new IceCreamShop();
        while (true) {
            System.out.format("Adding customers [%s]%n", Thread.currentThread().getName());
            shop.addWaveOfCustomers();
            shop.removeDeadThreads();
            Thread.sleep(6 * timeUnit);
        }
    }
}
