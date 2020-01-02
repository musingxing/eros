package com.eros.job.shared;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * SharedHouse to manage products.
 *
 * <P> product
 * @author Eros
 * @since   2020-01-02 15:58
 */
public class SharedHouse<P> implements BlockingQueue<P> {

    /**
     * Default capacity in shared storehouse
     */
    private static final int DEFAULT_CAPACITY = 128;
    /**
     * Receive product
     */
    private final BlockingQueue<P> blockingDeque;
    /**
     * Identify as produce/consume task if stopped
     */
    private volatile boolean producerStopped = false;
    private volatile boolean consumerStopped = false;

    /**
     * Default Constructor
     */
    public SharedHouse() {
        this.blockingDeque = new ArrayBlockingQueue<P>(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with capacity limit
     */
    public SharedHouse(int capacity) {
        this.blockingDeque = new ArrayBlockingQueue<P>(capacity);
    }

    @Override
    public boolean add(P p) {
        return blockingDeque.add(p);
    }

    @Override
    public boolean offer(P p) {
        return blockingDeque.offer(p);
    }

    @Override
    public void put(P p) throws InterruptedException {
        blockingDeque.put(p);
    }

    @Override
    public boolean offer(P p, long timeout, TimeUnit unit) throws InterruptedException {
        return blockingDeque.offer(p, timeout, unit);
    }

    @Override
    public P take() throws InterruptedException {
        return blockingDeque.take();
    }

    @Override
    public P poll(long timeout, TimeUnit unit) throws InterruptedException {
        return blockingDeque.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return blockingDeque.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        return blockingDeque.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return blockingDeque.contains(o);
    }

    @Override
    public int drainTo(Collection<? super P> c) {
        return blockingDeque.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super P> c, int maxElements) {
        return blockingDeque.drainTo(c, maxElements);
    }

    @Override
    public P remove() {
        return blockingDeque.remove();
    }

    @Override
    public P poll() {
        return blockingDeque.poll();
    }

    @Override
    public P element() {
        return blockingDeque.element();
    }

    @Override
    public P peek() {
        return blockingDeque.peek();
    }

    @Override
    public int size() {
        return blockingDeque.size();
    }

    @Override
    public boolean isEmpty() {
        return blockingDeque.isEmpty();
    }

    @Override
    public Iterator<P> iterator() {
        return blockingDeque.iterator();
    }

    @Override
    public Object[] toArray() {
        return blockingDeque.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return blockingDeque.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return blockingDeque.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends P> c) {
        return blockingDeque.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return blockingDeque.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return blockingDeque.retainAll(c);
    }

    @Override
    public void clear() {
        blockingDeque.clear();
    }

    /* ******************** Add for Produce-Consume model***********************/

    public boolean isProducerStopped() {
        return producerStopped;
    }

    public void setProducerStopped(boolean producerStopped) {
        this.producerStopped = producerStopped;
    }

    public boolean isConsumerStopped() {
        return consumerStopped;
    }

    public void setConsumerStopped(boolean consumerStopped) {
        this.consumerStopped = consumerStopped;
    }
}
