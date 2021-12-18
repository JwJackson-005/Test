package scheduleApp.DBAccess;

import javafx.collections.ObservableList;

/**
 * This is the generic Interface for a DAO class. It states that all classes that
 * implement the DAO interface must be able to do basic CRUD operations with the database.
 * A DAO will be required for each class Model in the database.
 * @param <T> the Object type for the DAO implementation
 * @author Jason Jackson
 */
public interface DAO<T> {

    ObservableList<T> getAll();
    void create(T object);
    T read(int id);
    void update(T object);
    boolean delete(int key);
}
