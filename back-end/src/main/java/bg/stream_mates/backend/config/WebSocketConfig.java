package bg.stream_mates.backend.config;

import bg.stream_mates.backend.feather.chat.repositories.ChatRepository;
import bg.stream_mates.backend.feather.chat.services.ChatWebSocketHandler;
import bg.stream_mates.backend.feather.user.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public WebSocketConfig(ObjectMapper objectMapper,
                           ChatRepository chatRepository,
                           UserRepository userRepository) {

        this.objectMapper = objectMapper;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler(), "/chat").setAllowedOrigins("*");
    }

    @Bean(name = "customChatWebSocketHandler")
    public ChatWebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler(objectMapper, chatRepository, userRepository);
    }
}
