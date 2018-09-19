package alex.ru.utils.pdf.queue;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PdfDynamicQueue<E> implements PdfQueue<E>{

    private Object [] objects;
    private int size = 0;
    private static int INIT_SIZE = 10;
    private static double RESIZE_COEFFICIENT = 0.5;
    private static int PREFER_ARRAY_SIZE = 10;
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public PdfDynamicQueue() {
        objects = new Object[INIT_SIZE];
    }

    @Override
    public void add(final E element) {
        lock.writeLock().lock();
        try {
            if(size == objects.length)
                increase();

            if(size == 0 && objects.length > PREFER_ARRAY_SIZE)
                decrease();

            objects[size++] = element;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E[] poolAll() {
        lock.readLock().lock();
        try {
            final E[] elements = (E[])objects;
            decrease();
            return elements;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void increase() {
        int newSize = (int) (objects.length + (objects.length * RESIZE_COEFFICIENT));
        final Object[] swapArray = new Object[newSize];
        System.arraycopy(objects, 0, swapArray, 0, objects.length);
        objects = swapArray;
    }

    private void decrease() {
        size = 0;
        objects = new Object[INIT_SIZE];
    }

    public static void setInitSize(final int initSize) {
        INIT_SIZE = initSize;
    }

    public static void setResizeCoefficient(final double resizeCoefficient) {
        RESIZE_COEFFICIENT = resizeCoefficient;
    }

    public static void setPreferArraySize(final int preferArraySize) {
        PREFER_ARRAY_SIZE = preferArraySize;
    }
}
