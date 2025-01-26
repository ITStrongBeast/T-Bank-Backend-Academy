package backend.academy;

import java.time.LocalDateTime;

@SuppressWarnings("RecordComponentNumber")
public record LogRecord(
    String remoteAddr,
    String remoteUser,
    LocalDateTime timeLocal,
    String typeRequest,
    String request,
    String protoRequest,
    String status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
}
