package br.com.async.web.controller;

import br.com.async.util.DataCollectCallable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class RemoteResponseAsyncCtrl {

    private Map<String, DataCollectCallable> inventory = new ConcurrentHashMap<>();

    @GetMapping(path="/api/v2/task")
    public WebAsyncTask<Object> requestDelayAsyncTask(@RequestParam(defaultValue="5") long t, @RequestParam String token) {

        if(inventory.containsKey(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicated token");
        } else {
            DataCollectCallable dataCollectCallable = new DataCollectCallable();
            inventory.put(token, dataCollectCallable);

            WebAsyncTask<Object> webAsyncTask = new WebAsyncTask<Object>(t * 6000, dataCollectCallable);
            webAsyncTask.onTimeout(() -> {
                dataCollectCallable.unfreeze();
                inventory.remove(token);
                throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT);
            });
            return webAsyncTask;
        }
    }

    @PostMapping(path="/api/v2/task")
    public Object provideDelayData(@RequestParam String token, @RequestBody String data){
        // there might have some token validation here

        DataCollectCallable dataCollectCallable = inventory.remove(token);
        if(dataCollectCallable != null) {
            dataCollectCallable.setData(data);
            dataCollectCallable.unfreeze();
            return Collections.singletonMap("msg", "Send data successfully");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found");
        }
    }
}