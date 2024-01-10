package org.aulich.wbh.vertiefung_3.programs;

public class MonitorDemo {
    public static void main(String[] args) throws InterruptedException {
        MonitorDemo monitorDemo = new MonitorDemo();
        monitorDemo.doIt();
    }

    public void doIt() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " starts.");
        Monitor monitor = new Monitor();
        Thread winner = new Thread(new Player(monitor, 8, Player.Type.WINNER));
        Thread looser = new Thread(new Player(monitor, 10, Player.Type.LOOSER));
        winner.start();
        looser.start();
        winner.join();
        looser.join();
        System.out.println(Thread.currentThread().getName() + " ended.");
    }

    class Monitor {
        private int rest = 40;
        private final int max = 80;

        public synchronized void add(int i, Player player) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " Try to add Random: " + i + " to Rest:" + rest);
            while (max < rest + i && rest >= 1) {
                wait();
            }
            if (rest < 1) {
                System.out.println(Thread.currentThread().getName() + " Stop requested!");
                player.stopIt();
            } else {
                rest += i;
                System.out.println(Thread.currentThread().getName() + " Random: +" + i + " Rest:" + rest);
            }
            notifyAll();
        }

        public synchronized void sub(int i, Player player) {
            rest -= i;
            System.out.println(Thread.currentThread().getName() + " Random: -" + i + " Rest:" + rest);
            if (rest < 1) {
                System.out.println(Thread.currentThread().getName() + " Stop requested!");
                player.stopIt();
            }
            notifyAll();
        }
    }

    class Player implements Runnable {
        enum Type {WINNER, LOOSER}
        Type type;
        int randomMax;
        Monitor monitor;
        Boolean stopIt = Boolean.FALSE;
        public Player(Monitor monitor, int randomMax, Type type) {
            this.type = type;
            this.monitor = monitor;
            this.randomMax = randomMax;
        }
        public void stopIt() {
            stopIt = Boolean.TRUE;
        }
        @Override
        public void run() {
            int random, rest;
            if (type == Type.WINNER) {
                do {
                    random = (int) (Math.random() * (randomMax)) + 1;
                    monitor.sub(random, this);
                } while (!stopIt);
            } else {
                while (!stopIt) {
                    random = (int) (Math.random() * (randomMax)) + 1;
                    try {
                        monitor.add(random, this);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}