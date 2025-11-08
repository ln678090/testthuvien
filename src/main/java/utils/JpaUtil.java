package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;
public class JpaUtil {
    // Xóa trường factory này đi, không lưu static nữa
/*
cách dùng
public class RoomDAO {

    private EntityManagerFactory factory;

    public RoomDAO(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public List<Room> fillAll() {
        return JpaUtil.execute(factory, em -> em.createQuery("select r from Room r", Room.class).getResultList());
    }

    public void add(Room room) {
        JpaUtil.execute(factory, em -> em.persist(room));
    }

    public void update(Room room) {
        JpaUtil.execute(factory, em -> em.merge(room));
    }

    public void delete(Room room) {
        JpaUtil.execute(factory, em -> {
            Room r = em.find(Room.class, room.getId());
            if (r != null) em.remove(r);
        });
    }
}

*/

    public static <R> R execute(EntityManager em, Function<EntityManager, R> function) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager must not be null");
        }
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = function.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
        // Không đóng em ở đây nếu em là tham số ngoài truyền vào
    }


    public static void execute(EntityManager em, Consumer<EntityManager> consumer) {
        execute(em, e -> {
            consumer.accept(e);
            return null;
        });
    }

}
