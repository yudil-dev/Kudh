package ca.umanitoba.cs.pariyary.logic;

import com.google.common.base.Preconditions;

public class LinkedListStack<T> implements Stack<T> {

    private Node<T> top;
    private int size;


    public LinkedListStack() {
        this.top = null;
        this.size = 0;
        checkStack();
    }

    @Override
    public void push(T item) {
        Preconditions.checkNotNull(item, "Item can't be null");

        top = new Node<>(item, top);
        size++;

        checkStack();
    }

    @Override
    public T pop() throws Stack.EmptyStackException {
        if (isEmpty()) {
            throw new Stack.EmptyStackException("Stack is empty, cannot pop from it");
        }

        T item = top.data;
        top = top.next;
        size--;

        checkStack();
        return item;
    }

    @Override
    public int size() {
        checkStack();
        return size;
    }

    @Override
    public boolean isEmpty() {
        checkStack();
        return size == 0;
    }

    @Override
    public T peek() throws Stack.EmptyStackException {
        if (isEmpty()) {
            throw new Stack.EmptyStackException("Stack is empty, can't peek from it");
        }
        checkStack();
        return top.data;
    }

    private void checkStack() {
        Preconditions.checkState(size >= 0, "Stack size can't be negative");
        Preconditions.checkState((size == 0 && top == null) || (size > 0 && top != null), "Stack size and top do not match");
    }

    private static class Node<T> {
        private final T data;
        private final Node<T> next;

        private Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }
}