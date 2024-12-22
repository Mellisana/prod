public class NotFoundException  extends RuntimeException {

    public NotFoundException(String noProduct) {
        super(noProduct);
    }
}
