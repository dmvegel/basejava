package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.model.Resume;

public class ListStorage extends AbstractStorage {
    private Node firstNode;
    private Node lastNode;

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    public Resume get(String uuid) {
        Node node = getNode(uuid);
        if (node != null)
            return node.element;
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void save(Resume r) {
        if (getNode(r.getUuid()) != null)
            throw new ExistStorageException(r.getUuid());
        append(r);
        size++;
    }

    @Override
    public void update(Resume r) {
        Node node = getNode(r.getUuid());
        if (node != null) {
            node.element = r;
            return;
        }
        throw new NotExistStorageException(r.getUuid());
    }

    @Override
    public void delete(String uuid) {
        Node node = getNode(uuid);
        if (node != null) {
            unlink(node);
            size--;
            if (size == 1) lastNode = null;
            return;
        }
        throw new NotExistStorageException(uuid);
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

    private Node getNode(String uuid) {
        Node node = firstNode;
        while (node != null) {
            if (node.element != null && node.element.getUuid().equals(uuid))
                return node;
            node = node.next;
        }
        return null;
    }

    private void append(Resume r) {
        if (firstNode == null) {
            firstNode = new Node(r, null, null);
        } else if (lastNode == null) {
            firstNode.next = lastNode = new Node(r, firstNode, null);
        } else {
            lastNode = lastNode.next = new Node(r, lastNode, null);
        }
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

    private static class Node {
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
