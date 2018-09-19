package alex.ru.utils.pdf.queue;

public interface PdfQueue<E> {

    void add(final E multipart);
    E[] poolAll();

}
