package online.popopo.common.command;

import java.util.List;

public interface Completer {
    List<String> candidateOf(String key);
}
