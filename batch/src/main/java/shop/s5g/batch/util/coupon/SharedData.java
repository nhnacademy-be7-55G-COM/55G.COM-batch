package shop.s5g.batch.util.coupon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SharedData<T> {

    private final Map<String, T> sharedDataMap = new ConcurrentHashMap<>();

    public void put(String key, T value) {
        sharedDataMap.put(key, value);
    }

    public T get(String key) {
        return sharedDataMap.get(key);
    }

    public int getSize() {
        return sharedDataMap.size();
    }
}
