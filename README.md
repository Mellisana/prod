Задание 1 (обязательное)
Представим себе репозиторий, хранящий товары.

Товары будут описываться классом Product:

public class Product {
    protected int id;
    protected String title;
    protected int price;

    public Product(int id, String title, int price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
    
    // Вспомогательные методы для корректной работы equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && price == product.price && title.equals(product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price);
    }
    
    // геттеры + сеттеры
    // на id только геттер — пусть будет неизменяемым для товара
}
И репозиторий:

public class ShopRepository {
    private Product[] products = new Product[0];

    /**
     * Вспомогательный метод для имитации добавления элемента в массив
     * @param current — массив, в который мы хотим добавить элемент
     * @param product — элемент, который мы хотим добавить
     * @return — возвращает новый массив, который выглядит, как тот, что мы передали,
     * но с добавлением нового элемента в конец
     */
    private Product[] addToArray(Product[] current, Product product) {
        Product[] tmp = new Product[current.length + 1];
        for (int i = 0; i < current.length; i++) {
            tmp[i] = current[i];
        }
        tmp[tmp.length - 1] = product;
        return tmp;
    }

    /**
     * Метод добавления товара в репозиторий
     * @param product — добавляемый товар
     */
    public void add(Product product) {
        products = addToArray(products, product);
    }

    public Product[] findAll() {
        return products;
    }

    // Этот способ мы рассматривали в теории в теме про композицию
    public void remove(int id) {
        Product[] tmp = new Product[products.length - 1];
        int copyToIndex = 0;
        for (Product product : products) {
            if (product.getId() != id) {
                tmp[copyToIndex] = product;
                copyToIndex++;
            }
        }
        products = tmp;
    }
}
Вы решили сделать так, чтобы при попытке удаления несуществующего объекта из репозитория генерировалось ваше исключение, а не ArrayIndexOfBoundsException.

Обратите внимание: это правильный подход, поскольку так вы сообщаете через генерацию исключения, что это исключение, вписывающееся в вашу логику, а не ошибка программиста.

Что нужно сделать

Создайте класс исключения NotFoundException, отнаследовавшись от RuntimeException, и реализуйте как минимум конструктор с параметром-сообщением. Он будет просто вызывать суперконструктор предка (см. ниже).
В методе удаления removeById сначала проверяйте, есть ли элемент. Для этого прямо из метода removeById вызывайте метод findById: если результат null, тогда выкидывайте исключение NotFoundException.
Напишите два автотеста на репозиторий: первый должен проверять успешность удаления существующего элемента, второй — генерации NotFoundException при попытке удаления несуществующего элемента.
Конструктор вашего исключения должен выглядеть так:

	public NotFoundException(String s) {
		super(s);
	}
Для реализации этой логики вам понадобится добавить метод findById, предназначенный для поиска товара в репозитории по его ID. Так он должен принимать параметр ID искомого товара, пробегаться по всем товарам репозитория и сверять их ID с искомым, в случае совпадения делать return этого товара.

Если же, пробежав все товары репозитория, ни один подходящий найден не был, то есть цикл закончился без вызова return внутри него, то следует сделать return null.

Общая схема этого метода будет такой:

public Product findById(???) {
  for (???) {
    if (???) {
      return product;
    }
  }
  return null;
}
Убедитесь, что ваши автотесты проходят. Напоминаем, что проект должен быть на базе Maven, с подключёнными зависимостями и необходимыми плагинами.

Мы рекомендуем вам указывать в сообщении исключения: при удалении по какому конкретно ID было сгенерировано ваше исключение.

Простейший способ, как это можно сделать: "Element with id: " + id + " not found".
