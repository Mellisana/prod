import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShopRepositoryTest {

    private ShopRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ShopRepository();
    }

    @Test
    public void successRemove() {
        Product product1 = new Product(1, "Product 1", 15);
        Product product2 = new Product(2, "Product 2", 40);

        repository.add(product1);
        repository.add(product2);

        try {
            repository.removeById(1);
        } catch (NotFoundException e) {
            throw new AssertionError(e);
        }

        Product[] expectedProducts = {product2};
        assertArrayEquals(expectedProducts, repository.findAll());
    }

    @Test
    void nonExistentRemoval() {
        Product product1 = new Product(1, "Product 1", 15);
        Product product2 = new Product(2, "Product 2", 40);

        repository.add(product1);
        repository.add(product2);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> repository.removeById(3),
                "Ожидалось исключение NotFoundException"
        );

        String expectedMessage = "Элемент с id: 3 не найден";
        String actualMessage = exception.getMessage();

        assertArrayEquals(new Product[]{product1, product2}, repository.findAll(), "Список товаров должен остаться неизменным.");
        assertEquals(expectedMessage, actualMessage, "Неверное сообщение исключения.");
    }
}