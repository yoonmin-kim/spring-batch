package hello.batch.springbatch.item_reader_adapter;

public class CustomService<T> {

	private int cnt = 0;

	public T customRead() {
		return (T)("item" + cnt);
	}
}
