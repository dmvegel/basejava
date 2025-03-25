/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    private int head;

    void clear() {
    }

    void save(Resume r) {
    }

    Resume get(String uuid) {
        return null;
    }

    void delete(String uuid) {
        int delPosition = -1;
        for (int i = 0; i < head; i++) {
            if (storage[i] != null && storage[i].toString().equals(uuid)) {
                delPosition = i;
            }
        }
        if (delPosition >= 0) {
            for (int i = delPosition; i < head; i++) {
                storage[i] = null;
            }
            head--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return new Resume[0];
    }

    int size() {
        return head;
    }
}
