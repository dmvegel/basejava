package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

public class ListStorage<T extends ListStorage.Node> extends AbstractStorage<T> {
    private Node firstNode;
    private Node lastNode;

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    protected Resume doGet(T searchKey) {
        return searchKey.element;
    }

    @Override
    protected void doSave(Resume resume, T searchKey) {
        if (firstNode == null) {
            firstNode = new Node(resume, null, null);
        } else if (lastNode == null) {
            firstNode.next = lastNode = new Node(resume, firstNode, null);
        } else {
            lastNode = lastNode.next = new Node(resume, lastNode, null);
        }
    }

    @Override
    protected void doUpdate(Resume resume, T searchKey) {
        searchKey.element = resume;
    }

    @Override
    protected void doDelete(T searchKey) {
        unlink(searchKey);
        if (size - 1 == 1)
            lastNode = null;
    }

    @Override
    protected T getSearchKey(String uuid) {
        Node node = firstNode;
        while (node != null) {
            if (node.element != null && node.element.getUuid().equals(uuid))
                return (T) node;
            node = node.next;
        }
        return null;
    }

    @Override
    protected boolean isExist(T searchKey) {
        return searchKey != null && searchKey.element != null;
    }

    @Override
    public Resume[] getAll() {
        Resume[] result = new Resume[size];
        Node node = firstNode;
        for (int i = 0; i < size; i++) {
            result[i] = node.element;
            node = node.next;
        }
        return result;
    }

    private void unlink(Node node) {
        if (node.previous != null)
            node.previous.next = node.next;
        else {
            firstNode = node.next;
        }
        if (node.next != null)
            node.next.previous = node.previous;
        else {
            lastNode = node.previous;
        }
    }

    protected static class Node {
        Resume element;
        Node previous;
        Node next;

        public Node(Resume element, Node previous, Node next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }
    }
}
