package packman.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogMessage {
    private static final Logger logger = LoggerFactory.getLogger(LogMessage.class);

    public static void setDataLog(String apiName, Object data, Long userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";

        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("PACKMAN, " + apiName + ", userId: " + userId + ", data: " + json);
    }

    public static void setNonDataLog(String apiName, Long userId) {
        logger.info("PACKMAN, " + apiName + ", userId: " + userId);
    }
}
