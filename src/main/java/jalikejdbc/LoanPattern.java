package jalikejdbc;

import java.util.List;
import java.util.function.Function;

public class LoanPattern{
    public static <T,R> R using(T resource, Function<T,R> f) {
        return f.apply(resource);
    }
}
