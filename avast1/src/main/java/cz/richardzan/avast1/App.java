package cz.richardzan.avast1;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableAsync
@RestController
@EnableAutoConfiguration
public class App {
    
    @Autowired
    private UrlService urlService;
    
    @Bean(name = "threadPoolExecutor")
    public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(1);
            executor.setMaxPoolSize(3);
            executor.setQueueCapacity(100);
            executor.setThreadNamePrefix("Async-");
            executor.initialize();
            return executor;
    }
    
    @RequestMapping(value="/", method = RequestMethod.POST)
    @ResponseBody
    public UrlsResponse home(@RequestBody Urls input) throws Exception {
        
        List<CompletableFuture<String>> resultFutures = new ArrayList<>();
        
        input.getUrls().forEach((x) -> {
            resultFutures.add(urlService.getStatus(x));
        });
        
        UrlsResponse response = new UrlsResponse();
        
        response.setUrlsResponses(checkIfDone(resultFutures).get());
        return response;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    private static <T> CompletableFuture<List<T>> checkIfDone(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }
}
