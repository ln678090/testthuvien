package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

public class JpaUtil {
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("ASMJAVA4v2");

    public static <R> R execute(Function<EntityManager, R> function) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            R result = function.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static void execute(Consumer<EntityManager> consumer) {
        execute(em -> {
            consumer.accept(em);
            return null;
        });
    }
}
