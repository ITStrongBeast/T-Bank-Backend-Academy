package backend.academy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import lombok.Getter;
import lombok.Setter;

public class Analyzer {
    @Getter @Setter private LocalDateTime from;
    @Getter @Setter private LocalDateTime to;
    @Setter private String filterField;
    @Setter private String filterValue;

    @Getter private long numberOfRequests;
    private long totalSize;
    Queue<Integer> percentile = new PriorityQueue<>(Collections.reverseOrder());
    private final Map<String, Integer> typeRequest = new HashMap<>();
    private final Map<String, Integer> agent = new HashMap<>();
    private final Map<String, Integer> request = new HashMap<>();
    private final Map<String, Integer> status = new HashMap<>();

    public void analysis(LogRecord log) {
        if (isValid(log)) {
            addStat(log);
        }
    }

    public long getTotalSize() {
        if (numberOfRequests == 0) {
            return 0L;
        }
        return totalSize / numberOfRequests;
    }

    @SuppressWarnings("MagicNumber")
    public int getPercentile() {
        int size = percentile.size() / 20;
        for (int i = 0; i < size - 1; i++) {
            percentile.poll();
        }
        return percentile.poll();
    }

    public TreeMap<Integer, String> getAgent() {
        return sortStat(agent);
    }

    public TreeMap<Integer, String> getTypeRequest() {
        return sortStat(typeRequest);
    }

    public TreeMap<Integer, String> getRequest() {
        return sortStat(request);
    }

    public TreeMap<Integer, String> getStatus() {
        return sortStat(status);
    }

    private TreeMap<Integer, String> sortStat(Map<String, Integer> stat) {
        TreeMap<Integer, String> result = new TreeMap<>(Comparator.reverseOrder());
        for (String key : stat.keySet()) {
            result.put(stat.get(key), key);
        }
        return result;
    }

    private void addStat(LogRecord log) {
        numberOfRequests++;
        totalSize += log.bodyBytesSent();
        percentile.add(log.bodyBytesSent());
        typeRequest.put(log.typeRequest(), typeRequest.getOrDefault(log.typeRequest(), 0) + 1);
        agent.put(log.httpUserAgent(), agent.getOrDefault(log.httpUserAgent(), 0) + 1);
        request.put(log.request(), request.getOrDefault(log.request(), 0) + 1);
        status.put(log.status(), status.getOrDefault(log.status(), 0) + 1);
    }

    private boolean isValid(LogRecord log) {
        return log != null
            && (from == null || log.timeLocal().isAfter(from))
            && (to == null || log.timeLocal().isBefore(to))
            && (filterField == null || filterValue == null || isFilter(log));
    }

    private boolean isFilter(LogRecord log) {
        if (Objects.equals(filterField, "agent")) {
            return Objects.equals(log.httpUserAgent(), filterValue);
        }
        if (Objects.equals(filterField, "method")) {
            return Objects.equals(log.typeRequest(), filterValue);
        }
        return false;
    }
}
