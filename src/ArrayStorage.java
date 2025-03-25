/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    private int head;

    void clear() {
        for (int i = 0; i < head; i++) {
            storage[i] = null;
        }
        head = 0;
    }

    void save(Resume r) {
        if (head == storage.length) {
            System.out.println("Превышен размер хранилища");
        } else {
            storage[head] = r;
            head++;
        }
    }

    Resume get(String uuid) {
        Resume resume = null;
        for (int i = 0; i < head; i++) {
            if (storage[i] != null && storage[i].toString().equals(uuid)) {
                resume = storage[i];
                break;
            }
        }
        return resume;
    }

    void delete(String uuid) {
        int delPosition = -1;
        for (int i = 0; i < head; i++) {
            if (storage[i] != null && storage[i].toString().equals(uuid)) {
                delPosition = i;
            }
        }
        if (delPosition >= 0) {
            for (int i = delPosition; i < head - 1; i++) {
                storage[i] = storage[i + 1];
            }
            head--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] allResumes = new Resume[head];
        System.arraycopy(storage, 0, allResumes, 0, head);
        return allResumes;
    }

    int size() {
        return head;
    }
}
